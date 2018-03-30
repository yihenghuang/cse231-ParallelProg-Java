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

import static org.hamcrest.CoreMatchers.either;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collector;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import edu.wustl.cse231s.v5.bookkeep.BookkeepingUtils;
import edu.wustl.cse231s.v5.impl.BookkeepingV5Impl;
import mapreduce.framework.core.Mapper;
import mapreduce.framework.lab.rubric.MapReduceRubric;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link MatrixMapReduceFramework#mapAndAccumulateAll(Object[])}
 */
@MapReduceRubric(MapReduceRubric.Category.MATRIX_MAP_AND_ACCUMULATE_ALL)
@RunWith(Parameterized.class)
public class CombineAndFinishAllParallelismTest extends AbstractNoOpTest {
	private final int reduceTaskCount;

	public CombineAndFinishAllParallelismTest(int reduceTaskCount) {
		this.reduceTaskCount = reduceTaskCount;
	}

	@Override
	protected <E, K, V, A, R> void execute(Mapper<E, K, V> noOpMapper, Collector<V, A, R> noOpCollector, E[] data) {
		int mapTaskCount = reduceTaskCount;

		@SuppressWarnings("unchecked")
		Map<K, A>[][] mapAndAccumulateAllResults = new Map[mapTaskCount][reduceTaskCount];
		for (int row = 0; row < mapTaskCount; row++) {
			for (int col = 0; col < reduceTaskCount; col++) {
				mapAndAccumulateAllResults[row][col] = Collections.emptyMap();
			}
		}
		BookkeepingV5Impl bookkeep = BookkeepingUtils.bookkeep(() -> {
			AccessMatrixFrameworkUtils.combineAndFinishAll(mapAndAccumulateAllResults, noOpCollector, mapTaskCount,
					reduceTaskCount);
		});

		int actualTaskCount = bookkeep.getTaskCount();
		int actualFinishCount = bookkeep.getNonAccumulatorFinishInvocationCount();
		final boolean IS_STRICTLY_ENFORCING_SINGLE_FOR_ALL = false;
		if (IS_STRICTLY_ENFORCING_SINGLE_FOR_ALL) {
			assertEquals(reduceTaskCount, actualTaskCount);
			assertEquals(1, actualFinishCount);
		} else {
			Assert.assertThat(actualTaskCount, either(is(reduceTaskCount)).or(is(reduceTaskCount * 2)));
			Assert.assertThat(actualFinishCount, either(is(1)).or(is(2)));
		}
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
