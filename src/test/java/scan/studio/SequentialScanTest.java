/*******************************************************************************
 * Copyright (C) 2016-2018 Dennis Cosgrove
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
package scan.studio;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.function.IntUnaryOperator;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import edu.wustl.cse231s.junit.JUnitUtils;
import edu.wustl.cse231s.v5.impl.BookkeepingV5Impl;
import scan.util.ValueGenerator;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link SequentialScan#sumScan(int[])}
 */
@RunWith(Parameterized.class)
public class SequentialScanTest extends AbstractScanTest {
	public SequentialScanTest(IntUnaryOperator valueGenerator, int length) {
		super(valueGenerator, length, new SequentialScan());
	}

	@Override
	protected void checkParallelism(BookkeepingV5Impl bookkeep, int length) {
		assertEquals(0, bookkeep.getTaskCount());
		assertEquals(0, bookkeep.getNonAccumulatorFinishInvocationCount());
	}

	@Parameters(name = "{0} length={1}")
	public static Collection<Object[]> getConstructorArguments() {
		return JUnitUtils.toParameterizedArguments2(ValueGenerator.values(),
				new Integer[] { 0, 1, 2, 3, 4, 8, 31, 32, 33, 71, 231 });
	}
}
