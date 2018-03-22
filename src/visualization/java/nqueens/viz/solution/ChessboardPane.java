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

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class ChessboardPane extends GridPane {
	private static final int BORDER = 8;
	/* package-private */ static final int CELL_SIZE = 48;
	private static final int CELL_PLUS_BORDER_SIZE = CELL_SIZE + BORDER * 2;

	/* package-private */ static void setSquareStyle(StackPane squarePane, int rowIndex, int columnIndex,
			String borderColorName, boolean isBorderColorDesired) {
		String colorName = ((rowIndex + columnIndex) % 2 == 0) ? "#d18b47" : "#ffce9e";
		StringBuilder sb = new StringBuilder();
		sb.append("-fx-background-color: ");
		sb.append(colorName);
		sb.append(";");
		sb.append("-fx-border-width: ");
		sb.append(BORDER);
		sb.append(";");
		sb.append("-fx-border-color: ");
		if (isBorderColorDesired) {
			sb.append(borderColorName);
		} else {
			sb.append(colorName);
		}
		sb.append(";");
		squarePane.setStyle(sb.toString());
	}

	/* package-private */ static void setSquareStyleWithBorder(StackPane squarePane, int rowIndex, int columnIndex,
			String otherColorName) {
		setSquareStyle(squarePane, rowIndex, columnIndex, otherColorName, true);
	}

	/* package-private */ static void setSquareStyleWithoutBorder(StackPane squarePane, int rowIndex, int columnIndex) {
		setSquareStyle(squarePane, rowIndex, columnIndex, /* don't care */null, false);
	}

	public ChessboardPane(int queenCount) {
		Font monospacedFont = Font.font("Monospaced");
		this.queenCount = queenCount;
		this.solutionCountLabel = new Label("at_count: [0]");
		this.queenArrayLabel = new Label("  queens: [-,-,-,-,-,-,-,-]");
		this.actionLabel = new Label("");
		this.solutionCountLabel.setFont(monospacedFont);
		this.queenArrayLabel.setFont(monospacedFont);

		this.add(new Label(""), 0, 0);
		for (int columnIndex = 0; columnIndex < this.queenCount; columnIndex++) {
			Label columnLabel = new Label("column=" + columnIndex);
			this.add(columnLabel, 1 + columnIndex, 0);
			GridPane.setHalignment(columnLabel, HPos.CENTER);
		}
		Insets runtimeStackInsets = new Insets(0, 0, 0, 16);
		Label runtimeStackLabel = new Label("runtime stack");
		this.add(runtimeStackLabel, this.queenCount + 1, 0);
		GridPane.setMargin(runtimeStackLabel, runtimeStackInsets);
		for (int rowIndex = 0; rowIndex < this.queenCount; rowIndex++) {
			Label rowLabel = new Label("row=" + this.flipY(rowIndex) + " ");
			this.add(rowLabel, 0, rowIndex + 1);
			for (int columnIndex = 0; columnIndex < this.queenCount; columnIndex++) {
				StackPane squarePane = new StackPane();
				int y = (this.queenCount - rowIndex) - 1;
				setSquareStyleWithoutBorder(squarePane, y, columnIndex);
				squarePane.setPrefSize(CELL_PLUS_BORDER_SIZE, CELL_PLUS_BORDER_SIZE);
				this.add(squarePane, 1 + columnIndex, rowIndex + 1);
			}
			Label stackLabel = new Label();
			stackLabel.setFont(monospacedFont);
			this.add(stackLabel, this.queenCount + 1, rowIndex + 1);
			GridPane.setMargin(stackLabel, runtimeStackInsets);
		}

		this.getColumnConstraints().add(new ColumnConstraints());
		for (int columnIndex = 0; columnIndex < this.queenCount; columnIndex++) {
			this.getColumnConstraints().add(new ColumnConstraints());
		}
		ColumnConstraints columnConstraints = new ColumnConstraints();
		columnConstraints.setPrefWidth(256);
		this.getColumnConstraints().add(columnConstraints);

		this.add(this.actionLabel, 1, this.queenCount + 2);
		GridPane.setColumnSpan(this.actionLabel, this.queenCount);

		this.add(this.solutionCountLabel, this.queenCount + 1, this.queenCount + 2);
		GridPane.setMargin(this.solutionCountLabel, runtimeStackInsets);
		this.add(this.queenArrayLabel, this.queenCount + 1, this.queenCount + 3);
		GridPane.setMargin(this.queenArrayLabel, runtimeStackInsets);
	}

	public StackPane getSquare(int row, int column) {
		if( column >= 0 ) {
			return (StackPane) this.getChildren().get(((this.flipY(row) + 1) * (this.queenCount + 2)) + column + 1);
		} else {
			return null;
		}
	}

	public Label getStackLabel(int row) {
		return (Label) this.getChildren().get((((this.flipY(row) + 1) * (this.queenCount + 2)) + this.queenCount + 1));
	}

	public int getQueenCount() {
		return this.queenCount;
	}

	public void updateSolutionCountLabelLater(int solutionCount) {
		Platform.runLater(() -> {
			this.solutionCountLabel.setText("at_count: [" + solutionCount + "]");
		});
	}

	public void updateQueenArrayLabelLater(int[] queens, int row) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		String prefix = "";
		for (int i = 0; i <= row; i++) {
			sb.append(prefix);
			sb.append(queens[i]);
			prefix = ",";
		}
		for (int i = row + 1; i < queens.length; i++) {
			sb.append(prefix);
			sb.append("-");
			prefix = ",";
		}
		sb.append("]");
		Platform.runLater(() -> {
			this.queenArrayLabel.setText("  queens: " + sb.toString());
		});
	}

	public void updateStatusLabelLater(String status) {
		Platform.runLater(() -> {
			this.actionLabel.setText(status);
		});
	}

	private int flipY(int rowIndex) {
		return (this.queenCount - rowIndex) - 1;
	}

	private final int queenCount;
	private final Label solutionCountLabel;
	private final Label queenArrayLabel;
	private final Label actionLabel;
}
