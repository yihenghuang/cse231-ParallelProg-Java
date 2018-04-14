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
package lock.allornothing.studio;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableObject;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import lock.allornothing.studio.LockUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link LockUtils#runWithAllLocksOrDontRunAtAll(List, Runnable)}
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RunWithAllLocksOrDontRunAtAllTest {
	@Test(timeout = 5000)
	public void testA_SingleUncontendedLock() throws InterruptedException {
		ReentrantLock uncontendedLock = new ReentrantLock();
		List<ReentrantLock> locks = Arrays.asList(uncontendedLock);
		for (ReentrantLock lock : locks) {
			assertFalse(lock.isLocked());
		}
		Mutable<Boolean> isSuccessful = new MutableObject<>(null);
		ConcurrentLinkedQueue<ReentrantLock> unheldLocks = new ConcurrentLinkedQueue<>();
		startAndJoinThread(() -> {
			isSuccessful.setValue(LockUtils.runWithAllLocksOrDontRunAtAll(locks, () -> {
				for (ReentrantLock lock : locks) {
					if (lock.isHeldByCurrentThread()) {
						// pass
					} else {
						unheldLocks.offer(lock);
					}
				}
			}));
		});

		assertNotNull(isSuccessful.getValue());
		assertTrue(isSuccessful.getValue().booleanValue());
		assertTrue(unheldLocks.isEmpty());
		for (ReentrantLock lock : locks) {
			assertFalse(lock.isLocked());
		}
	}

	@Test(timeout = 5000)
	public void testB_MultipleUncontendedLocks() throws InterruptedException {
		List<ReentrantLock> locks = new LinkedList<>();
		for (int i = 0; i < 3; i++) {
			locks.add(new ReentrantLock());
		}
		for (ReentrantLock lock : locks) {
			assertFalse(lock.isLocked());
		}
		Mutable<Boolean> isSuccessful = new MutableObject<>(null);
		ConcurrentLinkedQueue<ReentrantLock> unheldLocks = new ConcurrentLinkedQueue<>();
		startAndJoinThread(() -> {
			isSuccessful.setValue(LockUtils.runWithAllLocksOrDontRunAtAll(locks, () -> {
				for (ReentrantLock lock : locks) {
					if (lock.isHeldByCurrentThread()) {
						// pass
					} else {
						unheldLocks.offer(lock);
					}
				}
			}));
		});

		assertNotNull(isSuccessful.getValue());
		assertTrue(isSuccessful.getValue().booleanValue());
		assertTrue(unheldLocks.isEmpty());

		for (ReentrantLock lock : locks) {
			assertFalse(lock.isLocked());
		}
	}

	@Test(timeout = 5000)
	public void testC_SingleHeldLock() throws InterruptedException {
		ReentrantLock heldLock = new ReentrantLock();
		heldLock.lock();
		assertTrue(heldLock.isHeldByCurrentThread());
		List<ReentrantLock> locks = Arrays.asList(heldLock);

		Mutable<Boolean> isSuccessful = new MutableObject<>(null);
		MutableBoolean isBodyInvoked = new MutableBoolean(false);
		startAndJoinThread(() -> {
			isSuccessful.setValue(LockUtils.runWithAllLocksOrDontRunAtAll(locks, () -> {
				isBodyInvoked.setTrue();
			}));
		});
		assertNotNull(isSuccessful.getValue());
		assertFalse(isSuccessful.getValue().booleanValue());
		assertFalse(isBodyInvoked.booleanValue());

		assertTrue(heldLock.isHeldByCurrentThread());
		heldLock.unlock();
		for (ReentrantLock lock : locks) {
			assertFalse(lock.isLocked());
		}
	}

	@Test(timeout = 5000)
	public void testD_SingleHeldOfMultipleLock() throws InterruptedException {
		List<ReentrantLock> locks = new LinkedList<>();
		for (int i = 0; i < 3; i++) {
			locks.add(new ReentrantLock());
		}

		for (int i = 0; i < locks.size(); i++) {
			for (ReentrantLock lock : locks) {
				assertFalse(lock.isLocked());
			}

			ReentrantLock heldLock = locks.get(i);
			heldLock.lock();

			assertTrue(heldLock.isHeldByCurrentThread());

			Mutable<Boolean> isSuccessful = new MutableObject<>(null);
			MutableBoolean isBodyInvoked = new MutableBoolean(false);
			startAndJoinThread(() -> {
				isSuccessful.setValue(LockUtils.runWithAllLocksOrDontRunAtAll(locks, () -> {
					isBodyInvoked.setTrue();
				}));
			});
			assertNotNull(isSuccessful.getValue());
			assertFalse(isSuccessful.getValue().booleanValue());
			assertFalse(isBodyInvoked.booleanValue());

			assertTrue(heldLock.isHeldByCurrentThread());
			heldLock.unlock();

			for (ReentrantLock lock : locks) {
				assertFalse(lock.isLocked());
			}
		}
	}

	@Test(timeout = 5000)
	public void testE_AllHeldMultipleLocks() throws InterruptedException {
		List<ReentrantLock> locks = new LinkedList<>();
		for (int i = 0; i < 3; i++) {
			locks.add(new ReentrantLock());
		}

		for (ReentrantLock lock : locks) {
			assertFalse(lock.isLocked());
			lock.lock();
			assertTrue(lock.isHeldByCurrentThread());
		}

		Mutable<Boolean> isSuccessful = new MutableObject<>(null);
		MutableBoolean isBodyInvoked = new MutableBoolean(false);
		startAndJoinThread(() -> {
			isSuccessful.setValue(LockUtils.runWithAllLocksOrDontRunAtAll(locks, () -> {
				isBodyInvoked.setTrue();
			}));
		});

		assertNotNull(isSuccessful.getValue());
		assertFalse(isSuccessful.getValue().booleanValue());
		assertFalse(isBodyInvoked.booleanValue());

		for (ReentrantLock lock : locks) {
			assertTrue(lock.isHeldByCurrentThread());
			lock.unlock();
			assertFalse(lock.isLocked());
		}
	}

	private static Thread startAndJoinThread(Runnable body) throws InterruptedException {
		Thread thread = new Thread(body);
		thread.start();
		thread.join();
		return thread;
	}
}
