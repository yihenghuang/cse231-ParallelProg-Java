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
package matrixmultiply.core;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

import edu.wustl.cse231s.IntendedForStaticAccessOnlyError;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class MatrixUtils {
	private MatrixUtils() {
		throw new IntendedForStaticAccessOnlyError();
	}

	public static double[][] createMultiplyResultBufferInitializedToZeros(double[][] a, double[][] b) {
		Objects.requireNonNull(a);
		Objects.requireNonNull(b);
		if (a.length > 0 && b.length > 0) {
			int aRowCount = a.length;
			int aColCount = a[0].length;
			int bRowCount = b.length;
			int bColCount = b[0].length;
			if (aColCount == bRowCount) {
				return new double[aRowCount][bColCount];
			} else {
				throw new IllegalArgumentException(
						"a's column (" + aColCount + ") != b's row count (" + bRowCount + ")");
			}
		} else {
			throw new IllegalArgumentException("Not Supported: a.length=" + a.length + "; b.length=" + b.length);
		}
	}

	public static void setIdentity(double[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				matrix[i][j] = i == j ? 1.0 : 0.0;
			}
		}
	}

	public static boolean isIdentity(double[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] != (i == j ? 1.0 : 0.0)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Adds random values to the matrix.
	 * 
	 * @param matrix,
	 *            the matrix to add random values to
	 */
	public static void setAllRandom(double[][] matrix) {
		Random random = new Random();
		for (double[] array : matrix) {
			for (int j = 0; j < array.length; j++) {
				array[j] += random.nextDouble();
			}
		}
	}

	/**
	 * Outputs a visual representation of the matrix in the form of a string
	 * 
	 * @param matrix,
	 *            the specific matrix to use
	 * @return the matrix as a string
	 */
	public static String toString(double[][] matrix) {
		StringBuilder sb = new StringBuilder();
		for (double[] array : matrix) {
			sb.append(Arrays.toString(array)).append("\n");
		}
		return sb.toString();
	}

	public static double[][] copy(double[][] m) {
		double[][] result = new double[m.length][m[0].length];
		for (int i = 0; i < m.length; i++) {
			System.arraycopy(m[i], 0, result[i], 0, result[i].length);
		}
		return result;
	}
}
