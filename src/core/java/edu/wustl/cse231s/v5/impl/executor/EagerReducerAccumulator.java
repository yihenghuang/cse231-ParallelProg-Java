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

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import edu.wustl.cse231s.v5.api.AccumulatorReducer;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
// Based on "Finish Accumulators: a Deterministic Reduction Construct for Dynamic Task Parallelism"
// Jun Shirako, Vincent Cave', Jisheng Zhao, and Vivek Sarkar
// https://wiki.rice.edu/confluence/download/attachments/4425835/wodet2013-shirako-sarkar.pdf
public class EagerReducerAccumulator<T> extends ReducerAccumulator<T> {
	public EagerReducerAccumulator(AccumulatorReducer<T> reducer) {
		super(reducer);
		this.transitionState = reducer.identity();
	}

	@Override
	protected void putAccessible(T value) {
		lock.lock();
		try {
			this.transitionState = this.reducer.reduce(this.transitionState, value);
		} finally {
			lock.unlock();
		}
	}

	@Override
	protected void calculateAccum() {
		this.resultVal = this.reducer.reduce(this.resultVal, this.transitionState);
		this.transitionState = this.reducer.identity();
	}

	//TODO: investigate.
	private final Lock lock = new ReentrantLock();

	//TODO: rename?
	private T transitionState;
}
