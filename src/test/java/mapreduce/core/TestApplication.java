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

import mapreduce.apps.cards.studio.CardMapper;
import mapreduce.apps.cholera.studio.CholeraMapper;
import mapreduce.apps.friends.core.Account;
import mapreduce.apps.friends.studio.MutualFriendsClassicReducer;
import mapreduce.apps.friends.studio.MutualFriendsMapper;
import mapreduce.apps.friends.util.MutualFriendsTestUtils;
import mapreduce.apps.intsum.studio.IntegerSumClassicReducer;
import mapreduce.apps.kmer.studio.KMerMapper;
import mapreduce.apps.wordcount.studio.WordCountMapper;
import mapreduce.framework.core.Mapper;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@SuppressWarnings("unchecked")
public enum TestApplication {
	CARDS() {
		@Override
		public <E, K, V> Mapper<E, K, V> createStudentMapper() {
			return (Mapper<E, K, V>) new CardMapper();
		}

		@Override
		public <E, V, A, R> Collector<V, A, R> createStudentCollector(E[] input) {
			return (Collector<V, A, R>) new IntegerSumClassicReducer();
		}
	},
	CHOLERA() {
		@Override
		public <E, K, V> Mapper<E, K, V> createStudentMapper() {
			return (Mapper<E, K, V>) new CholeraMapper();
		}

		@Override
		public <E, V, A, R> Collector<V, A, R> createStudentCollector(E[] input) {
			return (Collector<V, A, R>) new IntegerSumClassicReducer();
		}
	},
	TWELVE_MER() {
		@Override
		public <E, K, V> Mapper<E, K, V> createStudentMapper() {
			return (Mapper<E, K, V>) new KMerMapper(12);
		}

		@Override
		public <E, V, A, R> Collector<V, A, R> createStudentCollector(E[] input) {
			return (Collector<V, A, R>) new IntegerSumClassicReducer();
		}
	},
	WORD_COUNT() {
		@Override
		public <E, K, V> Mapper<E, K, V> createStudentMapper() {
			return (Mapper<E, K, V>) new WordCountMapper();
		}

		@Override
		public <E, V, A, R> Collector<V, A, R> createStudentCollector(E[] input) {
			return (Collector<V, A, R>) new IntegerSumClassicReducer();
		}
	},
	MUTUAL_FRIENDS() {
		@Override
		public <E, K, V> Mapper<E, K, V> createStudentMapper() {
			return (Mapper<E, K, V>) new MutualFriendsMapper();
		}

		@Override
		public <E, V, A, R> Collector<V, A, R> createStudentCollector(E[] input) {
			return (Collector<V, A, R>) new MutualFriendsClassicReducer(
					MutualFriendsTestUtils.getUniverseOfIds((Account[]) input));
		}
	};

	public abstract <E, K, V> Mapper<E, K, V> createStudentMapper();

	public abstract <E, V, A, R> Collector<V, A, R> createStudentCollector(E[] input);
}
