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
package util.lab.map;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.iterators.LazyIteratorChain;

import edu.wustl.cse231s.util.KeyMutableValuePair;
import net.jcip.annotations.NotThreadSafe;
import util.lab.collection.LinkedNodesCollection;

/**
 * @author Yiheng Huang
 * @author Ben Choi (benjaminchoi@wustl.edu)
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@NotThreadSafe
public class BucketsHashMap<K, V> extends AbstractMap<K, V> {

	private final Collection<Entry<K, V>>[] buckets;

	/**
	 * A simple HashMap which extends the simple map you implemented earlier. This
	 * implementation will make use of buckets based on an object's hash in order to
	 * cut down on the runtime of finding entries. See the two given methods in
	 * order to find out which bucket an entry should go in depending on the
	 * hashcode of the entry's key. Note: this class is not thread-safe.
	 */
	@SuppressWarnings("unchecked")
	public BucketsHashMap(int capacity) {
		buckets = new Collection[capacity];
		for (int i = 0; i < buckets.length; ++i) {
			buckets[i] = new LinkedNodesCollection<>();
		}
	}

	public BucketsHashMap() {
		this(1024);
	}

	/**
	 * Uses the key's hashcode and the length of the number of buckets to determine
	 * which bucket the entry should go into
	 * 
	 * @param key
	 * @return the index of the bucket the entry should go into
	 */
	private int hash(Object key) {
		return Math.floorMod(key.hashCode(), buckets.length);
	}

	/**
	 * Uses the key's hash to create a reference to the bucket the key is in
	 * 
	 * @param key
	 * @return the bucket the key is in
	 */
	private Collection<Entry<K, V>> getBucketFor(Object key) {
		return buckets[hash(key)];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		int size = 0;
		for (int i = 0; i < buckets.length; i++) {
			size += buckets[i].size();
		}
		return size;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public V put(K key, V value) {
		Collection<Entry<K, V>> bucket = getBucketFor(key);
		Iterator<Entry<K, V>> i = bucket.iterator();

		while (i.hasNext()) {
			Entry<K, V> temp = i.next();
			if (temp.getKey().equals(key)) {
				V v = temp.getValue();
				temp.setValue(value);
				return v;
			}
		}

		KeyMutableValuePair<K, V> a = new KeyMutableValuePair<K, V>(key, value);
		bucket.add(a);
		return null;
	}

	/**
	 * {@inheritDoc}
	 */

	/*
	 * ------------------------------------------- the bucketsSizeAfterRemoveTest
	 * cannot pass for some reason. And also the testReturnValueSimply subtest
	 * cannot pass I cannot find out why
	 * -------------------------------------------- but I used two friends' code to
	 * test with my test files in here it turns out that theirs cannot pass here as
	 * well while theirs passed with their own tests in their own package
	 * 
	 * -----------And my code was passed in my friend's package
	 * 
	 * ----------Therefore the test file was corrupted by me :( But the code is
	 * alright. ---------------------
	 */
	@Override
	public V remove(Object key) {
		Collection<Entry<K, V>> bucket = getBucketFor(key);
		Iterator<Entry<K, V>> i = bucket.iterator();
		Entry<K, V> r = new KeyMutableValuePair<K, V>(null, null);
		while (i.hasNext()) {
			Entry<K, V> temp = i.next();
			if (temp.getKey().equals(key)) {
				r = temp;
				V rv = r.getValue();
				i.remove();
				return rv;
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public V get(Object key) {
		Collection<Entry<K, V>> bucket = getBucketFor(key);
		Iterator<Entry<K, V>> i = bucket.iterator();
		while (i.hasNext()) {
			Entry<K, V> temp = i.next();
			if (temp.getKey().equals(key)) {
				return temp.getValue();
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Entry<K, V>> entrySet() {
		return new AbstractSet<Entry<K, V>>() {
			@Override
			public Iterator<Entry<K, V>> iterator() {
				return new LazyIteratorChain<Map.Entry<K, V>>() {
					@Override
					protected Iterator<? extends java.util.Map.Entry<K, V>> nextIterator(int count) {
						int index = count - 1;
						return index < BucketsHashMap.this.buckets.length
								? BucketsHashMap.this.buckets[index].iterator()
								: null;
					}
				};
			}

			@Override
			public int size() {
				return BucketsHashMap.this.size();
			}
		};
	}
}
