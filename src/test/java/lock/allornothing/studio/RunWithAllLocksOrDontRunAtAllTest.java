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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableObject;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runners.MethodSorters;

import edu.wustl.cse231s.junit.JUnitUtils;
import edu.wustl.cse231s.v5.api.CheckedRunnable;
import lock.allornothing.studio.LockUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link LockUtils#runWithAllLocksOrDontRunAtAll(List, Runnable)}
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RunWithAllLocksOrDontRunAtAllTest {
	@Rule
	public TestRule timeout = JUnitUtils.createTimeoutRule(4);

	@Test
	public void testA_SingleUncontendedLock() throws InterruptedException, ExecutionException {
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

	@Test
	public void testB_MultipleUncontendedLocks() throws InterruptedException, ExecutionException {
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

	@Test
	public void testC_SingleHeldLock() throws InterruptedException, ExecutionException {
		ReentrantLock heldLock = new ReentrantLock();
		heldLock.lock();
		assertTrue(heldLock.isHeldByCurrentThread());
		List<ReentrantLock> locks = Arrays.asList(heldLock);

		MutableBoolean isBodyInvoked = new MutableBoolean(false);
		boolean isSuccessful = startAndJoinThreadRunWithAllLocksOrDontRunAtAll(locks, () -> {
			isBodyInvoked.setTrue();
		});
		assertFalse(isSuccessful);
		assertFalse(isBodyInvoked.booleanValue());

		assertTrue(heldLock.isHeldByCurrentThread());
		heldLock.unlock();
		for (ReentrantLock lock : locks) {
			assertFalse(lock.isLocked());
		}
	}

	@Test
	public void testD_SingleHeldOfMultipleLock() throws InterruptedException, ExecutionException {
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

			MutableBoolean isBodyInvoked = new MutableBoolean(false);
			boolean isSuccessful = startAndJoinThreadRunWithAllLocksOrDontRunAtAll(locks, () -> {
				isBodyInvoked.setTrue();
			});
			assertFalse(isSuccessful);
			assertFalse(isBodyInvoked.booleanValue());

			assertTrue(heldLock.isHeldByCurrentThread());
			heldLock.unlock();

			for (ReentrantLock lock : locks) {
				assertFalse(lock.isLocked());
			}
		}
	}

	@Test
	public void testE_AllHeldMultipleLocks() throws InterruptedException, ExecutionException {
		List<ReentrantLock> locks = new LinkedList<>();
		for (int i = 0; i < 3; i++) {
			locks.add(new ReentrantLock());
		}

		for (ReentrantLock lock : locks) {
			assertFalse(lock.isLocked());
			lock.lock();
			assertTrue(lock.isHeldByCurrentThread());
		}

		MutableBoolean isBodyInvoked = new MutableBoolean(false);
		boolean isSuccessful = startAndJoinThreadRunWithAllLocksOrDontRunAtAll(locks, () -> {
			isBodyInvoked.setTrue();
		});

		assertFalse(isSuccessful);
		assertFalse(isBodyInvoked.booleanValue());

		for (ReentrantLock lock : locks) {
			assertTrue(lock.isHeldByCurrentThread());
			lock.unlock();
			assertFalse(lock.isLocked());
		}
	}

	@Test
	public void testF_OnlyUnlockingImmediatelyPreviouslyAcquiredLocks()
			throws InterruptedException, ExecutionException {
		List<ReentrantLock> locks = new ArrayList<>();
		for (int i = 0; i < 7; i++) {
			locks.add(new ReentrantLock());
		}

		// acquiring this lock will prevent the runWithAllLocksOrDontRunAtAll thread
		// from succeeding
		Lock lock = locks.get(3);
		lock.lock();

		MutableObject<Boolean> isSuccessful = new MutableObject<>();
		MutableBoolean isBodyInvoked = new MutableBoolean(false);
		Thread thread = new Thread(() -> {
			// since lock 5 will come after 3, it should not be locked by
			// runWithAllLocksOrDontRunAtAll so it should not be unlocked.
			int indexOfLock = 5;
			locks.get(indexOfLock).lock();
			isSuccessful.setValue(LockUtils.runWithAllLocksOrDontRunAtAll(locks, () -> {
				isBodyInvoked.setTrue();
			}));
			assertTrue("Only unlock the locks you successfully acquired.  Do NOT invoke unlockAll.",
					locks.get(indexOfLock).isHeldByCurrentThread());
		});
		startAndJoinThread(thread);
		assertNotNull(isSuccessful.getValue());
		assertFalse(isSuccessful.getValue());
		assertFalse(isBodyInvoked.booleanValue());
	}

	@Test
	public void testG_TryFinally() throws InterruptedException, ExecutionException {
		class CustomException extends RuntimeException {
			private static final long serialVersionUID = 1L;
		}

		List<ReentrantLock> locks = Arrays.asList(new ReentrantLock(), new ReentrantLock(), new ReentrantLock());
		Throwable cause = null;
		try {
			startAndJoinThreadRunWithAllLocksOrDontRunAtAll(locks, () -> {
				throw new CustomException();
			});
		} catch (ExecutionException ee) {
			cause = ee.getCause();
		}
		assertNotNull(cause);
		assertTrue(cause instanceof CustomException);
		for (ReentrantLock lock : locks) {
			assertFalse("Use the try/finally to unlock all acquired locks in the finally block.", lock.isLocked());
		}
	}

	private static Thread startAndJoinThread(Runnable body) throws InterruptedException, ExecutionException {
		Thread thread = new Thread(body);
		MutableObject<Throwable> throwableReference = new MutableObject<>();
		thread.setUncaughtExceptionHandler((_thread, _throwable) -> {
			throwableReference.setValue(_throwable);
		});
		thread.start();
		thread.join();
		Throwable throwableValue = throwableReference.getValue();
		if (throwableValue != null) {
			throw new ExecutionException(throwableValue);
		} else {
			return thread;
		}
	}

	private static boolean startAndJoinThreadRunWithAllLocksOrDontRunAtAll(List<ReentrantLock> locks, Runnable body)
			throws InterruptedException, ExecutionException {
		MutableObject<Boolean> isSuccessful = new MutableObject<>();
		Thread thread = new Thread(() -> {
			isSuccessful.setValue(LockUtils.runWithAllLocksOrDontRunAtAll(locks, body));
		});
		startAndJoinThread(thread);
		assertNotNull(isSuccessful.getValue());
		return isSuccessful.getValue().booleanValue();
	}
}
