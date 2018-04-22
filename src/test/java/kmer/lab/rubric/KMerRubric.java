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
package kmer.lab.rubric;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import edu.wustl.cse231s.rubric.RubricCategory;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface KMerRubric {
	public static enum Category implements RubricCategory {
		THRESHOLD_SLICES("Correct ThresholdSlices", 0.1),
		LONG_MAP_CORRECTNESS("Correct Long ConcurrentHashMap", 0.10),
		LONG_MAP_PARALLELISM("Parallel Long ConcurrentHashMap", 0.05),
		INT_ARRAY("Correct Int Array", 0.15),
		ATOMIC_INTEGER_ARRAY_CORRECTNESS("Correct AtomicIntegerArray", 0.10),
		ATOMIC_INTEGER_ARRAY_PARALLELISM("Parallel AtomicIntegerArray", 0.05),
		BUCKET_COUNTER_CORRECTNESS("Correct ConcurrentBucketHashMapCounter", 0.05),
		BUCKET_COUNTER_PARALLELISM("Parallel ConcurrentBucketHashMapCounter", 0.05),
		BUCKET_MAP("Correct ConcurrentBucketHashMap", 0.25),
		NO_PRINTING(null, 0.0);

		private final String title;
		private final double portion;

		private Category(String title, double portion) {
			this.title = title;
			this.portion = portion;
		}

		public String getTitle() {
			return title;
		}

		@Override
		public double getPortion() {
			return portion;
		}
	}

	Category[] value();
}
