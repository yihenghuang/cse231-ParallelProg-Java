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

import edu.wustl.cse231s.v5.api.NumberReductionOperator;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
// Based on the implementation by Jun Shirako (shirako@rice.edu) and Vivek Sarkar (vsarkar@rice.edu)
public abstract class IntegerAccumulator extends Accumulator<Integer> {
	public IntegerAccumulator(NumberReductionOperator operator) {
		this.operator = operator;
		this.resultVal = this.operator.getInitialIntValue();
	}

	@Override
	public final Integer get() {
		if (this.isAccessible()) {
			//pass
		} else {
			this.checkGet();
		}
		return this.resultVal;
	}

	protected abstract void putAccessible(int value);

	@Override
	public final void put(Integer v) {
		int value = v.intValue();
		if (this.isAccessible()) {
			this.putAccessible(value);
		} else {
			this.checkPut();
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
		}
	}

	protected final NumberReductionOperator operator;
	protected int resultVal;
}
