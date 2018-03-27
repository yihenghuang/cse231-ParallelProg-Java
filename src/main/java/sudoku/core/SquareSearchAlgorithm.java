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
package sudoku.core;

import java.util.Iterator;

/**
 * An algorithm for choosing the next square to examine in a
 * {@link SudokuPuzzle}. This class can be used like an {@link Iterator}, except
 * that its {@link #selectNextUnfilledSquare(SudokuPuzzle)} method takes a
 * {@code SudokuPuzzle} as an argument, allowing it to be used with many
 * {@link ImmutableSudokuPuzzle}s.
 * 
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public interface SquareSearchAlgorithm {

	/**
	 * Selects the next square to examine in the given puzzle. This square will be
	 * unfilled. If there are no unfilled squares remaining, this method returns
	 * null.
	 * 
	 * @param puzzle
	 *            The sudoku puzzle to examine
	 * @return the next unfilled square to examine in the puzzle
	 */
	Square selectNextUnfilledSquare(SudokuPuzzle puzzle);

}
