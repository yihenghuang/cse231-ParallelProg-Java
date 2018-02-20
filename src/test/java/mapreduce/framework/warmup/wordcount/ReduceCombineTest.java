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
package mapreduce.framework.warmup.wordcount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import mapreduce.framework.warmup.wordcount.WordCountConcreteStaticMapReduce;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link WordCountConcreteStaticMapReduce#reduceCombine(List, List)}
 */
public class ReduceCombineTest {
	@Test
	public void test() {
		List<Integer> a = Arrays.asList(1, 1, 2, 3);
		List<Integer> b = Arrays.asList(5, 8, 13, 21);
		List<Integer> expected = new ArrayList<>(a.size() + b.size());
		expected.addAll(a);
		expected.addAll(b);
		List<Integer> actual = new LinkedList<>();
		WordCountConcreteStaticMapReduce.reduceCombine(actual, a);
		WordCountConcreteStaticMapReduce.reduceCombine(actual, b);
		Assert.assertEquals(expected, actual);
	}
}
