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

import java.io.IOException;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import backtrack.lab.rubric.BacktrackRubric;
import edu.wustl.cse231s.junit.JUnitUtils;
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
@BacktrackRubric(BacktrackRubric.Category.PARALLEL_N_QUEENS_CORRECTNESS)
public class ParallelNQueensCorrectnessTest extends AbstractNQueensCorrectnessTest {
	private final int boardSize;

	public ParallelNQueensCorrectnessTest(int boardSize) throws IOException {
		this.boardSize = boardSize;
	}

	@Test
	public void test() {
		test(boardSize, () -> {
			ImmutableQueenLocations queenLocations = new DefaultImmutableQueenLocations(boardSize);
			return ParallelNQueens.countSolutions(queenLocations);
		});
	}

	@Parameters(name = "boardSize={0}")
	public static Collection<Object[]> getConstructorArguments() {
		return JUnitUtils.toParameterizedArguments(NQueensCorrectnessUtils.getBoardSizes());
	}
}
