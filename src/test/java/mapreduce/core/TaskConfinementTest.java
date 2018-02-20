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
package mapreduce.core;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Assert;
import org.junit.Test;

import mapreduce.apps.wordcount.core.TextSection;
import mapreduce.apps.wordcount.core.WordCountUtils;
import mapreduce.apps.wordcount.core.io.WordsResource;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class TaskConfinementTest {

	private static abstract class ConfinementChecker<V, T> {
		protected abstract T currentTask();

		public void assertConfinement(V notThreadSafeObject, String message) {
			Assert.assertNotNull(notThreadSafeObject);
			mapObjectToTask.compute(notThreadSafeObject, (k, prevValue) -> {
				T nextValue = this.currentTask();
				if (prevValue != null) {
					Assert.assertEquals(message, prevValue, nextValue);
				}
				return nextValue;
			});
		}

		private Map<V, T> mapObjectToTask = new ConcurrentHashMap<>();
	}

	private static class ThreadConfinementChecker<V> extends ConfinementChecker<V, Thread> {
		@Override
		protected Thread currentTask() {
			return Thread.currentThread();
		}
	}

	// private static class HabaneroActivityConfinementChecker<V> extends
	// ConfinementChecker<V, HabaneroActivity> {
	// @Override
	// protected HabaneroActivity currentTask() {
	// return BaseRuntime.currentHabaneroActivity();
	// }
	// }
	//
	// private static class ContextThreadTrackingMapper<E, K, V> implements
	// Mapper<E, K, V> {
	// @Override
	// public void map(E item, BiConsumer<K, V> keyValuePairConsumer) {
	// this.confinementChecker.assertConfinement(keyValuePairConsumer,
	// "Data Race: MapOutputCollector (designed to be non-thread safe) detected
	// from multiple tasks: "
	// + keyValuePairConsumer);
	// }
	//
	// private final HabaneroActivityConfinementChecker<BiConsumer<K, V>>
	// confinementChecker = new HabaneroActivityConfinementChecker<>();
	// }

	private void testTaskConfinement(TextSection[] data, int mapTaskCount) {
		throw new RuntimeException("TODO");
		// ContextThreadTrackingMapper<TextSection, String, Integer> mapper =
		// new ContextThreadTrackingMapper<>();
		// Collector<Integer, ?, Integer> collector =
		// Collectors.summingInt(Integer::intValue);
		// final int reduceTaskCount = 1;
		// MapReduceFramework<TextSection, String, Integer, ?, Integer>
		// framework = new MatrixMapReduceFramework<>(mapper,
		// collector, mapTaskCount, reduceTaskCount);
		// launchApp(() -> {
		// framework.mapReduceAll(data);
		// });
	}

	@Test
	public void testTaskConfinementForSmallDataSetOnOneMapTask() {
		this.testTaskConfinement(WordCountUtils.toTextSections("a", "b c", "d e f", "h i j k"), 1);
	}

	@Test
	public void testTaskConfinementForWarAndPeaceOnAllProcessors() throws IOException {
		TextSection[] data = WordCountUtils.readAllLinesOfWords(WordsResource.WAR_AND_PEACE);
		this.testTaskConfinement(data, Runtime.getRuntime().availableProcessors());
	}
}
