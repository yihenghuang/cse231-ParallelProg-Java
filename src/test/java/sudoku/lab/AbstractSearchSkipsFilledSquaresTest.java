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

import static org.junit.Assert.assertNotEquals;

import java.util.Collection;
import java.util.LinkedList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import sudoku.core.ConstraintPropagator;
import sudoku.core.Square;
import sudoku.core.SquareSearchAlgorithm;
import sudoku.instructor.InstructorSudokuTestUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@RunWith(Parameterized.class)
public abstract class AbstractSearchSkipsFilledSquaresTest {
	private final SquareSearchAlgorithm squareSearchAlgorithm;
	private final String givens;
	private final Square expectedSquare;

	public AbstractSearchSkipsFilledSquaresTest(SquareSearchAlgorithm squareSearchAlgorithm, String givens,
			Square expectedSquare) {
		this.squareSearchAlgorithm = squareSearchAlgorithm;
		this.givens = givens;
		this.expectedSquare = expectedSquare;
	}

	@Test
	public void testAlreadySet() {
		ConstraintPropagator constraintPropagator = InstructorSudokuTestUtils
				.createPeerOnlyConstraintPropagator();
		DefaultImmutableSudokuPuzzle puzzle = new DefaultImmutableSudokuPuzzle(constraintPropagator, givens);
		Square actualSquare = squareSearchAlgorithm.selectNextUnfilledSquare(puzzle);
		assertNotEquals("The selected square is already filled, it should be an unfilled square", expectedSquare,
				actualSquare);
	}

	@Parameters(name = "givens:{0} expectedSquare:{1}")
	public static Collection<Object[]> getConstructorArguments() {
		Collection<Object[]> results = new LinkedList<>();
		results.add(new Object[] { "1................................................................................",
				Square.A1 });
		results.add(new Object[] { "................................................................................1",
				Square.I9 });
		results.add(new Object[] { "........................................1........................................",
				Square.E5 });
		return results;
	}

}
