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

package mapreduce.core;

import java.util.stream.Collector;

import mapreduce.framework.core.ExceptionCollector;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public enum CollectorSolution {
	INSTRUCTOR() {
		@Override
		public <E, V, A, R> Collector<V, A, R> create(TestApplication testApplication, E[] input) {
			throw new UnsupportedOperationException();
		}
	},
	STUDENT_FINISHER_ONLY() {
		@Override
		public <E, V, A, R> Collector<V, A, R> create(TestApplication testApplication, E[] input) {
			throw new UnsupportedOperationException();
		}
	},
	STUDENT_COMPLETE() {
		@Override
		public <E, V, A, R> Collector<V, A, R> create(TestApplication testApplication, E[] input) {
			return testApplication.createStudentCollector(input);
		}
	},
	NOT_USED_STUDENT_WARM_UP() {
		@Override
		public <E, V, A, R> Collector<V, A, R> create(TestApplication testApplication, E[] input) {
			switch (testApplication) {
			case WORD_COUNT:
				return new ExceptionCollector<>(testApplication.name());
			case MUTUAL_FRIENDS:
				return new ExceptionCollector<>(testApplication.name());
			default:
				throw new IllegalArgumentException(testApplication.name());
			}
		}
	};

	public abstract <E, V, A, R> Collector<V, A, R> create(TestApplication testApplication, E[] input);

	public static CollectorSolution[] getNonWarmUpValues() {
		return new CollectorSolution[] { INSTRUCTOR, STUDENT_FINISHER_ONLY, STUDENT_COMPLETE };
	}
}
