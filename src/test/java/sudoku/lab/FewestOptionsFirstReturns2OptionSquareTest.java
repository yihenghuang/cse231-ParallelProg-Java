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
import static org.junit.Assert.assertTrue;

import java.util.EnumSet;
import java.util.Set;

import org.junit.Test;

import backtrack.lab.rubric.BacktrackRubric;
import sudoku.core.ConstraintPropagator;
import sudoku.core.Square;
import sudoku.instructor.InstructorSudokuTestUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link SquareSearchAlgorithms#FEWEST_OPTIONS_FIRST}
 */
@BacktrackRubric(BacktrackRubric.Category.FEWEST_OPTIONS_FIRST_SEARCH)
public class FewestOptionsFirstReturns2OptionSquareTest {
	@Test
	public void testOptionsLength() {
		// source: http://norvig.com/hardest.txt
		String givensWithFewestOptionsSquareWhichIsNotFirstOrLastOpenSquare = "7.....4...2..7..8...3..8.799..5..3...6..2..9...1.97..6...3..9...3..4..6...9..1.35";

		ConstraintPropagator constraintPropagator = InstructorSudokuTestUtils.createPeerOnlyConstraintPropagator();
		DefaultImmutableSudokuPuzzle puzzle = new DefaultImmutableSudokuPuzzle(constraintPropagator,
				givensWithFewestOptionsSquareWhichIsNotFirstOrLastOpenSquare);

		Set<Square> expectedSquareSet = EnumSet.of(Square.B9, Square.D6, Square.E6, Square.F4, Square.I5);
		int expectedOptionCount = 2;
		for (Square square : Square.values()) {
			int actualOptionsCount = puzzle.getOptions(square).size();
			if (expectedSquareSet.contains(square)) {
				assertEquals(expectedOptionCount, actualOptionsCount);
			} else {
				if (puzzle.isSquareValueDetermined(square)) {
					assertEquals(1, actualOptionsCount);
				} else {
					assertTrue(expectedOptionCount < actualOptionsCount);
				}
			}
		}

		Square actualSquare = new FewestOptionsFirstSquareSearchAlgorithm().selectNextUnfilledSquare(puzzle);

		assertNotEquals("first square <A1> returned despite being already filled", Square.A1, actualSquare);
		assertNotEquals("last square <I9> returned despite being already filled", Square.I9, actualSquare);

		Square firstUnfilledSquare = Square.A2;
		assertNotEquals(
				"first unfilled square <" + firstUnfilledSquare + "> returned despite not having fewest options",
				firstUnfilledSquare, actualSquare);
		Square lastUnfilledSquare = Square.I7;
		assertNotEquals("last unfilled square <" + lastUnfilledSquare + "> returned despite not having fewest options",
				lastUnfilledSquare, actualSquare);

		assertTrue(expectedSquareSet.contains(actualSquare));
	}
}
