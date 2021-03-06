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
package kmer.lab.concurrentbuckethashmap;

import static edu.wustl.cse231s.v5.V5.forall;

import java.util.List;
import java.util.concurrent.ExecutionException;

import kmer.core.KMerCount;
import kmer.core.KMerCounter;
import kmer.core.KMerUtils;
import kmer.core.codecs.LongKMerCodec;
import kmer.core.map.MapKMerCount;
import kmer.lab.util.ThresholdSlices;
import slice.core.Slice;

/**
 * @author Yiheng Huang
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class ConcurrentBucketHashMapKMerCounter implements KMerCounter {
	@Override
	public KMerCount parse(List<byte[]> sequences, int k) throws InterruptedException, ExecutionException {

		ConcurrentBucketHashMap<Long, Integer> amap = new ConcurrentBucketHashMap<Long, Integer>(1024);
		List<Slice<byte[]>> s = ThresholdSlices.createSlicesBelowReasonableThreshold(sequences, k);
		forall(s, (slice) -> {
			for (int i = slice.getMinInclusive(); i < slice.getMaxExclusive(); ++i) {
				long key = KMerUtils.toPackedLong(slice.getOriginalUnslicedData(), i, k);
				amap.compute(key, (Key, j) -> {
					if (j == null) {
						return 1;
					} else {
						j++;
						return j;
					}
				});
			}
		});
		return new MapKMerCount<>(k, amap, LongKMerCodec.INSTANCE);
	}
}
