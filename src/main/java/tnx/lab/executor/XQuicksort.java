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

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import edu.wustl.cse231s.IntendedForStaticAccessOnlyError;
import sort.core.quick.Partitioner;
import sort.core.quick.PivotLocation;

/**
 * An implementation of the quicksort algorithm that uses Java's Executors.
 * 
 * @author Yiheng Huang
 * @author Finn Voichick
 */
public final class XQuicksort {

	/**
	 * This class is noninstantiable. Do not modify or call this constructor.
	 */
	private XQuicksort() {
		throw new IntendedForStaticAccessOnlyError();
	}

	/**
	 * Sequentially sorts the given array using the quicksort algorithm.
	 * 
	 * @param data
	 *            the array to sort
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public static void sequentialQuicksort(int[] data, Partitioner partitioner)
			throws InterruptedException, ExecutionException {
		sequentialQuicksortKernel(data, 0, data.length, partitioner);
	}

	/**
	 * Should sequentially and recursively sort the given range of the array, from
	 * min (inclusive) to max (exclusive).
	 * 
	 * @param data
	 *            the array to sort
	 * @param min
	 *            the minimum value (inclusive) of the range to sort
	 * @param maxExclusive
	 *            the maximum value (exclusive) of the range to sort
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	private static void sequentialQuicksortKernel(int[] data, int min, int maxExclusive, Partitioner partitioner)
			throws InterruptedException, ExecutionException {
		if (maxExclusive - min <= 1) {
			return;
		}

		PivotLocation p = partitioner.partitionRange(data, min, maxExclusive);

		int left = p.getLeftSidesUpperExclusive();
		int right = p.getRightSidesLowerInclusive();

		sequentialQuicksortKernel(data, min, left, partitioner);
		sequentialQuicksortKernel(data, right, maxExclusive, partitioner);
	}

	/**
	 * Should sort the given array in parallel using the quicksort algorithm.
	 * 
	 * @param executor
	 *            an {@code ExecutorService} that you should use to submit tasks
	 * @param data
	 *            the array to sort
	 * @param threshold
	 *            the range length below which processing should be sequential to
	 *            reduce overhead
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static void parallelQuicksort(ExecutorService executor, int[] data, int threshold, Partitioner partitioner)
			throws InterruptedException, ExecutionException {

		ConcurrentLinkedQueue<Future<?>> futures = new ConcurrentLinkedQueue<>();

		parallelQuicksortKernel(executor, data, 0, data.length, futures, threshold, partitioner);

		while (!futures.isEmpty()) {
			futures.poll().get();
		}
	}

	/**
	 * Should sort the given range of the array in parallel using the quicksort
	 * algorithm. If the range length is less than the threshold, it is specified as
	 * not worth parallelizing, and this method should count sequentially.
	 * Otherwise, it should submit two tasks to the executor for the lower and upper
	 * halves of this range.
	 * 
	 * @param executor
	 *            an {@code ExecutorService} that you should use to submit tasks
	 * @param data
	 *            the array to sort
	 * @param min
	 *            the minimum value (inclusive) of the range to sort
	 * @param maxExclusive
	 *            the maximum value (exclusive) of the range to sort
	 * @param futures
	 *            a thread-safe collection of futures to which newly-submitted tasks
	 *            can be added
	 * @param threshold
	 *            the range length below which processing should be sequential to
	 *            reduce overhead
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	private static void parallelQuicksortKernel(ExecutorService executor, int[] data, int min, int maxExclusive,
			Queue<Future<?>> futures, int threshold, Partitioner partitioner)
			throws InterruptedException, ExecutionException {

		if (maxExclusive - min >= threshold) {

			PivotLocation p = partitioner.partitionRange(data, min, maxExclusive);

			Future<?> future1 = executor.submit(() -> {
				parallelQuicksortKernel(executor, data, min, p.getLeftSidesUpperExclusive(), futures, threshold,
						partitioner);
				return null;
			});
			Future<?> future2 = executor.submit(() -> {
				parallelQuicksortKernel(executor, data, p.getRightSidesLowerInclusive(), maxExclusive, futures,
						threshold, partitioner);
				return null;
			});

			futures.add(future1);
			futures.add(future2);

		} else {
			sequentialQuicksortKernel(data, min, maxExclusive, partitioner);
		}

	}

}
