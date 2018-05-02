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
package kmer.lab.util;

import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import edu.wustl.cse231s.junit.JUnitUtils;
import slice.core.Slice;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link ThresholdSlices#createSlicesBelowThreshold(List, int, int)}
 */
@RunWith(Parameterized.class)
public class ThresholdSlicesComprehensiveTest {
	private final int arrayLength;
	private final int k;
	private final int sliceThreshold;

	public ThresholdSlicesComprehensiveTest(int arrayLength, int k, int sliceThreshold) {
		this.arrayLength = arrayLength;
		this.k = k;
		this.sliceThreshold = sliceThreshold;
	}

	@Rule
	public TestRule timeout = JUnitUtils.createTimeoutRule();

	@Test
	public void test() {
		byte[] array = new byte[arrayLength];
		List<byte[]> subSequences = Arrays.asList(array);
		List<Slice<byte[]>> originalSlices = ThresholdSlices.createSlicesBelowThreshold(subSequences, k,
				sliceThreshold);
		assertNotNull(originalSlices);

		if (k <= array.length) {
			List<Slice<byte[]>> sortedSlices = new ArrayList<>(originalSlices);
			sortedSlices.sort((a, b) -> {
				return Integer.compare(a.getMinInclusive(), b.getMinInclusive());
			});
			assertNotNull(sortedSlices);

			Iterator<Slice<byte[]>> iterator = sortedSlices.iterator();
			Slice<byte[]> slice0 = iterator.next();
			assertNotNull(slice0);
			assertSame(array, slice0.getOriginalUnslicedData());
			assertEquals(0, slice0.getMinInclusive());

			int prevMax = slice0.getMaxExclusive();
			while (iterator.hasNext()) {
				Slice<byte[]> slice = iterator.next();
				assertNotNull(slice);
				assertSame(array, slice.getOriginalUnslicedData());
				assertEquals(prevMax, slice.getMinInclusive());
				int sliceLength = slice.getMaxExclusive() - slice.getMinInclusive();
				assertThat(sliceLength, lessThanOrEqualTo(sliceThreshold));
				prevMax = slice.getMaxExclusive();
			}
			assertEquals(array.length - k + 1, prevMax);
		} else {
			if (originalSlices.size() == 1) {
				Slice<byte[]> slice = originalSlices.get(0);
				assertEquals(0, slice.getMinInclusive());
				assertThat(slice.getMaxExclusive(), lessThanOrEqualTo(0));
			} else {
				assertEquals(0, originalSlices.size());
			}
		}
	}

	@Parameters(name = "arrayLength={0}; k={1}; sliceThreshold={2}")
	public static Collection<Object[]> getConstructorArguments() {
		List<Object[]> result = new LinkedList<>();
		for (int length = 71; length < 231; length += 11) {
			for (int k : new int[] { 1, 12, 23, length }) {
				for (int sliceThreshold : new int[] { 2, 3, 8, 17, 101 }) {
					result.add(new Object[] { length, k, sliceThreshold });
				}
			}
		}
		return result;
	}

}
