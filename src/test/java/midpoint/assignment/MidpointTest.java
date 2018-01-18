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
package midpoint.assignment;

import org.junit.Assert;
import org.junit.Test;

import count.assignment.rubric.CountRubric;

/**
 * @author Ben Choi
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link MidpointUtils#calculateMidpoint(int, int)}
 */
@CountRubric(CountRubric.Category.MIDPOINT)
public class MidpointTest {
	private static void assertWithinRange(String message, int expectedMinInclusive, int expected,
			int expectedMaxInclusive, int actual) {
		String prefix = actual + " not in acceptable range [" + expectedMinInclusive + "," + expectedMaxInclusive
				+ "].  Expected: " + expected + ". ";
		Assert.assertTrue(prefix + message, expectedMinInclusive <= actual);
		Assert.assertTrue(prefix + message, expectedMaxInclusive >= actual);
	}

	private static void assertWithinOneOfExpected(String message, int lowerBound, int upperBound) {
		int expected = (lowerBound + upperBound) / 2;
		int actual = MidpointUtils.calculateMidpoint(lowerBound, upperBound);
		assertWithinRange(message, expected - 1, expected, expected + 1, actual);
	}

	@Test
	public void lowerBoundZero() {
		assertWithinOneOfExpected("The midpoint does not work with a lower bound of zero and an even upper bound!", 0,
				100);
		assertWithinOneOfExpected("The midpoint does not work with a lower bound of zero and an odd upper bound!", 0,
				77);
	}

	@Test
	public void lowerBoundNotZero() {
		assertWithinOneOfExpected("The midpoint does not work with a non-zero lower bound and even numbers!", 90, 100);
		assertWithinOneOfExpected("The midpoint does not work with a non-zero lower bound and odd numbers!", 95, 105);
	}

}
