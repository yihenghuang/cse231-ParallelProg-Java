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
package mapreduce.framework.lab.matrix;

import static edu.wustl.cse231s.v5.V5.launchApp;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import mapreduce.core.CollectorSolution;
import mapreduce.core.InstructorMapReduceTestUtils;
import mapreduce.core.MapperSolution;
import mapreduce.core.TestApplication;
import mapreduce.framework.core.FrameworkTestUtils;
import mapreduce.framework.core.matrix.MatrixMapAccumulateAllSolution;
import mapreduce.framework.lab.matrix.MatrixMapReduceFramework;
import mapreduce.framework.lab.rubric.MapReduceRubric;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link MatrixMapReduceFramework#mapAndAccumulateAll(Object[])}
 */
@RunWith(Parameterized.class)
@MapReduceRubric(MapReduceRubric.Category.MATRIX_MAP_AND_ACCUMULATE_ALL)
public class MapAccumulateAllMatrixFrameworkStressTest<E> {
	private final MapperSolution mapperSolution;
	private final CollectorSolution collectorSolution;
	private final E[] input;
	private final TestApplication application;

	public MapAccumulateAllMatrixFrameworkStressTest(MapperSolution mapperSolution, CollectorSolution collectorSolution,
			Object resource) throws IOException {
		this.mapperSolution = mapperSolution;
		this.collectorSolution = collectorSolution;
		this.input = FrameworkTestUtils.getInput(resource);
		this.application = FrameworkTestUtils.getApplication(resource);
	}

	@Test
	public void testMapAccumulateAll() throws InterruptedException, ExecutionException {
		launchApp(() -> {
			InstructorMapReduceTestUtils.checkMatrixMapAndAccumulateAll(input, application, mapperSolution,
					collectorSolution, MatrixMapAccumulateAllSolution.STUDENT_ASSIGNMENT);
		});
	}

	@Parameters(name = "mapper={0}; collector={1}; {2}")
	public static Collection<Object[]> getConstructorArguments() {
		return FrameworkTestUtils.getConstructorArguments(MapperSolution.getNonWarmUpValues(),
				CollectorSolution.getNonWarmUpValues());
	}
}
