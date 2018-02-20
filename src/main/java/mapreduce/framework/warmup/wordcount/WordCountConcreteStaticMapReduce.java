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
package mapreduce.framework.warmup.wordcount;

import static edu.wustl.cse231s.v5.V5.forall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;

import edu.wustl.cse231s.NotYetImplementedException;
import edu.wustl.cse231s.util.KeyValuePair;
import mapreduce.apps.intsum.studio.IntegerSumClassicReducer;
import mapreduce.apps.wordcount.core.TextSection;
import mapreduce.apps.wordcount.studio.WordCountMapper;
import mapreduce.collector.studio.ClassicReducer;
import mapreduce.framework.lab.simple.SimpleMapReduceFramework;

/**
 * @author __STUDENT_NAME__
 * @author Finn Voichick
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class WordCountConcreteStaticMapReduce {
	/**
	 * {@link WordCountMapper#map(TextSection, BiConsumer)};
	 */
	static void map(TextSection textSection, BiConsumer<String, Integer> keyValuePairConsumer) {
		throw new NotYetImplementedException();
	}

	/**
	 * {@link ClassicReducer#supplier()};
	 */
	static List<Integer> reduceCreateList() {
		throw new NotYetImplementedException();
	}

	/**
	 * {@link ClassicReducer#accumulator()};
	 */
	static void reduceAccumulate(List<Integer> list, int v) {
		throw new NotYetImplementedException();
	}

	/**
	 * NOTE: this method is not used for the warm-up or simple framework
	 * 
	 * {@link ClassicReducer#combiner()};
	 */
	static void reduceCombine(List<Integer> a, List<Integer> b) {
		throw new NotYetImplementedException();
	}

	/**
	 * {@link IntegerSumClassicReducer#finisher()};
	 */
	static int reduceFinish(List<Integer> list) {
		throw new NotYetImplementedException();
	}

	/**
	 * {@link SimpleMapReduceFramework#mapAll(E[])};
	 */
	static List<KeyValuePair<String, Integer>>[] mapAll(TextSection[] input)
			throws InterruptedException, ExecutionException {
		throw new NotYetImplementedException();
	}

	/**
	 * {@link SimpleMapReduceFramework#accumulateAll(List<KeyValuePair<K,V>[])};
	 */
	static Map<String, List<Integer>> accumulateAll(List<KeyValuePair<String, Integer>>[] mapAllResults) {
		throw new NotYetImplementedException();
	}

	/**
	 * {@link SimpleMapReduceFramework#finishAll(Map)};
	 */
	static Map<String, Integer> finishAll(Map<String, List<Integer>> accumulateAllResult)
			throws InterruptedException, ExecutionException {
		throw new NotYetImplementedException();
	}

	public static Map<String, Integer> mapReduce(TextSection[] input) throws InterruptedException, ExecutionException {
		List<KeyValuePair<String, Integer>>[] mapAllResult = mapAll(input);
		Map<String, List<Integer>> accumulateAllResult = accumulateAll(mapAllResult);
		return finishAll(accumulateAllResult);
	}
}
