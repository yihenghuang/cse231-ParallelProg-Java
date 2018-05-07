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

package sort.fun.merge;

import static edu.wustl.cse231s.v5.V5.async;
import static edu.wustl.cse231s.v5.V5.finish;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import sort.core.merge.Combiner;

/**
 * @author Yiheng Huang
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class ParallelCombiner implements Combiner {
	private final int[] buffer;
	private final int threshold;

	public ParallelCombiner(int bufferLength, int threshold) {
		this.buffer = new int[bufferLength];
		this.threshold = threshold;
	}

	private void sequentialCombine(int bufferIndex, int[] data, int aMin, int aMaxExclusive, int bMin,
			int bMaxExclusive) {
		int indexA = aMin;
		int indexB = bMin;
		while (indexA < aMaxExclusive && indexB < bMaxExclusive) {
			this.buffer[bufferIndex++] = (data[indexA] < data[indexB]) ? data[indexA++] : data[indexB++];
		}
		while (indexA < aMaxExclusive) {
			this.buffer[bufferIndex++] = data[indexA++];
		}
		while (indexB < bMaxExclusive) {
			this.buffer[bufferIndex++] = data[indexB++];
		}
	}

	private void parallelCombine(int bufferIndex, int[] data, int aMin, int aMaxExclusive, int bMin, int bMaxExclusive)
			throws InterruptedException, ExecutionException {
		int m = aMaxExclusive - aMin;
		int n = bMaxExclusive - bMin;
		if (m < n) {
			int temp = aMin;
			aMin = bMin;
			bMin = temp;
			temp = aMaxExclusive;
			aMaxExclusive = bMaxExclusive;
			bMaxExclusive = temp;
			temp = m;
			m = n;
			n = temp;
		}
		if (m < threshold) {
			sequentialCombine(bufferIndex, data, aMin, aMaxExclusive, bMin, bMaxExclusive);
		} else {
			int r = (aMin + aMaxExclusive) / 2;
			int[] data2 = new int[aMaxExclusive - aMin];
			for (int i = bMin; i < bMaxExclusive; ++i) {
				data2[i - bMin] = data[i];
			}
			int s = Arrays.binarySearch(data2, r);
			if (s < 0) {
				s = -1 * s;
			}
			int t = bufferIndex + (r - aMin) + (s - bMin);
			final int maxA = aMaxExclusive;
			final int maxB = bMaxExclusive;
			final int minA = aMin;
			final int minB = bMin;
			final int ss = s;
			finish(() -> {
				async(() -> {
					parallelCombine(bufferIndex, data, minA, r - 1, minB, ss - 1);
				});
				parallelCombine(t + 1, data, r + 1, maxA, ss, maxB);
			});
		}
	}

	@Override
	public void combineRange(int[] data, int min, int mid, int maxExclusive)
			throws InterruptedException, ExecutionException {
		parallelCombine(min, data, min, mid, mid, maxExclusive);
		System.arraycopy(buffer, min, data, min, maxExclusive - min);
	}
}
