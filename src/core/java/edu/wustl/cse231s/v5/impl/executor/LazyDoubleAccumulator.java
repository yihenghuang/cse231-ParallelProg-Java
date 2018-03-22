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
//Based on "Finish Accumulators: a Deterministic Reduction Construct for Dynamic Task Parallelism"
//Jun Shirako, Vincent Cave', Jisheng Zhao, and Vivek Sarkar
//https://wiki.rice.edu/confluence/download/attachments/4425835/wodet2013-shirako-sarkar.pdf
public class LazyDoubleAccumulator extends DoubleAccumulator {
	public LazyDoubleAccumulator(NumberReductionOperator operator) {
		super(operator);
	}

	@Override
	/*package-private*/ void open() {
		super.open();
		int arrayLength = LazyUtils.getPoolSize();
		this.array = new double[arrayLength];
		for (int i = 0; i < this.array.length; i++) {
			this.array[i] = this.operator.getInitialDoubleValue();
		}
	}
	@Override
	/*package-private*/ void close() {
		super.close();
		this.array = null;
	}
	
	@Override
	protected void putAccessible(double value) {
		int index = LazyUtils.getPoolIndex();
		switch (this.operator) {
		case SUM:
			this.array[index] += value;
			break;
		case PRODUCT:
			this.array[index] *= value;
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

	@Override
	protected void calculateAccum() {
		switch (this.operator) {
		case SUM:
			for (double value : this.array) {
				this.resultVal += value;
			}
			break;
		case PRODUCT:
			for (double value : this.array) {
				this.resultVal *= value;
			}
			break;
		case MIN:
			for (double value : this.array) {
				if (value < this.resultVal) {
					this.resultVal = value;
				}
			}
			break;
		case MAX:
			for (double value : this.array) {
				if (value > this.resultVal) {
					this.resultVal = value;
				}
			}
			break;
		}
	}

	private double[] array;
}
