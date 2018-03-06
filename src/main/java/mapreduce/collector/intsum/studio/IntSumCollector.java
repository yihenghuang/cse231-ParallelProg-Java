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

import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import org.apache.commons.lang3.mutable.MutableInt;

/**
 * @author Yiheng Huang
 * @author Finn Voichick
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class IntSumCollector implements Collector<Integer, MutableInt, Integer> {
	@Override
	public Supplier<MutableInt> supplier() {
		return new Supplier<MutableInt>() {

			@Override
			public MutableInt get() {
				return new MutableInt();

			}
		};
	}

	@Override
	public BiConsumer<MutableInt, Integer> accumulator() {
		return new BiConsumer<MutableInt, Integer>() {
			@Override
			public void accept(MutableInt mut, Integer i) {
				mut.add(i);
			}
		};

	}

	@Override
	public BinaryOperator<MutableInt> combiner() {
		return new BinaryOperator<MutableInt>() {
			@Override
			public MutableInt apply(MutableInt a, MutableInt b) {
				a.getAndAdd(b);
				return a;
			}
		};
	}

	@Override
	public Function<MutableInt, Integer> finisher() {
		return new Function<MutableInt, Integer>() {

			@Override
			public Integer apply(MutableInt t) {
				return t.getValue();
			}
		};
	}

	@Override
	public Set<Characteristics> characteristics() {
		return EnumSet.of(Characteristics.UNORDERED);
	}
}
