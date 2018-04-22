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
package kmer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import edu.wustl.cse231s.v5.bookkeep.BookkeepingUtils;
import edu.wustl.cse231s.v5.impl.BookkeepingV5Impl;
import kmer.core.KMerCounter;
import kmer.util.KMerResource;
import slice.core.Slice;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public abstract class AbstractKMerCounterParallelismTest {
	protected abstract KMerCounter createKMerCounter();

	@Test
	public void test() {
		KMerCounter kMerCounter = this.createKMerCounter();
		List<byte[]> sequences = KMerResource.Y_CHROMOSOME_TINY.getSubSequences();

		BookkeepingV5Impl bookkeep = BookkeepingUtils.bookkeep(() -> {
			kMerCounter.parse(sequences, 5);
		});

		assertEquals(1, bookkeep.getForasyncTotalInvocationCount());
		assertEquals(0, bookkeep.getForasyncRangeInvocationCount());
		assertEquals(0, bookkeep.getForasyncArrayInvocationCount());
		assertEquals(1, bookkeep.getForasyncIterableInvocationCount());
		assertEquals(1, bookkeep.getNonAccumulatorFinishInvocationCount());

		Collection<Iterable<?>> iterables = bookkeep.getForasyncIterables();
		assertEquals(1, iterables.size());
		Iterable<?> iterable = iterables.iterator().next();

		Iterator<?> iterator = iterable.iterator();
		assertTrue(iterator.hasNext());
		while (iterator.hasNext()) {
			Object o = iterator.next();
			assertTrue(o instanceof Slice);
		}
	}
}
