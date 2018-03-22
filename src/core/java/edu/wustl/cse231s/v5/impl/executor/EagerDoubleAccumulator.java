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

import java.util.concurrent.atomic.AtomicLong;

import edu.wustl.cse231s.v5.api.NumberReductionOperator;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class EagerDoubleAccumulator extends DoubleAccumulator {
	public EagerDoubleAccumulator(NumberReductionOperator operator) {
		super(operator);
		this.atom = new AtomicLong(Double.doubleToRawLongBits(this.resultVal));
	}

	@Override
	protected void putAccessible(double value) {
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
		double value = Double.longBitsToDouble(this.atom.get());
		switch (this.operator) {
		case SUM:
			this.resultVal += value;
			break;
		case PRODUCT:
			this.resultVal *= value;
			break;
		case MIN:
			if (value < this.resultVal) {
				this.resultVal = value;
			}
			break;
		case MAX:
			if (value > this.resultVal) {
				this.resultVal = value;
			}
			break;
		}

		this.atom.set(Double.doubleToRawLongBits(this.operator.getInitialDoubleValue()));
	}

	private void atomicAdd(double val) {
		if (val == 0.0) {
			return;
		}

		while (true) {
			long expect = this.atom.get();
			double curr = Double.longBitsToDouble(expect);
			double next = curr + val;
			long update = Double.doubleToRawLongBits(next);
			if (this.atom.compareAndSet(expect, update)) {
				return;
			}
		}
	}

	private void atomicMul(double val) {
		if (val == 1.0) {
			return;
		}

		while (true) {
			long expect = this.atom.get();
			double curr = Double.longBitsToDouble(expect);
			double next = curr * val;
			long update = Double.doubleToRawLongBits(next);
			if (this.atom.compareAndSet(expect, update)) {
				return;
			}
		}
	}

	private void atomicMin(double val) {
		while (true) {
			long expect = this.atom.get();
			double curr = Double.longBitsToDouble(expect);
			if (val >= curr) {
				return;
			}
			long update = Double.doubleToRawLongBits(val);
			if (this.atom.compareAndSet(expect, update)) {
				return;
			}
		}
	}

	private void atomicMax(double val) {
		while (true) {
			long expect = this.atom.get();
			double curr = Double.longBitsToDouble(expect);
			if (val <= curr) {
				return;
			}
			long update = Double.doubleToRawLongBits(val);
			if (this.atom.compareAndSet(expect, update)) {
				return;
			}
		}
	}

	private final AtomicLong atom;
}
