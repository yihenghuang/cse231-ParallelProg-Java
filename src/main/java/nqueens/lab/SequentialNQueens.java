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
package nqueens.lab;

import static edu.wustl.cse231s.v5.V5.doWork;

import org.apache.commons.lang3.mutable.MutableInt;

import edu.wustl.cse231s.IntendedForStaticAccessOnlyError;
import nqueens.core.MutableQueenLocations;

/**
 * @author Yiheng Huang
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class SequentialNQueens {
	private SequentialNQueens() {
		throw new IntendedForStaticAccessOnlyError();
	}

	/**
	 * For sequential n-queens, implement the method placeQueenInRow. Note that
	 * countSolutions contains a call to placeQueenInRow(count, queenLocations, 0),
	 * and countSolutions is called in TestNQueens. This method is recursive; that
	 * is, it should place a queen in the given row and then call itself on the next
	 * row.
	 * 
	 * @param count
	 *            An instance of MutableInt for keeping track of how many solutions
	 *            you find. Increment it every time you find a solution.
	 * 
	 * @param queenLocations
	 *            A representation of the chess board containing the queens. For
	 *            more information about the MutableQueenLocations class, ctrl-click
	 *            or command-click on "MutableQueenLocations" below.
	 * 
	 * @param row
	 *            The index of the row you are placing a queen into.
	 * 
	 */
	private static void placeQueenInRow(MutableInt count, MutableQueenLocations queenLocations, int row) {

		doWork(1);

		for (int col = 0; col < queenLocations.getBoardSize(); col++) {

			if (queenLocations.isCandidateThreatFree(row, col)) {

				if (row == queenLocations.getBoardSize() - 1) {
					count.increment();
				} else {
					queenLocations.setColumnOfQueenInRow(row, col);
					placeQueenInRow(count, queenLocations, row + 1);
				}
			}
		}

	}

	public static int countSolutions(MutableQueenLocations queenLocations) {
		MutableInt count = new MutableInt(0);
		placeQueenInRow(count, queenLocations, 0);
		return count.intValue();
	}
}
