/*******************************************************************************
 * Copyright (C) 2016-2018 Dennis Cosgrove
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
package atomicity.stockportfolio.studio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import atomicity.stockportfolio.util.Listing;
import edu.wustl.cse231s.junit.JUnitUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link StockPortfolio}
 */
public class StockPortfolioComputeInvocationTest {
	@Rule
	public TestRule timeout = JUnitUtils.createTimeoutRule();

	private boolean isAsExpected(int expectedCompute, AtomicInteger computeCount, int expectedMerge,
			AtomicInteger mergeCount, int expectedPutIfAbsent, AtomicInteger putIfAbsentCount, int expectedReplace,
			AtomicInteger replaceCount) {
		return expectedCompute == computeCount.intValue() && expectedMerge == mergeCount.intValue()
				&& expectedPutIfAbsent == putIfAbsentCount.intValue() && expectedReplace == replaceCount.intValue();
	}

	@Test
	public void test() {
		AtomicInteger computeCount = new AtomicInteger();
		AtomicInteger mergeCount = new AtomicInteger();
		AtomicInteger putIfAbsentCount = new AtomicInteger();
		AtomicInteger replaceCount = new AtomicInteger();

		Supplier<ConcurrentMap<String, Integer>> supplier = () -> {
			ConcurrentMap<String, Integer> map = new ConcurrentHashMap<String, Integer>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Integer compute(String key,
						BiFunction<? super String, ? super Integer, ? extends Integer> remappingFunction) {
					computeCount.incrementAndGet();
					return super.compute(key, remappingFunction);
				}

				@Override
				public Integer merge(String key, Integer value,
						BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
					mergeCount.incrementAndGet();
					return super.merge(key, value, remappingFunction);
				}

				@Override
				public Integer putIfAbsent(String key, Integer value) {
					putIfAbsentCount.incrementAndGet();
					return super.putIfAbsent(key, value);
				}

				@Override
				public boolean replace(String key, Integer oldValue, Integer newValue) {
					replaceCount.incrementAndGet();
					return super.replace(key, oldValue, newValue);
				}

			};
			return map;
		};
		StockPortfolio portfolio = new StockPortfolio(supplier);
		portfolio.buy(Listing.APERTURE_LABS.getSymbol(), 71);

		assertTrue(isAsExpected(1, computeCount, 0, mergeCount, 0, putIfAbsentCount, 0, replaceCount)
				|| isAsExpected(0, computeCount, 1, mergeCount, 0, putIfAbsentCount, 0, replaceCount)
				|| isAsExpected(0, computeCount, 0, mergeCount, 1, putIfAbsentCount, 0, replaceCount));

		portfolio.buy(Listing.APERTURE_LABS.getSymbol(), 231);

		assertTrue(isAsExpected(2, computeCount, 0, mergeCount, 0, putIfAbsentCount, 0, replaceCount)
				|| isAsExpected(0, computeCount, 2, mergeCount, 0, putIfAbsentCount, 0, replaceCount)
				|| isAsExpected(0, computeCount, 0, mergeCount, 2, putIfAbsentCount, 1, replaceCount));
	}
}
