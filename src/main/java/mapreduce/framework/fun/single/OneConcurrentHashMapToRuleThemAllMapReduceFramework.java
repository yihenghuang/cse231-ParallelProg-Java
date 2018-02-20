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
package mapreduce.framework.fun.single;

import static edu.wustl.cse231s.v5.V5.forall;
import static edu.wustl.cse231s.v5.V5.chunked;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;

import edu.wustl.cse231s.NotYetImplementedException;
import mapreduce.framework.core.MapReduceFramework;
import mapreduce.framework.core.Mapper;

/**
 * @author Yiheng Huang
 * @author Finn Voichick
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class OneConcurrentHashMapToRuleThemAllMapReduceFramework<E, K, V, A, R>
		implements MapReduceFramework<E, K, V, A, R> {
	private final Mapper<E, K, V> mapper;
	private final Collector<V, A, R> collector;

	public OneConcurrentHashMapToRuleThemAllMapReduceFramework(Mapper<E, K, V> mapper, Collector<V, A, R> collector) {
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

	@Override
	public Map<K, R> mapReduceAll(E[] input) throws InterruptedException, ExecutionException {
		throw new NotYetImplementedException();
	}
}
