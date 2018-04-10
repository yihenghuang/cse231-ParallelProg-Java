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
package kmer.assignment;

import static edu.wustl.cse231s.v5.V5.launchApp;

import java.util.List;

import edu.wustl.cse231s.sleep.SleepUtils;
import edu.wustl.cse231s.timing.ImmutableTimer;
import kmer.core.KMerCount;
import kmer.core.KMerCounter;
import kmer.core.KMerUtils;
import kmer.util.KMerResource;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class CompareKMerCounters {
	public static void compareKMerCounters(KMerResource resource, int k, KMerCounter... kMerCounters) {
		System.out.println(resource);
		System.out.println(String.format("k: %d", k));
		System.out.println(String.format("availableProcessors: %d", Runtime.getRuntime().availableProcessors()));
		System.out.println();
		byte[] polyATail = KMerUtils.unpackInt(0, k);
		List<byte[]> sequences = resource.getSubSequences();
		launchApp(() -> {
			final int ITERATION_COUNT = 3;
			for (int iteration = 0; iteration < ITERATION_COUNT; iteration++) {
				System.out.println(String.format("iteration: %d", iteration));
				for (KMerCounter counter : kMerCounters) {
					ImmutableTimer timer = new ImmutableTimer(
							String.format("%40s", counter.getClass().getSimpleName()));
					KMerCount kMerCount = counter.parse(sequences, k);
					timer.markAndPrintResults(String.format(" polyA-tail: %d;", kMerCount.getCount(polyATail)));
					for (int i = 0; i < 3; i++) {
						Runtime.getRuntime().gc();
						SleepUtils.sleep(100);
					}
				}
				System.out.println();
			}
		});
	}
}
