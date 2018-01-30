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
package sort.core.merge;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.ExecutionException;

import sort.core.SortUtils;
import sort.fun.merge.ParallelCombiner;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class ParallelTestCombiner extends ParallelCombiner {
	public ParallelTestCombiner(int bufferLength, int threshold) {
		super(bufferLength, threshold);
	}

	@Override
	public void combineRange(int[] array, int min, int mid, int maxExclusive)
			throws InterruptedException, ExecutionException {
		assertTrue(SortUtils.isInSortedOrder(array, min, mid));
		assertTrue(SortUtils.isInSortedOrder(array, mid, maxExclusive));
		super.combineRange(array, min, mid, maxExclusive);
	}
}
