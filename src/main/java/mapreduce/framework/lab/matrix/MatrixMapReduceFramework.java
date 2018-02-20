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
package mapreduce.framework.lab.matrix;

import static edu.wustl.cse231s.v5.V5.forall;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;

import edu.wustl.cse231s.NotYetImplementedException;
import edu.wustl.cse231s.util.MultiWrapMap;
import mapreduce.framework.core.MapReduceFramework;
import mapreduce.framework.core.Mapper;
import mapreduce.framework.lab.simple.SimpleMapReduceFramework;
import net.jcip.annotations.Immutable;
import slice.core.Slice;
import slice.studio.Slices;

/**
 * A MapReduce framework that uses a matrix to organize the map tasks and reduce
 * tasks. Unlike the {@link SimpleMapReduceFramework}, this class doesn't
 * require the accumulation stage to run sequentially. It gets around the issue
 * by creating a whole matrix of {@code Map}s, where each row is a map task, and
 * each column is a reduce task.
 * 
 * The {@link #mapAndAccumulateAll(Object[])} method slices up the input and
 * maps each slice to one row, accumulating as it goes. It places items into the
 * correct column based on the {@link #getReduceIndex(Object)} method. Then, the
 * {@link #combineAndFinishAll(Map[][])} method combines the mutable result
 * containers from the various map tasks, and finishes them into the result
 * type.
 * 
 * This is the most difficult part of the assignment, but it's also worth the
 * most. Try doing it sequentially before attempting to parallelize it.
 * 
 * @author Yiheng Huang
 * @author Finn Voichick
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@Immutable
public class MatrixMapReduceFramework<E, K, V, A, R> implements MapReduceFramework<E, K, V, A, R> {
	private final Mapper<E, K, V> mapper;
	private final Collector<V, A, R> collector;
	private final int mapTaskCount;
	private final int reduceTaskCount;

	public MatrixMapReduceFramework(Mapper<E, K, V> mapper, Collector<V, A, R> collector, int mapTaskCount,
			int reduceTaskCount) {
		this.mapper = mapper;
		this.collector = collector;
		this.mapTaskCount = mapTaskCount;
		this.reduceTaskCount = reduceTaskCount;
	}

	public MatrixMapReduceFramework(Mapper<E, K, V> mapper, Collector<V, A, R> collector) {
		this(mapper, collector, Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors());
	}

	@Override
	public Mapper<E, K, V> getMapper() {
		return this.mapper;
	}

	@Override
	public Collector<V, A, R> getCollector() {
		return this.collector;
	}

	private int getReduceIndex(K key) {
		return Math.floorMod(key.hashCode(), this.reduceTaskCount);
	}

	/**
	 * Should create the matrix used to store the mutable result containers. Each
	 * row in the matrix (using the first index of the 2D array) is a map task, and
	 * it should be a slice of the input. Each column in the matrix (using the
	 * second index) is a reduce task, and it should be based on the hash of the
	 * key, using the {@link #getReduceIndex(Object)} method.
	 * 
	 * At each index in this matrix is a Map. Each key is written by the map method,
	 * and each value is an accumulation of values written by the map method. You
	 * will need to use the {@link Collector#supplier()} to provide these mutable
	 * result containers and the {@link Collector#accumulator()} to accumulate them.
	 * 
	 * @param input
	 *            the original input of E items
	 * @return a 2D array holding all of mapped keys and their accumulations
	 * @see Slice
	 * @see Slices
	 * @throws InterruptedException,
	 *             ExecutionException
	 */
	Map<K, A>[][] mapAndAccumulateAll(E[] input) throws InterruptedException, ExecutionException {
		throw new NotYetImplementedException();
	}

	/**
	 * Should use the matrix provided by the {@link #mapAndAccumulateAll(Object[])}
	 * method to reduce everything into a map from K to R. Each column should be
	 * consolidated in parallel. For this method, you will need to use the
	 * {@link Collector#combiner()} method to combine the mutable result containers
	 * in each row. You will also need to call the {@link Collector#finisher()} when
	 * you're done combining, to finish off the reduction. At the end, you will want
	 * to use the {@link MultiWrapMap} to combine the results of each column into a
	 * single map.
	 * 
	 * @param input
	 *            the matrix produced by the mapAndAccumulateAll method
	 * @return the final result, a map from K to R
	 * @see MultiWrapMap
	 * @throws InterruptedException,
	 *             ExecutionException
	 */
	Map<K, R> combineAndFinishAll(Map<K, A>[][] input) throws InterruptedException, ExecutionException {
		throw new NotYetImplementedException();
	}

	@Override
	public Map<K, R> mapReduceAll(E[] input) throws InterruptedException, ExecutionException {
		Map<K, A>[][] mapAndGroupAllResult = this.mapAndAccumulateAll(input);
		return this.combineAndFinishAll(mapAndGroupAllResult);
	}
}
