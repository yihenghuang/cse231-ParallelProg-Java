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

package util.core.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Lists;

import edu.wustl.cse231s.junit.JUnitUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link LinkedNodesIterator#remove()}
 */
public abstract class AbstractSizeAfterRemoveTest {
	protected abstract <K, V> Map<K, V> createMap();

	private Map<Character, Integer> createMap(String s) {
		Map<Character, Integer> result = this.createMap();
		int index = 0;
		for (Character c : Lists.charactersOf(s)) {
			result.put(c, index);
			index++;
		}
		return result;
	}

	@Test(timeout = JUnitUtils.DEFAULT_TIMEOUT)
	public void testRemoveSinglePresentKey() {
		String s = "abcde";
		for (Character c : Lists.charactersOf(s)) {
			Map<Character, Integer> map = createMap(s);
			assertEquals(s.length(), map.size());
			assertTrue(map.containsKey(c));
			map.remove(c);
			assertEquals(s.length() - 1, map.size());
			assertFalse(map.containsKey(c));
		}
	}

	@Test(timeout = JUnitUtils.DEFAULT_TIMEOUT)
	public void testRemoveAllPresentKeys() {
		String s = "abcde";
		Map<Character, Integer> map = createMap(s);
		int expectedSize = s.length();
		for (Character c : Lists.charactersOf(s)) {
			assertEquals(expectedSize, map.size());
			assertTrue(map.containsKey(c));
			map.remove(c);
			expectedSize--;
			assertEquals(expectedSize, map.size());
			assertFalse(map.containsKey(c));
		}
		assertTrue(expectedSize == 0);
	}

	@Test(timeout = JUnitUtils.DEFAULT_TIMEOUT)
	public void testRemoveSingleAbsentKey() {
		String s = "abcde";
		char absentCharacter = 'f';
		Map<Character, Integer> map = createMap(s);
		assertEquals(s.length(), map.size());
		assertFalse(map.containsKey(absentCharacter));
		map.remove('f');
		assertEquals(s.length(), map.size());
		assertFalse(map.containsKey(absentCharacter));
	}
}
