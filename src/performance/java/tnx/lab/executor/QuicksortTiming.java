package tnx.lab.executor;
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

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.wustl.cse231s.NotYetImplementedException;
import edu.wustl.cse231s.timing.ImmutableTimer;
import sort.core.quick.Partitioner;
import sort.core.quick.SequentialPartitioner;
import tnx.lab.executor.XQuicksort;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class QuicksortTiming {
	private static void timeSequentialQuicksort(int[] original, Partitioner partitioner, int[] expected)
			throws InterruptedException, ExecutionException {
		try {
			int[] array = Arrays.copyOf(original, original.length);
			ImmutableTimer timer = new ImmutableTimer(String.format("%52s", "XNucleobaseCount.countSequential"));
			XQuicksort.sequentialQuicksort(array, partitioner);
			long dt = timer.mark();
			if (Arrays.equals(array, expected)) {
				timer.printResults(dt);
			} else {
				throw new RuntimeException();
			}
		} catch (NotYetImplementedException nyie) {
			System.out.println("XQuicksort.sequentialQuicksort: NOT YET IMPLEMENTED");
		}
	}

	private static void timeParallelQuicksort(ExecutorService executor, int[] original, int threshold,
			Partitioner partitioner, int[] expected) throws InterruptedException, ExecutionException {
		try {
			int[] array = Arrays.copyOf(original, original.length);
			ImmutableTimer timer = new ImmutableTimer(
					String.format("%52s", "XNucleobaseCount.parallelQuicksort(threshold=" + threshold + ")"));
			XQuicksort.parallelQuicksort(executor, array, threshold, partitioner);
			long dt = timer.mark();
			if (Arrays.equals(array, expected)) {
				timer.printResults(dt);
			} else {
				throw new RuntimeException();
			}
		} catch (NotYetImplementedException nyie) {
			System.out.println("XQuicksort.parallelQuicksort: NOT YET IMPLEMENTED");
		}
	}

	public static void main(String[] args) throws Exception {
		int size = 1_000_000;
		Random random = new Random();
		int[] original = random.ints(size, 0, size).toArray();
		int[] sorted = Arrays.copyOf(original, size);
		Arrays.parallelSort(sorted);

		ExecutorService executor = Executors.newCachedThreadPool();
		Partitioner partitioner = new SequentialPartitioner();
		try {
			final int ITERATION_COUNT = 10;
			for (int iteration = 0; iteration < ITERATION_COUNT; iteration++) {

				timeSequentialQuicksort(original, partitioner, sorted);
				timeParallelQuicksort(executor, original, size / 3, partitioner, sorted);
				timeParallelQuicksort(executor, original, size / 100, partitioner, sorted);
				timeParallelQuicksort(executor, original, size / 1_000, partitioner, sorted);
				System.out.println();
			}
		} finally {
			executor.shutdown();
		}

	}

}
