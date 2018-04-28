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

import java.util.Objects;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import edu.wustl.cse231s.junit.JUnitUtils;
import kmer.lab.concurrentbuckethashmap.ConcurrentBucketHashMap;
import kmer.lab.rubric.KMerRubric;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link ConcurrentBucketHashMap#compute(Object, java.util.function.BiFunction)}
 */
@KMerRubric(KMerRubric.Category.BUCKET_MAP)
public class NotInvokingGetFromComputeTest extends AbstractDictionaryTest {
	@Rule
	public TestRule timeout = JUnitUtils.createTimeoutRule();

	@Test
	public void test() {
		launchApp(() -> {
			ConcurrentBucketHashMap<Integer, String> map = createMap();
			try {
				map.compute(71, (k, v) -> {
					return "score";
				});
			} catch (IllegalStateException ise) {
				if (Objects.equals("isolated block threw an exception!", ise.getMessage())) {
					Throwable cause = ise.getCause();
					if (cause instanceof IllegalStateException) {
						if (Objects.equals(
								"Attempt to obtain a lock on object not originally locked by the root isolated block",
								cause.getMessage())) {
							StringBuilder sb = new StringBuilder();
							sb.append(cause.getMessage());
							sb.append("\n----");
							sb.append(
									"\n    Guessing that you called get() which holds the read lock for a bucket from within compute() which holds the write lock.");
							sb.append("\n    This is not allowed for admittedly non-inuitive reasons.");
							sb.append(
									"\n    Luckily, you should not need to call get from within compute as the Entry has a getValue() method.");
							sb.append("\n----");
							throw new IllegalStateException(sb.toString(), ise);
						}
					}
				}
				throw ise;
			}
		});
	}
}
