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

import static org.junit.Assert.assertEquals;

import java.util.function.BiConsumer;
import java.util.stream.Collector;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import edu.wustl.cse231s.junit.JUnitUtils;
import edu.wustl.cse231s.v5.bookkeep.BookkeepingUtils;
import edu.wustl.cse231s.v5.impl.BookkeepingV5Impl;
import mapreduce.framework.core.Mapper;
import mapreduce.framework.core.NoOpCollector;
import mapreduce.framework.core.NoOpMapper;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public abstract class AbstractNoOpTest {
	@Rule
	public TestRule timeout = JUnitUtils.createTimeoutRule();

	protected abstract <E, K, V, A, R> void execute(Mapper<E, K, V> noOpMapper, Collector<V, A, R> noOpCollector,
			E[] data);

	@Test
	public void test() {
		class Input {
		}
		class MapOutputKey {
		}
		class MapOutputValue {
		}

		int length = 231;
		Input[] data = new Input[length];
		Mapper<Input, MapOutputKey, MapOutputValue> noOpMapper = new NoOpMapper<>();
		Collector<MapOutputValue, ?, ?> noOpCollector = new NoOpCollector<>();

		execute(noOpMapper, noOpCollector, data);
	}

}
