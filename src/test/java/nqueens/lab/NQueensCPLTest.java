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

import static edu.wustl.cse231s.v5.V5.abstractMetrics;
import static edu.wustl.cse231s.v5.V5.launchApp;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;

import java.io.IOException;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import backtrack.lab.rubric.BacktrackRubric;
import edu.wustl.cse231s.junit.JUnitUtils;
import edu.wustl.cse231s.v5.options.SystemPropertiesOption;
import nqueens.core.ImmutableQueenLocations;
import nqueens.core.NQueensCorrectnessUtils;
import nqueens.lab.DefaultImmutableQueenLocations;
import nqueens.lab.ParallelNQueens;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link ParallelNQueens}
 */
@RunWith(Parameterized.class)
@BacktrackRubric(BacktrackRubric.Category.PARALLEL_N_QUEENS_PARALLELISM)
public class NQueensCPLTest {
	private final int boardSize;

	public NQueensCPLTest(int boardSize) throws IOException {
		this.boardSize = boardSize;
	}

	@Parameters(name = "boardSize={0}")
	public static Collection<Object[]> getConstructorArguments() {
		return JUnitUtils.toParameterizedArguments(NQueensCorrectnessUtils.getBoardSizes());
	}

	@Test
	public void testCPL() {
		int truthAndBeauty = NQueensCorrectnessUtils.getSolutionCountForBoardSize(boardSize);
		launchApp(new SystemPropertiesOption.Builder().isAbstractMetricsDesired(true).build(), () -> {
			ImmutableQueenLocations queenLocations = new DefaultImmutableQueenLocations(boardSize);
			int result = ParallelNQueens.countSolutions(queenLocations);
			Assert.assertEquals("Your solution count did not match the true count", truthAndBeauty, result);
		}, () -> {
			long actualCPL = abstractMetrics().criticalPathLength();
			Assert.assertNotEquals("place a doWork(1) at the top of placeQueenInRow().  Expected: boardSize ("
					+ boardSize + ") or boardSize+1 (" + (boardSize + 1) + ")", actualCPL, 0L);
			Assert.assertThat("expected CPL: boardSize or boardSize+1", (int) actualCPL,
					anyOf(is(boardSize), is(boardSize + 1)));
		});
	}
}
