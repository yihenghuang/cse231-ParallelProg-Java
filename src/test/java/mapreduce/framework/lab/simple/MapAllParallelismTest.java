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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.stream.Collector;

import edu.wustl.cse231s.util.KeyValuePair;
import edu.wustl.cse231s.v5.bookkeep.BookkeepingUtils;
import edu.wustl.cse231s.v5.impl.BookkeepingV5Impl;
import mapreduce.framework.core.Mapper;
import mapreduce.framework.lab.AbstractNoOpTest;
import mapreduce.framework.lab.rubric.MapReduceRubric;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link SimpleMapReduceFramework#mapAll(Object[])}
 */
@MapReduceRubric(MapReduceRubric.Category.SIMPLE_MAP_ALL)
public class MapAllParallelismTest extends AbstractNoOpTest {
	@Override
	protected <E, K, V, A, R> void execute(Mapper<E, K, V> noOpMapper, Collector<V, A, R> noOpCollector, E[] data) {
		BookkeepingV5Impl bookkeep = BookkeepingUtils.bookkeep(() -> {
			List<KeyValuePair<K, V>>[] mapAllResult = AccessSimpleFrameworkUtils.mapAllOnly(data, noOpMapper);
			assertNotNull(mapAllResult);
			assertEquals(mapAllResult.length, data.length);
		});
		assertEquals(data.length, bookkeep.getTaskCount());
		assertEquals(1, bookkeep.getNonAccumulatorFinishInvocationCount());
	}
}
