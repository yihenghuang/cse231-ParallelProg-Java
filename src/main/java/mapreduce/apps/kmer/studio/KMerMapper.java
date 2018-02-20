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
package mapreduce.apps.kmer.studio;

import java.nio.charset.StandardCharsets;
import java.util.function.BiConsumer;

import edu.wustl.cse231s.NotYetImplementedException;
import mapreduce.framework.core.Mapper;
import net.jcip.annotations.Immutable;

/**
 * @author __STUDENT_NAME__
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@Immutable
public class KMerMapper implements Mapper<byte[], String, Integer> {
	private final int kMerLength;

	public KMerMapper(int kMerLength) {
		this.kMerLength = kMerLength;
	}

	@Override
	public void map(byte[] sequence, BiConsumer<String, Integer> keyValuePairConsumer) {
		throw new NotYetImplementedException();
	}

	/**
	 * Stores the information from the given sequence into a String. For example, if
	 * you had the sequence, "ACCTGTCAAAA" and you called this method with an offset
	 * of 1 and a kMerLength of 4, it would return "CCTG".
	 * 
	 * @param sequence
	 *            the sequence of nucleobases to draw the bytes from
	 * @param offset
	 *            the offset for where to start looking for bytes
	 * @param kMerLength
	 *            the length of the k-mer to make a String for
	 * @return a String representation of the k-mer at the desired position
	 */
	private static String toStringKMer(byte[] sequence, int offset, int kMerLength) {
		return new String(sequence, offset, kMerLength, StandardCharsets.UTF_8);
	}
}
