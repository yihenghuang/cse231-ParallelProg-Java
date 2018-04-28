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

import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.locks.ReentrantLock;

import edu.wustl.cse231s.concurrent.Interruptible;

/**
 * @author Yiheng Huang
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class LockUtils {
	/**
	 * Attempts to lock all of the specified locks. If any of the locks fail to be
	 * acquired this method must unlock all of the previous locks this invocation
	 * acquired.
	 */
	public static boolean tryLockAll(List<ReentrantLock> locks) {
		boolean lockAll = true;
		ListIterator<ReentrantLock> it = locks.listIterator();
		while (it.hasNext()) {
			if (!it.next().tryLock()) {
				lockAll = false;
				break;
			}
		}
		if (lockAll != true) {
			it.previous();
			while (it.hasPrevious()) {
				it.previous().unlock();
			}
		}
		return lockAll;
	}

	/**
	 * Unlocks all of the specified locks.
	 */
	public static void unlockAllHeldLocks(List<ReentrantLock> locks) {
		for (ReentrantLock each : locks) {
			each.unlock();
		}
	}

	/**
	 * Runs the specified action if and only if all of the specified locks can be
	 * acquired.
	 * 
	 * @return whether or not successful
	 */
	public static boolean runWithAllLocksOrDontRunAtAll(List<ReentrantLock> locks, Runnable body) {
		boolean status = tryLockAll(locks);
		if (status) {
			try {
				body.run();
			} finally {
				unlockAllHeldLocks(locks);
			}
		}
		return status;
	}

	/**
	 * Repeatedly attempts to run the specified body of code until it succeeds with
	 * the specified failure to acquire all locks action.
	 */
	public static void runWithAllLocks(List<ReentrantLock> locks, Runnable body, Interruptible failureAction)
			throws InterruptedException {
		while (true) {
			if (runWithAllLocksOrDontRunAtAll(locks, body)) {
				break;
			} else {
				failureAction.run();
			}
		}
	}

	/**
	 * Repeatedly attempts to run the specified body of code until it succeeds with
	 * default failure to acquire all locks action.
	 */
	public static void runWithAllLocks(List<ReentrantLock> locks, Runnable body) throws InterruptedException {
		runWithAllLocks(locks, body, () -> {
			if (Thread.currentThread().isInterrupted()) {
				throw new InterruptedException();
			}
			Thread.yield();
		});
	}
}
