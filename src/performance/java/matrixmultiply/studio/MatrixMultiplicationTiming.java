package matrixmultiply.studio;

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
import static edu.wustl.cse231s.v5.V5.launchApp;

import java.util.Arrays;

import edu.wustl.cse231s.NotYetImplementedException;
import edu.wustl.cse231s.timing.ImmutableTimer;
import matrixmultiply.core.MatrixMultiplier;
import matrixmultiply.core.MatrixUtils;
//import matrixmultiply.demo.Forall2dGroupedMatrixMultiplier;
//import matrixmultiply.demo.ForallGroupedMatrixMultiplier;
import matrixmultiply.demo.SequentialMatrixMultiplier;
import matrixmultiply.fun.ParallelDivideAndConquerMatrixMultiplier;
import matrixmultiply.fun.SequentialDivideAndConquerMatrixMultiplier;
import matrixmultiply.studio.Forall2dChunkedMatrixMultiplier;
import matrixmultiply.studio.Forall2dMatrixMultiplier;
import matrixmultiply.studio.ForallForallMatrixMultiplier;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class MatrixMultiplicationTiming {
	public static void main(String[] args) {
		int size = 512;
		double[][] a = new double[size][size];
		double[][] b = new double[size][size];

		MatrixUtils.setAllRandom(a);
		MatrixUtils.setAllRandom(b);

		MatrixMultiplier[] matrixMultipliers = { new SequentialMatrixMultiplier(), new ForallForallMatrixMultiplier(),
				new Forall2dMatrixMultiplier(), new Forall2dChunkedMatrixMultiplier(),
				// new ForallGroupedMatrixMultiplier(),
				// new Forall2dGroupedMatrixMultiplier(),
				new SequentialDivideAndConquerMatrixMultiplier(), new ParallelDivideAndConquerMatrixMultiplier() };
		launchApp(() -> {
			double[][] expected = new SequentialMatrixMultiplier().multiply(a, b);
			for (int iteration = 0; iteration < 10; iteration++) {
				for (MatrixMultiplier matrixMultiplier : matrixMultipliers) {
					String name = matrixMultiplier.getClass().getSimpleName();
					ImmutableTimer timer = new ImmutableTimer(String.format("%1$42s; iteration=", name) + iteration);
					try {
						double[][] actual = matrixMultiplier.multiply(a, b);
						if (Arrays.deepEquals(expected, actual)) {
							timer.markAndPrintResults(" SUCCESS");
						} else {
							System.err.println("RESULT " + matrixMultiplier);
							System.err.println(actual);
							System.err.println("RESULT truth and beauty");
							System.err.println(expected);
							throw new RuntimeException("result for " + name + " != truth and beauty");
						}
					} catch (NotYetImplementedException nyie) {
						System.out.println(String.format("%1$41s; NOT YET IMPLEMENTED", name));
					}
				}
				System.out.println();
			}
		});
	}
}
