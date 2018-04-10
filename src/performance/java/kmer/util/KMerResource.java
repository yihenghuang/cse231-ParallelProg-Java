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

package kmer.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import edu.wustl.cse231s.bioinformatics.io.FastaSplitUtils;
import edu.wustl.cse231s.bioinformatics.io.FastaUtils;
import edu.wustl.cse231s.bioinformatics.io.resource.ChromosomeResource;
import edu.wustl.cse231s.lazy.Lazy;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public enum KMerResource {
	Y_CHROMOSOME_COMPLETE() {
		private final Lazy<List<byte[]>> lazy = new Lazy<>(() -> {
			try {
				return ChromosomeResource.readSubSequences(ChromosomeResource.HOMO_SAPIENS_Y);
			} catch (IOException ioe) {
				throw new RuntimeException(ioe);
			}
		});

		@Override
		public List<byte[]> getSubSequences() {
			return lazy.get();
		}
	},
	Y_CHROMOSOME_PARTIAL() {
		@Override
		public List<byte[]> getSubSequences() {
			return Y_CHROMOSOME_COMPLETE.getSubSequences().subList(0, 12);
		}
	},
	Y_CHROMOSOME_TINY() {
		@Override
		public List<byte[]> getSubSequences() {
			return Y_CHROMOSOME_COMPLETE.getSubSequences().subList(0, 2);
		}
	},
	CHOLERAE_ORI_C() {
		@Override
		public List<byte[]> getSubSequences() {
			StringBuilder sb = new StringBuilder();
			sb.append("atcaatgatcaacgtaagcttctaagcatgatcaaggtgctcacacagtttatccacaac");
			sb.append("ctgagtggatgacatcaagataggtcgttgtatctccttcctctcgtactctcatgacca");
			sb.append("cggaaagatgatcaagagaggatgatttcttggccatatcgcaatgaatacttgtgactt");
			sb.append("gtgcttccaattgacatcttcagcgccatattgcgctggccaaggtgacggagcgggatt");
			sb.append("acgaaagcatgatcatggctgttgttctgtttatcttgttttgactgagacttgttagga");
			sb.append("tagacggtttttcatcactgactagccaaagccttactctgcctgacatcgaccgtaaat");
			sb.append("tgataatgaatttacatgcttccgcgacgatttacctcttgatcatcgatccgattgaag");
			sb.append("atcttcaattgttaattctcttgcctcgactcatagccatgatgagctcttgatcatgtt");
			sb.append("tccttaaccctctattttttacggaagaatgatcaagctgctgctcttgatcatcgtttc");

			String text = sb.toString().toUpperCase();
			return Arrays.asList(text.getBytes());
		}
	},
	MITOCHONDRION() {
		@Override
		public List<byte[]> getSubSequences() {
			try {
				InputStream inputStream = KMerResource.class
						.getResourceAsStream("/edu/wustl/cse231s/executors/HomoSapiensMitochondrion.fasta");
				Objects.requireNonNull(inputStream);
				byte[] sequence = FastaUtils.read(inputStream);
				return FastaSplitUtils.splitIntoSubSequences(sequence);
			} catch (IOException ioe) {
				throw new RuntimeException(ioe);
			}
		}
	};

	public abstract List<byte[]> getSubSequences();
}
