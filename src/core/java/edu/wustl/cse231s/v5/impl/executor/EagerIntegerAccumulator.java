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

import java.util.concurrent.atomic.AtomicInteger;

import edu.wustl.cse231s.v5.api.NumberReductionOperator;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
// Based on the implementation by Jun Shirako (shirako@rice.edu) and Vivek Sarkar (vsarkar@rice.edu)
public class EagerIntegerAccumulator extends IntegerAccumulator {
	public EagerIntegerAccumulator(NumberReductionOperator operator) {
		super(operator);
		this.atom = new AtomicInteger(this.resultVal);
	}

	@Override
	protected void putAccessible(int value) {
		switch (this.operator) {
		case SUM:
			this.atomicAdd(value);
			break;
		case PRODUCT:
			this.atomicMul(value);
			break;
		case MIN:
			this.atomicMin(value);
			break;
		case MAX:
			this.atomicMax(value);
			break;
		}
	}

	@Override
	protected void calculateAccum() {
		int atomValue = this.atom.get();
		switch (this.operator) {
		case SUM:
			this.resultVal += atomValue;
			break;
		case PRODUCT:
			this.resultVal *= atomValue;
			break;
		case MIN:
			if (atomValue < this.resultVal) {
				this.resultVal = atomValue;
			}
			break;
		case MAX:
			if (atomValue > this.resultVal) {
				this.resultVal = atomValue;
			}
			break;
		}

		this.atom.set(this.operator.getInitialIntValue());
	}

	private void atomicAdd(int val) {
		if (val == 0) {
			//pass
		} else {
			this.atom.addAndGet(val);
		}
	}

	private void atomicMul(int val) {
		if (val == 1) {
			return;
		} else {
			while (true) {
				int expect = this.atom.get();
				int update = expect * val;
				if (this.atom.compareAndSet(expect, update)) {
					return;
				}
			}
		}
	}

	private void atomicMin(int val) {
		while (true) {
			int expect = this.atom.get();
			if (val >= expect) {
				return;
			}

			if (this.atom.compareAndSet(expect, val)) {
				return;
			}
		}
	}

	private void atomicMax(int val) {
		while (true) {
			int expect = this.atom.get();
			if (val <= expect) {
				return;
			}

			if (this.atom.compareAndSet(expect, val)) {
				return;
			}
		}
	}

	private final AtomicInteger atom;
}
