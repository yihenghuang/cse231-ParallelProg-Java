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
package mapreduce.framework.lab.simple;

import static edu.wustl.cse231s.v5.V5.launchApp;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.mutable.MutableObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import edu.wustl.cse231s.junit.JUnitUtils;
import mapreduce.apps.intsum.studio.IntegerSumClassicReducer;
import mapreduce.framework.lab.rubric.MapReduceRubric;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link SimpleMapReduceFramework#finishAll(Map)}
 */
@MapReduceRubric(MapReduceRubric.Category.SIMPLE_FINISH_ALL)
public class FinishAllPointedTest {
	private void test(int... array) {
		List<Integer> list = Arrays.stream(array).boxed().collect(Collectors.toList());
		String key = "testKey";

		Map<String, List<Integer>> accumulateAllResult = new HashMap<>();
		accumulateAllResult.put(key, list);

		MutableObject<Map<String, Integer>> actualReference = new MutableObject<>();
		launchApp(() -> {
			actualReference.setValue(
					AccessSimpleFrameworkUtils.finishAllOnly(accumulateAllResult, new IntegerSumClassicReducer()));
		});

		Map<String, Integer> actual = actualReference.getValue();
		int expectedValue = list.stream().mapToInt(Integer::intValue).sum();
		assertEquals(1, actual.size());
		assertTrue(actual.containsKey(key));
		assertEquals(expectedValue, actual.get(key).intValue());
	}

	@Rule
	public TestRule timeout = JUnitUtils.createTimeoutRule();

	@Test
	public void test42() {
		test(42);
	}

	@Test
	public void testEmpty() {
		test();
	}

	@Test
	public void testAll1s() {
		int expected = 71;
		int[] array = new int[expected];
		Arrays.fill(array, 1);
		test(array);
	}

	@Test
	public void testFibonaccis() {
		test(1, 1, 2, 3, 5, 8, 13, 21);
	}

	@Test
	public void testGauss() {
		int[] array = new int[100];
		for (int i = 0; i < 100; i++) {
			array[i] = i + 1;
		}
		test(array);
	}
}
