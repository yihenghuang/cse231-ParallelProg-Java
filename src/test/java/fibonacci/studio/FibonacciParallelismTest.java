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
package fibonacci.studio;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import edu.wustl.cse231s.junit.JUnitUtils;
import edu.wustl.cse231s.v5.bookkeep.BookkeepingUtils;
import edu.wustl.cse231s.v5.impl.BookkeepingV5Impl;
import fibonacci.core.FibonacciCalculator;
import fibonacci.core.FibonacciUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link RecurrenceRelationParallelFibonacciCalculator}
 */
public class FibonacciParallelismTest {
	@Rule
	public TestRule timeout = JUnitUtils.createTimeoutRule();

	@Test
	public void test() {
		FibonacciCalculator fibonacciCalculator = new RecurrenceRelationParallelFibonacciCalculator();
		int n = 10;
		BigInteger expectedCalculation = FibonacciUtils.fibonacci(n);
		BookkeepingV5Impl bookkeepingV5Impl = BookkeepingUtils.bookkeep(() -> {
			BigInteger actualCalculation = fibonacciCalculator.fibonacci(n);
			Assert.assertEquals(expectedCalculation, actualCalculation);
		});

		int expectedForSingleFuture = 88;
		int ones = 55;
		int zeros = 34;
		List<Matcher<? super Integer>> expectedValidFutureInvocationCounts = new LinkedList<>();
		for (int i = 1; i <= 2; i++) {
			int expected = expectedForSingleFuture * i;
			expectedValidFutureInvocationCounts.add(is(expected));
			expectedValidFutureInvocationCounts.add(is(expected + zeros));
			expectedValidFutureInvocationCounts.add(is(expected + ones));
			expectedValidFutureInvocationCounts.add(is(expected + zeros + ones));
		}

		int actualFutureInvocationCount = bookkeepingV5Impl.getFutureInvocationCount();
		assertThat(actualFutureInvocationCount, anyOf(expectedValidFutureInvocationCounts));
	}
}
