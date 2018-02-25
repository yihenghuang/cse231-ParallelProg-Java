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
package mapreduce.collector.intsum.studio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;

import org.apache.commons.lang3.mutable.MutableInt;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import edu.wustl.cse231s.junit.JUnitUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link IntSumCollector#combiner()}
 */
public class IntSumCollectorCombinerTest {
	@Rule
	public TestRule timeout = JUnitUtils.createTimeoutRule();

	@Test
	public void test() {
		IntSumCollector collector = new IntSumCollector();

		Supplier<MutableInt> supplier = collector.supplier();
		assertNotNull(supplier);

		BiConsumer<MutableInt, Integer> accumulator = collector.accumulator();
		assertNotNull(accumulator);

		BinaryOperator<MutableInt> combiner = collector.combiner();
		assertNotNull(combiner);

		MutableInt a = supplier.get();
		assertNotNull(a);
		assertEquals(0, a.intValue());

		accumulator.accept(a, 4);
		assertEquals(4, a.intValue());

		accumulator.accept(a, 66);
		assertEquals(70, a.intValue());

		accumulator.accept(a, 99);
		assertEquals(169, a.intValue());

		MutableInt b = supplier.get();
		assertNotNull(b);
		assertEquals(0, b.intValue());

		accumulator.accept(b, 6);
		assertEquals(6, b.intValue());

		accumulator.accept(b, 23);
		assertEquals(29, b.intValue());

		MutableInt ab = combiner.apply(a, b);
		assertTrue(ab == a || ab == b);
		assertEquals(198, ab.intValue());
	}
}