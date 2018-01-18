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

import count.assignment.rubric.CountRubric;
import edu.wustl.cse231s.bioinformatics.Nucleobase;
import edu.wustl.cse231s.bioinformatics.io.resource.ChromosomeResource;
import edu.wustl.cse231s.print.AbstractNoPrintingTest;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link NucleobaseCounting}
 */
@CountRubric(CountRubric.Category.NO_PRINTING)
public class NoPrintingTest extends AbstractNoPrintingTest {
	@Override
	protected void testKernel() {
		byte[] chromosome = ChromosomeResource.HOMO_SAPIENS_Y.getData();
		Nucleobase targetNucleobase = Nucleobase.ADENINE;
		NucleobaseCounting.countSequential(chromosome, targetNucleobase);
		launchApp(() -> {
			NucleobaseCounting.countParallelLowerUpperSplit(chromosome, targetNucleobase);
			NucleobaseCounting.countParallelNWaySplit(chromosome, targetNucleobase, 71);
			NucleobaseCounting.countParallelDivideAndConquer(chromosome, targetNucleobase, chromosome.length / 231);
		});
	}
}
