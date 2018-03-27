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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.base.Supplier;

import backtrack.lab.rubric.BacktrackRubric;
import sudoku.core.ConstraintPropagator;
import sudoku.core.GivensUtils;
import sudoku.core.ImmutableSudokuPuzzle;
import sudoku.core.SolutionUtils;
import sudoku.instructor.InstructorSudokuTestUtils;
import sudoku.viz.solution.SquareSearchAlgorithmSupplier;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@BacktrackRubric(BacktrackRubric.Category.PARALLEL_SUDOKU_CORRECTNESS)
@RunWith(Parameterized.class)
public class SolveTest {
	private final SquareSearchAlgorithmSupplier squareSearchAlgorithmSupplier;
	private final Supplier<ConstraintPropagator> constraintPropagatorSupplier;
	private final String givens;

	public SolveTest(SquareSearchAlgorithmSupplier squareSearchAlgorithmSupplier,
			Supplier<ConstraintPropagator> constraintPropagatorSupplier, String givens) {
		this.squareSearchAlgorithmSupplier = squareSearchAlgorithmSupplier;
		this.constraintPropagatorSupplier = constraintPropagatorSupplier;
		this.givens = givens;
	}

	@Test
	public void test() {
		ImmutableSudokuPuzzle original = new DefaultImmutableSudokuPuzzle(this.constraintPropagatorSupplier.get(), this.givens);
		ImmutableSudokuPuzzle result = launchAppWithReturn(() -> {
			return ParallelSudoku.solve(original, this.squareSearchAlgorithmSupplier.get());
		});

		assertNotNull(result);
		assertNotEquals(this.givens, result.toString());
		assertTrue(SolutionUtils.isCompletelyFilledIn(result));
		assertTrue(SolutionUtils.isCompletelyFilledInAndEachSquareIsValid(result));
		assertTrue(SolutionUtils.containsOriginal(original, result));
	}

	@Parameters(name = "{0}, givens: {1}")
	public static Collection<Object[]> getConstructorArguments() {
		Collection<Object[]> results = new LinkedList<>();
		Supplier<ConstraintPropagator> constraintPropagatorSupplier = ()-> InstructorSudokuTestUtils.createPeerAndUnitConstraintPropagator();
		for (SquareSearchAlgorithmSupplier squareSearchAlgorithmSupplier : SquareSearchAlgorithmSupplier
				.studentValues()) {
			List<String> givensList = GivensUtils.getGivensToTest(squareSearchAlgorithmSupplier);
			for (String givens : givensList) {
				results.add(new Object[] { squareSearchAlgorithmSupplier, constraintPropagatorSupplier, givens });
			}
		}
		return results;
	}
}
