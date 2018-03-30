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
import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collector;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import edu.wustl.cse231s.junit.JUnitUtils;
import edu.wustl.cse231s.v5.bookkeep.BookkeepingUtils;
import edu.wustl.cse231s.v5.impl.BookkeepingV5Impl;
import mapreduce.core.CollectorSolution;
import mapreduce.core.MapperSolution;
import mapreduce.framework.core.FrameworkTestUtils;
import mapreduce.framework.core.Mapper;
import mapreduce.framework.core.NoOpCollector;
import mapreduce.framework.lab.rubric.MapReduceRubric;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link MatrixMapReduceFramework#mapAndAccumulateAll(Object[])}
 */
@MapReduceRubric(MapReduceRubric.Category.MATRIX_MAP_AND_ACCUMULATE_ALL)
@RunWith(Parameterized.class)
public class MapAccumulateAllParallelismTest extends AbstractNoOpTest {
	private final int mapTaskCount;

	public MapAccumulateAllParallelismTest(int mapTaskCount) {
		this.mapTaskCount = mapTaskCount;
	}

	@Override
	protected <E, K, V, A, R> void execute(Mapper<E, K, V> noOpMapper, Collector<V, A, R> noOpCollector, E[] data) {
		int reduceTaskCount = mapTaskCount;
		BookkeepingV5Impl bookkeep = BookkeepingUtils.bookkeep(() -> {
			AccessMatrixFrameworkUtils.mapAndAccumulateAll(data, noOpMapper, noOpCollector, mapTaskCount,
					reduceTaskCount);
		});

		assertEquals(mapTaskCount, bookkeep.getTaskCount());
		assertEquals(1, bookkeep.getNonAccumulatorFinishInvocationCount());
	}

	@Parameters(name = "mapTaskCount={0}")
	public static Collection<Object[]> getConstructorArguments() {
		int numProcecessors = Runtime.getRuntime().availableProcessors();
		Collection<Object[]> result = new LinkedList<>();
		result.add(new Object[] { numProcecessors });
		result.add(new Object[] { numProcecessors * 2 });
		result.add(new Object[] { 71 });
		return result;
	}
}
