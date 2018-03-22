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
package nqueens.viz.solution;

import static edu.wustl.cse231s.v5.V5.launchApp;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang3.mutable.MutableInt;

import edu.wustl.cse231s.v5.options.SystemPropertiesOption;
import edu.wustl.cse231s.viz.RuntimeInterruptedException;
import edu.wustl.cse231s.viz.VizApp;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import nqueens.core.ImmutableQueenLocations;
import nqueens.core.MutableQueenLocations;
import nqueens.instructor.InstructorNQueensTestUtils;
import nqueens.lab.ParallelNQueens;
import nqueens.lab.SequentialNQueens;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class NQueensVizApp extends VizApp {
	private static enum SolutionCounter {
		STUDENT_SEQUENTIAL("student's Sequential Solution") {
			@Override
			public int countSolutions(NQueensVizApp app) {
				MutableQueenLocations queenLocations = InstructorNQueensTestUtils.createVizMutableQueenLocations(app);
				return SequentialNQueens.countSolutions(queenLocations);
			}
		},
		STUDENT_PARALLEL("student's (Serialized) Parallel Solution") {
			@Override
			public int countSolutions(NQueensVizApp app) {
				MutableInt result = new MutableInt();
				launchApp(new SystemPropertiesOption.Builder().isLinearized(true).build(), () -> {
					ImmutableQueenLocations queenLocations = InstructorNQueensTestUtils
							.createVizImmutableQueenLocations(app);
					result.setValue(ParallelNQueens.countSolutions(queenLocations));
				});
				return result.getValue();
			}
		},
		INSTRUCTOR("instructor's Solution") {
			@Override
			public int countSolutions(NQueensVizApp app) {
				MutableQueenLocations queenLocations = InstructorNQueensTestUtils.createVizMutableQueenLocations(app);
				return InstructorNQueensTestUtils.countSolutions(queenLocations);
			}
		};

		private SolutionCounter(String repr) {
			this.repr = repr;
		}

		public abstract int countSolutions(NQueensVizApp app);

		@Override
		public String toString() {
			return this.repr;
		}

		private final String repr;
	};

	private static enum StepOption {
		SHOW_ALL_STEPS, SHOW_THREAT_FREE_STEPS_ONLY, SHOW_SOLUTIONS_ONLY
	};

	public void cancelIfAppropriate() {
		if (Thread.currentThread().isInterrupted()) {
			// throw new InterruptedException();
			throw new RuntimeInterruptedException();
		}
	}

	@Override
	protected void resetIfNecessary() {
		this.updateQueens(new int[8], -1);
	}

	private void updateQueens(int[] queens, int row) {
		Platform.runLater(new UpdateQueensRunnable(this.chessboardPane, queens, row + 1));
	}

	@Override
	protected void solve() {
		solutionCount = 0;
		SolutionCounter solutionCounter = this.solutionCounterComboBox.getValue();
		int count = solutionCounter.countSolutions(this);
		// todo: clear
		System.out.println(count);
	}

	@Override
	protected Collection<Node> getBonusNodesToEnableOnThreadStart() {
		return Arrays.asList();
	}

	@Override
	protected Collection<Node> getBonusNodesToDisableOnThreadStart() {
		return Arrays.asList(solutionCounterComboBox);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		super.start(primaryStage);
		this.chessboardPane = new ChessboardPane(8);

		final int PADDING = 8;
		BorderPane root = new BorderPane();
		root.setCenter(chessboardPane);
		root.setPadding(new Insets(PADDING));

		ObservableList<SolutionCounter> solutionCounterOptions = FXCollections
				.observableArrayList(SolutionCounter.values());
		this.solutionCounterComboBox = new ComboBox<>(solutionCounterOptions);
		this.solutionCounterComboBox.setValue(SolutionCounter.STUDENT_SEQUENTIAL);

		ObservableList<StepOption> stepOptions = FXCollections.observableArrayList(StepOption.values());
		this.stepOptionComboBox = new ComboBox<>(stepOptions);
		this.stepOptionComboBox.setValue(StepOption.SHOW_ALL_STEPS);

		Region spacerA = new Region();
		spacerA.setMinWidth(10.0);
		Region spacerB = new Region();
		spacerB.setMinWidth(10.0);
		Region spacerC = new Region();
		spacerC.setMinWidth(10.0);
		FlowPane controls = new FlowPane();
		controls.setHgap(2.0);
		controls.getChildren().addAll(this.solutionCounterComboBox, this.stepOptionComboBox, spacerA, getSolveButton(),
				getStepButton(), spacerB, getPauseButton(), getResumeButton(), spacerC, getCancelButton());

		root.setTop(controls);

		Scene scene = new Scene(root);

		primaryStage.sizeToScene();
		primaryStage.setTitle("N-Queens");
		primaryStage.setScene(scene);
		primaryStage.show();

		primaryStage.setOnCloseRequest(e -> {
			Platform.exit();
			System.exit(0);
		});
	}

	private ChessboardPane chessboardPane;
	private ComboBox<SolutionCounter> solutionCounterComboBox;
	private ComboBox<StepOption> stepOptionComboBox;
	private int solutionCount;

	public void handleIsCandidateThreatFree(boolean result, int row, int candidateColumn, int[] locations) {
		if (result) {
			// pass
		} else {
			StepOption stepOption = this.stepOptionComboBox.getValue();
			if (stepOption == StepOption.SHOW_ALL_STEPS) {
				int[] buffer = new int[8];
				Arrays.fill(buffer, -1);
				if (locations.length > 0) {
					System.arraycopy(locations, 0, buffer, 0, row);
				}

				buffer[row] = candidateColumn;
				this.updateQueens(buffer, row);
				// TODO
				this.chessboardPane.updateQueenArrayLabelLater(buffer, row);
				buffer[row] = -1;
				this.takeFromQueueIfNecessary();
			}
		}
	}

	public void handleSetColumnOfQueenInRow(int row, int column, int[] locations) {
		StepOption stepOption = this.stepOptionComboBox.getValue();

		boolean isShowDesired;
		int[] buffer = new int[8];
		if (row == buffer.length - 1) {
			this.solutionCount++;
			this.chessboardPane.updateSolutionCountLabelLater(this.solutionCount);
			isShowDesired = stepOption != StepOption.SHOW_ALL_STEPS;
		} else {
			isShowDesired = stepOption == StepOption.SHOW_THREAT_FREE_STEPS_ONLY;
		}

		if (isShowDesired) {
			Arrays.fill(buffer, -1);
			if (locations.length > 0) {
				System.arraycopy(locations, 0, buffer, 0, row);
			}

			buffer[row] = column;
			this.updateQueens(buffer, row);
			this.chessboardPane.updateQueenArrayLabelLater(buffer, row);
			this.takeFromQueueIfNecessary();
		}
	}

	public static void main(String[] args) throws Exception {
		launch(args);
	}
}
