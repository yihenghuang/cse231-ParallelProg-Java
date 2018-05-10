/*******************************************************************************
 * Copyright (C) 2016-2018 Dennis Cosgrove
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

package connectfour.studio;

import connectfour.core.Board;
import connectfour.core.ColumnEvaluationPair;
import connectfour.core.Config;

/**
 * @author Yiheng Huang
 * @author Finn Voichick
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class SequentialConnectFour {
	public static ColumnEvaluationPair negamax(Board board, Config config, int currentDepth) {
		// base case
		if (currentDepth == config.getMaxDepth() || board.isDone()) {
			return new ColumnEvaluationPair(0, config.getHeuristic().evaluate(board));
		}

		ColumnEvaluationPair[] storage = new ColumnEvaluationPair[board.WIDTH];

		for (Integer i : board.getValidPlays()) {
			Board next_board = board.createNextBoard(i);
			storage[i] = new ColumnEvaluationPair(i, -negamax(next_board, config, currentDepth + 1).getEvaluation());
		}

		double max_val = -100;
		int max_col = 0;
		for (ColumnEvaluationPair p : storage) {
			if (p.getEvaluation() > max_val) {
				max_col = p.getColumn();
				max_val = p.getEvaluation();
			}
		}
		return storage[max_col];
	}
}
