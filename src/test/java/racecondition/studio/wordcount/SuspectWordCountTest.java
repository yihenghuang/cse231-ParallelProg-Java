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

package racecondition.studio.wordcount;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import org.junit.Test;

import edu.wustl.cse231s.junit.JUnitUtils;
import edu.wustl.cse231s.v5.bookkeep.BookkeepingUtils;
import edu.wustl.cse231s.v5.impl.BookkeepingV5Impl;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link SuspectWordCount#countWords(Iterable)}}
 */
public class SuspectWordCountTest {
	@Test(timeout = JUnitUtils.DEFAULT_TIMEOUT)
	public void test() {
		Collection<String> words = createWords(10_000);
		Map<String, Integer> expected = new HashMap<>();
		for (String word : words) {
			Integer count = expected.get(word);
			if (count != null) {
				count = count + 1;
			} else {
				count = 1;
			}
			expected.put(word, count);
		}
		BookkeepingV5Impl bookkeeping = BookkeepingUtils.bookkeep(() -> {
			Map<String, Integer> actual = SuspectWordCount.countWords(words);
			assertEquals(expected, actual);
		});
		assertNotEquals("parallelism must not be removed", 0, bookkeeping.getTaskCount());
		assertEquals("unexpected task count", words.size(), bookkeeping.getTaskCount());
	}

	private static Collection<String> createWords(int size) {
		Random random = new Random();
		Collection<String> result = new LinkedList<>();
		for (int i = 0; i < size; i++) {
			result.add(random.nextBoolean() ? "race" : "condition");
		}
		return result;
	}
}
