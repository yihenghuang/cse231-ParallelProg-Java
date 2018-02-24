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
package matrixmultiply.studio;

import static edu.wustl.cse231s.v5.V5.forall2d;

import java.util.concurrent.ExecutionException;

import matrixmultiply.core.MatrixMultiplier;
import matrixmultiply.core.MatrixUtils;

/**
 * @author Yiheng Huang
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class Forall2dMatrixMultiplier implements MatrixMultiplier {
	/**
	 * This implementation should perform matrix multiplication using a forall2d
	 * loop. Look at the wiki for a good explanation on how to format the syntax of
	 * this type of loop.
	 */
	@Override
	public double[][] multiply(double[][] a, double[][] b) throws InterruptedException, ExecutionException {
		double[][] result = MatrixUtils.createMultiplyResultBufferInitializedToZeros(a, b);
		int n = a.length;
		int m = b[0].length;
		int p = a[0].length;
		forall2d(0, n, 0, m, (i, j) -> {
			for (int k = 0; k < p; k++) {
				result[i][j] += a[i][k] * b[k][j];
			}
		});
		return result;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
