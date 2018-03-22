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

import static edu.wustl.cse231s.v5.V5.launchApp;

import org.junit.Test;

import backtrack.lab.rubric.BacktrackRubric;
import nqueens.core.ImmutableQueenLocations;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link ParallelNQueens}
 */
@BacktrackRubric(BacktrackRubric.Category.PARALLEL_N_QUEENS_CORRECTNESS)
public class ParallelNQueensRegisterFinishAccumulatorTest {
	@Test
	public void test() {
		launchApp(() -> {
			ImmutableQueenLocations queenLocations = new DefaultImmutableQueenLocations(4);
			throw new RuntimeException("TODO");
			// try {
			// ParallelNQueens.countSolutions(queenLocations);
			// } catch (Throwable t) {
			// t.printStackTrace();
			// }
		});
	}
}
