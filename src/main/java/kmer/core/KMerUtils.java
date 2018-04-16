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
package kmer.core;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import kmer.core.bytearrayrange.ByteArrayRange;

/**
 * An assortment of utility methods useful for counting k-mers.
 * 
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class KMerUtils {

	private KMerUtils() {
		throw new AssertionError("This class is non-instantiable");
	}

	private static final byte A = (byte) 'A';
	private static final byte C = (byte) 'C';
	private static final byte T = (byte) 'T';
	private static final byte G = (byte) 'G';

	private static final int A_MASK_INT = 0x0;
	private static final int T_MASK_INT = 0x1;
	private static final int C_MASK_INT = 0x2;
	private static final int G_MASK_INT = 0x3;

	// private static final long A_MASK_LONG = (long) A_MASK_INT;
	private static final long T_MASK_LONG = (long) T_MASK_INT;
	private static final long C_MASK_LONG = (long) C_MASK_INT;
	private static final long G_MASK_LONG = (long) G_MASK_INT;

	/**
	 * Stores the information from the given sequence into a String. For example, if
	 * you had the sequence, "ACCTGTCAAAA" and you called this method with an offset
	 * of 1 and a k of 4, it would return "CCTG".
	 * 
	 * @param sequence
	 *            the sequence of nucleobases to draw the bytes from
	 * @param offset
	 *            the offset for where to start looking for bytes
	 * @param k
	 *            the length of the k-mer to make a String for
	 * @return a String representation of the k-mer at the desired position
	 */
	public static String toString(byte[] sequence, int offset, int k) {
		return new String(sequence, offset, k, StandardCharsets.UTF_8);
	}

	/**
	 * Converts the given k-mer (represented as an array of bytes) into a
	 * human-readable String. For example, if you had the k-mer, {'C', 'C', 'T',
	 * 'G'}, this method would return "CCTG".
	 * 
	 * @param kMer
	 *            the k-mer to generate a String for.
	 * @return the k-mer's String reprentation
	 */
	public static String toString(byte[] kMer) {
		return new String(kMer, StandardCharsets.UTF_8);
	}

	public static byte[] toBytes(String s) {
		return s.getBytes(StandardCharsets.UTF_8);
	}

	public static ByteArrayRange toByteArrayRange(byte[] sequence, int offset, int k) {
		return new ByteArrayRange(sequence, offset, offset + k);
	}

	public static ByteArrayRange toByteArrayRange(byte[] kMer) {
		return toByteArrayRange(kMer, 0, kMer.length);
	}

	public static byte[] toBytes(ByteArrayRange range) {
		return range.toBytes();
	}

	/**
	 * "Packs" a k-mer into an int. The bits of the int will be used to store
	 * individual nucleobases.
	 * 
	 * @param sequence
	 *            a sequence of nucleobases
	 * @param offset
	 *            the index of the first nucleobase in the desired k-mer
	 * @param k
	 *            the length of the desired k-mer
	 * @return an integer uniquely representing this k-mer
	 */
	public static int toPackedInt(byte[] sequence, int offset, int k) {
		int result = 0;
		for (int i = 0; i < k; i++) {
			switch (sequence[offset + i]) {
			// case A:
			// result |= (A_MASK_INT << (i + i));
			// break;
			case T:
				result |= (T_MASK_INT << (i + i));
				break;
			case C:
				result |= (C_MASK_INT << (i + i));
				break;
			case G:
				result |= (G_MASK_INT << (i + i));
				break;
			}
		}
		return result;
	}

	/**
	 * Packs the entire k-mer into an int. Exactly like
	 * {@link #toPackedInt(byte[], int, int)}, but treats the whole array as a
	 * single k-mer.
	 * 
	 * @param kMer
	 *            the k-mer represented as an array of bytes
	 * @return an integer uniquely representing this k-mer
	 */
	public static int toPackedInt(byte[] kMer) {
		return toPackedInt(kMer, 0, kMer.length);
	}

	/**
	 * Unpacks the k-mer from an integer back to an array of bytes.
	 * 
	 * @param kMer
	 *            an integer uniquely identifying a k-mer
	 * @param k
	 *            the length of the k-mer packed in the integer
	 * @return an array of bytes representing the same k-mer
	 */
	public static byte[] unpackInt(int kMer, int k) {
		byte[] result = new byte[k];

		for (int i = 0; i < k; i++) {
			int v = (kMer >> (i + i)) & 0x3;
			switch (v) {
			case A_MASK_INT:
				result[i] = A;
				break;
			case T_MASK_INT:
				result[i] = T;
				break;
			case C_MASK_INT:
				result[i] = C;
				break;
			case G_MASK_INT:
				result[i] = G;
				break;
			}
		}
		return result;
	}

	/**
	 * "Packs" a k-mer into a long. The bits of the long will be used to store
	 * individual nucleobases.
	 * 
	 * @param sequence
	 *            a sequence of nucleobases
	 * @param offset
	 *            the index of the first nucleobase in the desired k-mer
	 * @param k
	 *            the length of the desired k-mer
	 * @return a long integer uniquely representing this k-mer
	 */
	public static long toPackedLong(byte[] sequence, int offset, int k) {
		long result = 0;
		for (int i = 0; i < k; i++) {
			switch (sequence[offset + i]) {
			// case A:
			// result |= (A_MASK_LONG << (i + i));
			// break;
			case T:
				result |= (T_MASK_LONG << (i + i));
				break;
			case C:
				result |= (C_MASK_LONG << (i + i));
				break;
			case G:
				result |= (G_MASK_LONG << (i + i));
				break;
			}
		}
		return result;
	}

	/**
	 * Packs the entire k-mer into a long. Exactly like
	 * {@link #toPackedLong(byte[], int, int)}, but treats the whole array as a
	 * single k-mer.
	 * 
	 * @param kMer
	 *            the k-mer represented as an array of bytes
	 * @return a long integer uniquely representing this k-mer
	 */
	public static long toPackedLong(byte[] sequence) {
		return toPackedLong(sequence, 0, sequence.length);
	}

	/**
	 * Unpacks the k-mer from a long back to an array of bytes.
	 * 
	 * @param kMer
	 *            a long integer uniquely identifying a k-mer
	 * @param k
	 *            the length of the k-mer packed in the long
	 * @return an array of bytes representing the same k-mer
	 */
	public static byte[] unpackLong(long kMer, int k) {
		byte[] result = new byte[k];

		for (int i = 0; i < k; i++) {
			long v = (kMer >> (i + i)) & 0x3;
			switch ((int) v) {
			case A_MASK_INT:
				result[i] = A;
				break;
			case T_MASK_INT:
				result[i] = T;
				break;
			case C_MASK_INT:
				result[i] = C;
				break;
			case G_MASK_INT:
				result[i] = G;
				break;
			}
		}
		return result;
	}

	public static int createIntTMask(int k) {
		int lastBaseIndex = k - 1;
		return (0x1 << (lastBaseIndex + lastBaseIndex));
	}

	public static int createIntCMask(int k) {
		int lastBaseIndex = k - 1;
		return (0x2 << (lastBaseIndex + lastBaseIndex));
	}

	public static int createIntGMask(int k) {
		int lastBaseIndex = k - 1;
		return (0x3 << (lastBaseIndex + lastBaseIndex));
	}

	public static long createLongTMask(int k) {
		int lastBaseIndex = k - 1;
		return (0x1L << (lastBaseIndex + lastBaseIndex));
	}

	public static long createLongCMask(int k) {
		int lastBaseIndex = k - 1;
		return (0x2L << (lastBaseIndex + lastBaseIndex));
	}

	public static long createLongGMask(int k) {
		int lastBaseIndex = k - 1;
		return (0x3L << (lastBaseIndex + lastBaseIndex));
	}

	public static int shiftAndUpdateLastBaseIndexOfPackedInt(int prev, byte[] sequence, int offset, int k, int T_MASK,
			int C_MASK, int G_MASK) {
		byte b = sequence[offset + k - 1];
		int packedKMer = prev;
		packedKMer >>= 2;

		switch (b) {
		// case A: skip A since its mask is 0
		case T:
			packedKMer |= T_MASK;
			break;
		case C:
			packedKMer |= C_MASK;
			break;
		case G:
			packedKMer |= G_MASK;
			break;
		}
		return packedKMer;
	}

	public static long shiftAndUpdateLastBaseIndexOfPackedLong(long prev, byte[] sequence, int offset, int k,
			long T_MASK, long C_MASK, long G_MASK) {
		byte b = sequence[offset + k - 1];
		long packedKMer = prev;
		packedKMer >>= 2;

		switch (b) {
		// case A: skip A since its mask is 0
		case T:
			packedKMer |= T_MASK;
			break;
		case C:
			packedKMer |= C_MASK;
			break;
		case G:
			packedKMer |= G_MASK;
			break;
		}
		return packedKMer;
	}

	public static byte[] nextRandom(int k) {
		long r = ThreadLocalRandom.current().nextLong();
		long possibleKMers = 1L << (k * 2);
		long mask = possibleKMers - 1;
		long kMer = r & mask;
		return unpackLong(kMer, k);
	}

	/**
	 * Gets the number of possible k-mers that can exist with the given length. Note
	 * that all possible k-mers with the desired length can be represented by the
	 * integers from 0 (inclusive) to the result of this method (exclusive).
	 * 
	 * @param k
	 *            the k in k-mer
	 * @return the number of possible k-mers that could exist with the given length
	 */
	public static long calculatePossibleKMers(int k) {
		int bitsPerBase = 2;
		int bitsPerKMer = k * bitsPerBase;
		long possibleKMers = 1L << bitsPerKMer;
		return possibleKMers;
	}

	/**
	 * Gets the total number of (not necessarily unique) k-mers in all of the
	 * sequences. If a single sequence is given, this method will return n - k + 1.
	 * 
	 * @param sequences
	 *            a list of sequences of nucleobases
	 * @param k
	 *            the k in k-mer
	 * @return the sum of all k-mer counts
	 */
	public static int calculateSumOfAllKMers(List<byte[]> sequences, int k) {
		int sum = 0;
		for (byte[] sequence : sequences) {
			int i = (sequence.length - k + 1);
			if (i > 0) {
				sum += i;
			}
		}
		return sum;
	}

	public static int toArrayLength(long possibleKMers) {
		// maximum array length is VM dependent, but must be indexed by an int.
		// reportedly some vms reserve some header words (see ArrayList) which puts its
		// limit as just below 2^31.
		// we will choose 2^30 (inclusive) as the maximum since it is the largest
		// potential k-mer (4^15)
		final long MAX_K_MER_ARRAY_LENGTH_INCLUSIVE = 1 << 30;
		if (possibleKMers <= MAX_K_MER_ARRAY_LENGTH_INCLUSIVE) {
			return (int) possibleKMers;
		} else {
			throw new IllegalArgumentException(possibleKMers + " > " + MAX_K_MER_ARRAY_LENGTH_INCLUSIVE);
		}
	}

}
