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

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import edu.wustl.cse231s.bioinformatics.Nucleobase;
import edu.wustl.cse231s.junit.JUnitUtils;
import tnx.lab.executor.XNucleobaseCounting;
import tnx.lab.rubric.TnXRubric;

/**
 * @author Finn Voichick
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link XNucleobaseCounting#countLowerUpperSplit(ExecutorService, byte[], Nucleobase)}.
 */
@RunWith(Parameterized.class)
@TnXRubric(TnXRubric.Category.EXECUTOR_COUNT_2WAY)
public class NucleobaseCountLowerUpperSplitTest extends AbstractNucleobaseTest {
	public NucleobaseCountLowerUpperSplitTest(Nucleobase nucleobase) throws IOException {
		super(nucleobase);
	}

	@Override
	protected List<Integer> getAcceptableSubmitCounts() {
		return Arrays.asList(1, 2);
	}

	@Override
	protected List<Integer> getInvokeAllSizes() {
		return Collections.emptyList();
	}

	@Override
	protected int count(ExecutorService executor, byte[] chromosome, Nucleobase nucleobase)
			throws InterruptedException, ExecutionException {
		return XNucleobaseCounting.countLowerUpperSplit(executor, chromosome, nucleobase);
	}

	@Parameters(name = "{0}")
	public static Collection<Object[]> getConstructorArguments() {
		return JUnitUtils.toParameterizedArguments(Nucleobase.values());
	}
}
