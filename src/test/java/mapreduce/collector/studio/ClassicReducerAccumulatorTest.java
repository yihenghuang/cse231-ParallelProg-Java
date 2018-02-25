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
package mapreduce.collector.studio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import edu.wustl.cse231s.junit.JUnitUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link ClassicReducer#accumulator()}
 */
public class ClassicReducerAccumulatorTest {
	@Rule
	public TestRule timeout = JUnitUtils.createTimeoutRule();

	@Test
	public void test() {
		ClassicReducer<Integer, Integer> reducer = new ClassicReducer<Integer, Integer>() {
			@Override
			public Function<List<Integer>, Integer> finisher() {
				throw new Error();
			}
		};

		Supplier<List<Integer>> supplier = reducer.supplier();
		assertNotNull(supplier);
		
		BiConsumer<List<Integer>, Integer> accumulator = reducer.accumulator();
		assertNotNull(accumulator);

		List<Integer> mutableContainer = supplier.get();
		assertNotNull(mutableContainer);
		assertTrue(mutableContainer.isEmpty());
		
		accumulator.accept(mutableContainer, 4);
		assertEquals(1, mutableContainer.size());
		assertEquals(mutableContainer, Arrays.asList(4));

		accumulator.accept(mutableContainer, 66);
		assertEquals(2, mutableContainer.size());
		assertEquals(mutableContainer, Arrays.asList(4,66));

		accumulator.accept(mutableContainer, 99);
		assertEquals(3, mutableContainer.size());
		assertEquals(mutableContainer, Arrays.asList(4,66,99));
	}
}
