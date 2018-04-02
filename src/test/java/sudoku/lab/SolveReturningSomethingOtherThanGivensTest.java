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
package sudoku.lab;

import static edu.wustl.cse231s.v5.V5.launchAppWithReturn;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import backtrack.lab.rubric.BacktrackRubric;
import sudoku.core.ImmutableSudokuPuzzle;
import sudoku.instructor.InstructorSudokuTestUtils;
import sudoku.util.SearchSupplier;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@BacktrackRubric(BacktrackRubric.Category.PARALLEL_SUDOKU_CORRECTNESS)
public class SolveReturningSomethingOtherThanGivensTest {
	@Test
	public void test() {
		// source: http://norvig.com/hardest.txt
		String givens = "85...24..72......9..4.........1.7..23.5...9...4...........8..7..17..........36.4.";
		ImmutableSudokuPuzzle original = new DefaultImmutableSudokuPuzzle(
				InstructorSudokuTestUtils.createPeerAndUnitConstraintPropagator(), givens);
		ImmutableSudokuPuzzle solution = launchAppWithReturn(() -> {
			return ParallelSudoku.solve(original, SearchSupplier.INSTRUCTOR_FEWEST_OPTIONS_FIRST.get());
		});

		assertNotEquals(givens, solution.toString());
	}
}
