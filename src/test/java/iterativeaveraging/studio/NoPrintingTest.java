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

import static edu.wustl.cse231s.v5.V5.launchApp;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.wustl.cse231s.print.AbstractNoPrintingTest;
import edu.wustl.cse231s.v5.impl.V5Impl;
import edu.wustl.cse231s.v5.impl.executor.ExecutorV5Impl;
import iterativeaveraging.core.IterativeAverager;
import iterativeaveraging.core.IterativeAveragingUtils;
import slice.core.Slice;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link ForForallIterativeAverager#iterativelyAverage(List, double[], double[], int)}
 *         {@link ForallForPhasedIterativeAverager#iterativelyAverage(List, double[], double[], int)}
 */
public class NoPrintingTest extends AbstractNoPrintingTest {
	private void testIterativeAverager(IterativeAverager iterativeAverager) {
		int numSlices = Runtime.getRuntime().availableProcessors();
		int indicesPerSlice = 2;
		int length = (numSlices * indicesPerSlice) + 2;
		int iterationCount = 100;

		double[] original = new double[length];
		original[original.length - 1] = 1.0;
		ExecutorService executorService = Executors.newFixedThreadPool(numSlices);
		V5Impl impl = new ExecutorV5Impl(executorService);

		double[] a = Arrays.copyOf(original, original.length);
		double[] b = Arrays.copyOf(original, original.length);
		List<Slice<double[]>> slices = IterativeAveragingUtils.createSlices(a, numSlices);
		launchApp(impl, () -> {
			iterativeAverager.iterativelyAverage(slices, a, b, iterationCount);
		});
		executorService.shutdown();
	}

	@Override
	protected void testKernel() {
		testIterativeAverager(new ForForallIterativeAverager());
		testIterativeAverager(new ForallForPhasedIterativeAverager());
	}
}
