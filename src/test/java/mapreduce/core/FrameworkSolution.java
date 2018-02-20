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

import mapreduce.framework.core.MapReduceFramework;
import mapreduce.framework.core.Mapper;
import mapreduce.framework.fun.single.OneConcurrentHashMapToRuleThemAllMapReduceFramework;
import mapreduce.framework.fun.stream.StreamMapReduceFramework;
import mapreduce.framework.lab.matrix.MatrixMapReduceFramework;
import mapreduce.framework.lab.simple.SimpleMapReduceFramework;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public enum FrameworkSolution {
	INSTRUCTOR() {
		@Override
		public <E, K, V, A, R> MapReduceFramework<E, K, V, A, R> create(Mapper<E, K, V> mapper,
				Collector<V, A, R> collector) {
			throw new UnsupportedOperationException();
		}
	},
	STUDENT_SIMPLE() {
		@Override
		public <E, K, V, A, R> MapReduceFramework<E, K, V, A, R> create(Mapper<E, K, V> mapper,
				Collector<V, A, R> collector) {
			return new SimpleMapReduceFramework<>(mapper, collector);
		}
	},
	STUDENT_MATRIX() {
		@Override
		public <E, K, V, A, R> MapReduceFramework<E, K, V, A, R> create(Mapper<E, K, V> mapper,
				Collector<V, A, R> collector) {
			return new MatrixMapReduceFramework<>(mapper, collector);
		}
	},
	STUDENT_ONE_CONCURRENT_HASH_MAP_TO_RULE_THEM_ALL() {
		@Override
		public <E, K, V, A, R> MapReduceFramework<E, K, V, A, R> create(Mapper<E, K, V> mapper,
				Collector<V, A, R> collector) {
			return new OneConcurrentHashMapToRuleThemAllMapReduceFramework<>(mapper, collector);
		}
	},
	STUDENT_STREAM() {
		@Override
		public <E, K, V, A, R> MapReduceFramework<E, K, V, A, R> create(Mapper<E, K, V> mapper,
				Collector<V, A, R> collector) {
			return new StreamMapReduceFramework<>(mapper, collector);
		}
	};

	public abstract <E, K, V, A, R> MapReduceFramework<E, K, V, A, R> create(Mapper<E, K, V> mapper,
			Collector<V, A, R> collector);
	
	public static FrameworkSolution[] getInstructorPlusSimpleValues() {
		return new FrameworkSolution[] {INSTRUCTOR, STUDENT_SIMPLE };
	}
	public static FrameworkSolution[] getInstructorPlusRequiredValues() {
		return new FrameworkSolution[] {INSTRUCTOR, STUDENT_SIMPLE, STUDENT_MATRIX };
	}
}
