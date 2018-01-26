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
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

import edu.wustl.cse231s.v5.api.CheckedCallable;
import edu.wustl.cse231s.v5.api.CheckedIntConsumer;
import edu.wustl.cse231s.v5.api.CheckedRunnable;
import edu.wustl.cse231s.v5.impl.AbstractV5Impl;
import edu.wustl.cse231s.v5.options.AwaitFuturesOption;
import edu.wustl.cse231s.v5.options.PhasedEmptyOption;

public class ExecutorV5Impl extends AbstractV5Impl {
	private static ExecutorService executorService = ForkJoinPool.commonPool();
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

	@Override
	public void async(CheckedRunnable body) {
		FinishContext context = contextStack.get().peek();
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
	public void forasync(PhasedEmptyOption phasedEmptyOption, int min, int maxExclusive, CheckedIntConsumer body)
			throws InterruptedException, ExecutionException {
		throw new RuntimeException("todo");
	}
	
	@Override
	public void doWork(long n) {
	}
}
