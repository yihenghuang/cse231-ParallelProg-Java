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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import edu.wustl.cse231s.junit.JUnitUtils;

/**
 * @author Ben Choi (benjaminchoi@wustl.edu)
 */

public abstract class AbstractComprehensiveTest {
	protected abstract <K, V> Map<K, V> createMap();

	@Rule
	public TestRule timeout = JUnitUtils.createTimeoutRule();

	@Test
	public void test() {
		Map<Integer, Double> map = this.createMap();

		assertTrue("Your isEmpty method is not working properly", map.isEmpty());
		assertEquals("Your size method is not working properly", 0, map.size());

		for (int i = 0; i < 100; ++i) {
			assertNull("Something is wrong with your put method", map.put((Integer) i, (double) i / 10));
		}

		assertEquals("Your size method is not updating properly", 100, map.size());
		assertFalse("Your isEmpty method is not updating properly", map.isEmpty());

		int count = 99;
		for (int i = 0; i < 100; ++i) {
			assertEquals("Your get method is returning an incorrect value", new Double((double) i / 10), map.get(i));
			assertNotNull("Your compute method is not returning anything", map.compute(i, (key, value) -> {
				return value * 2;
			}));
			assertEquals("Your compute method is not returning the correct value", new Double((double) i / 5),
					map.get(i));
			assertNotNull("Your remove method should return the old value of the key before the deletion",
					map.remove(i));
			assertNull("Your map returns an entry even though it should have been deleted", map.get(i));
			assertEquals("Your map size is not updating properly", count, map.size());
			--count;
		}
	}
}
