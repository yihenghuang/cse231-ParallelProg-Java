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
package mapreduce.framework.lab.simple;

import static edu.wustl.cse231s.v5.V5.launchApp;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import edu.wustl.cse231s.junit.JUnitUtils;
import mapreduce.core.CollectorSolution;
import mapreduce.core.InstructorMapReduceTestUtils;
import mapreduce.core.TestApplication;
import mapreduce.framework.core.FrameworkTestUtils;
import mapreduce.framework.lab.rubric.MapReduceRubric;
import mapreduce.framework.lab.simple.SimpleMapReduceFramework;
import mapreduce.framework.simple.core.SimpleAccumulateAllSolution;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link SimpleMapReduceFramework#accumulateAll(List[])}
 */
@RunWith(Parameterized.class)
@MapReduceRubric(MapReduceRubric.Category.SIMPLE_ACCUMULATE_ALL)
public class AccumulateAllSimpleFrameworkStressTest<E> {
	private final CollectorSolution collectorSolution;
	private final E[] input;
	private final TestApplication application;

	public AccumulateAllSimpleFrameworkStressTest(CollectorSolution collectorSolution, Object resource)
			throws IOException {
		this.collectorSolution = collectorSolution;
		this.input = FrameworkTestUtils.getInput(resource);
		this.application = FrameworkTestUtils.getApplication(resource);
	}

	@Rule
	public TestRule timeout = JUnitUtils.createTimeoutRule(16);

	@Test
	public void testMapAll() throws InterruptedException, ExecutionException {
		launchApp(() -> {
			InstructorMapReduceTestUtils.checkSimpleAccumulateAll(input, application, collectorSolution,
					SimpleAccumulateAllSolution.STUDENT_ASSIGNMENT);
		});
	}

	@Parameters(name = "collector={0}; {1}")
	public static Collection<Object[]> getConstructorArguments() {
		return FrameworkTestUtils.getConstructorArguments(CollectorSolution.getNonWarmUpValues());
	}
}
