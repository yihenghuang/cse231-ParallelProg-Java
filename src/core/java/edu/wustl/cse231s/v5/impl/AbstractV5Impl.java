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

import java.util.Spliterator;
import java.util.concurrent.ExecutionException;

import edu.wustl.cse231s.v5.api.CheckedConsumer;
import edu.wustl.cse231s.v5.api.CheckedIntConsumer;
import edu.wustl.cse231s.v5.api.CheckedIntIntConsumer;
import edu.wustl.cse231s.v5.api.CheckedRunnable;
import edu.wustl.cse231s.v5.options.AwaitFuturesOption;
import edu.wustl.cse231s.v5.options.ChunkedOption;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public abstract class AbstractV5Impl implements V5Impl {
	@Override
	public void launch(CheckedRunnable body) throws InterruptedException, ExecutionException {
		finish(body);
	}

	protected abstract void asyncTasks(CheckedRunnable[] tasks) throws InterruptedException, ExecutionException;

	private void forasyncInt(int min, int maxExclusive, CheckedIntConsumer body)
			throws InterruptedException, ExecutionException {
		int rangeLength = maxExclusive - min;
		CheckedRunnable[] tasks = new CheckedRunnable[rangeLength];
		for (int i = min, taskIndex = 0; i < maxExclusive; i++, taskIndex++) {
			final int _i = i;
			tasks[taskIndex] = () -> {
				body.accept(_i);
			};
		}
		asyncTasks(tasks);
	}

	private void forasyncIntInt(int minA, int maxExclusiveA, int minB, int maxExclusiveB, CheckedIntIntConsumer body)
			throws InterruptedException, ExecutionException {
		int rangeLengthA = maxExclusiveA - minA;
		int rangeLengthB = maxExclusiveB - minB;
		CheckedRunnable[] tasks = new CheckedRunnable[rangeLengthA * rangeLengthB];
		int taskIndex = 0;
		for (int a = minA; a < maxExclusiveA; a++) {
			final int _a = a;
			for (int b = minB; b < maxExclusiveB; b++) {
				final int _b = b;
				tasks[taskIndex] = () -> {
					body.accept(_a, _b);
				};
				taskIndex++;
			}
		}
		asyncTasks(tasks);
	}

	private void forasyncInt(ChunkedOption chunkedOption, int min, int maxExclusive, CheckedIntConsumer body)
			throws InterruptedException, ExecutionException {
		int rangeLength = maxExclusive - min;
		int numTasks;
		int chunkSize;
		int remainder;
		if (chunkedOption.isSizeDecidedBySystem()) {
			numTasks = numWorkerThreads() * 2;
			chunkSize = rangeLength / numTasks;
			remainder = rangeLength % numTasks;
			if (chunkSize == 0) {
				chunkSize = 1;
				numTasks = remainder;
				remainder = 0;
			}
		} else {
			chunkSize = chunkedOption.getSize();
			if (chunkSize > rangeLength) {
				numTasks = rangeLength / chunkSize;
				remainder = rangeLength % numTasks;
			} else {
				numTasks = 1;
				chunkSize = rangeLength;
				remainder = 0;
			}
		}

		CheckedRunnable[] tasks = new CheckedRunnable[numTasks];
		int taskMin = min;
		for (int taskIndex = 0; taskIndex < numTasks; taskIndex++) {
			int taskMax = taskMin + chunkSize;
			if (taskIndex < remainder) {
				taskMax++;
			}
			final int _taskMin = taskMin;
			final int _taskMax = taskMax;
			tasks[taskIndex] = () -> {
				for (int i = _taskMin; i < _taskMax; i++) {
					body.accept(i);
				}
			};
			taskMin = taskMax;
		}

		asyncTasks(tasks);
	}

	@Override
	public void forseq(int min, int maxExclusive, CheckedIntConsumer body)
			throws InterruptedException, ExecutionException {
		for (int i = min; i < maxExclusive; i++) {
			body.accept(i);
		}
	}

	@Override
	public void forasync(int min, int maxExclusive, CheckedIntConsumer body)
			throws InterruptedException, ExecutionException {
		forasyncInt(min, maxExclusive, body);
	}

	@Override
	public void forall(int min, int maxExclusive, CheckedIntConsumer body)
			throws InterruptedException, ExecutionException {
		finish(() -> {
			forasync(min, maxExclusive, body);
		});
	}

	@Override
	public void forseq(ChunkedOption chunkedOption, int min, int maxExclusive, CheckedIntConsumer body)
			throws InterruptedException, ExecutionException {
		forseq(min, maxExclusive, body);
	}

	@Override
	public void forasync(ChunkedOption chunkedOption, int min, int maxExclusive, CheckedIntConsumer body)
			throws InterruptedException, ExecutionException {
		forasyncInt(chunkedOption, min, maxExclusive, body);
	}

	@Override
	public <T> void forasync(ChunkedOption chunkedOption, T[] array, CheckedConsumer<T> body)
			throws InterruptedException, ExecutionException {
		forasyncInt(chunkedOption, 0, array.length, (i) -> {
			body.accept(array[i]);
		});
	}

	@Override
	public void forall(ChunkedOption chunkedOption, int min, int maxExclusive, CheckedIntConsumer body)
			throws InterruptedException, ExecutionException {
		finish(() -> {
			forasync(chunkedOption, min, maxExclusive, body);
		});
	}

	@Override
	public <T> void forseq(T[] array, CheckedConsumer<T> body) throws InterruptedException, ExecutionException {
		for (T item : array) {
			body.accept(item);
		}
	}

	@Override
	public <T> void forasync(T[] array, CheckedConsumer<T> body) throws InterruptedException, ExecutionException {
		forasyncInt(0, array.length, (i) -> {
			body.accept(array[i]);
		});
	}

	@Override
	public <T> void forall(T[] array, CheckedConsumer<T> body) throws InterruptedException, ExecutionException {
		finish(() -> {
			forasync(array, body);
		});
	}

	@Override
	public <T> void forseq(ChunkedOption chunkedOption, T[] array, CheckedConsumer<T> body)
			throws InterruptedException, ExecutionException {
		forseq(array, body);
	}

	@Override
	public <T> void forall(ChunkedOption chunkedOption, T[] array, CheckedConsumer<T> body)
			throws InterruptedException, ExecutionException {
		finish(() -> {
			forasync(chunkedOption, array, body);
		});
	}

	@Override
	public <T> void forseq(Iterable<T> iterable, CheckedConsumer<T> body)
			throws InterruptedException, ExecutionException {
		for (T item : iterable) {
			body.accept(item);
		}
	}

	protected abstract <T> void split(Spliterator<T> spliterator, long threshold, CheckedConsumer<T> body)
			throws InterruptedException, ExecutionException;

	@Override
	public <T> void forasync(Iterable<T> iterable, CheckedConsumer<T> body)
			throws InterruptedException, ExecutionException {
		Spliterator<T> spliterator = iterable.spliterator();
		split(spliterator, 1L, body);
	}

	@Override
	public <T> void forall(Iterable<T> iterable, CheckedConsumer<T> body)
			throws InterruptedException, ExecutionException {
		finish(() -> {
			forasync(iterable, body);
		});
	}

	@Override
	public <T> void forseq(ChunkedOption chunkedOption, Iterable<T> iterable, CheckedConsumer<T> body)
			throws InterruptedException, ExecutionException {
		forseq(iterable, body);
	}

	@Override
	public <T> void forasync(ChunkedOption chunkedOption, Iterable<T> iterable, CheckedConsumer<T> body)
			throws InterruptedException, ExecutionException {
		Spliterator<T> spliterator = iterable.spliterator();
		long chunkSize;
		if (chunkedOption.isSizeDecidedBySystem()) {
			int numTasks = numWorkerThreads() * 2;
			long size = spliterator.estimateSize();
			// TODO: handle cases
			chunkSize = size / numTasks;
		} else {
			chunkSize = chunkedOption.getSize();
		}
		chunkSize = Math.max(chunkSize, 1L);
		split(spliterator, chunkSize, body);
	}

	@Override
	public <T> void forall(ChunkedOption chunkedOption, Iterable<T> iterable, CheckedConsumer<T> body)
			throws InterruptedException, ExecutionException {
		finish(() -> {
			forasync(chunkedOption, iterable, body);
		});
	}

	@Override
	public void forseq2d(int minA, int maxExclusiveA, int minB, int maxExclusiveB, CheckedIntIntConsumer body)
			throws InterruptedException, ExecutionException {
		for (int i = minA; i < maxExclusiveB; i++) {
			for (int j = minB; j < maxExclusiveB; j++) {
				body.accept(i, j);
			}
		}
	}

	@Override
	public void forasync2d(int minA, int maxExclusiveA, int minB, int maxExclusiveB, CheckedIntIntConsumer body)
			throws InterruptedException, ExecutionException {
		forasyncIntInt(minA, maxExclusiveA, minB, maxExclusiveB, body);
	}

	@Override
	public void forall2d(int minA, int maxExclusiveA, int minB, int maxExclusiveB, CheckedIntIntConsumer body)
			throws InterruptedException, ExecutionException {
		finish(() -> {
			forasync2d(minA, maxExclusiveA, minB, maxExclusiveB, body);
		});
	}

	@Override
	public void forseq2d(ChunkedOption chunkedOption, int minA, int maxExclusiveA, int minB, int maxExclusiveB,
			CheckedIntIntConsumer body) throws InterruptedException, ExecutionException {
		forseq2d(minA, maxExclusiveA, minB, maxExclusiveB, body);
	}

	@Override
	public void forasync2d(ChunkedOption chunkedOption, int minA, int maxExclusiveA, int minB, int maxExclusiveB,
			CheckedIntIntConsumer body) throws InterruptedException, ExecutionException {
		// TODO
		forasyncInt(chunkedOption, minA, maxExclusiveA, (a) -> {
			for (int b = minB; b < maxExclusiveB; b++) {
				body.accept(a, b);
			}
		});
	}

	@Override
	public void forall2d(ChunkedOption chunkedOption, int minA, int maxExclusiveA, int minB, int maxExclusiveB,
			CheckedIntIntConsumer body) throws InterruptedException, ExecutionException {
		finish(() -> {
			forasync2d(chunkedOption, minA, maxExclusiveA, minB, maxExclusiveB, body);
		});
	}

	@Override
	public void async(AwaitFuturesOption awaitFuturesOption, CheckedRunnable body) {
		future(awaitFuturesOption, () -> {
			body.run();
			return null;
		});
	}
}
