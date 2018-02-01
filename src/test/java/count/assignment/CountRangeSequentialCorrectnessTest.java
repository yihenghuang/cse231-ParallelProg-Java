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
package count.assignment;

import org.junit.Assert;
import org.junit.Test;

import count.assignment.rubric.CountRubric;
import edu.wustl.cse231s.bioinformatics.Nucleobase;
import edu.wustl.cse231s.junit.JUnitUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link NucleobaseCounting#countRangeSequential(byte[], Nucleobase, int, int)}
 */
@CountRubric(CountRubric.Category.RANGE)
public class CountRangeSequentialCorrectnessTest {
	@Test(timeout = JUnitUtils.DEFAULT_TIMEOUT)
	public void testAllAdenine() throws Exception {
		byte[] chromosome = "AAAAAAAA".getBytes();

		int actual = NucleobaseCounting.countRangeSequential(chromosome, Nucleobase.ADENINE, 0, chromosome.length);
		Assert.assertEquals(chromosome.length, actual);

		int min = 2;
		int maxExclusive = 5;
		actual = NucleobaseCounting.countRangeSequential(chromosome, Nucleobase.ADENINE, min, maxExclusive);
		Assert.assertNotEquals("guessing... do NOT count [0,chromosome.length).  count from [min,maxExclusive).",
				chromosome.length, actual);
		Assert.assertNotEquals("guessing... do NOT count [0,maxExclusive).  count from [min,maxExclusive).",
				maxExclusive, actual);
		Assert.assertNotEquals("guessing... do NOT count [min,chromosome.length).  count from [min,maxExclusive).",
				chromosome.length - min, actual);

		int expected = maxExclusive - min;
		Assert.assertEquals(expected, actual);
	}
}
