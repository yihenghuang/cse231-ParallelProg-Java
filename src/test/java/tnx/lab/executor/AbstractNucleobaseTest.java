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
package tnx.lab.executor;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import org.hamcrest.Matcher;

import count.core.NucleobaseCountUtils;
import edu.wustl.cse231s.bioinformatics.Nucleobase;
import edu.wustl.cse231s.bioinformatics.io.resource.ChromosomeResource;
import edu.wustl.cse231s.executors.BookkeepingExecutorService;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public abstract class AbstractNucleobaseTest extends AbstractExecutorTest {
	private final byte[] chromosome;
	private final Nucleobase nucleobase;
	private final int expectedResult;

	public AbstractNucleobaseTest(Nucleobase nucleobase) throws IOException {
		this.chromosome = ChromosomeResource.MITOCHONDRION.getData();
		this.nucleobase = nucleobase;
		this.expectedResult = NucleobaseCountUtils.countSequential(chromosome, nucleobase);
	}

	protected int getChromosomeLength() {
		return this.chromosome.length;
	}

	protected abstract int count(ExecutorService executor, byte[] chromosome, Nucleobase nucleobase)
			throws InterruptedException, ExecutionException;

	@Override
	protected void accept(BookkeepingExecutorService bookkeepingExecutorService)
			throws InterruptedException, ExecutionException {

		int actualResult = count(bookkeepingExecutorService, chromosome, nucleobase);

		assertEquals(0, bookkeepingExecutorService.getNotYetJoinedTaskCount());
		assertEquals(expectedResult, actualResult);

		List<Integer> acceptableSubmitCounts = this.getAcceptableSubmitCounts();
		List<Matcher<? super Integer>> expectedSubmitCountMatchers = new LinkedList<>();
		for (int i : acceptableSubmitCounts) {
			expectedSubmitCountMatchers.add(is(i));
		}
		assertThat(bookkeepingExecutorService.getSubmitCount(), anyOf(expectedSubmitCountMatchers));

		assertEquals(getInvokeAllSizes().size(), bookkeepingExecutorService.getInvokeAllCount());

	}
}
