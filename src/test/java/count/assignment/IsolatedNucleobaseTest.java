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

import org.junit.Test;

import count.assignment.rubric.CountRubric;
import edu.wustl.cse231s.bioinformatics.Nucleobase;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link NucleobaseCounting}
 */
public class IsolatedNucleobaseTest {
	@Test
	@CountRubric(CountRubric.Category.SEQUENTIAL)
	public void justASequential() {
		byte[] b = new byte[100];
		for (int i = 0; i < 100; ++i) {
			b[i] = Nucleobase.ADENINE.toByte();
		}
		for (Nucleobase n : Nucleobase.values()) {
			int count = NucleobaseCounting.countSequential(b, n);
			if (n == Nucleobase.ADENINE) {
				assertEquals("Your sequential method is not counting the correct number of nucleobases", 100, count);
			} else {
				assertEquals("Your sequential method is not counting the correct number of nucleobaases", 0, count);
			}
		}
	}

	@Test
	@CountRubric(CountRubric.Category.LOWER_UPPER)
	public void justAUpperLowerSplit() {
		byte[] b = new byte[100];
		for (int i = 0; i < 100; ++i) {
			b[i] = Nucleobase.ADENINE.toByte();
		}
		launchApp(() -> {
			for (Nucleobase n : Nucleobase.values()) {
				int count = NucleobaseCounting.countParallelLowerUpperSplit(b, n);
				if (n == Nucleobase.ADENINE) {
					assertEquals("Your upper/lower split method is not counting the correct number of nucleobases", 100,
							count);
				} else {
					assertEquals("Your upper/lower split method is not counting the correct number of nucleobaases", 0,
							count);
				}
			}
		});
	}

	@Test
	@CountRubric(CountRubric.Category.NWAY)
	public void justANWaySplit() {
		byte[] b = new byte[100];
		for (int i = 0; i < 100; ++i) {
			b[i] = Nucleobase.ADENINE.toByte();
		}
		launchApp(() -> {
			for (Nucleobase n : Nucleobase.values()) {
				int count = NucleobaseCounting.countParallelNWaySplit(b, n, 4);
				if (n == Nucleobase.ADENINE) {
					assertEquals("Your n-way split method is not counting the correct number of nucleobases", 100,
							count);
				} else {
					assertEquals("Your n-way split method is not counting the correct number of nucleobaases", 0,
							count);
				}
			}
		});
	}

}
