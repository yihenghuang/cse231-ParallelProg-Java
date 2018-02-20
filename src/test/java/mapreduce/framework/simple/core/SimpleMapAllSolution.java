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

package mapreduce.framework.simple.core;

import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.wustl.cse231s.util.KeyValuePair;
import mapreduce.apps.friends.core.Account;
import mapreduce.apps.wordcount.core.TextSection;
import mapreduce.core.TestApplication;
import mapreduce.framework.core.Mapper;
import mapreduce.framework.lab.simple.AccessSimpleFrameworkUtils;
import mapreduce.framework.warmup.friends.AccessWarmUpMutualFriendsUtils;
import mapreduce.framework.warmup.wordcount.AccessWarmUpWordCountUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public enum SimpleMapAllSolution {
	INSTRUCTOR() {
		@Override
		public <E, K, V> List<KeyValuePair<K, V>>[] mapAll(TestApplication application, E[] input,
				Mapper<E, K, V> mapper) throws InterruptedException, ExecutionException {
			throw new UnsupportedOperationException();
		}
	},
	STUDENT_WARMUP() {
		@SuppressWarnings("unchecked")
		@Override
		public <E, K, V> List<KeyValuePair<K, V>>[] mapAll(TestApplication application, E[] input,
				Mapper<E, K, V> mapper) throws InterruptedException, ExecutionException {
			switch (application) {
			case WORD_COUNT:
				return (List[]) AccessWarmUpWordCountUtils.mapAll((TextSection[]) input);
			case MUTUAL_FRIENDS:
				return (List[]) AccessWarmUpMutualFriendsUtils.mapAll((Account[]) input);
			default:
				throw new IllegalArgumentException(application.name());
			}
		}
	},
	STUDENT_ASSIGNMENT() {
		@Override
		public <E, K, V> List<KeyValuePair<K, V>>[] mapAll(TestApplication application, E[] input,
				Mapper<E, K, V> mapper) throws InterruptedException, ExecutionException {
			return AccessSimpleFrameworkUtils.mapAllOnly(input, mapper);
		}
	};
	public abstract <E, K, V> List<KeyValuePair<K, V>>[] mapAll(TestApplication application, E[] input,
			Mapper<E, K, V> mapper) throws InterruptedException, ExecutionException;
}
