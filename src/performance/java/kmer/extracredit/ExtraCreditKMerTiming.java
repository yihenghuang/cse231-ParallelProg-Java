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
package kmer.extracredit;

import static edu.wustl.cse231s.v5.V5.launchApp;

import java.util.List;

import edu.wustl.cse231s.sleep.SleepUtils;
import edu.wustl.cse231s.timing.ImmutableTimer;
import kmer.core.KMerCount;
import kmer.core.KMerUtils;
import kmer.instructor.InstructorKMerTestUtils;
import kmer.util.KMerResource;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class ExtraCreditKMerTiming {
	public static void main(String[] args) throws Exception {
		int k = 23;
		byte[] polyATail = KMerUtils.unpackLong(0L, k);
		List<byte[]> sequences = KMerResource.Y_CHROMOSOME_COMPLETE.getSubSequences();
		launchApp(() -> {
			final int ITERATION_COUNT = 7;
			for (int iteration = 0; iteration < ITERATION_COUNT; iteration++) {
				ImmutableTimer timer = new ImmutableTimer("OpenEndedKMerCounter");

				OptimisticConcurrencyKMerCounter counter = new OptimisticConcurrencyKMerCounter();
				KMerCount actualKMerCount = counter.parse(sequences, k);
				InstructorKMerTestUtils.checkSampleKMers(sequences, k, 1_000, actualKMerCount);

				timer.markAndPrintResults(actualKMerCount.getCount(polyATail));

				humblyRequestThatGarbageCollectionBePerformedNow();
			}
		});
	}

	private static void humblyRequestThatGarbageCollectionBePerformedNow() {
		for (int i = 0; i < 3; i++) {
			Runtime.getRuntime().gc();
			SleepUtils.sleep(100);
		}
	}
}
