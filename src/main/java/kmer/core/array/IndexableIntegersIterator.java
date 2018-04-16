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
package kmer.core.array;

import java.util.Iterator;
import java.util.function.IntUnaryOperator;

import kmer.core.KMerUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public final class IndexableIntegersIterator implements Iterator<byte[]> {
	private final int k;
	private final int indexCount;
	private final IntUnaryOperator indexToValueOperator;
	private int nextIndex;

	public IndexableIntegersIterator(int k, int indexCount, IntUnaryOperator indexToValueOperator) {
		this.k = k;
		this.indexCount = indexCount;
		this.indexToValueOperator = indexToValueOperator;
		this.nextIndex = -1;
		this.updateNextIndex();
	}

	private void updateNextIndex() {
		this.nextIndex++;
		while (this.nextIndex < this.indexCount) {
			if (this.indexToValueOperator.applyAsInt(this.nextIndex) != 0) {
				return;
			}
			this.nextIndex++;
		}
	}

	@Override
	public boolean hasNext() {
		return this.nextIndex < this.indexCount;
	}

	@Override
	public byte[] next() {
		int packedKMer = this.indexToValueOperator.applyAsInt(this.nextIndex);
		this.updateNextIndex();
		return KMerUtils.unpackInt(packedKMer, k);
	}

}
