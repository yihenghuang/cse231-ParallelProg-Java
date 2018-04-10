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

import kmer.core.KMerCount;
import kmer.core.KMerUtils;

/**
 * A {@link KMerCount} implementation that wraps an int array.
 * 
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class IntArrayKMerCount implements KMerCount {
	private final int k;
	private final int[] array;

	/**
	 * Constructs an IntArrayKMerCount that wrap the given int array.
	 * 
	 * @param k
	 * @param array
	 *            the array to wrap
	 */
	public IntArrayKMerCount(int k, int[] array) {
		this.k = k;
		this.array = array;
	}

	@Override
	public int getCount(byte[] kMer) {
		return this.array[KMerUtils.toPackedInt(kMer)];
	}

	@Override
	public Iterator<byte[]> iterator() {
		return new IndexableIntegersIterator(k, array.length, (index) -> array[index]);
	}

}
