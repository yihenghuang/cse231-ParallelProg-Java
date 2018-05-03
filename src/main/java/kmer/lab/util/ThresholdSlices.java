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
package kmer.lab.util;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import kmer.core.KMerUtils;
import slice.core.Slice;

/**
 * @author Yiheng Huang
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class ThresholdSlices {

	private ThresholdSlices() {
		throw new AssertionError("This class is not instantiable");
	}

	/**
	 * Should either add the given range to the collection or recursively split the
	 * given range, depending on the sliceThreshold.
	 * 
	 * @param slices
	 *            the collection of slices to add to
	 * @param sequence
	 *            a sequence of nucleobases
	 * @param min
	 *            the minimum of the range to look at
	 * @param max
	 *            the maximum of the range to look at
	 * @param sliceThreshold
	 *            the threshold below which it is not worth dividing the slices
	 */
	private static void addToCollectionKernel(Collection<Slice<byte[]>> slices, byte[] sequence, int min, int max,
			int sliceThreshold) {
		if (max - min < sliceThreshold) {
			Slice<byte[]> s = new Slice<byte[]>(sequence, -1, min, max);
			slices.add(s);
		} else {
			int mid = (max + min) / 2;
			addToCollectionKernel(slices, sequence, min, mid, sliceThreshold);
			addToCollectionKernel(slices, sequence, mid, max, sliceThreshold);

		}
	}

	/**
	 * Should create a list of slices from the given list of sequences. Each
	 * sequence may be divided many times or not divided at all, depending on its
	 * size relative to the sliceThreshold.
	 * 
	 * @param sequences
	 *            a list of nucleobase sequences
	 * @param k
	 *            the k in k-mer
	 * @param sliceThreshold
	 *            the threshold below which it is not worth dividing the slices
	 *            further
	 * @return an unmodifiable list of slices
	 */
	public static List<Slice<byte[]>> createSlicesBelowThreshold(List<byte[]> sequences, int k, int sliceThreshold) {

		LinkedList<Slice<byte[]>> linkedlist = new LinkedList<>();

		for (byte[] each : sequences) {
			addToCollectionKernel(linkedlist, each, 0, each.length + 1 - k, sliceThreshold);
		}
		return linkedlist;
	}

	/**
	 * Calculate a reasonable threshold to be used with
	 * {@link ThresholdSlices#createSlicesBelowThreshold(List, int, int)} where
	 * reasonable is defined to mean will create between 2X and 10X tasks per
	 * available processor.
	 * 
	 * @see Runtime#availableProcessors()
	 * 
	 * @param sequences
	 *            a list of nucleobase sequences
	 * @param k
	 *            the k in k-mer
	 * @return a reasonable threshold
	 */
	public static int calculateReasonableThreshold(List<byte[]> sequences, int k) {
		int numProc = Runtime.getRuntime().availableProcessors();
		int threshold = KMerUtils.calculateSumOfAllKMers(sequences, k) / (numProc);
		return threshold;
	}

	public static List<Slice<byte[]>> createSlicesBelowReasonableThreshold(List<byte[]> sequences, int k) {
		return createSlicesBelowThreshold(sequences, k, calculateReasonableThreshold(sequences, k));
	}
}
