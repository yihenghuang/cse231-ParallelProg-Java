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
package sudoku.viz.common;

import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import sudoku.core.ImmutableSudokuPuzzle;
import sudoku.core.Square;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public abstract class FxSudokuPane extends GridPane {
	public void init() throws Exception {
		for (int rowMacro = 0; rowMacro < 3; rowMacro++) {
			if (rowMacro > 0) {
				Separator separator = new Separator(Orientation.HORIZONTAL);
				GridPane.setColumnSpan(separator, 9 + 2);
				this.add(separator, 1, rowMacro * 4);
			}
			for (int colMacro = 0; colMacro < 3; colMacro++) {
				if (colMacro > 0) {
					Separator colSeparator = new Separator(Orientation.VERTICAL);
					GridPane.setRowSpan(colSeparator, 9 + 2);
					this.add(colSeparator, colMacro * 4, 1);
				}
				for (int r = 0; r < 3; r++) {
					int row = rowMacro*3+r;
					for (int c = 0; c < 3; c++) {
						int column = colMacro*3+c;
						Square square = Square.valueOf(row,column);
						Node squareNode = this.createSquareNode(square);
						this.add(squareNode, c + colMacro * 4 + 1, r + rowMacro * 4 + 1);
					}
				}
			}
		}
		for (int i = 0; i < 9; i++) {
			Label label = new Label(Character.toString((char) ('A' + i)));
			label.setId("headingtext");
			GridPane.setHalignment(label, HPos.CENTER);
			this.add(label, 0, 1 + i + i / 3);
		}

		for (int i = 0; i < 9; i++) {
			Label label = new Label(Integer.toString(i+1));
			label.setId("headingtext");
			this.add(label, 1 + i + i / 3, 0);
		}

		this.getStyleClass().add("sudoku-grid");
	}
	
	protected abstract Node createSquareNode( Square square );
	protected abstract void updateSquare( ImmutableSudokuPuzzle puzzle, Square square );
	public void setInitialPuzzle( ImmutableSudokuPuzzle puzzle ) {
		for( Square square : Square.values()) {
			this.updateSquare(puzzle, square);
		}
	}
}
