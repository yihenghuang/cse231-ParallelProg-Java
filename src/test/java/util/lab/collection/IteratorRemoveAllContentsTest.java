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
package util.lab.collection;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import edu.wustl.cse231s.junit.JUnitUtils;
import util.lab.rubric.UtilRubric;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link LinkedNodesIterator#remove()}
 */
@UtilRubric(UtilRubric.Category.ITERATOR_REMOVE)
public class IteratorRemoveAllContentsTest {
	@Rule
	public TestRule timeout = JUnitUtils.createTimeoutRule();

	private static <T> int sizeViaIterator(Collection<T> collection) {
		int result = 0;
		Iterator<T> iterator = collection.iterator();
		while (iterator.hasNext()) {
			iterator.next();
			result++;
		}
		return result;
	}

	@Test
	public void testSize() {
		char[] array = "abcde".toCharArray();
		ArrayUtils.reverse(array);

		Collection<Character> collection = new LinkedNodesCollection<>();
		for (char c : array) {
			collection.add(c);
		}

		int expectedSize = array.length;
		assertEquals(array.length, collection.size());

		Iterator<Character> iterator = collection.iterator();
		while (iterator.hasNext()) {
			iterator.next();
			iterator.remove();
			expectedSize--;
			int reportedSize = collection.size();
			int sizeViaIterator = sizeViaIterator(collection);
			assertEquals(expectedSize, reportedSize);
			assertEquals(expectedSize, sizeViaIterator);
		}
		assertEquals(0, expectedSize);
	}
}
