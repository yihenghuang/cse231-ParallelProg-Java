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
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
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
 *         {@link ConcurrentBucketHashMap#ConcurrentBucketHashMap(int)}
 */
@RunWith(Parameterized.class)
@KMerRubric(KMerRubric.Category.BUCKET_MAP)
public class ConstructorTest {
	private final int arrayLength;

	@Rule
	public TestRule timeout = JUnitUtils.createTimeoutRule();

	public ConstructorTest(int arrayLength) {
		this.arrayLength = arrayLength;
	}

	@Test
	public void testBucketsInitialized()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		ConcurrentBucketHashMap<String, Void> map = new ConcurrentBucketHashMap<>(arrayLength);
		Field fldBuckets = ConcurrentBucketHashMap.class.getDeclaredField("buckets");
		fldBuckets.setAccessible(true);
		@SuppressWarnings("unchecked")
		List<Entry<String, Void>>[] buckets = (List<Entry<String, Void>>[]) fldBuckets.get(map);
		assertEquals(arrayLength, buckets.length);
		for (int i = 0; i < buckets.length; i++) {
			assertNotNull(buckets[i]);
		}
	}

	@Test
	public void testLocksInitialized()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		ConcurrentBucketHashMap<String, Void> map = new ConcurrentBucketHashMap<>(arrayLength);
		Field fldLocks = ConcurrentBucketHashMap.class.getDeclaredField("locks");
		fldLocks.setAccessible(true);
		ReadWriteLock[] locks = (ReadWriteLock[]) fldLocks.get(map);
		assertEquals(arrayLength, locks.length);
		for (int i = 0; i < locks.length; i++) {
			assertNotNull(locks[i]);
		}
	}

	@Parameters(name = "arrayLength={0}")
	public static Collection<Object[]> getConstructorArguments() {
		return Arrays.asList(new Object[] { 1024 }, new Object[] { 71 });
	}
}
