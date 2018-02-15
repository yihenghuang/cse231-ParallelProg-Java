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
package matrixmultiply.studio;

import static edu.wustl.cse231s.v5.V5.launchApp;

import java.util.Arrays;

import org.junit.Assert;

import edu.wustl.cse231s.print.AbstractNoPrintingTest;
import matrixmultiply.core.MatrixMultiplier;
import matrixmultiply.core.MatrixMultiplyTestUtils;
import matrixmultiply.core.MatrixUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link NucleobaseCounting}
 */
public class NoPrintingTest extends AbstractNoPrintingTest {
	@Override
	protected void testKernel() {
		int size = 16;
		int A_ROW_COUNT = size;
		int B_COL_COUNT = size;
		int A_COL_AND_B_ROW_COUNT = size;
		double[][] originalA = new double[A_ROW_COUNT][A_COL_AND_B_ROW_COUNT];
		double[][] originalB = new double[A_COL_AND_B_ROW_COUNT][B_COL_COUNT];
		MatrixUtils.setAllRandom(originalA);
		MatrixUtils.setAllRandom(originalB);
		double[][] a = MatrixUtils.copy(originalA);
		Assert.assertTrue(Arrays.deepEquals(originalA, a));
		double[][] b = MatrixUtils.copy(originalB);
		Assert.assertTrue(Arrays.deepEquals(originalB, b));
		launchApp(() -> {
			for (MatrixMultiplier matrixMultiplier : Arrays.asList(new ForallForallMatrixMultiplier(),
					new Forall2dMatrixMultiplier(), new Forall2dChunkedMatrixMultiplier())) {
				double[][] expected = MatrixMultiplyTestUtils.multiply(a, b);
				double[][] actual = matrixMultiplier.multiply(a, b);
				Assert.assertTrue("do not mutate parameter a", Arrays.deepEquals(originalA, a));
				Assert.assertTrue("do not mutate parameter b", Arrays.deepEquals(originalB, b));
				Assert.assertTrue("incorrect result", Arrays.deepEquals(expected, actual));
			}
		});
	}
}
