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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiFunction;

import org.junit.Test;

import kmer.lab.rubric.KMerRubric;
import kmer.lab.util.TestRuntimeException;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link ConcurrentBucketHashMap#compute(Object, java.util.function.BiFunction)}
 */
@KMerRubric(KMerRubric.Category.BUCKET_DICTIONARY)
public class ComputeHoldsLockTest {
	private void testRemap(BiFunction<? super String, ? super Integer, ? extends Integer> remappingFunction)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		int arrayLength = 71;
		ConcurrentBucketHashMap<String, Integer> map = new ConcurrentBucketHashMap<>(arrayLength);
		String word = "score";
		Method mthdGetLock = ConcurrentBucketHashMap.class.getDeclaredMethod("getLock", Object.class);
		mthdGetLock.setAccessible(true);
		ReentrantReadWriteLock lock = (ReentrantReadWriteLock) mthdGetLock.invoke(map, word);
		assertNotNull(lock);
		assertFalse(lock.isWriteLocked());
		try {
			map.compute(word, (k, v) -> {
				assertTrue(lock.isWriteLockedByCurrentThread());
				return remappingFunction.apply(k, v);
			});
		} finally {
			assertFalse(lock.isWriteLocked());
		}
	}

	@Test
	public void test() throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		testRemap((k, v) -> {
			return v != null ? v + 1 : 1;
		});
	}

	@Test(expected = TestRuntimeException.class)
	public void testTryFinally() throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		testRemap((k, v) -> {
			throw new TestRuntimeException();
		});
	}
}
