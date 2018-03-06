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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;

import edu.wustl.cse231s.v5.api.Metrics;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class MetricsExecutorV5Impl extends ExecutorV5Impl {
	private final AtomicLong work = new AtomicLong();

	public MetricsExecutorV5Impl(ExecutorService executorService) {
		super(executorService);
	}

	@Override
	public void doWork(long n) {
		work.addAndGet(n);
	}

	@Override
	public Metrics getMetrics() {
		return new WorkMetrics(work.get());
	}

	private static class WorkMetrics implements Metrics {
		private final long totalWork;

		public WorkMetrics(long totalWork) {
			this.totalWork = totalWork;
		}

		@Override
		public long totalWork() {
			return totalWork;
		}

		@Override
		public long criticalPathLength() {
			throw new UnsupportedOperationException();
		}
	}
}
