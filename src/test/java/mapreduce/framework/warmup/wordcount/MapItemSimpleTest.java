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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import edu.wustl.cse231s.junit.JUnitUtils;
import edu.wustl.cse231s.util.KeyValuePair;
import mapreduce.apps.wordcount.core.TextSection;
import mapreduce.framework.warmup.wordcount.WordCountConcreteStaticMapReduce;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link WordCountConcreteStaticMapReduce#map(TextSection, java.util.function.BiConsumer)}
 */
public class MapItemSimpleTest {
	@Rule
	public TestRule timeout = JUnitUtils.createTimeoutRule();

	@Test
	public void test() {
		TextSection textSection = new TextSection("a b c d");

		List<KeyValuePair<String, Integer>> expected = new ArrayList<>();
		expected.add(new KeyValuePair<String, Integer>("a", 1));
		expected.add(new KeyValuePair<String, Integer>("b", 1));
		expected.add(new KeyValuePair<String, Integer>("c", 1));
		expected.add(new KeyValuePair<String, Integer>("d", 1));

		List<KeyValuePair<String, Integer>> list = new LinkedList<>();
		WordCountConcreteStaticMapReduce.map(textSection, (k, v) -> {
			list.add(new KeyValuePair<>(k, v));
		});

		Assert.assertEquals(expected, list);
	}
}
