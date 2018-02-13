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
package edu.wustl.cse231s.v5.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import edu.wustl.cse231s.v5.api.CheckedCallable;
import edu.wustl.cse231s.v5.api.CheckedConsumer;
import edu.wustl.cse231s.v5.api.CheckedIntConsumer;
import edu.wustl.cse231s.v5.api.CheckedIntIntConsumer;
import edu.wustl.cse231s.v5.api.CheckedRunnable;
import edu.wustl.cse231s.v5.api.Metrics;
import edu.wustl.cse231s.v5.options.AwaitFuturesOption;
import edu.wustl.cse231s.v5.options.ChunkedOption;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public interface V5Impl {
	void launch(CheckedRunnable body) throws InterruptedException, ExecutionException;

	void finish(CheckedRunnable body) throws InterruptedException, ExecutionException;

	void async(CheckedRunnable body);

	<R> Future<R> future(CheckedCallable<R> body);

	void forseq(int min, int maxExclusive, CheckedIntConsumer body) throws InterruptedException, ExecutionException;

	void forasync(int min, int maxExclusive, CheckedIntConsumer body) throws InterruptedException, ExecutionException;

	void forall(int min, int maxExclusive, CheckedIntConsumer body) throws InterruptedException, ExecutionException;

	void forseq(ChunkedOption chunkedOption, int min, int maxExclusive, CheckedIntConsumer body)
			throws InterruptedException, ExecutionException;

	void forasync(ChunkedOption chunkedOption, int min, int maxExclusive, CheckedIntConsumer body)
			throws InterruptedException, ExecutionException;

	void forall(ChunkedOption chunkedOption, int min, int maxExclusive, CheckedIntConsumer body)
			throws InterruptedException, ExecutionException;

	<T> void forseq(T[] array, CheckedConsumer<T> body) throws InterruptedException, ExecutionException;

	<T> void forasync(T[] array, CheckedConsumer<T> body) throws InterruptedException, ExecutionException;

	<T> void forall(T[] array, CheckedConsumer<T> body) throws InterruptedException, ExecutionException;

	<T> void forseq(ChunkedOption chunkedOption, T[] array, CheckedConsumer<T> body)
			throws InterruptedException, ExecutionException;

	<T> void forasync(ChunkedOption chunkedOption, T[] array, CheckedConsumer<T> body)
			throws InterruptedException, ExecutionException;

	<T> void forall(ChunkedOption chunkedOption, T[] array, CheckedConsumer<T> body)
			throws InterruptedException, ExecutionException;

	<T> void forseq(Iterable<T> iterable, CheckedConsumer<T> body) throws InterruptedException, ExecutionException;

	<T> void forasync(Iterable<T> iterable, CheckedConsumer<T> body) throws InterruptedException, ExecutionException;

	<T> void forall(Iterable<T> iterable, CheckedConsumer<T> body) throws InterruptedException, ExecutionException;

	<T> void forseq(ChunkedOption chunkedOption, Iterable<T> iterable, CheckedConsumer<T> body)
			throws InterruptedException, ExecutionException;

	<T> void forasync(ChunkedOption chunkedOption, Iterable<T> iterable, CheckedConsumer<T> body)
			throws InterruptedException, ExecutionException;

	<T> void forall(ChunkedOption chunkedOption, Iterable<T> iterable, CheckedConsumer<T> body)
			throws InterruptedException, ExecutionException;

	void forseq2d(int minA, int maxExclusiveA, int minB, int maxExclusiveB, CheckedIntIntConsumer body)
			throws InterruptedException, ExecutionException;

	void forasync2d(int minA, int maxExclusiveA, int minB, int maxExclusiveB, CheckedIntIntConsumer body)
			throws InterruptedException, ExecutionException;

	void forall2d(int minA, int maxExclusiveA, int minB, int maxExclusiveB, CheckedIntIntConsumer body)
			throws InterruptedException, ExecutionException;

	void forseq2d(ChunkedOption chunkedOption, int minA, int maxExclusiveA, int minB, int maxExclusiveB,
			CheckedIntIntConsumer body) throws InterruptedException, ExecutionException;

	void forasync2d(ChunkedOption chunkedOption, int minA, int maxExclusiveA, int minB, int maxExclusiveB,
			CheckedIntIntConsumer body) throws InterruptedException, ExecutionException;

	void forall2d(ChunkedOption chunkedOption, int minA, int maxExclusiveA, int minB, int maxExclusiveB,
			CheckedIntIntConsumer body) throws InterruptedException, ExecutionException;

	void async(AwaitFuturesOption awaitFuturesOption, CheckedRunnable body);

	<R> Future<R> future(AwaitFuturesOption awaitFuturesOption, CheckedCallable<R> body);

	void doWork(long n);

	Metrics getMetrics();
}
