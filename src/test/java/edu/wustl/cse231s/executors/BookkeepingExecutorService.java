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
package edu.wustl.cse231s.executors;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import net.jcip.annotations.GuardedBy;

import edu.wustl.cse231s.sleep.SleepUtils;
import net.jcip.annotations.ThreadSafe;

/**
 * @author Finn Voichick
 */
@ThreadSafe
public final class BookkeepingExecutorService implements ExecutorService {

	private final ExecutorService wrappedExecutor;
	private final long preSleep;
	private final TimeUnit preUnit;
	private final long postSleep;
	private final TimeUnit postUnit;
	private final int submitLimit;
	private final int invokeAllLimit;

	private final ReadWriteLock countLock = new ReentrantReadWriteLock();
	@GuardedBy("countLock")
	private int submitCount = 0;
	@GuardedBy("countLock")
	private int invokeAllCount = 0;
	@GuardedBy("countLock")
	private int notYetJoinedTaskCount = 0;

	public static class Builder {

		private final ExecutorService wrappedExecutor;
		private final int submitLimit;
		private final int invokeAllLimit;
		private long preSleep;
		private TimeUnit preUnit;
		private long postSleep;
		private TimeUnit postUnit;

		public Builder(ExecutorService wrappedExecutor, int submitLimit, int invokeAllLimit) {
			this.wrappedExecutor = wrappedExecutor;
			this.submitLimit = submitLimit;
			this.invokeAllLimit = invokeAllLimit;
		}

		public Builder preSleep(long duration, TimeUnit unit) {
			preSleep = duration;
			preUnit = unit;
			return this;
		}

		public Builder postSleep(long duration, TimeUnit unit) {
			postSleep = duration;
			postUnit = unit;
			return this;
		}

		public BookkeepingExecutorService build() {
			return new BookkeepingExecutorService(this);
		}

	}

	private BookkeepingExecutorService(Builder builder) {
		wrappedExecutor = builder.wrappedExecutor;
		preSleep = builder.preSleep;
		preUnit = builder.preUnit;
		postSleep = builder.postSleep;
		postUnit = builder.postUnit;
		submitLimit = builder.submitLimit;
		invokeAllLimit = builder.invokeAllLimit;
	}

	public int getSubmitCount() {
		countLock.readLock().lock();
		try {
			return submitCount;
		} finally {
			countLock.readLock().unlock();
		}
	}

	public int getInvokeAllCount() {
		countLock.readLock().lock();
		try {
			return invokeAllCount;
		} finally {
			countLock.readLock().unlock();
		}
	}

	public int getNotYetJoinedTaskCount() {
		countLock.readLock().lock();
		try {
			return notYetJoinedTaskCount;
		} finally {
			countLock.readLock().unlock();
		}
	}

	@Override
	public void execute(Runnable command) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void shutdown() {
		wrappedExecutor.shutdown();
	}

	@Override
	public List<Runnable> shutdownNow() {
		return wrappedExecutor.shutdownNow();
	}

	@Override
	public boolean isShutdown() {
		return wrappedExecutor.isShutdown();
	}

	@Override
	public boolean isTerminated() {
		return wrappedExecutor.isTerminated();
	}

	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		return wrappedExecutor.awaitTermination(timeout, unit);
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		Objects.requireNonNull(task, "task cannot be null");
		updateCountsForSubmit();
		return wrappedExecutor.submit(adapt(task));
	}

	@Override
	public <T> Future<T> submit(Runnable task, T result) {
		Objects.requireNonNull(task, "task cannot be null");
		updateCountsForSubmit();
		return wrappedExecutor.submit(adapt(task), result);
	}

	@Override
	public Future<?> submit(Runnable task) {
		Objects.requireNonNull(task, "task cannot be null");
		updateCountsForSubmit();
		return wrappedExecutor.submit(adapt(task));
	}

	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
		Objects.requireNonNull(tasks, "tasks cannot be null");
		for (Callable<T> task : tasks) {
			Objects.requireNonNull(task);
		}
		updateCountsForInvokeAll(tasks.size());
		return wrappedExecutor.invokeAll(adapt(tasks));
	}

	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
			throws InterruptedException {
		Objects.requireNonNull(tasks, "tasks cannot be null");
		for (Callable<T> task : tasks) {
			Objects.requireNonNull(task);
		}
		updateCountsForInvokeAll(tasks.size());
		return wrappedExecutor.invokeAll(adapt(tasks), timeout, unit);
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		throw new UnsupportedOperationException();
	}

	private Runnable adapt(Runnable task) {
		return () -> {
			if (preSleep > 0)
				SleepUtils.sleep(preSleep, preUnit);
			task.run();
			if (postSleep > 0)
				SleepUtils.sleep(postSleep, postUnit);
			decrementNotYetJoinedTaskCount();
		};
	}

	private <T> Callable<T> adapt(Callable<T> task) {
		return () -> {
			if (preSleep > 0)
				SleepUtils.sleep(preSleep, preUnit);
			T result = task.call();
			if (postSleep > 0)
				SleepUtils.sleep(postSleep, postUnit);
			decrementNotYetJoinedTaskCount();
			return result;
		};
	}

	private <T> Collection<Callable<T>> adapt(Collection<? extends Callable<T>> tasks) {
		Collection<Callable<T>> result = new LinkedList<>();
		for (Callable<T> task : tasks)
			result.add(adapt(task));
		return result;
	}

	private void updateCountsForInvokeAll(int numTasks) {
		countLock.writeLock().lock();
		try {
			if (invokeAllCount + 1 > invokeAllLimit) {
				throw new RejectedExecutionException("invokeAll limit reached: " + this.invokeAllLimit);
			} else {
				invokeAllCount++;
				notYetJoinedTaskCount+=numTasks;
			}
		} finally {
			countLock.writeLock().unlock();
		}
	}

	private void updateCountsForSubmit() {
		countLock.writeLock().lock();
		try {
			if (submitCount + 1 > submitLimit) {
				throw new RejectedExecutionException("submit limit reached: " + this.submitLimit);
			} else {
				submitCount++;
				notYetJoinedTaskCount++;
			}
		} finally {
			countLock.writeLock().unlock();
		}
	}

	private void decrementNotYetJoinedTaskCount() {
		countLock.writeLock().lock();
		try {
			notYetJoinedTaskCount--;
		} finally {
			countLock.writeLock().unlock();
		}
	}

}
