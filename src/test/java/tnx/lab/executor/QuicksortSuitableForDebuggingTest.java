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
package tnx.lab.executor;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Assert;
import org.junit.Test;

import sort.core.quick.SequentialPartitioner;
import tnx.lab.executor.XQuicksort;
import tnx.lab.rubric.TnXRubric;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * @author Finn Voichick
 * 
 *         {@link XQuicksort}
 */
public final class QuicksortSuitableForDebuggingTest {
	private final int[] original;
	private final int[] sorted;

	public QuicksortSuitableForDebuggingTest() {
		this.original = new int[] { 3, 8, 2, 5, 1, 7, 4, 6 };
		this.sorted = Arrays.copyOf(this.original, this.original.length);
		Arrays.parallelSort(sorted);
	}

	@Test
	@TnXRubric(TnXRubric.Category.SEQUENTIAL_QUICKSORT)
	public void testSequential() throws InterruptedException, ExecutionException {
		int[] array = Arrays.copyOf(this.original, this.original.length);
		XQuicksort.sequentialQuicksort(array, new SequentialPartitioner());
		Assert.assertArrayEquals(sorted, array);
	}

	@Test
	@TnXRubric(TnXRubric.Category.EXECUTOR_QUICKSORT)
	public void testParallel() throws InterruptedException, ExecutionException {
		int[] array = Arrays.copyOf(this.original, this.original.length);
		int threshold = 3;

		ExecutorService executorService = Executors.newWorkStealingPool();
		try {
			XQuicksort.parallelQuicksort(executorService, array, threshold, new SequentialPartitioner());
			Assert.assertArrayEquals(sorted, array);
		} finally {
			executorService.shutdown();
		}

	}
}
