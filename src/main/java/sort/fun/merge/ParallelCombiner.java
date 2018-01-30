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

import edu.wustl.cse231s.NotYetImplementedException;
import sort.core.merge.Combiner;

/**
 * @author __STUDENT_NAME__
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
		throw new NotYetImplementedException();
	}

	@Override
	public void combineRange(int[] data, int min, int mid, int maxExclusive)
			throws InterruptedException, ExecutionException {
		parallelCombine(min, data, min, mid, mid, maxExclusive);
		System.arraycopy(buffer, min, data, min, maxExclusive - min);
	}
}
