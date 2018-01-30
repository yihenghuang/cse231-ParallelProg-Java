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

package sort.core.merge;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.junit.Assert;

import sort.core.SortUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class AbstractMergeSortTest {
	private final int[] original;
	private final int[] expected;

	public AbstractMergeSortTest() {
		this.original = new int[10_000];
		Random random = new Random();
		for (int i = 0; i < original.length; i++) {
			this.original[i] = random.nextInt(this.original.length*10);
		}

		this.expected = Arrays.copyOf(this.original, this.original.length);
		Arrays.sort(this.expected);
	}
	
	protected static interface MergeSorter {
		void mergeSort(int[] array) throws InterruptedException, ExecutionException;
	}

	protected void testMergeSorter(MergeSorter mergeSorter)
			throws InterruptedException, ExecutionException {
		int[] array = Arrays.copyOf(original, original.length);
		mergeSorter.mergeSort(array);
		Assert.assertTrue(Arrays.toString(array), SortUtils.isInSortedOrder(array));
		Assert.assertArrayEquals(this.expected, array);
	}

}
