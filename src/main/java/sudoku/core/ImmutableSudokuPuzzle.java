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

/**
 * An unmodifiable sudoku board. Because the values in the board cannot change,
 * the only way to modify an <code>ImmutableSudokuPuzzle</code> is to create a
 * new one using the {@link #createNext(Square, int)} method.
 * 
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public interface ImmutableSudokuPuzzle extends SudokuPuzzle {

	/**
	 * Creates a new puzzle from the provided other puzzle. The new
	 * {@code ImmutableSudokuPuzzle} will have values identical to this one, except
	 * that the given square will be filled in with the given value (and any
	 * propagated constraints).
	 * 
	 * @param square
	 *            the (currently unfilled) square to fill.
	 * @param value
	 *            the value to put in square
	 * @return a new puzzle with the modified square
	 */
	ImmutableSudokuPuzzle createNext(Square square, int value);
}
