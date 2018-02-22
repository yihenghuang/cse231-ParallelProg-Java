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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.Test;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link ClassicReducer#supplier()}
 */
public class ClassicReducerSupplierTest {
	@Test
	public void test() {
		ClassicReducer<Void, Void> reducer = new ClassicReducer<Void, Void>() {
			@Override
			public Function<List<Void>, Void> finisher() {
				throw new Error();
			}
		};

		Supplier<List<Void>> supplier = reducer.supplier();
		assertNotNull(supplier);

		List<Void> mutableContainer = supplier.get();
		assertNotNull(mutableContainer);
		assertTrue(mutableContainer.isEmpty());

		List<Void> other = supplier.get();
		assertNotNull(other);
		assertTrue(other.isEmpty());

		assertTrue("supplier().get() should return a new mutableContainer", mutableContainer != other);
	}
}
