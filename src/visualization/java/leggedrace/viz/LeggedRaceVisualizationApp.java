/*******************************************************************************
 * Copyright (C) 2016-2017 Dennis Cosgrove
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package leggedrace.viz;

import static edu.wustl.cse231s.v5.V5.launchApp;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.mutable.MutableBoolean;

import edu.wustl.cse231s.color.ColorUtil;
import edu.wustl.cse231s.goldenratio.GoldenRatioUtils;
import edu.wustl.cse231s.timing.ImmutableTimer;
import edu.wustl.cse231s.v5.options.SystemPropertiesOption;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import leggedrace.core.LeggedRace;
import leggedrace.core.Participant;
import leggedrace.studio.ForallLeggedRace;
import leggedrace.studio.ForallPhasedLeggedRace;
import leggedrace.studio.ForallPhasedPointToPointLeggedRace;
import leggedrace.studio.SequentialLeggedRace;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class LeggedRaceVisualizationApp extends Application {
	private static final int PARTICIPANT_COUNT = 16;// Runtime.getRuntime().availableProcessors();
	private static final int STEP_COUNT = 10;

	private static final double MAX_STEP_DURATION_IN_SECONDS = 1.0;

	private static final List<Color> colorList;
	static {
		colorList = new LinkedList<>(Arrays.asList(ColorUtil.getColorPalette()));
		colorList.add(3, Color.BLACK);
	}

	private static final class ParticipantColumn implements Participant {
		public ParticipantColumn(GridPane gridPane, int columnIndex) {
			this.gridPane = gridPane;
			this.columnIndex = columnIndex;
		}

		@Override
		public void takeStep(int stepIndex) {
			StepCell stepCell = (StepCell) gridPane.getChildren().get(this.columnIndex * STEP_COUNT + stepIndex);
			stepCell.setTaken();
		}

		private final GridPane gridPane;
		private final int columnIndex;
	}

	private static int getColorIndex(int columnIndex) {
		return (columnIndex / 2) % colorList.size();
	}

	private static class StepCell extends ProgressBar {
		public StepCell(int columnIndex, int rowIndex) {
			this.columnIndex = columnIndex;
			this.rowIndex = rowIndex;

			this.nodeOrientationProperty()
					.set(rowIndex % 2 == 0 ? NodeOrientation.LEFT_TO_RIGHT : NodeOrientation.RIGHT_TO_LEFT);

			Color color = colorList.get(getColorIndex(this.columnIndex));
			this.setMaxHeight(20.0);
			this.setMaxWidth(GoldenRatioUtils.PHI * this.getMaxHeight());
			this.setStyle("-fx-accent: " + ColorUtil.toWeb(color) + ";");
			this.reset();
		}

		public void reset() {
			this.setProgress(0.0);
		}

		public void setTaken() {
			if (isToBeCanceled) {
				Thread.currentThread().interrupt();
			} else {
				CountDownLatch latch = new CountDownLatch(1);
				Platform.runLater(() -> {
					double columnPortion = this.columnIndex / (double) (PARTICIPANT_COUNT - 1);
					double rowPortion = this.rowIndex / (double) (STEP_COUNT - 1);

					double c0 = columnPortion;
					double c1 = 1.0 - columnPortion;

					double p = c0 + (c1 - c0) * rowPortion;

					Duration duration = Duration.seconds(0.1 + p * MAX_STEP_DURATION_IN_SECONDS);

					Timeline timeline = new Timeline();

					KeyFrame keyFrame = new KeyFrame(duration, (e) -> {
						latch.countDown();
					}, new KeyValue(this.progressProperty(), 1.0));
					timeline.getKeyFrames().add(keyFrame);

					timeline.play();
				});
				try {
					latch.await();
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
				}
			}
		}

		private final int columnIndex;
		private final int rowIndex;
	}

	private static boolean isToBeCanceled = false;

	private static void run(LeggedRace leggedRace, Participant[] participants, GridPane gridPane, Label algorithmLabel,
			List<Button> algorithmButtons, Button cancelButton) {
		for (Node node : gridPane.getChildren()) {
			if (node instanceof StepCell) {
				StepCell stepCell = (StepCell) node;
				stepCell.reset();
			}
		}
		for (Button button : algorithmButtons) {
			button.setDisable(true);
		}
		cancelButton.setDisable(false);
		algorithmLabel.setText("running:\n  " + leggedRace.getClass().getSimpleName());
		isToBeCanceled = false;
		Thread thread = new Thread(() -> {
			try {
				ImmutableTimer timer = new ImmutableTimer(leggedRace.getClass().getSimpleName());
				MutableBoolean wasCanceled = new MutableBoolean(false);
				launchApp(new SystemPropertiesOption.Builder().numWorkerThreads(PARTICIPANT_COUNT).build(), () -> {
					try {
						leggedRace.takeSteps(participants, STEP_COUNT);
//					} catch (ExecutionException ee) {
//						if (ee.getCause() instanceof InterruptedException) {
//							// from forall
//							wasCanceled.setTrue();
//						} else {
//							throw ee;
//						}
					} catch (InterruptedException ie) {
						//System.out.println("sequential");
						wasCanceled.setTrue();
					}
				});
				if (wasCanceled.booleanValue()) {
					System.out.println(leggedRace.getClass().getSimpleName() + " canceled.");
				} else {
					timer.markAndPrintResults();
				}
			} finally {
				Platform.runLater(() -> {
					for (Button button : algorithmButtons) {
						button.setDisable(false);
					}
					cancelButton.setDisable(true);
					algorithmLabel.setText("\n  ");
				});
			}
		});
		thread.start();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane borderPane = new BorderPane();

		GridPane gridPane = new GridPane();
		gridPane.setStyle("-fx-background-color: black; -fx-padding: 2; -fx-hgap: 2; -fx-vgap: 2; ");

		Font titleFont = Font.font("Serif", FontWeight.BOLD, 18);
		Label label0 = new Label("Starts Fast\nRuns Out Of Gas");
		label0.setFont(titleFont);
		label0.setTextFill(colorList.get(getColorIndex(0)));
		Label labelNMinus1 = new Label("Starts Slow\nBig Kick");
		labelNMinus1.setFont(titleFont);
		labelNMinus1.setTextFill(colorList.get(getColorIndex(PARTICIPANT_COUNT - 1)));

		Participant[] participants = new Participant[PARTICIPANT_COUNT];
		for (int columnIndex = 0; columnIndex < PARTICIPANT_COUNT; columnIndex++) {
			for (int rowIndex = 0; rowIndex < STEP_COUNT; rowIndex++) {
				gridPane.add(new StepCell(columnIndex, rowIndex), columnIndex, rowIndex + 1);
			}
			participants[columnIndex] = new ParticipantColumn(gridPane, columnIndex);
		}

		VBox box = new VBox();

		Button cancelButton = new Button("cancel");
		cancelButton.setDisable(true);
		cancelButton.setOnAction(e -> {
			isToBeCanceled = true;
		});

		Label algorithmLabel = new Label("\n  ");
		List<Button> algorithmButtons = new LinkedList<>();
		LeggedRace[] leggedRaces = { new SequentialLeggedRace(), new ForallLeggedRace(), new ForallPhasedLeggedRace(),
				new ForallPhasedPointToPointLeggedRace() };
		for (LeggedRace leggedRace : leggedRaces) {
			Button button = new Button(leggedRace.getClass().getSimpleName());
			button.setOnAction(
					e -> run(leggedRace, participants, gridPane, algorithmLabel, algorithmButtons, cancelButton));
			algorithmButtons.add(button);
		}
		box.setSpacing(2.0);
		box.getChildren().addAll(algorithmButtons);
		box.getChildren().add(new Separator());
		box.getChildren().add(algorithmLabel);
		box.getChildren().add(cancelButton);

		BorderPane.setMargin(box, new Insets(0, 10, 0, 10));

		AnchorPane anchorPane = new AnchorPane();
		anchorPane.getChildren().addAll(label0, labelNMinus1);
		anchorPane.setStyle("-fx-background-color: black;");
		AnchorPane.setLeftAnchor(label0, 2.0);
		AnchorPane.setRightAnchor(labelNMinus1, 2.0);

		BorderPane innerBorderPane = new BorderPane();
		innerBorderPane.setTop(anchorPane);
		innerBorderPane.setCenter(gridPane);

		borderPane.setRight(box);
		borderPane.setCenter(innerBorderPane);

		Scene scene = new Scene(borderPane);
		primaryStage.setTitle("Legged Race");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launchApp(() -> {
		});
		launch(args);
	}
}
