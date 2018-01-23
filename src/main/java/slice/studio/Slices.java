/*******************************************************************************
 * Copyright (C) 2016-2017 Dennis Cosgrove, Finn Voichick
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
package slice.studio;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.wustl.cse231s.IntendedForStaticAccessOnlyError;
import edu.wustl.cse231s.NotYetImplementedException;
import slice.core.Slice;

/**
 * @author __STUDENT_NAME__
 * @author Finn Voichick
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class Slices {
	private Slices() {
		throw new IntendedForStaticAccessOnlyError();
	}

	/**
	 * Should create a list of {@link Slice} objects of length numSlices. Each slice
	 * in the returned result should be roughly data.length/numSlices in length. 
	 * Any remaining data should be distributed one each to the front
	 * slices. The sliceIndexId of each slice should be its index in the returned list.
	 * 
	 * @param data
	 *            the data to be broken up into a list of slices
	 * @param numSlices
	 *            the number of slices to divide data into
	 * @return the created list of slices
	 */
	private static <T> List<Slice<T>> createNSlicesForArrayObject(T data, int numSlices) {
		throw new NotYetImplementedException();
	}

	public static <T> List<Slice<T[]>> createNSlices(T[] data, int numSlices) {
		return createNSlicesForArrayObject(data, numSlices);
	}

	public static List<Slice<byte[]>> createNSlices(byte[] data, int numSlices) {
		return createNSlicesForArrayObject(data, numSlices);
	}

	public static List<Slice<char[]>> createNSlices(char[] data, int numSlices) {
		return createNSlicesForArrayObject(data, numSlices);
	}

	public static List<Slice<short[]>> createNSlices(short[] data, int numSlices) {
		return createNSlicesForArrayObject(data, numSlices);
	}

	public static List<Slice<int[]>> createNSlices(int[] data, int numSlices) {
		return createNSlicesForArrayObject(data, numSlices);
	}

	public static List<Slice<long[]>> createNSlices(long[] data, int numSlices) {
		return createNSlicesForArrayObject(data, numSlices);
	}

	public static List<Slice<float[]>> createNSlices(float[] data, int numSlices) {
		return createNSlicesForArrayObject(data, numSlices);
	}

	public static List<Slice<double[]>> createNSlices(double[] data, int numSlices) {
		return createNSlicesForArrayObject(data, numSlices);
	}
}
