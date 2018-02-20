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

import static edu.wustl.cse231s.v5.V5.forall;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collector;

import edu.wustl.cse231s.NotYetImplementedException;
import edu.wustl.cse231s.util.KeyValuePair;
import mapreduce.framework.core.MapReduceFramework;
import mapreduce.framework.core.Mapper;
import net.jcip.annotations.Immutable;

/**
 * A MapReduce framework that separates mapping, accumulating, and finishing
 * into three separate stages. The {@link #mapAll(Object[])} method calls the
 * {@code Mapper} on everything in the given array in parallel. Then, the
 * {@link #accumulateAll(List[])} method sequentially groups the output of the
 * {@code mapAll} method. Finally, the {@link #finishAll(Map)} method finishes
 * off the reduction, going from a map with values of type A to a map with
 * values of type R.
 * 
 * @param <E>
 *            the type of element that is originally being input
 * @param <K>
 *            the key in the key-value pairs written by the mapper
 * @param <V>
 *            the value in the key-value pairs written by the mapper
 * @param <A>
 *            the mutable result container of the collector
 * @param <R>
 *            the final result of the collector
 * 
 * @author __STUDENT_NAME__
 * @author Finn Voichick
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@Immutable
public final class SimpleMapReduceFramework<E, K, V, A, R> implements MapReduceFramework<E, K, V, A, R> {
	/** The mapper used to convert the original data into key-value pairs */
	private final Mapper<E, K, V> mapper;
	/** The collector used to reduce many values into a single reduced value */
	private final Collector<V, A, R> collector;

	/**
	 * Constructs a {@code SimpleMapReduceFramework} using the given mapper and
	 * collector.
	 * 
	 * @param mapper
	 *            the mapper used to convert the original data into key-value
	 *            pairs
	 * @param collector
	 *            the collector used to reduce many values into a single reduced
	 *            value
	 */
	public SimpleMapReduceFramework(Mapper<E, K, V> mapper, Collector<V, A, R> collector) {
		this.mapper = mapper;
		this.collector = collector;
	}

	@Override
	public Mapper<E, K, V> getMapper() {
		return this.mapper;
	}

	@Override
	public Collector<V, A, R> getCollector() {
		return this.collector;
	}

	/**
	 * Should call the {@link Mapper#map(Object, java.util.function.BiConsumer)}
	 * method on everything in the input. The input array contains a items of
	 * type E. In the end, this method should return an array of lists. Each of
	 * these lists contains key-value pairs that are the result of the mapper's
	 * map method. Each item can be mapped in parallel.
	 * 
	 * @param input
	 *            the original input array
	 * @return an array of lists corresponding to the mapped output of the input
	 *         array
	 * @throws InterruptedException,
	 *             ExecutionException
	 */
	List<KeyValuePair<K, V>>[] mapAll(E[] input) throws InterruptedException, ExecutionException {
		throw new NotYetImplementedException();
	}

	/**
	 * Should sequentially accumulate the results of the
	 * {@link #mapAll(Object[])} method. This method should build up a result
	 * map by sequentially go through the array of lists, looking at each
	 * key-value pair. For each one, it should accumulate the value with the
	 * other values currently associated with the key in the map, using the
	 * {@link Collector#accumulator()}. If there is nothing in the result map
	 * associated with a key, a mutable result container should be created using
	 * the {@link Collector#supplier()}.
	 * 
	 * @param mapAllResults
	 *            the returns from the mapAll method
	 * @return a map where each key is a key in one of the key-value pairs, and
	 *         each value is the accumulation of all of the values associated
	 *         with that key in the input data
	 * 
	 * @see Map#compute(Object, java.util.function.BiFunction)
	 * @see Map#computeIfAbsent(Object, java.util.function.Function)
	 */
	Map<K, A> accumulateAll(List<KeyValuePair<K, V>>[] mapAllResults) {
		throw new NotYetImplementedException();
	}

	/**
	 * Should finish up the reduction, using the {@link Collector#finisher()}.
	 * When you parallelize this method, you will want to use a
	 * {@link ConcurrentHashMap} because it is thread-safe.
	 * 
	 * @param accumulateAllResult
	 *            the results from the accumulateAll method
	 * @return a map that corresponds to the input one, with identical values
	 *         except that the {@link Collector#finisher()} has been called on
	 *         all of them.
	 * @throws InterruptedException,
	 *             ExecutionException
	 */
	Map<K, R> finishAll(Map<K, A> accumulateAllResult) throws InterruptedException, ExecutionException {
		throw new NotYetImplementedException();
	}

	@Override
	public Map<K, R> mapReduceAll(E[] input) throws InterruptedException, ExecutionException {
		List<KeyValuePair<K, V>>[] mapAllResult = this.mapAll(input);
		Map<K, A> accumulateAllResult = this.accumulateAll(mapAllResult);
		return this.finishAll(accumulateAllResult);
	}
}
