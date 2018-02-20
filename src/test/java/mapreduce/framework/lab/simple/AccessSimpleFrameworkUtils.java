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

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collector;

import edu.wustl.cse231s.util.KeyValuePair;
import mapreduce.framework.core.ExceptionCollector;
import mapreduce.framework.core.ExceptionMapper;
import mapreduce.framework.core.Mapper;
import mapreduce.framework.lab.simple.SimpleMapReduceFramework;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class AccessSimpleFrameworkUtils {
	public static <E, K, V, A, R> List<KeyValuePair<K, V>>[] mapAllOnly(E[] data, Mapper<E, K, V> mapper)
			throws InterruptedException, ExecutionException {
		// Collector<V, Void, Void> collector = new NoOpCollector<>();
		Collector<V, Void, Void> collector = new ExceptionCollector<>("collector should not be used in mapAll");
		SimpleMapReduceFramework<E, K, V, Void, Void> framework = new SimpleMapReduceFramework<>(mapper, collector);
		return framework.mapAll(data);
	}

	public static <E, K, V, A, R> Map<K, A> accumulateAllOnly(List<KeyValuePair<K, V>>[] mapAllResults,
			Collector<V, A, R> collector) {
		Mapper<E, K, V> mapper = new ExceptionMapper<>("mapper should not be used in accumulateAll");
		SimpleMapReduceFramework<E, K, V, A, R> framework = new SimpleMapReduceFramework<>(mapper, collector);
		return framework.accumulateAll(mapAllResults);
	}

	public static <E, K, V, A, R> Map<K, R> finishAllOnly(Map<K, A> accumulateAllResult, Collector<V, A, R> collector)
			throws InterruptedException, ExecutionException {
		Mapper<E, K, V> mapper = new ExceptionMapper<>("mapper should not be used in finishAll");
		SimpleMapReduceFramework<E, K, V, A, R> framework = new SimpleMapReduceFramework<>(mapper, collector);
		return framework.finishAll(accumulateAllResult);
	}
}
