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

package edu.wustl.cse231s.v5;

import static edu.wustl.cse231s.v5.V5.chunked;
import static edu.wustl.cse231s.v5.V5.forall;
import static edu.wustl.cse231s.v5.V5.launchApp;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

import edu.wustl.cse231s.v5.api.CheckedConsumer;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class ForallTest {
	private static Integer[] createValues() {
		Random random = new Random();
		Integer[] result = new Integer[1_000];
		for (int i = 0; i < result.length; i++) {
			result[i] = random.nextInt();
		}
		return result;
	}

	private static class Pair {
		Integer[] values;
		Set<Integer> actual;
	}

	private static void test(CheckedConsumer<Pair> task) {
		Pair pair = new Pair();
		pair.values = createValues();

		Set<Integer> expected = new HashSet<>(Arrays.asList(pair.values));
		pair.actual = ConcurrentHashMap.newKeySet(expected.size());
		launchApp(() -> {
			task.accept(pair);
		});
		assertEquals(expected, pair.actual);
	}

	@Test
	public void testForallArray() {
		test((pair) -> {
			forall(pair.values, (v) -> {
				pair.actual.add(v);
			});
		});
	}

	@Test
	public void testForallChunkedArray() {
		test((pair) -> {
			forall(chunked(), pair.values, (v) -> {
				pair.actual.add(v);
			});
		});
	}

	@Test
	public void testForallRange() {
		test((pair) -> {
			forall(0, pair.values.length, (i) -> {
				pair.actual.add(pair.values[i]);
			});
		});
	}

	@Test
	public void testForallChunkedRange() {
		test((pair) -> {
			forall(chunked(), 0, pair.values.length, (i) -> {
				pair.actual.add(pair.values[i]);
			});
		});
	}

	@Test
	public void testForallIterable() {
		test((pair) -> {
			Iterable<Integer> list = Arrays.asList(pair.values);
			forall(list, (v) -> {
				pair.actual.add(v);
			});
		});
	}

	@Test
	public void testForallChunkedIterable() {
		test((pair) -> {
			Iterable<Integer> list = Arrays.asList(pair.values);
			forall(chunked(), list, (v) -> {
				pair.actual.add(v);
			});
		});
	}
}
