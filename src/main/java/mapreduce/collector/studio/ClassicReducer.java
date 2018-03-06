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

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Collector;

import mapreduce.apps.intsum.studio.IntegerSumClassicReducer;

/**
 * An interface used for {@code Collector}s that are similar to Hadoop's
 * reducer. In Hadoop, the collecting of the values into one place is done for
 * you, and you are given an {@link Iterable} of the values. Java's
 * {@code Collector} works a bit differently because it can have any type as its
 * mutable result container. In other words, Java's {@code Collector} is more
 * general, because you don't have to store the values in a list. In this
 * interface, we're making it easier to use Java's {@code Collector} like a
 * Hadoop reducer.
 * 
 * Java's {@code Collector} interface has four methods:
 * {@link Collector#supplier()}, {@link Collector#accumulator()},
 * {@link Collector#combiner()}, and {@link Collector#finisher()}. This
 * interface defines three of those methods.
 * 
 * This interface is built around the list as the mutable result container. The
 * {@code supplier}'s {@code get} method creates a new list, the
 * {@code accumulator}'s {@code accept} method adds an item to the list, and the
 * {@code combiner}'s {@code apply} method combines two lists. The last method,
 * {@code finisher}, must be defined by any class implementing this interface.
 * It finishes the reduction by converting everything in the list into some
 * result type. Take a look at {@link IntegerSumClassicReducer} to see an
 * example of a {@code finisher} method.
 * 
 * @param <V>
 *            the type of value being reduced
 * @param <R>
 *            the result of the reduction
 * 
 * @author Yiheng Huang
 * @author Finn Voichick
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public interface ClassicReducer<V, R> extends Collector<V, List<V>, R> {

	/**
	 * A function that creates and returns a new mutable result container. In this
	 * case, the mutable result container is a {@code List}.
	 */
	@Override
	default Supplier<List<V>> supplier() {

		return new Supplier<List<V>>() {

			@Override
			public List<V> get() {
				return new LinkedList<V>();

			}

		};

	}

	/**
	 * A function that folds a value into a mutable result container. In this case,
	 * because the mutable result container is a list, the function just needs to
	 * add the item to the list.
	 */
	@Override
	default BiConsumer<List<V>, V> accumulator() {

		return new BiConsumer<List<V>, V>() {

			@Override
			public void accept(List<V> v, V item) {
				v.add(item);
			}

		};

	}

	/**
	 * A function that accepts two partial results and merges them. In other words,
	 * the function takes two {@code List}s, and combines them into a single
	 * {@code List}. This method would be most efficient to fold one of the lists
	 * into the other and return the full list.
	 */
	@Override
	default BinaryOperator<List<V>> combiner() {

		return new BinaryOperator<List<V>>() {

			@Override
			public List<V> apply(List<V> a, List<V> b) {
				List<V> val = new LinkedList<V>(b);
				for (V v : a) {
					val.add(v);
				}
				return val;
			}

		};

	}

	@Override
	default Set<Characteristics> characteristics() {
		return EnumSet.of(Characteristics.UNORDERED);
	}
}
