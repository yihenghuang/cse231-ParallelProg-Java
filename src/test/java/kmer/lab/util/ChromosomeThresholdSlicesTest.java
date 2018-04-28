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

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import edu.wustl.cse231s.bioinformatics.io.resource.ChromosomeResource;
import edu.wustl.cse231s.junit.JUnitUtils;
import kmer.lab.rubric.KMerRubric;
import kmer.lab.util.ThresholdSlices;
import slice.core.Slice;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link ThresholdSlices}
 */
@KMerRubric(KMerRubric.Category.THRESHOLD_SLICES)
@RunWith(Parameterized.class)
public class ChromosomeThresholdSlicesTest {
	private final ChromosomeResource chromosomeResource;
	private final int k;

	public ChromosomeThresholdSlicesTest(ChromosomeResource chromosomeResource, int k) {
		this.chromosomeResource = chromosomeResource;
		this.k = k;
	}
	
	@Rule
	public TestRule timeout = JUnitUtils.createTimeoutRule(10);

	@Test
	public void test() throws IOException {
		List<byte[]> sequences = ChromosomeResource.readSubSequences(chromosomeResource);
		int sliceThreshold = ThresholdSlices.calculateReasonableThreshold(sequences, 12);
		List<Slice<byte[]>> slices = ThresholdSlices.createSlicesBelowThreshold(sequences, k, sliceThreshold);

		int numProcessors = Runtime.getRuntime().availableProcessors();

		int desiredMin = 2 * numProcessors;
		int desiredMax = 10 * numProcessors;

		// we choose not to nitpick and allow the maximum to be twice as many as
		// desired, plus allow for an extra task per sub-sequence
		int generousMax = desiredMax;
		generousMax *= 2;
		generousMax += sequences.size();

		int numTasks = slices.size();

		assertThat(
				String.format("slice threshold %d failed to produce acceptable number of slices (eventually tasks)",
						sliceThreshold),
				numTasks, allOf(greaterThanOrEqualTo(desiredMin), lessThanOrEqualTo(generousMax)));
	}

	@Parameters(name = "{0}, k= {1}")
	public static Collection<Object[]> getConstructorArguments() {
		List<Object[]> result = new LinkedList<>();
		result.add(new Object[] { ChromosomeResource.HOMO_SAPIENS_Y, 12 });
		result.add(new Object[] { ChromosomeResource.HOMO_SAPIENS_X, 12 });
		return result;
	}
}
