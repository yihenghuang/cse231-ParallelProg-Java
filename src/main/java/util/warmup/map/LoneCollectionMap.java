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
package util.warmup.map;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import edu.wustl.cse231s.NotYetImplementedException;
import edu.wustl.cse231s.util.KeyMutableValuePair;
import net.jcip.annotations.NotThreadSafe;
import util.lab.collection.LinkedNodesCollection;

/**
 * @author __STUDENT_NAME__
 * @author Ben Choi (benjaminchoi@wustl.edu)
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@NotThreadSafe
public class LoneCollectionMap<K, V> extends AbstractMap<K, V> {

	private final Collection<Entry<K, V>> entries;

	/**
	 * A simple map created using your implementation of a singly linked list to
	 * keep track of key/value pair entries. It takes in a key of type K and a value
	 * of type V. It initializes as an empty map with a size of 0. It only uses one
	 * bucket. Note: this class is not thread-safe.
	 */
	public LoneCollectionMap() {
		entries = new LinkedNodesCollection<>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		throw new NotYetImplementedException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public V put(K key, V value) {
		throw new NotYetImplementedException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public V remove(Object key) {
		throw new NotYetImplementedException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public V get(Object key) {
		throw new NotYetImplementedException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Entry<K, V>> entrySet() {
		return new AbstractSet<Entry<K, V>>() {
			@Override
			public Iterator<Entry<K, V>> iterator() {
				return entries.iterator();
			}

			@Override
			public int size() {
				return LoneCollectionMap.this.size();
			}
		};
	}
}
