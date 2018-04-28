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

package nqueens.lab;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import backtrack.lab.rubric.BacktrackRubric;
import edu.wustl.cse231s.v5.bookkeep.BookkeepingUtils;
import edu.wustl.cse231s.v5.impl.BookkeepingV5Impl;
import nqueens.core.ImmutableQueenLocations;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@BacktrackRubric(BacktrackRubric.Category.PARALLEL_N_QUEENS_PARALLELISM)
public class ParallelNQueensTaskAndFinishTest {
	@Test
	public void test() {
		BookkeepingV5Impl bookkeep = BookkeepingUtils.bookkeep(() -> {
			ImmutableQueenLocations queenLocations = new DefaultImmutableQueenLocations(4);
			ParallelNQueens.countSolutions(queenLocations);
		});

		if (bookkeep.getTaskCount() > 0) {
			assertTrue("spawning tasks without ever invoking finish", (bookkeep.getNonAccumulatorFinishInvocationCount()
					+ bookkeep.getAccumulatorFinishInvocationCount()) > 0);
			assertEquals(0, bookkeep.getNonAccumulatorFinishInvocationCount());
			assertEquals(1, bookkeep.getAccumulatorFinishInvocationCount());
			assertEquals(1, bookkeep.getAccumulatorRegisterCount());
		}
	}
}
