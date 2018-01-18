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
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.mutable.MutableInt;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import count.assignment.rubric.CountRubric;
import count.core.NucleobaseCountUtils;
import edu.wustl.cse231s.bioinformatics.Nucleobase;
import edu.wustl.cse231s.bioinformatics.io.resource.ChromosomeResource;

/**
 * @author Finn Voichick
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link NucleobaseCounting#countParallelDivideAndConquer(byte[], Nucleobase, int)}
 */
@RunWith(Parameterized.class)
@CountRubric(CountRubric.Category.DIVIDE_AND_CONQUER)
public class DivideAndConquerTest {
	private final byte[] chromosome;
	private final Nucleobase targetNucleobase;
	private final int threshold;
	private final int truthAndBeautyCount;

	public DivideAndConquerTest(ChromosomeResource chromosomeResource, Nucleobase targetNucleobase, int thresholdDenom)
			throws IOException {
		this.chromosome = chromosomeResource.getData();
		this.targetNucleobase = targetNucleobase;
		this.threshold = Math.max(this.chromosome.length / thresholdDenom, 2);
		this.truthAndBeautyCount = NucleobaseCountUtils.countSequential(chromosome, targetNucleobase);
	}

	@Test
	public void test() {
		MutableInt count = new MutableInt();
		launchApp(() -> {
			count.setValue(NucleobaseCounting.countParallelDivideAndConquer(chromosome, targetNucleobase, threshold));
		});
		assertEquals(truthAndBeautyCount, count.getValue().intValue());
	}

	@Parameters(name = "{0}, {1}, thresholdDenom={2}")
	public static Collection<Object[]> getConstructorArguments() {
		int[] thresholdDenoms = { 8, 71, 231 };

		List<Object[]> list = new LinkedList<>();
		for (ChromosomeResource chromosomeResource : ChromosomeResource.getTestEnumConstants()) {
			for (Nucleobase targetNucleobase : Nucleobase.values()) {
				for (int thresholdDenom : thresholdDenoms) {
					list.add(new Object[] { chromosomeResource, targetNucleobase, thresholdDenom });
				}
			}
		}
		return list;
	}
}
