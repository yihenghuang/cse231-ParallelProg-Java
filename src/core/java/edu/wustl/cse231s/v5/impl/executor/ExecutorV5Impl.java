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

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Spliterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

import edu.wustl.cse231s.v5.api.CheckedCallable;
import edu.wustl.cse231s.v5.api.CheckedConsumer;
import edu.wustl.cse231s.v5.api.CheckedRunnable;
import edu.wustl.cse231s.v5.api.Metrics;
import edu.wustl.cse231s.v5.impl.AbstractV5Impl;
import edu.wustl.cse231s.v5.options.AwaitFuturesOption;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class ExecutorV5Impl extends AbstractV5Impl {
	private final ExecutorService executorService;

	public ExecutorV5Impl(ExecutorService executorService) {
		this.executorService = executorService;
	}

	@Override
	public void finish(CheckedRunnable body) throws InterruptedException, ExecutionException {
		Deque<FinishContext> stack = contextStack.get();
		FinishContext context = new FinishContext();
		stack.push(context);
		try {
			body.run();
			context.joinAllTasks();
		} finally {
			stack.pop();
		}
	}

	private void asyncRunnable(FinishContext context, CheckedRunnable body) {
		Future<Void> task = executorService.submit(() -> {
			Deque<FinishContext> stack = contextStack.get();
			stack.push(context);
			try {
				body.run();
			} finally {
				stack.pop();
			}
			return null;
		});
		context.offerTask(task);
	}

	private void asyncRangeOfTasks(FinishContext context, CheckedRunnable[] tasks, int min, int maxExclusive)
			throws InterruptedException, ExecutionException {
		int rangeLength = maxExclusive - min;
		if (rangeLength > 1) {
			int mid = (maxExclusive + min) / 2;
			Future<Void> future = executorService.submit(() -> {
				Deque<FinishContext> stack = contextStack.get();
				stack.push(context);
				try {
					asyncRangeOfTasks(context, tasks, min, mid);
				} finally {
					stack.pop();
				}
				return null;
			});
			context.offerTask(future);
			asyncRangeOfTasks(context, tasks, mid, maxExclusive);
		} else {
			tasks[min].run();
		}
	}

	@Override
	protected void asyncTasks(CheckedRunnable[] tasks) throws InterruptedException, ExecutionException {
		Objects.requireNonNull(tasks);
		for (CheckedRunnable task : tasks) {
			Objects.requireNonNull(task);
		}
		FinishContext context = contextStack.get().peek();
		asyncRangeOfTasks(context, tasks, 0, tasks.length);
	}

	private static class ChainedException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		ChainedException(InterruptedException ie) {
			super(ie);
		}

		ChainedException(ExecutionException ee) {
			super(ee);
		}
	}

	private static <T> void forEachRemaining(Spliterator<T> spliterator, CheckedConsumer<T> body)
			throws InterruptedException, ExecutionException {
		try {
			spliterator.forEachRemaining((v) -> {
				try {
					body.accept(v);
				} catch (InterruptedException ie) {
					throw new ChainedException(ie);
				} catch (ExecutionException ee) {
					throw new ChainedException(ee);
				}
			});
		} catch (ChainedException ce) {
			Throwable cause = ce.getCause();
			if (cause instanceof InterruptedException) {
				InterruptedException ie = (InterruptedException) cause;
				throw ie;
			} else {
				ExecutionException ee = (ExecutionException) cause;
				throw ee;
			}
		}
	}

	@Override
	protected <T> void split(Spliterator<T> spliterator, long threshold, CheckedConsumer<T> body)
			throws InterruptedException, ExecutionException {
		if (threshold > 1L) {
			if (spliterator.estimateSize() <= threshold) {
				forEachRemaining(spliterator, body);
				return;
			}
		}
		Spliterator<T> s = spliterator.trySplit();
		if (s != null) {
			FinishContext context = contextStack.get().peek();
			Future<Void> task = executorService.submit(() -> {
				Deque<FinishContext> stack = contextStack.get();
				stack.push(context);
				try {
					split(spliterator, threshold, body);
				} finally {
					stack.pop();
				}
				return null;
			});
			context.offerTask(task);
			split(s, threshold, body);
		} else {
			if (spliterator.getExactSizeIfKnown() == 1L) {
				forEachRemaining(spliterator, body);
			} else {
				// TODO: handle threshold > 1L
				FinishContext context = contextStack.get().peek();
				spliterator.forEachRemaining((v) -> {
					Future<Void> task = executorService.submit(() -> {
						Deque<FinishContext> stack = contextStack.get();
						stack.push(context);
						try {
							body.accept(v);
						} finally {
							stack.pop();
						}
						return null;
					});
					context.offerTask(task);
				});
			}
		}
	}

	@Override
	public void async(CheckedRunnable body) {
		Objects.requireNonNull(body);
		FinishContext context = contextStack.get().peek();
		asyncRunnable(context, body);
	}

	@Override
	public <R> Future<R> future(CheckedCallable<R> body) {
		FinishContext context = contextStack.get().peek();
		Future<R> task = executorService.submit(() -> {
			Deque<FinishContext> stack = contextStack.get();
			stack.push(context);
			try {
				return body.call();
			} finally {
				stack.pop();
			}
		});
		context.offerTask(task);
		return task;
	}

	@Override
	public <R> Future<R> future(AwaitFuturesOption awaitFuturesOption, CheckedCallable<R> body) {
		List<Future<?>> futures = awaitFuturesOption.getFutures();
		CompletableFuture<?>[] completableFutures = new CompletableFuture[futures.size()];
		int i = 0;
		for (Future<?> future : futures) {
			if (future instanceof CompletableFuture<?>) {
				completableFutures[i] = (CompletableFuture<?>) future;
			} else {
				completableFutures[i] = CompletableFuture.supplyAsync(() -> {
					try {
						return future.get();
					} catch (InterruptedException | ExecutionException e) {
						throw new RuntimeException(e);
					}
				});
			}
			i++;
		}
		FinishContext context = contextStack.get().peek();
		CompletableFuture<R> task = CompletableFuture.allOf(completableFutures).thenApplyAsync((a) -> {
			try {
				return body.call();
			} catch (InterruptedException | ExecutionException e) {
				throw new RuntimeException(e);
			}
		});
		context.offerTask(task);
		return task;
	}

	@Override
	public void doWork(long n) {
	}

	@Override
	public Metrics getMetrics() {
		return new Metrics() {
			@Override
			public long totalWork() {
				throw new UnsupportedOperationException();
			}

			@Override
			public long criticalPathLength() {
				throw new UnsupportedOperationException();
			}
		};
	}

	private static final ThreadLocal<Deque<FinishContext>> contextStack = ThreadLocal.withInitial(() -> {
		return new LinkedList<>();
	});

	private static class FinishContext {
		private final Queue<Future<?>> tasks = new ConcurrentLinkedQueue<>();

		void joinAllTasks() throws InterruptedException, ExecutionException {
			while (true) {
				if (tasks.isEmpty()) {
					break;
				} else {
					tasks.poll().get();
				}
			}
		}

		void offerTask(Future<?> task) {
			tasks.offer(task);
		}
	}
}
