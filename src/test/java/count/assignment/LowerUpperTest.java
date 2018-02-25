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

import static edu.wustl.cse231s.v5.V5.launchApp;

import java.io.IOException;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import count.assignment.rubric.CountRubric;
import count.core.NucleobaseCountUtils;
import edu.wustl.cse231s.bioinformatics.Nucleobase;
import edu.wustl.cse231s.bioinformatics.io.resource.ChromosomeResource;
import edu.wustl.cse231s.junit.JUnitUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link NucleobaseCounting#countParallelLowerUpperSplit(byte[], Nucleobase)}
 */
@RunWith(Parameterized.class)
@CountRubric(CountRubric.Category.LOWER_UPPER)
public class LowerUpperTest {
	private final byte[] chromosome;
	private final Nucleobase targetNucleobase;

	public LowerUpperTest(ChromosomeResource chromosomeResource, Nucleobase targetNucleobase) throws IOException {
		this.chromosome = chromosomeResource.getData();
		this.targetNucleobase = targetNucleobase;
	}

	@Rule
	public TestRule timeout = JUnitUtils.createTimeoutRule();

	@Test
	@CountRubric(CountRubric.Category.LOWER_UPPER)
	public void testUpperLower() {
		int expectedCount = NucleobaseCountUtils.countSequential(chromosome, targetNucleobase);
		launchApp(() -> {
			int actualCount = NucleobaseCounting.countParallelLowerUpperSplit(chromosome, targetNucleobase);
			Assert.assertEquals(expectedCount, actualCount);
		});
	}

	@Parameters(name = "{0} {1}")
	public static Collection<Object[]> getConstructorArguments() {
		return JUnitUtils.toParameterizedArguments2(new ChromosomeResource[] { ChromosomeResource.HOMO_SAPIENS_Y },
				Nucleobase.values());
	}
}
