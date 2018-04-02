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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import backtrack.lab.rubric.BacktrackRubric;
import sudoku.core.ConstraintPropagator;
import sudoku.core.GivensUtils;
import sudoku.core.Square;
import sudoku.instructor.InstructorSudokuTestUtils;
import sudoku.lab.DefaultImmutableSudokuPuzzle;
import sudoku.util.SearchSupplier;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link DefaultImmutableSudokuPuzzle#getValue(Square)}
 */
@BacktrackRubric(BacktrackRubric.Category.IMMUTABLE_SUDOKU_PUZZLE)
@RunWith(Parameterized.class)
public class GetValueComprehensiveTest {
	private final String givens;

	public GetValueComprehensiveTest(String givens) {
		this.givens = givens;
	}

	@Test
	public void test() {
		ConstraintPropagator constraintPropagator = InstructorSudokuTestUtils.createPeerOnlyConstraintPropagator();
		DefaultImmutableSudokuPuzzle puzzle = new DefaultImmutableSudokuPuzzle(constraintPropagator, givens);
		int row = 0;
		int column = 0;
		for (char c : givens.toCharArray()) {
			int expected;
			if (c == '.') {
				expected = 0;
			} else {
				expected = c - '0';
			}
			Square square = Square.valueOf(row, column);
			int actual = puzzle.getValue(square);

			String message = String.format("[row:%d][column:%d]", row, column);
			assertEquals(message, expected, actual);

			column++;
			if (column == 9) {
				row++;
				column = 0;
			}
		}
	}

	@Parameters(name = "givens: {0}")
	public static Collection<Object[]> getConstructorArguments() {
		Collection<Object[]> results = new LinkedList<>();
		List<String> givensList = GivensUtils
				.getGivensToTest(SearchSupplier.STUDENT_FEWEST_OPTIONS_FIRST);
		for (String givens : givensList) {
			results.add(new Object[] { givens });
		}
		return results;
	}
}
