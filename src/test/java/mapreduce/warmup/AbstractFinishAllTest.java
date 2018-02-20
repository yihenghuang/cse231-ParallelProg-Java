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
package mapreduce.warmup;

import static edu.wustl.cse231s.v5.V5.launchApp;

import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import mapreduce.core.CollectorSolution;
import mapreduce.core.InstructorMapReduceTestUtils;
import mapreduce.core.TestApplication;
import mapreduce.framework.simple.core.SimpleFinishAllSolution;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@RunWith(Parameterized.class)
public abstract class AbstractFinishAllTest<E> {
	private final E[] input;
	private final TestApplication application;

	public AbstractFinishAllTest(E[] input, TestApplication application) {
		this.input = input;
		this.application = application;
	}

	@Test
	public void testFinishAll() throws InterruptedException, ExecutionException {
		launchApp(() -> {
			InstructorMapReduceTestUtils.checkSimpleFinishAll(input, application,
					CollectorSolution.NOT_USED_STUDENT_WARM_UP, SimpleFinishAllSolution.STUDENT_WARMUP);
		});
	}
}
