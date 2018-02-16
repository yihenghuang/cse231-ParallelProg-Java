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
package util.lab.collection;

import java.util.AbstractCollection;
import java.util.Deque;
import java.util.Iterator;

import net.jcip.annotations.NotThreadSafe;

/**
 * @author Yiheng Huang
 * @author Ben Choi (benjaminchoi@wustl.edu)
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@NotThreadSafe
public class LinkedNodesCollection<E> extends AbstractCollection<E> {

	private final LinkedNode<E> head;
	private int size;

	/**
	 * A simple singly linked list which starts at size zero with a sentinel head
	 * node. The sentinel node should always be maintained with a value of null,
	 * with its next pointer indicating the true first node of the list. This
	 * information will come in useful if you choose to use the StudentListIterator
	 * for your method implementations. Note: the following class is not thread
	 * safe.
	 */
	public LinkedNodesCollection() {
		size = 0;
		head = new LinkedNode<E>(null, null);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<E> iterator() {
		return new LinkedNodesIterator<E>(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Inserts item at the front of the collection as
	 * {{@link Deque#offerFirst(Object)}.
	 */
	@Override
	public boolean add(E item) {
		LinkedNode<E> tempItem = new LinkedNode<E>(item, this.head.getNext());
		this.size++;
		this.head.setNext(tempItem);
		return true;
	}

	/* package-private */ LinkedNode<E> getHeadNode() {
		return this.head;
	}

	/* package-private */ void decrementSize() {
		this.size--;
	}
}
