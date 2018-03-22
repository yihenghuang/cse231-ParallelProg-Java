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

import edu.wustl.cse231s.v5.api.FinishAccumulator;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
// Based on the implementation by Jun Shirako (shirako@rice.edu) and Vivek
// Sarkar (vsarkar@rice.edu)
public abstract class Accumulator<T> implements FinishAccumulator<T> {
	/* package-private */void open() {
		this.setAccessible(true);
	}

	/* package-private */void close() {
		this.calculateAccum();
		this.setAccessible(false);
	}

	protected boolean isAccessible() {
		return this.isAccessible;
	}

	private void setAccessible(boolean isAccessible) {
		if (isAccessible) {
			this.checkRegistration();
			if (this.isAccessible) {
				throw new FinishAccumulatorException("Nested (double) registration is not allowed.");
			}
		}
		this.isAccessible = isAccessible;
	}

	protected abstract void calculateAccum();

	protected void checkGet() {
		// TODO
	}

	protected void checkPut() {
		// TODO
	}

	protected void checkRegistration() {
		// TODO
	}

	private boolean isAccessible;
}
