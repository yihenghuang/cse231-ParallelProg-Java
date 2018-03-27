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

import static edu.wustl.cse231s.v5.V5.abstractMetrics;
import static edu.wustl.cse231s.v5.V5.launchApp;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import edu.wustl.cse231s.v5.api.CheckedRunnable;
import edu.wustl.cse231s.v5.api.Metrics;
import edu.wustl.cse231s.v5.bookkeep.BookkeepingUtils;
import edu.wustl.cse231s.v5.impl.BookkeepingV5Impl;
import edu.wustl.cse231s.v5.options.SystemPropertiesOption;
import sudoku.core.ConstraintPropagator;
import sudoku.core.GivensUtils;
import sudoku.core.ImmutableSudokuPuzzle;
import sudoku.core.SquareSearchAlgorithm;
import sudoku.instructor.InstructorSudokuTestUtils;
import sudoku.lab.DefaultImmutableSudokuPuzzle;
import sudoku.lab.ParallelSudoku;
import sudoku.viz.solution.SquareSearchAlgorithmSupplier;

@RunWith(Parameterized.class)
public class AsyncTest {

	@Parameters(name = "{0}, givens: {1}")
	public static Collection<Object[]> getConstructorArguments() {
		Collection<Object[]> results = new LinkedList<>();
		int[] cpls = { 50, 60, 59, 50, 60, 59, 56, 58, 59, 58, 65, 65, 59, 59, 65 };
		int i = 0;
		for (SquareSearchAlgorithmSupplier squareSearchAlgorithmSupplier : SquareSearchAlgorithmSupplier
				.studentValues()) {
			// source: http://norvig.com/sudoku.html
			String easyGivens = "..3.2.6..9..3.5..1..18.64....81.29..7.......8..67.82....26.95..8..2.3..9..5.1.3..";
			results.add(new Object[] { squareSearchAlgorithmSupplier, easyGivens, cpls[i] });

			List<String> givensList = GivensUtils.getGivensToTest(squareSearchAlgorithmSupplier);
			i++;
			for (String givens : givensList) {
				results.add(new Object[] { squareSearchAlgorithmSupplier, givens, cpls[i] });
				i++;
			}
		}

		return results;
	}

	private final SquareSearchAlgorithm squareSearchAlgorithm;
	private final String givens;
	private final int expectedCPL;

	public AsyncTest(SquareSearchAlgorithm squareSearchAlgorithm, String givens, int cpl) {
		this.squareSearchAlgorithm = squareSearchAlgorithm;
		this.givens = givens;
		this.expectedCPL = cpl;
	}

	// Async and Finish Tests
	private void testFinishAndAsyncCounts(int expectedFinishCount, int asyncCountLowerBound, CheckedRunnable body) {
		BookkeepingV5Impl bookkeeping = BookkeepingUtils.bookkeep(body);

		// Test finish count
		int actualFinishCount = bookkeeping.getFinishInvocationCount();

		if (expectedFinishCount != 0) {
			assertNotEquals("finish never called; expected: " + expectedFinishCount, 0, actualFinishCount);
		}
		assertEquals(expectedFinishCount, actualFinishCount);

		// Test async count
		int totalAsyncCount = bookkeeping.getTaskCount();

		if (asyncCountLowerBound > 1) {
			assertNotEquals("async never called; expected at least: " + asyncCountLowerBound, 0, totalAsyncCount);
		} else if (asyncCountLowerBound > 2) {
			assertNotEquals("async only called once; expected at least: " + asyncCountLowerBound, 1, totalAsyncCount);
		}
		assertThat(totalAsyncCount, greaterThanOrEqualTo(asyncCountLowerBound));
	}

	@Test
	public void testTasksAndFinishes() throws InterruptedException, ExecutionException {
		ConstraintPropagator constraintPropagator = InstructorSudokuTestUtils.createPeerOnlyConstraintPropagator();
		ImmutableSudokuPuzzle original = new DefaultImmutableSudokuPuzzle(constraintPropagator, this.givens);

		int tasksNumLowerBound = getNumBlanks() - 1;
		int expectedNumFinishes = 1;
		testFinishAndAsyncCounts(expectedNumFinishes, tasksNumLowerBound, () -> {
			ParallelSudoku.solve(original, this.squareSearchAlgorithm);
		});
	}

	private int getNumBlanks() {
		return this.givens.length() - this.givens.replace(".", "").length();
	}

	// CPL Tests
	private void test(CheckedRunnable body, long expectedCPL) {
		launchApp(new SystemPropertiesOption.Builder().isAbstractMetricsDesired(true).build(), body, () -> {
			Metrics metrics = abstractMetrics();
			assertEquals(expectedCPL, metrics.criticalPathLength());
		});
	}

	@Test
	public void testCPL() throws InterruptedException, ExecutionException {
		ConstraintPropagator constraintPropagator = InstructorSudokuTestUtils.createPeerOnlyConstraintPropagator();
		ImmutableSudokuPuzzle original = new DefaultImmutableSudokuPuzzle(constraintPropagator, this.givens);

		test(() -> {
			ParallelSudoku.solve(original, this.squareSearchAlgorithm);
		}, this.expectedCPL);
	}
}
