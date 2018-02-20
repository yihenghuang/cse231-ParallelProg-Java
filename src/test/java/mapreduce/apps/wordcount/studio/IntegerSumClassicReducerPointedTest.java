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
package mapreduce.apps.wordcount.studio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import mapreduce.apps.intsum.studio.IntegerSumClassicReducer;
import mapreduce.collector.studio.ClassicReducer;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link IntegerSumClassicReducer}
 */
public class IntegerSumClassicReducerPointedTest {
	private <A> void testValues(List<Integer> items) {
		ClassicReducer<Integer, Integer> reducer = new IntegerSumClassicReducer();
		int actualReduction = reducer.finisher().apply(items);
		int expectedReduction = items.parallelStream().reduce(0, Integer::sum);
		if (expectedReduction != items.size()) {
			Assert.assertNotEquals(actualReduction, items.size());
		}
		Assert.assertEquals(actualReduction, expectedReduction);
	}

	@Test
	public void testNotJustOnes() {
		this.testValues(Arrays.asList(1, 2, 3, 4));
	}

	@Test
	public void testOnes() {
		int[] lengths = { 0, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89 };
		for (int length : lengths) {
			List<Integer> list = new ArrayList<>(length);
			for (int i = 0; i < length; i++) {
				list.add(1);
			}
			this.testValues(list);
		}
	}
}
