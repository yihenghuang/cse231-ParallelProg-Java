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

import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * @author Yiheng Huang
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class StockPortfolio {
	private final ConcurrentMap<String, Integer> map;

	public StockPortfolio(Supplier<ConcurrentMap<String, Integer>> concurrentMapSupplier) {
		this.map = concurrentMapSupplier.get();
	}

	public int buy(String listingSymbol, int shareCount) {
		return transfer(listingSymbol, shareCount);
	}

	public int sell(String listingSymbol, int shareCount) {
		return transfer(listingSymbol, -shareCount);
	}

	// NOTE: for simplicity we will allow negative values for the number of shares
	// interpreting it as selling short.
	private int transfer(String listingSymbol, int deltaShareCount) {
		Integer oldValue = this.map.get(listingSymbol);
		Integer newValue;
		if (oldValue != null) {
			newValue = oldValue + deltaShareCount; 
		} else {
			newValue = deltaShareCount;
		}
		this.map.put(listingSymbol, newValue);
		return newValue;
	}

	public int numShares(String listingSymbol) {
		// note:
		// from an atomicity standpoint there is nothing wrong with using get(),
		// checking for null and returning the result or 0 accordingly
		return map.getOrDefault(listingSymbol, 0);
	}
}
