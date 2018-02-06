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

import static edu.wustl.cse231s.v5.V5.launchApp;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import edu.wustl.cse231s.asymptoticanalysis.OrderOfGrowth;
import edu.wustl.cse231s.junit.JUnitUtils;
import fibonacci.core.FibonacciCalculator;
import fibonacci.core.FibonacciUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@RunWith(Parameterized.class)
public class FibonacciTest {
	private final FibonacciCalculator fibonacciCalculator;
	private final int n;

	public FibonacciTest(FibonacciCalculator fibonacciCalculator, int n) {
		this.fibonacciCalculator = fibonacciCalculator;
		this.n = n;
	}

	@Test(timeout = JUnitUtils.DEFAULT_TIMEOUT)
	public void test() {
		BigInteger expected = FibonacciUtils.fibonacci(n);
		launchApp(() -> {
			BigInteger value = this.fibonacciCalculator.fibonacci(n);
			Assert.assertEquals(expected, value);
		});
	}

	@Parameters(name = "{0} n={1}")
	public static Collection<Object[]> getConstructorArguments() {
		// NOTE: some of these values will overflow
		Map<OrderOfGrowth, List<Integer>> map = new HashMap<>();
		map.put(OrderOfGrowth.EXPONENTIAL, Arrays.asList(0, 1, 2, 3, 4, 5, 10));
		map.put(OrderOfGrowth.LINEAR, Arrays.asList(0, 1, 2, 3, 4, 5, 10, 25, 100, 1_000));
		map.put(OrderOfGrowth.LOGARITHMIC, Arrays.asList(0, 1, 2, 3, 4, 5, 10, 25, 100, 1_000, 10_000, 100_000));

		List<FibonacciCalculator> fibonacciCalculators = Arrays.asList(
				new RecurrenceRelationSequentialFibonacciCalculator(),
				new RecurrenceRelationParallelFibonacciCalculator(), new MemoizationSequentialFibonacciCalculator(),
				new DynamicIterativeSequentialFibonacciCalculator(),
				new LinearRecurrenceSequentialFibonacciCalculator());

		final int MAX_FOR_STACK_OVERFLOW_SUSCEPTIBLE = 1_000;
		List<Object[]> list = new LinkedList<>();
		for (FibonacciCalculator fibonacciCalculator : fibonacciCalculators) {
			for (int n : map.get(fibonacciCalculator.getOrderOfGrowth())) {
				// todo: check for OrderOfGrowth.LOGARITHMIC
				if (n > MAX_FOR_STACK_OVERFLOW_SUSCEPTIBLE && fibonacciCalculator.isSusceptibleToStackOverflowError()) {
					// pass
				} else {
					list.add(new Object[] { fibonacciCalculator, n });
				}
			}
		}
		return list;
	}
}
