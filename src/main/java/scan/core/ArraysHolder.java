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
 ******************************************************************************/

package scan.core;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class ArraysHolder {
	private final int[] data;
	private final int[] a;
	private final int[] b;
	private int phase;

	public ArraysHolder(int[] data) {
		this.data = data;
		this.a = new int[data.length];
		this.b = new int[data.length];
		this.phase = 0;
	}

	private boolean isEvenPhase() {
		return (phase & 1) == 0;
	}

	public int[] getSrc() {
		if (phase == 0) {
			return this.data;
		} else {
			return isEvenPhase() ? this.b : this.a;
		}
	}

	public int[] getDst() {
		return isEvenPhase() ? this.a : this.b;
	}

	public void nextSrcAndDst() {
		this.phase++;
	}
}
