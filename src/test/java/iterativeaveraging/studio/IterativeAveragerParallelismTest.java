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

package iterativeaveraging.studio;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

import edu.wustl.cse231s.v5.bookkeep.BookkeepingUtils;
import edu.wustl.cse231s.v5.impl.BookkeepingV5Impl;
import iterativeaveraging.core.IterativeAverager;
import iterativeaveraging.core.IterativeAveragingUtils;
import slice.core.Slice;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link ForForallIterativeAverager#iterativelyAverage(List, double[], double[], int)}
 *         {@link ForallForPhasedIterativeAverager#iterativelyAverage(List, double[], double[], int)}
 */
public class IterativeAveragerParallelismTest {
	private static final int NUM_SLICES = Runtime.getRuntime().availableProcessors();
	private static final int NUM_ITERATIONS = 100;

	private BookkeepingV5Impl bookkeep(IterativeAverager iterativeAverager) {
		int indicesPerSlice = 2;
		int length = (NUM_SLICES * indicesPerSlice) + 2;

		double[] original = new double[length];
		original[original.length - 1] = 1.0;
		double[] a = Arrays.copyOf(original, original.length);
		double[] b = Arrays.copyOf(original, original.length);
		List<Slice<double[]>> slices = IterativeAveragingUtils.createSlices(a, NUM_SLICES);

		ExecutorService executorService = Executors.newFixedThreadPool(NUM_SLICES);
		BookkeepingV5Impl bookkeepingV5Impl = BookkeepingUtils.bookkeep(executorService, () -> {
			iterativeAverager.iterativelyAverage(slices, a, b, NUM_ITERATIONS);
		});

		executorService.shutdown();

		return bookkeepingV5Impl;
	}

	@Test
	public void testParallel() {
		BookkeepingV5Impl bookkeepingV5Impl = bookkeep(new ForForallIterativeAverager());
		assertEquals(NUM_ITERATIONS, bookkeepingV5Impl.getForasyncTotalInvocationCount());
		assertEquals(NUM_ITERATIONS, bookkeepingV5Impl.getNonAccumulatorFinishInvocationCount());
		assertEquals(NUM_ITERATIONS * NUM_SLICES, bookkeepingV5Impl.getAsyncViaForasyncCount());
	}

	@Test
	public void testParallelPhased() {
		BookkeepingV5Impl bookkeepingV5Impl = bookkeep(new ForallForPhasedIterativeAverager());
		assertEquals(1, bookkeepingV5Impl.getForasyncTotalInvocationCount());
		assertEquals(1, bookkeepingV5Impl.getNonAccumulatorFinishInvocationCount());
		assertEquals(NUM_SLICES, bookkeepingV5Impl.getAsyncViaForasyncCount());
	}
}
