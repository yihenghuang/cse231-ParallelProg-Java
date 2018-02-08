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
package tnx.lab.executor;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

import edu.wustl.cse231s.executors.BookkeepingExecutorService;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public abstract class AbstractExecutorTest {
	protected abstract void accept(BookkeepingExecutorService executor)
			throws InterruptedException, ExecutionException;

	protected abstract List<Integer> getAcceptableSubmitCounts();

	protected abstract List<Integer> getInvokeAllSizes();

	protected ExecutorService executor() {
		return ForkJoinPool.commonPool();
	}

	protected void shutdownIfAppropriate(ExecutorService executorService) {
	}

	@Test
	public void test() throws InterruptedException, ExecutionException, TimeoutException {
		List<Integer> acceptableSubmitCounts = getAcceptableSubmitCounts();
		List<Integer> invokeAllSizes = getInvokeAllSizes();

		int submitLimit = Collections.max(acceptableSubmitCounts);

		BookkeepingExecutorService executor = new BookkeepingExecutorService.Builder(executor(), submitLimit, invokeAllSizes.size()).build();
		try {
			accept(executor);
		} finally {
			shutdownIfAppropriate(executor);
		}
	}
}
