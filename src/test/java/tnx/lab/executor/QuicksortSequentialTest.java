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
package tnx.lab.executor;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import sort.core.RandomDataUtils;
import sort.core.quick.SequentialPartitioner;
import tnx.lab.executor.XQuicksort;
import tnx.lab.rubric.TnXRubric;

/**
 * @author Finn Voichick
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link XQuicksort#sequentialQuicksort(int[], sort.core.quick.Partitioner)}
 */
@RunWith(Parameterized.class)
@TnXRubric(TnXRubric.Category.SEQUENTIAL_QUICKSORT)
public class QuicksortSequentialTest {
	private final int length;

	public QuicksortSequentialTest(int length) {
		this.length = length;
	}

	@Test
	public void test() throws InterruptedException, ExecutionException {
		int[] array = RandomDataUtils.createRandomData(this.length, System.currentTimeMillis());
		int[] sorted = Arrays.copyOf(array, array.length);
		Arrays.sort(sorted);

		XQuicksort.sequentialQuicksort(array, new SequentialPartitioner());
		Assert.assertArrayEquals("Array must be sorted", sorted, array);
	}

	@Parameters(name = "length={0}")
	public static Collection<Object[]> getConstructorArguments() {
		List<Object[]> result = new LinkedList<>();
		result.add(new Object[] { 24 });
		result.add(new Object[] { 10_000 });
		return result;
	}
}
