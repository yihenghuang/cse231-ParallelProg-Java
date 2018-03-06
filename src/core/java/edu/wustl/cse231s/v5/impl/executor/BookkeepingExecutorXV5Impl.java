/*******************************************************************************
 * Copyright (C) 2016-2018 Dennis Cosgrove
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
 *******************************************************************************/
package edu.wustl.cse231s.v5.impl.executor;

import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import edu.wustl.cse231s.v5.api.CheckedCallable;
import edu.wustl.cse231s.v5.api.CheckedConsumer;
import edu.wustl.cse231s.v5.api.CheckedIntConsumer;
import edu.wustl.cse231s.v5.api.CheckedIntIntConsumer;
import edu.wustl.cse231s.v5.api.CheckedRunnable;
import edu.wustl.cse231s.v5.impl.BookkeepingV5Impl;
import edu.wustl.cse231s.v5.options.ChunkedOption;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class BookkeepingExecutorXV5Impl extends MetricsExecutorV5Impl implements BookkeepingV5Impl {
	private final AtomicInteger launchInvocationCount = new AtomicInteger(0);

	private final AtomicInteger asyncInvocationCount = new AtomicInteger(0);
	private final AtomicInteger finishInvocationCount = new AtomicInteger(0);

	private final AtomicInteger forasyncInvovationCount = new AtomicInteger(0);
	private final AtomicInteger forasyncChunkedInvovationCount = new AtomicInteger(0);
	private final AtomicInteger asyncViaForasyncCount = new AtomicInteger(0);

	private final AtomicInteger forasync2dInvocationCount = new AtomicInteger(0);
	private final AtomicInteger forasync2dChunkedInvocationCount = new AtomicInteger(0);

	private final AtomicInteger futureInvocationCount = new AtomicInteger(0);

	public BookkeepingExecutorXV5Impl(ExecutorService executorService) {
		super(executorService);
	}

	@Override
	public void launch(CheckedRunnable body) throws InterruptedException, ExecutionException {
		super.launch(body);
		launchInvocationCount.incrementAndGet();
	}

	@Override
	public void async(CheckedRunnable body) {
		super.async(body);
		asyncInvocationCount.incrementAndGet();
	}

	@Override
	public void finish(CheckedRunnable body) throws InterruptedException, ExecutionException {
		super.finish(body);
		finishInvocationCount.incrementAndGet();
	}

	@Override
	public <R> Future<R> future(CheckedCallable<R> body) {
		Future<R> result = super.future(body);
		futureInvocationCount.incrementAndGet();
		return result;
	}

	@Override
	public void forasync(int min, int maxExclusive, CheckedIntConsumer body)
			throws InterruptedException, ExecutionException {
		super.forasync(min, maxExclusive, body);
		forasyncInvovationCount.incrementAndGet();
		asyncViaForasyncCount.addAndGet(maxExclusive - min);
	}
	
	@Override
	public <T> void forasync(T[] array, CheckedConsumer<T> body) throws InterruptedException, ExecutionException {
		super.forasync(array, body);
		forasyncInvovationCount.incrementAndGet();
		asyncViaForasyncCount.addAndGet(array.length);
	}
	
	@Override
	public <T> void forasync(Iterable<T> iterable, CheckedConsumer<T> body)
			throws InterruptedException, ExecutionException {
		super.forasync(iterable, body);
		forasyncInvovationCount.incrementAndGet();
		Iterator<T> iterator = iterable.iterator();
		int taskCount = 0;
		while(iterator.hasNext()) {
			iterator.next();
			taskCount++;
		}
		asyncViaForasyncCount.addAndGet(taskCount);
	}

	@Override
	public void forasync(ChunkedOption chunkedOption, int min, int maxExclusive, CheckedIntConsumer body)
			throws InterruptedException, ExecutionException {
		super.forasync(chunkedOption, min, maxExclusive, body);
		forasyncChunkedInvovationCount.incrementAndGet();
		// TODO: asyncViaForasyncCount?
	}

	@Override
	public void forasync2d(int minA, int maxExclusiveA, int minB, int maxExclusiveB, CheckedIntIntConsumer body)
			throws InterruptedException, ExecutionException {
		super.forasync2d(minA, maxExclusiveA, minB, maxExclusiveB, body);
		forasync2dInvocationCount.incrementAndGet();
		asyncViaForasyncCount.addAndGet((maxExclusiveA - minA) * (maxExclusiveB - minB));
	}

	@Override
	public void forasync2d(ChunkedOption chunkedOption, int minA, int maxExclusiveA, int minB, int maxExclusiveB,
			CheckedIntIntConsumer body) throws InterruptedException, ExecutionException {
		super.forasync2d(chunkedOption, minA, maxExclusiveA, minB, maxExclusiveB, body);
		forasync2dChunkedInvocationCount.incrementAndGet();
		// TODO: asyncViaForasyncCount?
	}

	@Override
	public int getLaunchInvocationCount() {
		return launchInvocationCount.get();
	}

	@Override
	public int getAsyncInvocationCount() {
		return asyncInvocationCount.get();
	}

	@Override
	public int getFinishInvocationCount() {
		return finishInvocationCount.get() - launchInvocationCount.get();
	}

	@Override
	public int getForasyncInvocationCount() {
		return forasyncInvovationCount.get();
	}

	@Override
	public int getForasyncChunkedInvovationCount() {
		return forasyncChunkedInvovationCount.get();
	}

	@Override
	public int getForasync2dInvocationCount() {
		return forasync2dInvocationCount.get();
	}

	@Override
	public int getForasync2dChunkedInvocationCount() {
		return forasync2dChunkedInvocationCount.get();
	}

	@Override
	public int getAsyncViaForasyncCount() {
		return asyncViaForasyncCount.get();
	}

	@Override
	public int getTaskCount() {
		// TODO: futureInvocationCount?
		return asyncInvocationCount.get() + asyncViaForasyncCount.get();
	}

	@Override
	public int getFutureInvocationCount() {
		return futureInvocationCount.get();
	}

	@Override
	public void resetAllInvocationCounts() {
		asyncInvocationCount.set(0);
		finishInvocationCount.set(0);
		forasyncInvovationCount.set(0);
		asyncViaForasyncCount.set(0);
		futureInvocationCount.set(0);
	}
}
