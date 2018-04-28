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
package mapreduce.apps.kmer.studio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.mutable.MutableInt;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import edu.wustl.cse231s.junit.JUnitUtils;
import mapreduce.apps.kmer.studio.KMerMapper;
import mapreduce.framework.core.Mapper;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@RunWith(Parameterized.class)
public class KMerSpecificKMapperTest {
	private final String chromosomeText;

	public KMerSpecificKMapperTest(String chromosomeText) {
		this.chromosomeText = chromosomeText;
	}

	@Rule
	public TestRule timeout = JUnitUtils.createTimeoutRule();

	@Test
	public void testKEqualTo1() {
		Mapper<byte[], String, Integer> mapper = new KMerMapper(1);
		MutableInt i = new MutableInt(0);
		byte[] chromosome = chromosomeText.getBytes(StandardCharsets.UTF_8);
		mapper.map(chromosome, (k, v) -> {
			assertEquals(1, v.intValue());
			assertEquals(1, k.length());
			assertEquals(chromosomeText.charAt(i.getValue()), k.charAt(0));
			i.increment();
		});
		assertEquals(i.intValue(), chromosomeText.length());
	}

	@Test
	public void testKEqualToN() {
		Mapper<byte[], String, Integer> mapper = new KMerMapper(chromosomeText.length());
		MutableInt i = new MutableInt(0);
		byte[] chromosome = chromosomeText.getBytes(StandardCharsets.UTF_8);
		mapper.map(chromosome, (k, v) -> {
			assertEquals(1, v.intValue());
			assertEquals(chromosomeText, k);
			i.increment();
		});
		assertEquals(1, i.intValue());
	}

	@Test
	public void testKGreaterThanN() {
		Mapper<byte[], String, Integer> mapper = new KMerMapper(chromosomeText.length() + 1);
		byte[] chromosome = chromosomeText.getBytes(StandardCharsets.UTF_8);
		mapper.map(chromosome, (k, v) -> {
			assertTrue(false);
		});
	}

	@Parameters(name = "{0}; k={1}")
	public static Collection<Object[]> getConstructorArguments() {
		List<Object[]> result = new LinkedList<>();
		String asThenTs = "AAATTT";
		result.add(new Object[] { asThenTs });

		String actgTwice = "ACGTACTG";
		result.add(new Object[] { actgTwice });

		return result;
	}
}
