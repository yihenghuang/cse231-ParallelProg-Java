/*******************************************************************************
 * Copyright (C) 2016-2017 Dennis Cosgrove, Ben Choi
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
package util.core.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Map;

import org.junit.Test;

import edu.wustl.cse231s.junit.JUnitUtils;

/**
 * @author Ben Choi (benjaminchoi@wustl.edu)
 */

public abstract class AbstractComputeTest {

	protected abstract <K, V> Map<K, V> createMap();

	@Test(timeout = JUnitUtils.DEFAULT_TIMEOUT)
	public void test() {
		Map<Integer, Double> map = this.createMap();

		for (int i = 0; i < 10; ++i) {
			assertNull("Something is wrong with your put method, fix that first",
					map.put((Integer) i, (double) i / 10));
		}

		for (int i = 0; i < 10; ++i) {
			final int ii = i;
			assertNotNull("Your compute method should be returning something, but is not",
					map.compute((Integer) i, (key, value) -> {
						return new Double((double) ii / 100);
					}));

			assertEquals("Your compute method is not changing the value correctly", new Double((double) ii / 100),
					map.get(i));
		}

		assertEquals(0.11, map.compute(11, (key, value) -> {
			return 0.11;
		}).doubleValue(), /* epsilon= */0.0);
		// assertNull("Your compute method works on a non-existent key, which it
		// shouldn't",
		// map.compute(11, (key, value) -> {
		// return value = 0.11;
		// }));
	}

	@Test(timeout = JUnitUtils.DEFAULT_TIMEOUT)
	public void testReturnValueSimply() {
		Map<String, Integer> map = this.createMap();
		Integer valueShouldBe1 = map.compute("a", (k, v) -> {
			return 1;
		});
		assertEquals(1, valueShouldBe1.intValue());

		Integer valueShouldBeNull = map.compute("b", (k, v) -> {
			return null;
		});
		assertNull(valueShouldBeNull);
	}

	@Test(timeout = JUnitUtils.DEFAULT_TIMEOUT)
	public void testReturnValueMoreComprehensively() {
		Map<String, Integer> map = this.createMap();
		Integer valueShouldBe1 = map.compute("a", (k, v) -> {
			return 1;
		});
		assertEquals(1, valueShouldBe1.intValue());
		assertEquals(1, map.get("a").intValue());
		assertEquals(1, map.size());

		Integer valueShouldBeNull = map.compute("a", (k, v) -> {
			return null;
		});
		assertNull(valueShouldBeNull);
		assertNull(map.get("a"));
		assertEquals(0, map.size());
	}

}
