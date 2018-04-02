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

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collector;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import edu.wustl.cse231s.junit.JUnitUtils;
import edu.wustl.cse231s.util.KeyValuePair;
import mapreduce.collector.studio.ClassicReducer;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link SimpleMapReduceFramework#accumulateAll(List[])}
 */
public abstract class AbstractAccumulateAllTest {
	@Rule
	public TestRule timeout = JUnitUtils.createTimeoutRule();

	protected abstract void execute(Collector<Boolean, List<Boolean>, Void> collector,
			List<KeyValuePair<String, Boolean>>[] mapAllResult);

	@Test
	public void test() {
		@SuppressWarnings("unchecked")
		List<KeyValuePair<String, Boolean>>[] mapAllResult = new List[] { Arrays.asList(new KeyValuePair<>("a", true),
				new KeyValuePair<>("b", true), new KeyValuePair<>("a", false)) };

		class NonsenseBooleanReducer implements ClassicReducer<Boolean, Void> {
			@Override
			public Function<List<Boolean>, Void> finisher() {
				return null;
			}
		}

		execute(new NonsenseBooleanReducer(), mapAllResult);
	}

}
