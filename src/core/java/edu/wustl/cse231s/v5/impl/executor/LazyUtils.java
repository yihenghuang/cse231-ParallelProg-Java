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
package edu.wustl.cse231s.v5.impl.executor;

import java.util.concurrent.ForkJoinWorkerThread;

import edu.wustl.cse231s.v5.V5;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
/* package-private */ class LazyUtils {
	private LazyUtils() {
		throw new Error();
	}

	/* package-private */ static int getPoolSize() {
		return V5.numWorkerThreads();
	}

	/* package-private */ static int getPoolIndex() {
		Thread thread = Thread.currentThread();
		if (thread instanceof ForkJoinWorkerThread) {
			ForkJoinWorkerThread forkJoinWorkerThread = (ForkJoinWorkerThread) thread;
			return forkJoinWorkerThread.getPoolIndex();
		} else {
			throw new RuntimeException(
					"TODO: getPoolIndex() equivalent for thread " + thread + " " + thread.getClass());
		}
	}

}
