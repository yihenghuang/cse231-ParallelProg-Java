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

package iterativeaveraging.demo;

import java.util.List;
import java.util.concurrent.ExecutionException;

import iterativeaveraging.core.IterativeAverager;
import slice.core.Slice;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class SequentialIterativeAverager implements IterativeAverager {
	@Override
	public void iterativelyAverage(List<Slice<double[]>> slices, double[] a, double b[], int iterationCount)
			throws InterruptedException, ExecutionException {
		for (int iteration = 0; iteration < iterationCount; iteration++) {
			double[] arrayPrev = ((iteration & 1) == 0) ? a : b;
			double[] arrayNext = ((iteration & 1) == 0) ? b : a;
			for (Slice<double[]> slice : slices) {
				for (int index = slice.getMinInclusive(); index < slice.getMaxExclusive(); index++) {
					arrayNext[index] = (arrayPrev[index - 1] + arrayPrev[index + 1]) * 0.5;
				}
			}
		}
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
