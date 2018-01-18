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

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import count.assignment.rubric.CountRubric;
import count.core.NucleobaseCountUtils;
import edu.wustl.cse231s.bioinformatics.Nucleobase;
import edu.wustl.cse231s.bioinformatics.io.resource.ChromosomeResource;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link NucleobaseCounting#countParallelLowerUpperSplit(byte[], Nucleobase)}
 */
@CountRubric(CountRubric.Category.LOWER_UPPER)
public class LowerUpperTest {
	private static List<byte[]> chromosomes;
	static {
		chromosomes = new ArrayList<>(1);
		chromosomes.add(ChromosomeResource.HOMO_SAPIENS_Y.getData());
	}

	private void testUpperLowerNucleobase(Nucleobase nucleobase) {
		launchApp(() -> {
			for (byte[] chromosome : chromosomes) {
				int expectedCount = NucleobaseCountUtils.countSequential(chromosome, nucleobase);
				int actualCount = NucleobaseCounting.countParallelLowerUpperSplit(chromosome, nucleobase);
				Assert.assertEquals(expectedCount, actualCount);
			}
		});
	}

	@Test
	@CountRubric(CountRubric.Category.LOWER_UPPER)
	public void testUpperLower() {
		for (Nucleobase nucleobase : Nucleobase.values()) {
			if (nucleobase == Nucleobase.URACIL) {
				// pass
			} else {
				this.testUpperLowerNucleobase(nucleobase);
			}
		}
	}

	@Test
	@CountRubric(CountRubric.Category.LOWER_UPPER)
	public void testUpperLowerUracil() {
		this.testUpperLowerNucleobase(Nucleobase.URACIL);
	}
}
