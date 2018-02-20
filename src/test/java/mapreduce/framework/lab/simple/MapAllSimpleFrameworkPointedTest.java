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

import static edu.wustl.cse231s.v5.V5.launchApp;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.function.BiConsumer;

import org.junit.Assert;
import org.junit.Test;

import edu.wustl.cse231s.util.KeyValuePair;
import mapreduce.framework.core.Mapper;
import mapreduce.framework.lab.rubric.MapReduceRubric;
import mapreduce.framework.lab.simple.AccessSimpleFrameworkUtils;
import mapreduce.framework.lab.simple.SimpleMapReduceFramework;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 *         
 *         {@link SimpleMapReduceFramework#mapAll(Object[])}
 */
@MapReduceRubric(MapReduceRubric.Category.SIMPLE_MAP_ALL)
public class MapAllSimpleFrameworkPointedTest {
	private static boolean areDistinctObjects(Object[] array) {
		for (int i = 0; i < array.length; i++) {
			for (int j = i + 1; j < array.length; j++) {
				if (array[i] == array[j]) {
					return false;
				}
			}
		}
		return true;
	}

	@Test
	public void testResultArrayNotNull() {
		class Input {
		}
		class MapOutputKey {
		}
		class MapOutputValue {
		}

		int length = 10;
		Input[] data = new Input[length];
		Mapper<Input, MapOutputKey, MapOutputValue> doNothingMapper = (Input item,
				BiConsumer<MapOutputKey, MapOutputValue> keyValuePairConsumer) -> {
		};

		launchApp(() -> {
			List<KeyValuePair<MapOutputKey, MapOutputValue>>[] mapAllResult = AccessSimpleFrameworkUtils.mapAllOnly(data,
					doNothingMapper);
			Assert.assertEquals(data.length, mapAllResult.length);
			for (List<KeyValuePair<MapOutputKey, MapOutputValue>> element : mapAllResult) {
				Assert.assertNotNull(element);
			}
			assertTrue(areDistinctObjects(mapAllResult));
		});
	}

	@Test
	public void test() {
		String[] input = { "hello" };
		Mapper<String, String, Integer> nonsenseMapper = (String item,
				BiConsumer<String, Integer> keyValuePairConsumer) -> {
			keyValuePairConsumer.accept(item, item.length());
		};
		launchApp(() -> {
			List<KeyValuePair<String, Integer>>[] mapAllResult = AccessSimpleFrameworkUtils.mapAllOnly(input,
					nonsenseMapper);
			Assert.assertEquals(1, mapAllResult.length);
			Assert.assertEquals(1, mapAllResult[0].size());
			KeyValuePair<String, Integer> keyValuePair = mapAllResult[0].get(0);
			Assert.assertNotNull(keyValuePair);
			Assert.assertEquals(input[0], keyValuePair.getKey());
			Assert.assertEquals(input[0].length(), keyValuePair.getValue().intValue());
		});
	}
}
