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
package matrixmultiply.demo;

import matrixmultiply.core.MatrixMultiplier;
import matrixmultiply.core.MatrixUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class SequentialMatrixMultiplier implements MatrixMultiplier {
	/**
	 * This has been provided for you and shows you how to perform matrix
	 * multiplication sequentially with regular for loops.
	 */
	@Override
	public double[][] multiply(double[][] a, double[][] b) {
		double[][] result = MatrixUtils.createMultiplyResultBufferInitializedToZeros(a, b);
		int n = a.length;
		int m = a[0].length;
		int p = b[0].length;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < p; j++) {
				// NOTE: result is already initialized to 0.0
				// result[i][j] = 0.0;
				for (int k = 0; k < m; k++) {
					result[i][j] += a[i][k] * b[k][j];
				}
			}
		}
		return result;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
