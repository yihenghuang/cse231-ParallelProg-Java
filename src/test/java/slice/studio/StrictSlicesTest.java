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
package slice.studio;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import slice.core.Slice;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class StrictSlicesTest {
	@Test
	public void test() {
		Object[] data = new Object[502];
		List<Slice<Object[]>> slices = Slices.createNSlices(data, 5);

		Slice<Object[]> slice0 = slices.get(0);
		Slice<Object[]> slice1 = slices.get(1);
		Slice<Object[]> slice2 = slices.get(2);
		Slice<Object[]> slice3 = slices.get(3);
		Slice<Object[]> slice4 = slices.get(4);

		assertEquals(0, slice0.getMinInclusive());
		assertEquals(101, slice0.getMaxExclusive());

		assertEquals(101, slice1.getMinInclusive());
		assertEquals(202, slice1.getMaxExclusive());

		assertEquals(202, slice2.getMinInclusive());
		assertEquals(302, slice2.getMaxExclusive());

		assertEquals(302, slice3.getMinInclusive());
		assertEquals(402, slice3.getMaxExclusive());

		assertEquals(402, slice4.getMinInclusive());
		assertEquals(502, slice4.getMaxExclusive());
	}
}
