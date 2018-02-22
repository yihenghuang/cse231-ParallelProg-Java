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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.Test;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link ClassicReducer#combiner()}
 */
public class ClassicReducerCombinerTest {
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
		
		BinaryOperator<List<Integer>> combiner = reducer.combiner();
		assertNotNull(combiner);

		List<Integer> a = supplier.get();
		assertNotNull(a);
		assertTrue(a.isEmpty());
		
		accumulator.accept(a, 4);
		assertEquals(1, a.size());
		assertEquals(a, Arrays.asList(4));

		accumulator.accept(a, 66);
		assertEquals(2, a.size());
		assertEquals(a, Arrays.asList(4,66));

		accumulator.accept(a, 99);
		assertEquals(3, a.size());
		assertEquals(a, Arrays.asList(4,66,99));

	
		List<Integer> b = supplier.get();
		assertNotNull(b);
		assertTrue(b.isEmpty());
		
		accumulator.accept(b, 6);
		assertEquals(1, b.size());
		assertEquals(b, Arrays.asList(6));

		accumulator.accept(b, 23);
		assertEquals(2, b.size());
		assertEquals(b, Arrays.asList(6,23));

		List<Integer> ab = combiner.apply(a, b);
		assertTrue(ab==a || ab==b);

		assertEquals(5, ab.size());
		assertTrue(ab.containsAll(Arrays.asList(4,66,99,6,23)));
	}
}
