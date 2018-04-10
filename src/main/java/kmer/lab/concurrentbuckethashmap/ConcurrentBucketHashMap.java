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
package kmer.lab.concurrentbuckethashmap;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiFunction;

import edu.wustl.cse231s.NotYetImplementedException;
import edu.wustl.cse231s.util.KeyMutableValuePair;
import net.jcip.annotations.ThreadSafe;

/**
 * @author Yiheng Huang
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@ThreadSafe
/* package-private */ class ConcurrentBucketHashMap<K, V> implements ConcurrentMap<K, V> {
	private final List<Entry<K, V>>[] buckets;
	private final ReadWriteLock[] locks;

	@SuppressWarnings("unchecked")
	public ConcurrentBucketHashMap(int bucketCount) {
		throw new NotYetImplementedException();
	}

	private int getIndex(Object key) {
		throw new NotYetImplementedException();
	}

	private List<Entry<K, V>> getBucket(Object key) {
		throw new NotYetImplementedException();
	}

	private ReadWriteLock getLock(Object key) {
		throw new NotYetImplementedException();
	}

	private static <K, V> Entry<K, V> getEntry(List<Entry<K, V>> bucket, Object key) {
		throw new NotYetImplementedException();
	}

	@Override
	public V get(Object key) {
		throw new NotYetImplementedException();
	}

	@Override
	public V put(K key, V value) {
		throw new NotYetImplementedException();
	}

	@Override
	public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		throw new NotYetImplementedException();
	}

	@Override
	public int size() {
		throw new RuntimeException("not required");
	}

	@Override
	public boolean isEmpty() {
		throw new RuntimeException("not required");
	}

	@Override
	public boolean containsKey(Object key) {
		throw new RuntimeException("not required");
	}

	@Override
	public boolean containsValue(Object value) {
		throw new RuntimeException("not required");
	}

	@Override
	public V putIfAbsent(K key, V value) {
		throw new RuntimeException("not required");
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		throw new RuntimeException("not required");
	}

	@Override
	public boolean replace(K key, V oldValue, V newValue) {
		throw new RuntimeException("not required");
	}

	@Override
	public V replace(K key, V value) {
		throw new RuntimeException("not required");
	}

	@Override
	public void clear() {
		throw new RuntimeException("not required");
	}

	@Override
	public boolean remove(Object key, Object value) {
		throw new RuntimeException("not required");
	}

	@Override
	public V remove(Object key) {
		throw new RuntimeException("not required");
	}

	@Override
	public Set<K> keySet() {
		throw new RuntimeException("not required");
	}

	@Override
	public Collection<V> values() {
		throw new RuntimeException("not required");
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		throw new RuntimeException("not required");
	}
}
