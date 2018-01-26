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

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

import util.lab.rubric.UtilRubric;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@UtilRubric(UtilRubric.Category.LIST_UNCATEGORIZED)
public class EqualsTest {
	private Collection<Integer> createBoxedPrimitiveList() {
		Collection<Integer> list = new LinkedNodesCollection<>();
		assertTrue(list.isEmpty());

		list.add(3);
		assertFalse(list.isEmpty());
		assertEquals(1, list.size());
		Iterator<Integer> iteratorA = list.iterator();
		assertTrue(iteratorA.hasNext());
		assertEquals(3, iteratorA.next().intValue());
		assertFalse(iteratorA.hasNext());

		list.add(2);
		assertFalse(list.isEmpty());
		assertEquals(2, list.size());
		Iterator<Integer> iteratorB = list.iterator();
		assertTrue(iteratorB.hasNext());
		assertEquals(2, iteratorB.next().intValue());
		assertTrue(iteratorB.hasNext());
		assertEquals(3, iteratorB.next().intValue());
		assertFalse(iteratorB.hasNext());

		list.add(1);
		assertFalse(list.isEmpty());
		assertEquals(3, list.size());
		Iterator<Integer> iteratorC = list.iterator();
		assertTrue(iteratorC.hasNext());
		assertEquals(1, iteratorC.next().intValue());
		assertTrue(iteratorC.hasNext());
		assertEquals(2, iteratorC.next().intValue());
		assertTrue(iteratorC.hasNext());
		assertEquals(3, iteratorC.next().intValue());
		assertFalse(iteratorC.hasNext());

		return list;
	}

	private Collection<BigDecimal> createNonPrimitiveList() {
		Collection<BigDecimal> list = new LinkedNodesCollection<>();
		assertTrue(list.isEmpty());

		list.add(new BigDecimal(3));
		assertFalse(list.isEmpty());
		assertEquals(1, list.size());
		Iterator<BigDecimal> iteratorA = list.iterator();
		assertTrue(iteratorA.hasNext());
		assertEquals(new BigDecimal(3), iteratorA.next());
		assertFalse(iteratorA.hasNext());

		list.add(new BigDecimal(2));
		assertFalse(list.isEmpty());
		assertEquals(2, list.size());
		Iterator<BigDecimal> iteratorB = list.iterator();
		assertTrue(iteratorB.hasNext());
		assertEquals(new BigDecimal(2), iteratorB.next());
		assertTrue(iteratorB.hasNext());
		assertEquals(new BigDecimal(3), iteratorB.next());
		assertFalse(iteratorB.hasNext());

		list.add(new BigDecimal(1));
		assertFalse(list.isEmpty());
		assertEquals(3, list.size());
		Iterator<BigDecimal> iteratorC = list.iterator();
		assertTrue(iteratorC.hasNext());
		assertEquals(new BigDecimal(1), iteratorC.next());
		assertTrue(iteratorC.hasNext());
		assertEquals(new BigDecimal(2), iteratorC.next());
		assertTrue(iteratorC.hasNext());
		assertEquals(new BigDecimal(3), iteratorC.next());
		assertFalse(iteratorC.hasNext());

		return list;
	}

	@Test
	public void testPrimitiveRemoveFirst() {
		Collection<Integer> list = createBoxedPrimitiveList();
		assertEquals(3, list.size());
		list.remove(1);
		assertEquals(2, list.size());

		Iterator<Integer> iterator = list.iterator();
		assertTrue(iterator.hasNext());
		assertEquals(2, iterator.next().intValue());
		assertTrue(iterator.hasNext());
		assertEquals(3, iterator.next().intValue());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testPrimitiveRemoveMiddle() {
		Collection<Integer> list = createBoxedPrimitiveList();
		assertEquals(3, list.size());
		list.remove(2);
		assertEquals(2, list.size());

		Iterator<Integer> iterator = list.iterator();
		assertTrue(iterator.hasNext());
		assertEquals(1, iterator.next().intValue());
		assertTrue(iterator.hasNext());
		assertEquals(3, iterator.next().intValue());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testPrimitiveRemoveEnd() {
		Collection<Integer> list = createBoxedPrimitiveList();
		assertEquals(3, list.size());
		list.remove(3);
		assertEquals(2, list.size());

		Iterator<Integer> iterator = list.iterator();
		assertTrue(iterator.hasNext());
		assertEquals(1, iterator.next().intValue());
		assertTrue(iterator.hasNext());
		assertEquals(2, iterator.next().intValue());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testNonPrimitiveRemoveFirst() {
		Collection<BigDecimal> list = createNonPrimitiveList();
		assertEquals(3, list.size());
		list.remove(new BigDecimal(1));
		assertEquals(2, list.size());

		Iterator<BigDecimal> iterator = list.iterator();
		assertTrue(iterator.hasNext());
		assertEquals(new BigDecimal(2), iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(new BigDecimal(3), iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testNonPrimitiveRemoveMiddle() {
		Collection<BigDecimal> list = createNonPrimitiveList();
		assertEquals(3, list.size());
		list.remove(new BigDecimal(2));
		assertEquals(2, list.size());

		Iterator<BigDecimal> iterator = list.iterator();
		assertTrue(iterator.hasNext());
		assertEquals(new BigDecimal(1), iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(new BigDecimal(3), iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testNonPrimitiveRemoveEnd() {
		Collection<BigDecimal> list = createNonPrimitiveList();
		assertEquals(3, list.size());
		list.remove(new BigDecimal(3));
		assertEquals(2, list.size());

		Iterator<BigDecimal> iterator = list.iterator();
		assertTrue(iterator.hasNext());
		assertEquals(new BigDecimal(1), iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(new BigDecimal(2), iterator.next());
		assertFalse(iterator.hasNext());
	}
}
