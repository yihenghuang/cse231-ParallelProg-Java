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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReadWriteLock;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import edu.wustl.cse231s.junit.JUnitUtils;
import kmer.lab.rubric.KMerRubric;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link ConcurrentBucketHashMap#getIndex(Object)}
 *         {@link ConcurrentBucketHashMap#getBucket(Object)}
 *         {@link ConcurrentBucketHashMap#getLock(Object)}
 */
@RunWith(Parameterized.class)
@KMerRubric(KMerRubric.Category.BUCKET_MAP)
public class GetIndexBucketAndLockTest {
	private final int keyHashCode;

	public GetIndexBucketAndLockTest(int keyHashCode) {
		this.keyHashCode = keyHashCode;
	}

	@Rule
	public TestRule timeout = JUnitUtils.createTimeoutRule();
	
	@Test
	public void test() throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchFieldException {

		final class Key {
			private final int state;

			public Key(int state) {
				this.state = state;
			}

			@Override
			public int hashCode() {
				return state;
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (getClass() != obj.getClass())
					return false;
				Key other = (Key) obj;
				if (state != other.state)
					return false;
				return true;
			}
		}

		Key key = new Key(keyHashCode);

		int length = 231;
		ConcurrentBucketHashMap<Key, Void> map = new ConcurrentBucketHashMap<>(length);

		Method mthdGetIndex = ConcurrentBucketHashMap.class.getDeclaredMethod("getIndex", Object.class);
		mthdGetIndex.setAccessible(true);
		int actualIndex = (int) mthdGetIndex.invoke(map, key);

		int expectedIndex = Math.floorMod(keyHashCode, length);
		assertTrue(actualIndex >= 0);
		assertTrue(actualIndex < length);
		assertEquals(expectedIndex, actualIndex);

		Field fldBuckets = ConcurrentBucketHashMap.class.getDeclaredField("buckets");
		fldBuckets.setAccessible(true);
		@SuppressWarnings("unchecked")
		List<Entry<Key, Void>>[] buckets = (List<Entry<Key, Void>>[]) fldBuckets.get(map);
	
		Method mthdGetBucket = ConcurrentBucketHashMap.class.getDeclaredMethod("getBucket", Object.class);
		mthdGetBucket.setAccessible(true);
		@SuppressWarnings("unchecked")
		List<Entry<Key, Void>> actualBucket = (List<Entry<Key, Void>>) mthdGetBucket.invoke(map, key);

		assertSame(buckets[expectedIndex], actualBucket);

		Field fldLocks = ConcurrentBucketHashMap.class.getDeclaredField("locks");
		fldLocks.setAccessible(true);
		ReadWriteLock[] locks = (ReadWriteLock[]) fldLocks.get(map);

		Method mthdGetLock = ConcurrentBucketHashMap.class.getDeclaredMethod("getLock", Object.class);
		mthdGetLock.setAccessible(true);
		ReadWriteLock actualLock = (ReadWriteLock) mthdGetLock.invoke(map, key);

		assertSame(locks[expectedIndex], actualLock);
	}

	@Parameters(name = "keyHashCode={0}")
	public static Collection<Object[]> getConstructorArguments() {
		Collection<Object[]> result = new LinkedList<>();
		int[] values = { 0, 1, 2, 71, 1000, -1, -1000 };
		for (int value : values) {
			result.add(new Object[] { value });
		}
		return result;
	}

}
