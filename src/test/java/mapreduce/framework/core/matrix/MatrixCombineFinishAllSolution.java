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

package mapreduce.framework.core.matrix;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collector;

import mapreduce.framework.lab.matrix.AccessMatrixFrameworkUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public enum MatrixCombineFinishAllSolution {
	INSTRUCTOR() {
		@Override
		public <E, K, V, A, R> Map<K, R> combineAndFinishAll(Map<K, A>[][] mapAndAccumulateAllResults,
				Collector<V, A, R> collector) throws InterruptedException, ExecutionException {
			throw new UnsupportedOperationException();
		}
	},
	STUDENT_ASSIGNMENT() {
		@Override
		public <E, K, V, A, R> Map<K, R> combineAndFinishAll(Map<K, A>[][] mapAndAccumulateAllResults,
				Collector<V, A, R> collector) throws InterruptedException, ExecutionException {
			return AccessMatrixFrameworkUtils.combineAndFinishAll(mapAndAccumulateAllResults, collector);
		}
	};
	public abstract <E, K, V, A, R> Map<K, R> combineAndFinishAll(Map<K, A>[][] mapAndAccumulateAllResults,
			Collector<V, A, R> collector) throws InterruptedException, ExecutionException;
}
