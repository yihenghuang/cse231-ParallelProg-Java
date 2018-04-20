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
package sudoku.lab;

import net.jcip.annotations.Immutable;
import sudoku.core.Square;
import sudoku.core.SquareSearchAlgorithm;
import sudoku.core.SudokuPuzzle;

/**
 * @author Yiheng Huang
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@Immutable
public class RowMajorSquareSearchAlgorithm implements SquareSearchAlgorithm {
	/**
	 * A square search algorithm based on
	 * <a href="https://en.wikipedia.org/wiki/Row-_and_column-major_order">
	 * row-major ordering</a>. This implementation should simply traverse the board
	 * as defined by Square's values method and return the first square that is
	 * unfilled. This algorithm should return a square with no options if it exists
	 * (meaning that the puzzle is impossible to solve), or it should return null,
	 * if the puzzle is already completely solved.
	 */
	@Override
	public Square selectNextUnfilledSquare(SudokuPuzzle puzzle) {

		Square[] set = Square.values();
		for (Square each : set) {
			if (puzzle.getValue(each) == 0) {
				return each;
			}
		}
		return null;
	}
}
