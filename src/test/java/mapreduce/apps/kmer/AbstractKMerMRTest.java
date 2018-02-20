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
package mapreduce.apps.kmer;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import edu.wustl.cse231s.bioinformatics.io.FastaSplitUtils;
import edu.wustl.cse231s.kmer.io.ChromosomeResourceTestUtils;
import mapreduce.core.AbstractMRTest;
import mapreduce.core.CollectorSolution;
import mapreduce.core.FrameworkSolution;
import mapreduce.core.MapperSolution;
import mapreduce.core.TestApplication;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public abstract class AbstractKMerMRTest<A> extends AbstractMRTest<byte[], String, Integer, A, Integer> {
	public AbstractKMerMRTest(FrameworkSolution frameworkSolution, MapperSolution mapperSolution,
			CollectorSolution collectorSolution) {
		super(TestApplication.TWELVE_MER, frameworkSolution, mapperSolution, collectorSolution);
	}

	@Test
	public void testHomoSapiensMitochondrion() throws IOException {
		byte[] chromosome = ChromosomeResourceTestUtils.loadHomoSapiensMitochondrion();
		List<byte[]> list = FastaSplitUtils.splitIntoSubSequences(chromosome);
		byte[][] sequences = new byte[list.size()][];
		list.toArray(sequences);
		this.testInput(sequences);
	}

	@Test
	public void testVibrioCholeraeOriC() {
		// cholera origin of replication
		// http://www.csbio.unc.edu/mcmillan/Comp555S16/Lecture02.html
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
		byte[][] sequences = { text.getBytes() };
		this.testInput(sequences);
	}
}
