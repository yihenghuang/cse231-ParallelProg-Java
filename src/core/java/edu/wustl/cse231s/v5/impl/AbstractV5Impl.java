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

import edu.wustl.cse231s.v5.api.CheckedConsumer;
import edu.wustl.cse231s.v5.api.CheckedIntConsumer;
import edu.wustl.cse231s.v5.api.CheckedIntIntConsumer;
import edu.wustl.cse231s.v5.api.CheckedRunnable;
import edu.wustl.cse231s.v5.options.AwaitFuturesOption;
import edu.wustl.cse231s.v5.options.ChunkedOption;
import edu.wustl.cse231s.v5.options.PhasedEmptyOption;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public abstract class AbstractV5Impl implements V5Impl {
	@Override
	public void launch(CheckedRunnable body) throws InterruptedException, ExecutionException {
		finish(body);
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
		for (int i = min; i < maxExclusive; i++) {
			final int _i = i;
			async(() -> {
				body.accept(_i);
			});
		}
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
		throw new RuntimeException();
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
		for (T item : array) {
			final T _item = item;
			async(() -> {
				body.accept(_item);
			});
		}
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
	public <T> void forasync(ChunkedOption chunkedOption, T[] array, CheckedConsumer<T> body)
			throws InterruptedException, ExecutionException {
		throw new RuntimeException();
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

	@Override
	public <T> void forasync(Iterable<T> iterable, CheckedConsumer<T> body)
			throws InterruptedException, ExecutionException {
		for (T item : iterable) {
			final T _item = item;
			async(() -> {
				body.accept(_item);
			});
		}
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
		throw new RuntimeException();
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
		for (int i = minA; i < maxExclusiveB; i++) {
			final int _i = i;
			for (int j = minB; j < maxExclusiveB; j++) {
				final int _j = j;
				async(() -> {
					body.accept(_i, _j);
				});
			}
		}
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
		throw new RuntimeException();
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
		future(awaitFuturesOption, ()->{
			body.run();
			return null;
		});
	}
	
	@Override
	public void forseq(PhasedEmptyOption phasedEmptyOption, int min, int maxExclusive, CheckedIntConsumer body)
			throws InterruptedException, ExecutionException {
		forseq(min, maxExclusive, body);
	}

	@Override
	public void forall(PhasedEmptyOption phasedEmptyOption, int min, int maxExclusive, CheckedIntConsumer body)
			throws InterruptedException, ExecutionException {
		finish(() -> {
			forasync(phasedEmptyOption, min, maxExclusive, body);
		});
	}
}
