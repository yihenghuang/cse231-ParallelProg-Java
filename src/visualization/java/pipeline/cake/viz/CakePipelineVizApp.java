/*******************************************************************************
 * Copyright (C) 2016-2018 Dennis Cosgrove
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

package pipeline.cake.viz;

import static edu.wustl.cse231s.v5.V5.launchApp;

import java.util.Arrays;

import org.apache.commons.lang3.mutable.MutableBoolean;

import edu.wustl.cse231s.color.ColorUtil;
import edu.wustl.cse231s.timing.ImmutableTimer;
import edu.wustl.cse231s.v5.options.SystemPropertiesOption;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import phaser.viz.StepCell;
import pipeline.cake.core.BakedCake;
import pipeline.cake.core.Baker;
import pipeline.cake.core.IcedCake;
import pipeline.cake.core.Icer;
import pipeline.cake.core.MixedIngredients;
import pipeline.cake.core.Mixer;
import pipeline.cake.studio.CakePipeline;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class CakePipelineVizApp extends Application {
	private static final int CAKE_COUNT = 12;
	private static final Color[] perRowColors = ColorUtil.getColorPalette12();
	// http://mkweb.bcgsc.ca/colorblind/img/colorblindness.palettes.simple.png
	private static final Color[] perColumnColors = { Color.web("fae6be"), Color.web("be8c3c"), Color.web("8214a0") };

	private static final class TaskColumn {
		private final VBox vBox;
		private final int columnIndex;
		private final Label titleLabel;
		private final StepCell[] stepCells;

		public TaskColumn(String title, int columnIndex) {
			this.vBox = new VBox();
			this.columnIndex = columnIndex;

			this.stepCells = new StepCell[CAKE_COUNT];
			for (int i = 0; i < this.stepCells.length; i++) {
				this.stepCells[i] = new StepCell(() -> {
					return Duration.seconds(0.5);
				}, () -> {
					return isToBeCanceled;
				});
			}

			Font titleFont = Font.font("Serif", FontWeight.BOLD, 18);
			this.titleLabel = new Label(title);
			this.titleLabel.setFont(titleFont);
			this.vBox.getChildren().add(this.titleLabel);
			this.vBox.getChildren().addAll(Arrays.asList(this.stepCells));
		}

		public VBox getVBox() {
			return vBox;
		}

		public void updateCellColors(boolean isPerTask) {
			int rowIndex = 0;
			for (StepCell stepCell : stepCells) {
				Color color = isPerTask ? perColumnColors[this.columnIndex] : perRowColors[rowIndex];
				stepCell.setStyle("-fx-accent: " + ColorUtil.toWeb(color) + ";");
				rowIndex++;
			}
			this.titleLabel.setTextFill(isPerTask ? perColumnColors[this.columnIndex] : Color.WHITE);
		}

		public void execute(int stepIndex) {
			stepCells[stepIndex].setTaken();
		}
	}

	private static boolean isToBeCanceled = false;
	private static Thread threadToInterrupt;

	private static void run(TaskColumn[] taskColumns, Button runButton, Button cancelButton) {
		for (TaskColumn taskColumn : taskColumns) {
			for (Node node : taskColumn.vBox.getChildren()) {
				if (node instanceof StepCell) {
					StepCell stepCell = (StepCell) node;
					stepCell.reset();
				}
			}
		}
		runButton.setDisable(true);
		cancelButton.setDisable(false);
		isToBeCanceled = false;
		threadToInterrupt = new Thread(() -> {
			try {
				ImmutableTimer timer = new ImmutableTimer("");
				MutableBoolean wasCanceled = new MutableBoolean(false);
				launchApp(new SystemPropertiesOption.Builder().numWorkerThreads(3).build(), () -> {
					try {
						class TestMixer implements Mixer {
							@Override
							public MixedIngredients mix(int cakeIndex) {
								MixedIngredients mixedIngredients = new MixedIngredients(cakeIndex);
								taskColumns[0].execute(cakeIndex);
								return mixedIngredients;
							}
						}

						class TestBaker implements Baker {
							@Override
							public BakedCake bake(int cakeIndex, MixedIngredients mixedIngredients) {
								BakedCake bakedCake = new BakedCake(cakeIndex, mixedIngredients);
								taskColumns[1].execute(cakeIndex);
								return bakedCake;
							}
						}

						class TestIcer implements Icer {
							@Override
							public IcedCake ice(int cakeIndex, BakedCake bakedCake) {
								IcedCake icedCake = new IcedCake(cakeIndex, bakedCake);
								taskColumns[2].execute(cakeIndex);
								return icedCake;
							}
						}

						TestMixer mixer = new TestMixer();
						TestBaker baker = new TestBaker();
						TestIcer icer = new TestIcer();
						CakePipeline.mixBakeAndIceCakes(mixer, baker, icer, CAKE_COUNT);
					} catch (InterruptedException ie) {
						wasCanceled.setTrue();
					}
				});
				if (wasCanceled.booleanValue()) {
					System.out.println("canceled.");
				} else {
					timer.markAndPrintResults();
				}
			} finally {
				Platform.runLater(() -> {
					runButton.setDisable(false);
					cancelButton.setDisable(true);
				});
			}
		});
		threadToInterrupt.start();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		String[] titles = { "Mixer", "Baker", "Icer" };
		TaskColumn[] taskColumns = new TaskColumn[3];
		for (int columnIndex = 0; columnIndex < 3; columnIndex++) {
			taskColumns[columnIndex] = new TaskColumn(titles[columnIndex], columnIndex);

			VBox box = taskColumns[columnIndex].getVBox();
			box.setStyle("-fx-background-color: black; -fx-padding: 2; -fx-hgap: 2; -fx-vgap: 2; ");
		}

		ToggleGroup colorToggleGroup = new ToggleGroup();
		RadioButton taskRB = new RadioButton("color per task");
		taskRB.setToggleGroup(colorToggleGroup);
		RadioButton cakeRB = new RadioButton("color per cake");
		cakeRB.setToggleGroup(colorToggleGroup);

		colorToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
			for (TaskColumn taskColumn : taskColumns) {
				taskColumn.updateCellColors(newValue == taskRB);
			}
		});

		taskRB.setSelected(true);
		VBox buttonBox = new VBox();

		Button cancelButton = new Button("cancel");
		cancelButton.setDisable(true);
		cancelButton.setOnAction(e -> {
			isToBeCanceled = true;
		});

		Button button = new Button("mix, bake, ice cakes");
		button.setOnAction(e -> {
			run(taskColumns, button, cancelButton);
		});

		Region spacer = new Region();
		spacer.setMinHeight(10);

		buttonBox.setSpacing(2.0);
		buttonBox.getChildren().add(button);
		buttonBox.getChildren().add(cancelButton);
		buttonBox.getChildren().add(spacer);
		buttonBox.getChildren().add(taskRB);
		buttonBox.getChildren().add(cakeRB);
		buttonBox.setPadding(new Insets(10, 10, 10, 10));

		HBox hBox = new HBox();
		for (TaskColumn taskColumn : taskColumns) {
			hBox.getChildren().add(taskColumn.vBox);
		}
		hBox.getChildren().add(buttonBox);

		Scene scene = new Scene(hBox);
		primaryStage.setTitle("Cake Pipeline");
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest((e) -> {
			Platform.exit();
			// TODO
			if (threadToInterrupt != null) {
				isToBeCanceled = true;
				threadToInterrupt.interrupt();
				System.exit(0);
			}
		});
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
