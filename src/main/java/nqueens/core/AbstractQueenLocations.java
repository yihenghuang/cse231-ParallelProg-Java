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
package nqueens.core;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public abstract class AbstractQueenLocations implements QueenLocations {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCandidateThreatFree(int row, int candidateColumn) {
		// check all previous rows for threatening queens
		for (int i = 0; i < row; i++) {
			int columnOfQueenInRowI = this.getColumnOfQueenInRow(i);
			// is in same column
			if (columnOfQueenInRowI == candidateColumn) {
				return false;
			}
			// is in same diagonal A
			if (row - i == columnOfQueenInRowI - candidateColumn) {
				return false;
			}
			// is in same diagonal B
			if (row - i == candidateColumn - columnOfQueenInRowI) {
				return false;
			}
		}
		return true;
	}

	@Override
	public final String toString() {
		StringBuilder result = new StringBuilder();

		int boardSize = this.getBoardSize();
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (this.getColumnOfQueenInRow(i) == j) {
					result.append("Q ");
				} else {
					result.append("* ");
				}
			}
			result.append('\n');
		}

		return result.toString();
	}
}
