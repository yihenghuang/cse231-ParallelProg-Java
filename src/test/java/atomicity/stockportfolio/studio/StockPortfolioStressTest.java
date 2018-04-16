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

import static edu.wustl.cse231s.v5.V5.forall;
import static edu.wustl.cse231s.v5.V5.launchApp;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import atomicity.stockportfolio.util.Listing;
import edu.wustl.cse231s.junit.JUnitUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link StockPortfolio}
 */
@RunWith(Parameterized.class)
public class StockPortfolioStressTest {
	private final boolean isYielding;

	public StockPortfolioStressTest(boolean isYielding) {
		this.isYielding = isYielding;
	}

	@Rule
	public TestRule timeout = JUnitUtils.createTimeoutRule();

	@Test
	public void test() {
		String key = Listing.UMBRELLA_CORP.getSymbol();
		AtomicInteger atom = new AtomicInteger();

		Supplier<ConcurrentMap<String, Integer>> supplier = () -> {
			if (isYielding) {
				return new ConcurrentHashMap<String, Integer>() {
					private static final long serialVersionUID = 1L;

					@Override
					public Integer get(Object key) {
						Integer value = super.get(key);
						Thread.yield();
						return value;
					}
				};
			} else {
				return new ConcurrentHashMap<>();
			}
		};
		StockPortfolio portfolio = new StockPortfolio(supplier);
		launchApp(() -> {
			forall(0, 100, (unused) -> {
				for (int i = 0; i < 100; i++) {
					int buyAmount = 2;
					atom.addAndGet(buyAmount);
					portfolio.buy(key, buyAmount);
					int sellAmount = 1;
					atom.addAndGet(-sellAmount);
					portfolio.sell(key, sellAmount);
				}
			});
		});
		assertEquals(atom.get(), portfolio.numShares(key));
	}

	@Parameters(name = "isYielding={0}")
	public static Collection<Object[]> getConstructorArguments() {
		return Arrays.asList(new Object[] { true }, new Object[] { false });
	}
}
