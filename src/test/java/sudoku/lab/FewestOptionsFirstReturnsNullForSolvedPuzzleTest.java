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

import org.junit.Assert;
import org.junit.Test;

import backtrack.lab.rubric.BacktrackRubric;
import sudoku.core.ConstraintPropagator;
import sudoku.core.Square;
import sudoku.instructor.InstructorSudokuTestUtils;
import sudoku.lab.DefaultImmutableSudokuPuzzle;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link FewestOptionsFirstSquareSearchAlgorithm#selectNextUnfilledSquare(sudoku.core.SudokuPuzzle)}
 */
@BacktrackRubric(BacktrackRubric.Category.FEWEST_OPTIONS_FIRST_SEARCH)
public class FewestOptionsFirstReturnsNullForSolvedPuzzleTest {
	@Test
	public void testSolution() {
		// source of original givens (now solved): http://norvig.com/hardest.txt
		String solution = "128547639345869217679213548912486375784352196536791482891624753467935821253178964";
		ConstraintPropagator constraintPropagator = InstructorSudokuTestUtils.createPeerOnlyConstraintPropagator();
		DefaultImmutableSudokuPuzzle puzzle = new DefaultImmutableSudokuPuzzle(constraintPropagator, solution);
		Square square = new FewestOptionsFirstSquareSearchAlgorithm().selectNextUnfilledSquare(puzzle);
		Assert.assertNull("This is a complete puzzle, no square should have been selected but one was selected",
				square);
	}
}
