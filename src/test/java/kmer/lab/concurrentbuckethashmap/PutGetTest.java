/*******************************************************************************
 * Copyright (C) 2016-2017 Dennis Cosgrove, Ben Choi
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
package kmer.lab.concurrentbuckethashmap;

import static edu.wustl.cse231s.v5.V5.launchApp;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import kmer.lab.concurrentbuckethashmap.ConcurrentBucketHashMap;
import kmer.lab.rubric.KMerRubric;

/**
 * @author Ben Choi (benjaminchoi@wustl.edu)
 * 
 *         {@link ConcurrentBucketHashMap#put(Object, Object)}
 *         {@link ConcurrentBucketHashMap#get(Object)}
 */
@KMerRubric(KMerRubric.Category.BUCKET_MAP)
public class PutGetTest extends AbstractDictionaryTest {
	@Test
	public void test() {
		launchApp(() -> {
			ConcurrentBucketHashMap<Integer, Double> map = createMap();
			for (int i = 0; i < 10; ++i) {
				double expected = i * 0.1;
				map.put(i, expected);
				Double actual = map.get(i);
				assertNotNull(actual);
				assertEquals(expected, actual.doubleValue(), /* epsilon= */0.0);
				assertNull("Your map is returning a value when it shouldn't be", map.get(i + 1));
			}
		});
	}
}
