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
package sudoku.util;

import sudoku.core.Square;
import sudoku.core.SudokuPuzzle;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class SolutionUtils {
	private static boolean isCandidateValid(SudokuPuzzle puzzle, Square square, int value) {
		for (Square peer : square.getPeers()) {
			if (puzzle.getValue(peer) == value) {
				return false;
			}
		}
		return true;
	}

	public static boolean isCompletelyFilledIn(SudokuPuzzle puzzle) {
		for (Square square : Square.values()) {
			int value = puzzle.getValue(square);
			if (value != 0) {
				// pass
			} else {
				return false;
			}
		}
		return true;
	}

	public static boolean isCompletelyFilledInAndEachSquareIsValid(SudokuPuzzle puzzle) {
		for (Square square : Square.values()) {
			int value = puzzle.getValue(square);
			if (value != 0) {
				if (isCandidateValid(puzzle, square, value)) {
					// pass
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		return true;
	}

	public static boolean containsOriginal(SudokuPuzzle original, SudokuPuzzle solution) {
		for (Square square : Square.values()) {
			int originalValue = original.getValue(square);
			if (originalValue != 0) {
				int value = solution.getValue(square);
				if (value == originalValue) {
					// pass
				} else {
					return false;
				}
			}
		}
		return true;
	}
}
