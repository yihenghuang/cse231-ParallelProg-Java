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
package matrixmultiply.fun;

import static edu.wustl.cse231s.v5.V5.async;
import static edu.wustl.cse231s.v5.V5.finish;

import java.util.concurrent.ExecutionException;

import edu.wustl.cse231s.NotYetImplementedException;
import matrixmultiply.core.MatrixUtils;

/**
 * @author Yiheng Huang
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public final class SubMatrix {

	/**
	 * True if the int is a power of 2, false otherwise
	 * 
	 * @param n,
	 *            the int to examine
	 * @return true if the int is a power of 2, false otherwise
	 */
	private static boolean isPowerOfTwo(int n) {
		return (n & (n - 1)) == 0;
	}

	/**
	 * Creates a SubMatrix based on a matrix of values, starts at row zero,
	 * column zero, with a size equal to the matrix's size
	 * 
	 * @param values,
	 *            the matrix of values
	 */
	public SubMatrix(double[][] values) {
		this(values, 0, 0, values.length);
		if (isPowerOfTwo(values.length)) {
			// pass
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Creates a square SubMatrix based on a matrix of values, a given start
	 * row, a given start column, and a given size
	 * 
	 * @param values,
	 *            the matrix of values
	 * @param row,
	 *            the row to start
	 * @param col,
	 *            the column to start
	 * @param size,
	 *            the size of the matrix
	 */
	private SubMatrix(double[][] values, int row, int col, int size) {
		this.values = values;
		this.row = row;
		this.col = col;
		this.size = size;
	}

	/**
	 * Creates a new SubMatrix which represents the top left quadrant of the
	 * original matrix
	 * 
	 * @return SubMatrix
	 */
	private SubMatrix newSub11() {
		int halfSize = size / 2;
		return new SubMatrix(this.values, row, col, halfSize);
	}

	/**
	 * Creates a new SubMatrix which represents the top right quadrant of the
	 * original matrix
	 * 
	 * @return SubMatrix
	 */
	private SubMatrix newSub12() {
		int halfSize = size / 2;
		return new SubMatrix(this.values, row, col + halfSize, halfSize);
	}

	/**
	 * Creates a new SubMatrix which represents the bottom left quadrant of the
	 * original matrix
	 * 
	 * @return SubMatrix
	 */
	private SubMatrix newSub21() {
		int halfSize = size / 2;
		return new SubMatrix(this.values, row + halfSize, col, halfSize);
	}

	/**
	 * Creates a new SubMatrix which represents the bottom right quadrant of the
	 * original matrix
	 * 
	 * @return SubMatrix
	 */
	private SubMatrix newSub22() {
		int halfSize = size / 2;
		return new SubMatrix(this.values, row + halfSize, col + halfSize, halfSize);
	}

	/**
	 * The kernel for the sequential implementation. This should recursively
	 * call itself a total of eight times, two times each for each quadrant of
	 * the result SubMatrices. Think of it as matrix multiplication for a 2x2
	 * matrix to figure out the input parameters.
	 * 
	 * @param a,
	 *            the SubMatrix of the first matrix
	 * @param b,
	 *            the SubMatrix of the second matrix
	 * @param result,
	 *            the SubMatrix of the result matrix
	 */
	private static void sequentialDivideAndConquerMultiplyKernel(SubMatrix a, SubMatrix b, SubMatrix result) {
		if (result.size == 1) {
			result.values[result.row][result.col] += a.values[a.row][a.col] * b.values[b.row][b.col];
		} else {
			SubMatrix a11 = a.newSub11();
			SubMatrix a12 = a.newSub12();
			SubMatrix a21 = a.newSub21();
			SubMatrix a22 = a.newSub22();

			SubMatrix b11 = b.newSub11();
			SubMatrix b12 = b.newSub12();
			SubMatrix b21 = b.newSub21();
			SubMatrix b22 = b.newSub22();

			SubMatrix result11 = result.newSub11();
			SubMatrix result12 = result.newSub12();
			SubMatrix result21 = result.newSub21();
			SubMatrix result22 = result.newSub22();

			// https://en.wikipedia.org/wiki/Matrix_multiplication_algorithm#Divide_and_conquer_algorithm
			throw new NotYetImplementedException();
		}
	}

	/**
	 * Calls the kernel to start the process of performing recursive matrix
	 * multiplication sequentially.
	 * 
	 * @param a,
	 *            the first matrix
	 * @param b,
	 *            the second matrix
	 * @return result, the result matrix
	 */
	/* package-private */ static double[][] sequentialDivideAndConquerMultiply(double[][] a, double[][] b) {
		double[][] result = MatrixUtils.createMultiplyResultBufferInitializedToZeros(a, b);
		sequentialDivideAndConquerMultiplyKernel(new SubMatrix(a), new SubMatrix(b), new SubMatrix(result));
		return result;
	}

	/**
	 * The kernel for the parallel implementation. This should recursively call
	 * itself a total of eight times, two times each for each quadrant of the
	 * result SubMatrices. Think of it as matrix multiplication for a 2x2 matrix
	 * to figure out the input parameters.
	 * 
	 * @param a,
	 *            the SubMatrix of the first matrix
	 * @param b,
	 *            the SubMatrix of the second matrix
	 * @param result,
	 *            the SubMatrix of the result matrix
	 * @throws InterruptedException,
	 *             ExecutionException
	 */
	private static void parallelDivideAndConquerMultiplyKernel(SubMatrix a, SubMatrix b, SubMatrix result)
			throws InterruptedException, ExecutionException {
		final int SIZE_THRESHOLD = 64;
		if (result.size <= SIZE_THRESHOLD) {
			sequentialDivideAndConquerMultiplyKernel(a, b, result);
		} else {
			SubMatrix a11 = a.newSub11();
			SubMatrix a12 = a.newSub12();
			SubMatrix a21 = a.newSub21();
			SubMatrix a22 = a.newSub22();

			SubMatrix b11 = b.newSub11();
			SubMatrix b12 = b.newSub12();
			SubMatrix b21 = b.newSub21();
			SubMatrix b22 = b.newSub22();

			SubMatrix result11 = result.newSub11();
			SubMatrix result12 = result.newSub12();
			SubMatrix result21 = result.newSub21();
			SubMatrix result22 = result.newSub22();

			throw new NotYetImplementedException();
		}
	}

	/**
	 * Calls the kernel to start the process of performing recursive matrix
	 * multiplication sequentially.
	 * 
	 * @param a,
	 *            the first matrix
	 * @param b,
	 *            the second matrix
	 * @return result, the result matrix
	 * @throws InterruptedException,
	 *             ExecutionException
	 */
	/* package-private */ static double[][] parallelDivideAndConquerMultiply(double[][] a, double[][] b)
			throws InterruptedException, ExecutionException {
		double[][] result = MatrixUtils.createMultiplyResultBufferInitializedToZeros(a, b);
		SubMatrix.parallelDivideAndConquerMultiplyKernel(new SubMatrix(a), new SubMatrix(b), new SubMatrix(result));
		return result;
	}

	private final double[][] values;
	private final int row;
	private final int col;
	private final int size;
}
