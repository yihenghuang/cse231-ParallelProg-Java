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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

import edu.wustl.cse231s.timing.ImmutableTimer;
import edu.wustl.cse231s.v5.impl.executor.ExecutorV5Impl;
import iterativeaveraging.core.IterativeAveragingUtils;
import iterativeaveraging.demo.SequentialIterativeAverager;
import slice.core.Slice;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class IterativeAveragingTiming {
	public static void main(String[] args) throws Exception {
		final int TASKS_PER_PROCESSOR = 1;
		final int TASK_COUNT = Runtime.getRuntime().availableProcessors() * TASKS_PER_PROCESSOR;
		final int INDICES_PER_TASK = 500;
		final int ARRAY_LENGTH = (TASK_COUNT * INDICES_PER_TASK) + 2;

		final boolean IS_COMMON_POOL_DESIRED = false;
		final boolean IS_FIXED = false;
		ExecutorService executorService = IS_COMMON_POOL_DESIRED ? ForkJoinPool.commonPool()
				: IS_FIXED ? Executors.newFixedThreadPool(TASK_COUNT) : Executors.newCachedThreadPool();
		ExecutorV5Impl impl = new ExecutorV5Impl(executorService);

		final int ITERATION_COUNT = 100_000;
		double[] original = new double[ARRAY_LENGTH];
		original[original.length - 1] = 1.0;
		launchApp(impl, () -> {
			final int RUN_COUNT = 10;
			for (int runIndex = 0; runIndex < RUN_COUNT; runIndex++) {

				timeForFor(original, TASK_COUNT, ITERATION_COUNT);
				timeForForall(original, TASK_COUNT, ITERATION_COUNT);
				timeForallForPhased(original, TASK_COUNT, ITERATION_COUNT);

				System.out.println();
			}
		});

		if (IS_COMMON_POOL_DESIRED) {
			// pass
		} else {
			executorService.shutdown();
		}
	}

	private static String FORMAT = "%12s";

	private static void timeForFor(double[] original, int numSlices, int iterationCount)
			throws InterruptedException, ExecutionException {
		double[] a = Arrays.copyOf(original, original.length);
		double[] b = Arrays.copyOf(original, original.length);
		List<Slice<double[]>> slices = IterativeAveragingUtils.createSlices(a, numSlices);
		ImmutableTimer timer = new ImmutableTimer(String.format(FORMAT, "for, for"));
		new SequentialIterativeAverager().iterativelyAverage(slices, a, b, iterationCount);
		timer.markAndPrintResults();
	}

	private static void timeForForall(double[] original, int numSlices, int iterationCount)
			throws InterruptedException, ExecutionException {
		double[] a = Arrays.copyOf(original, original.length);
		double[] b = Arrays.copyOf(original, original.length);
		List<Slice<double[]>> slices = IterativeAveragingUtils.createSlices(a, numSlices);
		ImmutableTimer timer = new ImmutableTimer(String.format(FORMAT, "for, forall"));
		new ForForallIterativeAverager().iterativelyAverage(slices, a, b, iterationCount);
		timer.markAndPrintResults();
	}

	private static void timeForallForPhased(double[] original, int numSlices, int iterationCount)
			throws InterruptedException, ExecutionException {
		double[] a = Arrays.copyOf(original, original.length);
		double[] b = Arrays.copyOf(original, original.length);
		List<Slice<double[]>> slices = IterativeAveragingUtils.createSlices(a, numSlices);
		ImmutableTimer timer = new ImmutableTimer(String.format(FORMAT, "forallphased"));
		new ForallForPhasedIterativeAverager().iterativelyAverage(slices, a, b, iterationCount);
		timer.markAndPrintResults();
	}
}
