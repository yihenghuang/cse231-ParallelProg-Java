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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import edu.wustl.cse231s.v5.api.CheckedCallable;
import edu.wustl.cse231s.v5.api.CheckedRunnable;
import edu.wustl.cse231s.v5.impl.BookkeepingV5Impl;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class BookkeepingExecutorXV5Impl extends MetricsExecutorV5Impl implements BookkeepingV5Impl {
	private final AtomicInteger launchInvocationCount = new AtomicInteger(0);

	private final AtomicInteger asyncInvocationCount = new AtomicInteger(0);
	private final AtomicInteger finishInvocationCount = new AtomicInteger(0);
	private final AtomicInteger forasyncRegionInvovationCount = new AtomicInteger(0);
	private final AtomicInteger asyncViaForasyncRegionCount = new AtomicInteger(0);
	private final AtomicInteger asyncAvoidedInContinuationCount = new AtomicInteger(0);

	private final AtomicInteger forasync2dRegionInvocationCount = new AtomicInteger(0);
	private final AtomicInteger forasync2dRegionChunkedInvocationCount = new AtomicInteger(0);

	private final AtomicInteger futureInvocationCount = new AtomicInteger(0);

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
	
	// @Override
	// public void forasync(HjRegion1D hjRegion, HjSuspendingProcedureInt1D
	// body) throws SuspendableException {
	// super.forasync(hjRegion, body);
	// forasyncRegionInvovationCount.incrementAndGet();
	// asyncViaForasyncRegionCount.addAndGet(hjRegion.numElements());
	// }
	//
	// @Override
	// protected <T> void forasync(Iterator<T> iterator,
	// HjSuspendingProcedure<T> body) throws SuspendableException {
	// if(iterator.hasNext()) {
	// asyncAvoidedInContinuationCount.incrementAndGet();
	// }
	// super.forasync(iterator, body);
	// }
	//
	// @Override
	// public <T> void forasync(T[] array, HjSuspendingProcedure<T> body) throws
	// SuspendableException {
	// if( array.length > 0 ) {
	// asyncAvoidedInContinuationCount.incrementAndGet();
	// }
	// super.forasync(array, body);
	// }
	//
	// @Override
	// public void forasync2d(HjRegion2D hjRegion, HjSuspendingProcedureInt2D
	// body) {
	// super.forasync2d(hjRegion, body);
	// forasync2dRegionInvocationCount.incrementAndGet();
	// }
	//
	// @Override
	// public void forasync2d(ChunkedOption chunkedOption, HjRegion2D hjRegion,
	// HjSuspendingProcedureInt2D body)
	// throws SuspendableException {
	// super.forasync2d(chunkedOption, hjRegion, body);
	// forasync2dRegionChunkedInvocationCount.incrementAndGet();
	// }

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
	public int getForasyncRegionInvocationCount() {
		return forasyncRegionInvovationCount.get();
	}

	@Override
	public int getForasync2dRegionInvocationCount() {
		return forasync2dRegionInvocationCount.get();
	}

	@Override
	public int getForasync2dRegionChunkedInvocationCount() {
		return forasync2dRegionChunkedInvocationCount.get();
	}

	@Override
	public int getAsyncViaForasyncRegionCount() {
		return asyncViaForasyncRegionCount.get();
	}

	@Override
	public int getTaskCount() {
		//TODO: futureInvocationCount?
		return asyncInvocationCount.get() + asyncViaForasyncRegionCount.get() + asyncAvoidedInContinuationCount.get();
	}

	@Override
	public int getFutureInvocationCount() {
		return futureInvocationCount.get();
	}
	
	@Override
	public void resetAllInvocationCounts() {
		asyncInvocationCount.set(0);
		finishInvocationCount.set(0);
		forasyncRegionInvovationCount.set(0);
		asyncViaForasyncRegionCount.set(0);
		futureInvocationCount.set(0);
	}
}
