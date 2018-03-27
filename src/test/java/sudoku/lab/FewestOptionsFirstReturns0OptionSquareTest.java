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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import backtrack.lab.rubric.BacktrackRubric;
import sudoku.core.ConstraintPropagator;
import sudoku.core.ImmutableSudokuPuzzle;
import sudoku.core.Square;
import sudoku.instructor.InstructorSudokuTestUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link FewestOptionsFirstSquareSearchAlgorithm#selectNextUnfilledSquare(sudoku.core.SudokuPuzzle)}
 */
@BacktrackRubric(BacktrackRubric.Category.FEWEST_OPTIONS_FIRST_SEARCH)
public class FewestOptionsFirstReturns0OptionSquareTest {
	@Test
	public void test() {
		// source of original givens (now constrained to no hope):
		// http://norvig.com/hardest.txt
		String doomedGivens = "85...24..72......9..4....2....147..2375..8914.4.......4..981.7..17....9....736.4.";
		ConstraintPropagator constraintPropagator = InstructorSudokuTestUtils.createPeerAndUnitConstraintPropagator();
		ImmutableSudokuPuzzle doomedPuzzle = new DefaultImmutableSudokuPuzzle(constraintPropagator, doomedGivens);
		ImmutableSudokuPuzzle deadPuzzle = doomedPuzzle.createNext(Square.A3, 1);
		List<Square> expectedSquares = Arrays.asList(Square.D1, Square.G3, Square.H1, Square.H7);
		for (Square square : Square.values()) {
			int size = deadPuzzle.getOptions(square).size();
			if (expectedSquares.contains(square) ) {
				assertEquals(square.name(), 0, size);
			} else {
				assertNotEquals(square.name(), 0, size);
			}
		}

		Square actualSquare = new FewestOptionsFirstSquareSearchAlgorithm().selectNextUnfilledSquare(deadPuzzle);
		Set<Integer> options = deadPuzzle.getOptions(actualSquare);
		Assert.assertEquals("The number of options for the square is incorrect", 0, options.size());
		Assert.assertTrue(expectedSquares.contains(actualSquare));
	}
}
