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
package mapreduce.apps.intsum.studio;

import java.util.List;
import java.util.function.Function;

import mapreduce.collector.studio.ClassicReducer;

/**
 * A reducer that sums all of the Integers passed to it. This is used in the
 * word count example to sum all of the ones passed to it.
 * 
 * Note that this class implements {@code ClassicReducer}. This means that it is
 * a {@code Collector}, and the other {@code Collector} methods are defined in
 * the {@code ClassicReducer} interface.
 * 
 * @author Yiheng Huang
 * @author Finn Voichick
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class IntegerSumClassicReducer implements ClassicReducer<Integer, Integer> {

	/**
	 * Returns a {@code Function} used to convert from a {@code List} of
	 * {@code Integer}s to a single {@code Integer}. This function should, given a
	 * list of integers, return their sum.
	 * 
	 * Note the interesting structure of this method. It is returning an anonymous
	 * class that implements the {@code Function} interface. The {@code Function}
	 * interface has a {@link Function#apply(Object)} method, which our anonymous
	 * inner class must implement. We're giving you this structure, and you just
	 * have to implement the {@code apply} method.
	 */
	@Override
	public Function<List<Integer>, Integer> finisher() {

		return new Function<List<Integer>, Integer>() {

			@Override
			public Integer apply(List<Integer> list) {
				Integer sum = 0;
				for (Integer i : list) {
					sum += i;
				}
				return sum;
			}

		};

	}

}
