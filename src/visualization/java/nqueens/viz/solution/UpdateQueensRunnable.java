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

import java.io.IOException;

import edu.wustl.cse231s.fx.FxmlUtils;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;
import nqueens.viz.solution.resources.Resources;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class UpdateQueensRunnable implements Runnable {
	private static boolean isThreatFreeWithQueensInPreviousRows(int[] queens, int row, int column) {
		for (int i = 0; i < row; i++) {
			// is in same column
			if (queens[i] == column) {
				return false;
			}
			// is in same diagonal A
			if (row - i == queens[i] - column) {
				return false;
			}
			// is in same diagonal B
			if (row - i == column - queens[i]) {
				return false;
			}
		}
		return true;
	}

	public UpdateQueensRunnable(ChessboardPane pane, int[] srcQueens, int depth) {
		this.pane = pane;
		this.queens = new int[depth];
		System.arraycopy(srcQueens, 0, this.queens, 0, depth);
		this.queenScaleGroups = new Group[srcQueens.length];
		for (int i = 0; i < this.queenScaleGroups.length; i++) {
			try {
				Group queen = FxmlUtils.loadGroup(Resources.QUEEN_FXML);
				queen.getTransforms().add(new Scale(0.09, 0.09));
				this.queenScaleGroups[i] = new Group();
				this.queenScaleGroups[i].getChildren().add(queen);
			} catch (IOException ioe) {
				throw new RuntimeException(ioe);
			}
		}
	}

	private boolean isValid(int i, int row, int column) {
		if (this.queens[i] == column) {
			return false;
		}
		if (row - i == this.queens[i] - column) {
			return false;
		}
		if (row - i == column - this.queens[i]) {
			return false;
		}
		return true;
	}

	@Override
	public void run() {
		synchronized (this.pane) {
			for (int row = 0; row < this.pane.getQueenCount(); row++) {
				for (int column = 0; column < this.pane.getQueenCount(); column++) {
					StackPane squarePane = this.pane.getSquare(row, column);
					squarePane.getChildren().clear();
					ChessboardPane.setSquareStyleWithoutBorder(squarePane, row, column);
				}

				Label stackLabel = this.pane.getStackLabel(row);
				if (row < this.queens.length) {
					stackLabel.setText("placeQueenInRow( row=" + row + " )\n\tcolumn=" + this.queens[row]);
					stackLabel.setStyle("-fx-border-color: black; -fx-border-width: 1;");
				} else {
					stackLabel.setText("");
					stackLabel.setStyle("");
				}
			}

			if (this.queens.length > 0) {
				int _r = this.queens.length - 1;
				int _c = this.queens[_r];

				int r = 0;
				for (int c : this.queens) {
					StackPane squarePane = this.pane.getSquare(r, c);
					if(squarePane != null) {
						if (r == _r && c == _c) {
							ChessboardPane.setSquareStyleWithBorder(squarePane, r, c, "#8080FF");
						}
						squarePane.getChildren().add(this.queenScaleGroups[r]);

						Node queen = this.queenScaleGroups[r].getChildren().get(0);
						double opacity = 1;
						if (r < _r && isValid(r, _r, _c)) {
							// pass
						} else if (r == _r && isThreatFreeWithQueensInPreviousRows(this.queens, _r, _c)) {
							// pass
						} else {
							squarePane.setStyle("-fx-background-color: red;");
							opacity = 0.5;
						}
						queen.setOpacity(opacity);
					}
					r++;
				}
			}
		}
	}

	private final ChessboardPane pane;
	private final int[] queens;
	private final Group[] queenScaleGroups;
}
