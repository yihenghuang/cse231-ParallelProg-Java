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

package scan.studio;

import static edu.wustl.cse231s.v5.V5.forall;

import java.util.concurrent.ExecutionException;

import scan.core.ArraysHolder;
import scan.core.Scan;

/**
 * @author Yiheng Huang
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class ParallelScan implements Scan {
	@Override
	public int[] sumScan(int[] data) throws InterruptedException, ExecutionException {

		int numCopiedDown = 1;
		int len = data.length;

		ArraysHolder arr = new ArraysHolder(data);

		while (numCopiedDown < len) {

			final int numAway = numCopiedDown;

			forall(0, len, (int i) -> {

				int[] dst = arr.getDst();
				int[] src = arr.getSrc();

				if (i >= numAway) {
					dst[i] = src[i] + src[i - numAway];
				} else {
					dst[i] = src[i];
				}
			});

			arr.nextSrcAndDst();
			numCopiedDown = 2 * numCopiedDown;
		}

		int[] result = arr.getSrc();
		return result;
	}

	@Override
	public boolean isInclusive() {
		return true;
	}
}
