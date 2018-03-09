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

package mapreduce;

import static edu.wustl.cse231s.v5.V5.launchApp;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

import org.apache.commons.lang3.mutable.MutableObject;

import edu.wustl.cse231s.NotYetImplementedException;
import edu.wustl.cse231s.timing.ImmutableTimer;
import mapreduce.apps.intsum.studio.IntegerSumClassicReducer;
import mapreduce.apps.wordcount.core.TextSection;
import mapreduce.apps.wordcount.core.WordCountUtils;
import mapreduce.apps.wordcount.core.io.WordsResource;
import mapreduce.apps.wordcount.studio.WordCountMapper;
import mapreduce.collector.intsum.studio.IntSumCollector;
import mapreduce.framework.core.MapReduceFramework;
import mapreduce.framework.core.Mapper;
import mapreduce.framework.fun.single.OneConcurrentHashMapToRuleThemAllMapReduceFramework;
import mapreduce.framework.fun.stream.StreamMapReduceFramework;
import mapreduce.framework.lab.matrix.MatrixMapReduceFramework;
import mapreduce.framework.lab.simple.SimpleMapReduceFramework;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class MapReduceTiming {
	private static <E, K, V, A, R> Map<K, R> mapReduce(E[] input, MapReduceFramework<E, K, V, A, R> framework) {
		MutableObject<Map<K, R>> result = new MutableObject<>();
		launchApp(() -> {
			try {
				result.setValue(framework.mapReduceAll(input));
			} catch (NotYetImplementedException nyie) {
				result.setValue(null);
			}
		});
		return result.getValue();
	}

	private static TextSection[] readTextSections() throws IOException {
		final boolean ALL = false;
		if (ALL) {
			List<TextSection> list = new LinkedList<>();
			for (WordsResource wordsResource : WordsResource.values()) {
				TextSection[] textSections = WordCountUtils.readAllLinesOfWords(wordsResource);
				list.addAll(Arrays.asList(textSections));
			}
			return list.toArray(new TextSection[list.size()]);
		} else {
			return WordCountUtils.readAllLinesOfWords(WordsResource.WAR_AND_PEACE);
		}
	}

	private static <A> void timeFramework(String prefix,
			MapReduceFramework<TextSection, String, Integer, A, Integer> framework, TextSection[] input) {
		ImmutableTimer timer = new ImmutableTimer(prefix);
		Map<String, Integer> map = mapReduce(input, framework);
		if (map != null) {
			String key = "the";
			timer.markAndPrintResults(" the: " + map.get(key));
		} else {
			timer.mark();
			System.out.println(prefix + "; not yet implemented");
		}
	}

	private static <A> void timeAll(Collector<Integer, A, Integer> collector) throws IOException {
		int tasksPerProcessor = 1;
		int taskCount = Runtime.getRuntime().availableProcessors() * tasksPerProcessor;
		Mapper<TextSection, String, Integer> mapper = new WordCountMapper();
		MapReduceFramework<TextSection, String, Integer, A, Integer> simpleFramework = new SimpleMapReduceFramework<>(
				mapper, collector);
		MapReduceFramework<TextSection, String, Integer, A, Integer> matrixFramework = new MatrixMapReduceFramework<>(
				mapper, collector, taskCount, taskCount);
		MapReduceFramework<TextSection, String, Integer, A, Integer> streamFramework = new StreamMapReduceFramework<>(
				mapper, collector);
		MapReduceFramework<TextSection, String, Integer, A, Integer> singleFramework = new OneConcurrentHashMapToRuleThemAllMapReduceFramework<>(
				mapper, collector);
		TextSection[] input = readTextSections();

		for (int i = 0; i < 10; i++) {
			timeFramework("simple", simpleFramework, input);
			timeFramework("single", singleFramework, input);
			timeFramework("matrix", matrixFramework, input);
			timeFramework("stream", streamFramework, input);
			System.out.println();
		}
	}

	public static void main(String[] args) throws IOException {
		final boolean IS_LIST_OF_ONES_DESIRED = false;
		if (IS_LIST_OF_ONES_DESIRED) {
			timeAll(new IntegerSumClassicReducer());
		} else {
			timeAll(new IntSumCollector());
		}
	}
}
