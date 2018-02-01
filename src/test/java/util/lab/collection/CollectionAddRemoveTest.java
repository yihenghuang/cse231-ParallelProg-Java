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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

import edu.wustl.cse231s.junit.JUnitUtils;
import util.lab.rubric.UtilRubric;

/**
 * @author Ben Choi (benjaminchoi@wustl.edu)
 */
@UtilRubric(UtilRubric.Category.LIST_UNCATEGORIZED)
public class CollectionAddRemoveTest {

	@Test(timeout = JUnitUtils.DEFAULT_TIMEOUT)
	public void test() {
		Collection<Integer> collection = new LinkedNodesCollection<>();

		assertEquals("Your list is not initializing with a size of 0", 0, collection.size());
		assertTrue("Your list is not returning isEmpty correctly", collection.isEmpty());

		for (int i = 0; i < 100; ++i) {
			collection.add(i);
		}

		assertEquals("Your list does not update size correctly", 100, collection.size());
		assertFalse("Your list is not returning isEmpty correctly", collection.isEmpty());

		int count = 99;
		for (Integer num : collection) {
			assertEquals("Your list is not iterating properly, check your put method", (Integer) count, num);
			--count;
		}

		assertTrue("Your list is not removing properly, check your returns", collection.remove(0));
		// assertEquals("Your get method does not return -1 if item is not found", -1,
		// list.indexOf(0));
		// assertNull("Your list is not removing properly, check your next references",
		// list.get(99));

		assertEquals("Your list does not update size correctly", 99, collection.size());
		assertFalse("Your list is not returning isEmpty correctly", collection.isEmpty());
	}

}
