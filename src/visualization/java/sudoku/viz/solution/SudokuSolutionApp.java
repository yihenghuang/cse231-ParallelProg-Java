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
package sudoku.viz.solution;

import static edu.wustl.cse231s.v5.V5.launchApp;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import edu.wustl.cse231s.fx.FxUtils;
import edu.wustl.cse231s.v5.options.SystemPropertiesOption;
import edu.wustl.cse231s.viz.RuntimeInterruptedException;
import edu.wustl.cse231s.viz.VizApp;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sudoku.core.ConstraintPropagator;
import sudoku.core.ImmutableSudokuPuzzle;
import sudoku.core.Square;
import sudoku.core.SquareSearchAlgorithm;
import sudoku.instructor.InstructorSudokuTestUtils;
import sudoku.lab.ConstraintPropagatorSupplier;
import sudoku.lab.ParallelSudoku;
import sudoku.viz.common.FxGivensUtils;
import sudoku.viz.common.FxSudokuPane;
import sudoku.viz.common.FxSudokuSceneUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class SudokuSolutionApp extends VizApp {
	private String givens;
	private ImmutableSudokuPuzzle initialPuzzle;

	private FxSudokuPane mainPane;

	private final StringProperty[][] propertyMatrix = new StringProperty[9][9];
	private ComboBox<Solver> solverComboBox;
	private ComboBox<Propagator> propagatorComboBox;
	private ComboBox<SquareSearchAlgorithmSupplier> searchComboBox;

	public SudokuSolutionApp() {
		for (int row = 0; row < this.propertyMatrix.length; row++) {
			for (int column = 0; column < this.propertyMatrix[row].length; column++) {
				this.propertyMatrix[row][column] = new SimpleStringProperty();
			}
		}
	}

	private FxSudokuPane createMainPane() {
		return new FxSudokuPane() {
			@Override
			protected Node createSquareNode(Square square) {
				Label label = new Label();
				label.setId("celltext");
				this.squareToLabelMap.put(square, label);
				return label;
			}

			private StringProperty getProperty(Square square) {
				int row = square.getRow();
				int column = square.getColumn();
				return SudokuSolutionApp.this.propertyMatrix[row][column];
			}

			@Override
			protected void updateSquare(ImmutableSudokuPuzzle puzzle, Square square) {
				Label label = this.squareToLabelMap.get(square);

				StringProperty property = this.getProperty(square);
				int value = puzzle.getValue(square);
				property.setValue(value > 0 ? Integer.toString(value) : "-");
				label.textProperty().unbind();
				label.textProperty().bind(property);
				label.setTextFill(SudokuSolutionApp.this.isGiven(square) ? Color.BLACK : Color.DARKGRAY);
			}

			private final Map<Square, Label> squareToLabelMap = new HashMap<>();
		};
	}

	private static enum Solver {
		STUDENT("student's Solver") {
			@Override
			public ImmutableSudokuPuzzle solve(ImmutableSudokuPuzzle puzzle,
					SquareSearchAlgorithm squareSearchAlgorithm) throws InterruptedException, ExecutionException {
				return ParallelSudoku.solve(puzzle, squareSearchAlgorithm);
			}
		},
		INSTRUCTOR("instructor's Solver") {
			@Override
			public ImmutableSudokuPuzzle solve(ImmutableSudokuPuzzle puzzle,
					SquareSearchAlgorithm squareSearchAlgorithm) throws InterruptedException, ExecutionException {
				return InstructorSudokuTestUtils.solve(puzzle, squareSearchAlgorithm);
			}
		};

		private final String repr;

		private Solver(String repr) {
			this.repr = repr;
		}

		public abstract ImmutableSudokuPuzzle solve(ImmutableSudokuPuzzle puzzle,
				SquareSearchAlgorithm squareSearchAlgorithm) throws InterruptedException, ExecutionException;

		@Override
		public String toString() {
			return this.repr;
		}

	};

	private static enum Propagator {
		STUDENT_LAB("student's Propagator") {
			@Override
			public ConstraintPropagator createConstaintPropagator() {
				return new ConstraintPropagatorSupplier().get();
			}
		},
		INSTRUCTOR_UNPROPAGATED("instructor's UNPROPAGATED") {
			@Override
			public ConstraintPropagator createConstaintPropagator() {
				return null;
			}
		},
		INSTRUCTOR_PEER_ONLY("instructor's PEER_ONLY Propagator") {
			@Override
			public ConstraintPropagator createConstaintPropagator() {
				return InstructorSudokuTestUtils.createPeerOnlyConstraintPropagator();
			}
		},
		INSTRUCTOR_PEER_AND_UNIT("instructor's PEER_AND_UNIT Propagator") {
			@Override
			public ConstraintPropagator createConstaintPropagator() {
				return InstructorSudokuTestUtils.createPeerAndUnitConstraintPropagator();
			}
		};
		private final String repr;

		private Propagator(String repr) {
			this.repr = repr;
		}

		public abstract ConstraintPropagator createConstaintPropagator();

		@Override
		public String toString() {
			return this.repr;
		}

	}

	public void handleCreateNextPuzzle(ImmutableSudokuPuzzle puzzle) throws RuntimeInterruptedException {
		Objects.requireNonNull(puzzle);
		if (Thread.currentThread().isInterrupted()) {
			throw new RuntimeInterruptedException();
		} else {
			Runnable runnable = () -> {
				for (Square s : Square.values()) {
					int row = s.getRow();
					int column = s.getColumn();
					this.propertyMatrix[row][column].set(toString(puzzle.getValue(s)));
				}
			};
			if (Platform.isFxApplicationThread()) {
				runnable.run();
			} else {
				try {
					FxUtils.runAndWait(runnable);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
				}
			}
		}
		this.takeFromQueueIfNecessary();
	}

	@Override
	protected Collection<Node> getBonusNodesToDisableOnThreadStart() {
		return null;
	}

	@Override
	protected Collection<Node> getBonusNodesToEnableOnThreadStart() {
		return null;
	}

	private void updateToInitial() {
		if (givens != null) {
			Propagator propagator = propagatorComboBox.getValue();
			ConstraintPropagator constraintPropagator = propagator.createConstaintPropagator();
			initialPuzzle = InstructorSudokuTestUtils.createPuzzle(givens, constraintPropagator, this);
			Platform.runLater(() -> {
				this.mainPane.setInitialPuzzle(initialPuzzle);
			});
		}
	}

	@Override
	protected void resetIfNecessary() {
		if (Platform.isFxApplicationThread()) {
			this.updateToInitial();
		} else {
			Platform.runLater(() -> updateToInitial());
		}
	}

	@Override
	protected void solve() {
		launchApp(new SystemPropertiesOption.Builder().isLinearized(true).build(), () -> {
			Solver sudokuSolver = this.solverComboBox.getValue();
			SquareSearchAlgorithmSupplier squareSearchAlgorithmSupplier = this.searchComboBox.getValue();

			this.updateToInitial();
			ImmutableSudokuPuzzle solution = sudokuSolver.solve(initialPuzzle, squareSearchAlgorithmSupplier.get());
			if (solution != null) {
				try {
					this.handleCreateNextPuzzle(solution);
				} catch (RuntimeInterruptedException rie) {
					System.out.println("canceled");
				}
			}
		});
	}

	private Node createControls(ComboBox<String> givensComboBox) {
		ObservableList<Solver> solverOptions = FXCollections.observableArrayList(Solver.values());
		this.solverComboBox = new ComboBox<>(solverOptions);

		ObservableList<SquareSearchAlgorithmSupplier> searchOptions = FXCollections
				.observableArrayList(SquareSearchAlgorithmSupplier.values());
		this.searchComboBox = new ComboBox<>(searchOptions);

		List<SquareSearchAlgorithmSupplier> unpropagatedSupportingSearches = Arrays.asList(
				SquareSearchAlgorithmSupplier.INSTRUCTOR_ROW_MAJOR,
				SquareSearchAlgorithmSupplier.INSTRUCTOR_FEWEST_OPTIONS_FIRST);

		ObservableList<Propagator> propagatorOptions = FXCollections.observableArrayList(Propagator.values());
		this.propagatorComboBox = new ComboBox<>(propagatorOptions);
		this.propagatorComboBox.getSelectionModel().selectedItemProperty()
				.addListener((options, oldValue, newValue) -> {
					if (newValue != null) {
						boolean isDisabled = newValue == Propagator.INSTRUCTOR_UNPROPAGATED;
						if (isDisabled) {
							solverComboBox.setValue(Solver.INSTRUCTOR);
							solverComboBox.setItems(FXCollections.observableArrayList(Solver.INSTRUCTOR));
							SquareSearchAlgorithmSupplier search = searchComboBox.getValue();
							if (unpropagatedSupportingSearches.contains(search)) {
								// pass
							} else {
								searchComboBox.setValue(unpropagatedSupportingSearches.get(0));
							}
							searchComboBox.setItems(FXCollections.observableArrayList(unpropagatedSupportingSearches));
						} else {
							solverComboBox.setItems(FXCollections.observableArrayList(Solver.values()));
							searchComboBox.setItems(
									FXCollections.observableArrayList(SquareSearchAlgorithmSupplier.values()));
						}
						this.onCancel();
						this.updateToInitial();
					}
				});

		this.solverComboBox.setValue(Solver.STUDENT);
		this.propagatorComboBox.setValue(Propagator.STUDENT_LAB);
		this.searchComboBox.setValue(SquareSearchAlgorithmSupplier.STUDENT_FEWEST_OPTIONS_FIRST);

		GridPane result = new GridPane();
		result.add(givensComboBox, 0, 0, 8, 1);
		result.add(solverComboBox, 0, 1);
		result.add(propagatorComboBox, 1, 1);
		result.add(searchComboBox, 2, 1);
		result.add(this.getSolveButton(), 3, 1);
		result.add(this.getStepButton(), 4, 1);
		result.add(this.getPauseButton(), 5, 1);
		result.add(this.getResumeButton(), 6, 1);
		result.add(this.getCancelButton(), 7, 1);
		return result;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		super.start(primaryStage);
		ComboBox<String> givensComboBox = FxGivensUtils
				.createGivensComboBox((ObservableValue<? extends String> ov, String oldValue, String newValue) -> {
					this.setGivens(newValue);
				});

		BorderPane root = new BorderPane();

		Scene scene = new Scene(root);
		FxSudokuSceneUtils.addStylesheet(scene);
		this.mainPane = this.createMainPane();
		this.mainPane.init();

		root.setCenter(this.mainPane);

		Node controls = this.createControls(givensComboBox);
		if (controls != null) {
			root.setTop(controls);
		}

		givensComboBox.getSelectionModel().selectFirst();

		primaryStage.setTitle(this.getClass().getSimpleName());
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.show();

		// TODO: investigate
		Platform.runLater(() -> primaryStage.sizeToScene());

		primaryStage.setOnCloseRequest(e -> {
			Platform.exit();
			System.exit(0);
		});
	}

	public boolean isGiven(Square square) {
		return Character.isDigit(this.givens.charAt(square.getRow() * 9 + square.getColumn()));
	}

	private void setGivens(String givens) {
		this.givens = givens;
		this.onCancel();
		this.updateToInitial();
	}

	private static String toString(int value) {
		return Math.abs(value) > 0 ? Integer.toString(value) : " ";
	}

	public static void main(String[] args) {
		launch(args);
	}
}
