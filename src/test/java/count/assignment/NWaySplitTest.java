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
import static edu.wustl.cse231s.v5.V5.numWorkerThreads;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
 *         {@link NucleobaseCounting#countParallelNWaySplit(byte[], Nucleobase, int)}
 */
@RunWith(Parameterized.class)
@CountRubric(CountRubric.Category.NWAY)
public class NWaySplitTest {
	private final byte[] chromosome;
	private final Nucleobase targetNucleobase;
	private final int numTasks;

	public NWaySplitTest(ChromosomeResource chromosomeResource, Nucleobase targetNucleobase, int numTasks)
			throws IOException {
		this.chromosome = chromosomeResource.getData();
		this.targetNucleobase = targetNucleobase;
		this.numTasks = numTasks;
	}

	@Rule
	public TestRule timeout = JUnitUtils.createTimeoutRule();

	@Test
	public void testNWay() {
		int expectedCount = NucleobaseCountUtils.countSequential(chromosome, targetNucleobase);
		launchApp(() -> {
			int actualCount = NucleobaseCounting.countParallelNWaySplit(chromosome, targetNucleobase, numTasks);
			Assert.assertEquals(expectedCount, actualCount);
		});
	}

	@Parameters(name = "{0} {1} numTasks={2}")
	public static Collection<Object[]> getConstructorArguments() {
		int numProcessors = Runtime.getRuntime().availableProcessors();
		List<Integer> list = Arrays.asList(numProcessors, numProcessors * 2, numProcessors * 10, 71);
		list.sort((a, b) -> a - b);
		return JUnitUtils.toParameterizedArguments3(new ChromosomeResource[] { ChromosomeResource.HOMO_SAPIENS_Y },
				Nucleobase.values(), list.toArray());
	}
}
