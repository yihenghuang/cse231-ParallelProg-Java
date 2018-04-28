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
package connectfour.studio.chooseyourownadventure;

import static edu.wustl.cse231s.v5.V5.launchAppWithReturn;

import connectfour.core.Board;
import connectfour.core.ColumnEvaluationPair;
import connectfour.core.Config;
import connectfour.studio.SequentialConnectFour;
import connectfour.studio.chooseyourownadventure.forall.ParallelForallConnectFour;
import connectfour.studio.chooseyourownadventure.futures.ParallelFuturesConnectFour;
import connectfour.studio.chooseyourownadventure.recursivetasks.NegamaxTask;

/**
 * @author Finn Voichick
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public enum Adventure {
	SEQUENTIAL_ONLY() {
		@Override
		public ColumnEvaluationPair chooseAdventure(Board board, Config config) {
			return SequentialConnectFour.negamax(board, config, 0);
		}

		@Override
		public boolean isParallel() {
			return false;
		}
	},
	FORALL() {
		@Override
		public ColumnEvaluationPair chooseAdventure(Board board, Config config) {
			return launchAppWithReturn(() -> {
				return ParallelForallConnectFour.negamax(board, config, 0);
			});
		}

		@Override
		public boolean isParallel() {
			return true;
		}
	},
	FUTURES() {
		@Override
		public ColumnEvaluationPair chooseAdventure(Board board, Config config) {
			return launchAppWithReturn(() -> {
				return ParallelFuturesConnectFour.negamax(board, config, 0);
			});
		}

		@Override
		public boolean isParallel() {
			return true;
		}
	},
	RECURSIVE_TASKS() {
		@Override
		public ColumnEvaluationPair chooseAdventure(Board board, Config config) {
			return new NegamaxTask(board, config, 0).compute();
		}

		@Override
		public boolean isParallel() {
			return true;
		}
	};

	public abstract ColumnEvaluationPair chooseAdventure(Board board, Config config);

	public abstract boolean isParallel();
}
