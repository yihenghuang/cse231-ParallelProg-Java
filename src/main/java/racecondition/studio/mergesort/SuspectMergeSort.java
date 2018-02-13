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
package racecondition.studio.mergesort;

import static edu.wustl.cse231s.v5.V5.async;
import static edu.wustl.cse231s.v5.V5.finish;

import java.util.concurrent.ExecutionException;

import sort.core.merge.Combiner;
import sort.studio.merge.MergeSort;

/**
 * @author __STUDENT_NAME__
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link MergeSort}
 */
public class SuspectMergeSort {
	private static void sequentialMergeSortKernel(int[] data, int lowInclusive, int highExclusive, Combiner combiner)
			throws InterruptedException, ExecutionException {
		int lengthOfSubArray = (highExclusive - lowInclusive);
		if (lengthOfSubArray > 1) {
			int mid = (highExclusive + lowInclusive) / 2;
			sequentialMergeSortKernel(data, lowInclusive, mid, combiner);
			sequentialMergeSortKernel(data, mid, highExclusive, combiner);
			combiner.combineRange(data, lowInclusive, mid, highExclusive);
		}
	}

	public static void sequentialMergeSort(int[] data, Combiner combiner)
			throws InterruptedException, ExecutionException {
		sequentialMergeSortKernel(data, 0, data.length, combiner);
	}

	private static void parallelMergeSortKernel(int[] data, int lowInclusive, int highExclusive, int threshold,
			Combiner combiner) throws InterruptedException, ExecutionException {
		int lengthOfSubArray = (highExclusive - lowInclusive);
		if (lengthOfSubArray > threshold) {
			int mid = (highExclusive + lowInclusive) / 2;
			finish(() -> {
				async(() -> {
					parallelMergeSortKernel(data, lowInclusive, mid, threshold, combiner);
				});
				parallelMergeSortKernel(data, mid, highExclusive, threshold, combiner);
			});
			combiner.combineRange(data, lowInclusive, mid, highExclusive);
		} else {
			sequentialMergeSort(data, combiner);
		}
	}

	public static void parallelMergeSort(int[] data, int threshold, Combiner combiner)
			throws InterruptedException, ExecutionException {
		parallelMergeSortKernel(data, 0, data.length, threshold, combiner);
	}
}
