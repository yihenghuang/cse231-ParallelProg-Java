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

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import org.apache.commons.lang3.mutable.MutableObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import edu.wustl.cse231s.junit.JUnitUtils;
import edu.wustl.cse231s.util.KeyValuePair;
import edu.wustl.cse231s.v5.bookkeep.BookkeepingUtils;
import edu.wustl.cse231s.v5.impl.BookkeepingV5Impl;
import mapreduce.framework.lab.rubric.MapReduceRubric;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link SimpleMapReduceFramework#finishAll(Map)}
 */
@MapReduceRubric(MapReduceRubric.Category.SIMPLE_FINISH_ALL)
public class FinishAllParallelismTest {
	@Rule
	public TestRule timeout = JUnitUtils.createTimeoutRule();

	private <K, V, A, R> void execute(Collector<V, A, R> collector, List<KeyValuePair<K, A>> keyValuePairs) {
		Map<K, A> accumulateAllResult = new HashMap<>();
		for (KeyValuePair<K, A> kv : keyValuePairs) {
			accumulateAllResult.put(kv.getKey(), kv.getValue());
		}

		MutableObject<Map<K, R>> actual = new MutableObject<>();
		BookkeepingV5Impl bookkeep = BookkeepingUtils.bookkeep(() -> {
			actual.setValue(AccessSimpleFrameworkUtils.finishAllOnly(accumulateAllResult, collector));
		});

		assertEquals(1, bookkeep.getNonAccumulatorFinishInvocationCount());
		assertEquals(accumulateAllResult.size(), bookkeep.getTaskCount());
	}

	@Test
	public void test() {
		Collector<Integer, List<Integer>, Integer> collector = new Collector<Integer, List<Integer>, Integer>() {
			@Override
			public Supplier<List<Integer>> supplier() {
				throw new RuntimeException();
			}

			@Override
			public BiConsumer<List<Integer>, Integer> accumulator() {
				throw new RuntimeException();
			}

			@Override
			public BinaryOperator<List<Integer>> combiner() {
				throw new RuntimeException();
			}

			@Override
			public Function<List<Integer>, Integer> finisher() {
				return (container) -> 0;
			}

			@Override
			public Set<Characteristics> characteristics() {
				return EnumSet.of(Characteristics.UNORDERED);
			}
		};
		execute(collector, Arrays.asList(new KeyValuePair<>("a", Collections.emptyList()),
				new KeyValuePair<>("b", Collections.emptyList()), new KeyValuePair<>("c", Collections.emptyList())));
	}
}
