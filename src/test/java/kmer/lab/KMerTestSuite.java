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
package kmer.lab;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import edu.wustl.cse231s.bioinformatics.io.resource.ChromosomeResource;
import kmer.lab.atomicintegerarray.AtomicIntegerArrayKMerCounterParallelismTest;
import kmer.lab.atomicintegerarray.AtomicIntegerArrayKMerCounterTest;
import kmer.lab.concurrentbuckethashmap.ConcurrentBucketMapKMerCounterParallelismTest;
import kmer.lab.concurrentbuckethashmap.ConcurrentBucketMapKMerCounterTest;
import kmer.lab.concurrentbuckethashmap.ConcurrentBucketMapTestSuite;
import kmer.lab.intarray.IntArrayKMerCounterTest;
import kmer.lab.longconcurrenthashmap.LongConcurrentHashMapKMerCounterParallelismTest;
import kmer.lab.longconcurrenthashmap.LongConcurrentHashMapKMerCounterTest;
import kmer.lab.util.ChromosomeThresholdSlicesTest;
import kmer.lab.util.ThresholdSlicesTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ ThresholdSlicesTest.class, ChromosomeThresholdSlicesTest.class,
		LongConcurrentHashMapKMerCounterTest.class, IntArrayKMerCounterTest.class,
		AtomicIntegerArrayKMerCounterTest.class, ConcurrentBucketMapTestSuite.class,
		ConcurrentBucketMapKMerCounterTest.class, LongConcurrentHashMapKMerCounterParallelismTest.class,
		AtomicIntegerArrayKMerCounterParallelismTest.class, ConcurrentBucketMapKMerCounterParallelismTest.class,
		NoPrintingTest.class })
public class KMerTestSuite {
	@BeforeClass
	public static void setUp() throws IOException {
		ChromosomeResource[] chromosomeResources = { ChromosomeResource.HOMO_SAPIENS_Y,
				ChromosomeResource.HOMO_SAPIENS_X };
		for (ChromosomeResource chromosomeResource : chromosomeResources) {
			ChromosomeResource.readSubSequences(chromosomeResource);
		}
	}
}
