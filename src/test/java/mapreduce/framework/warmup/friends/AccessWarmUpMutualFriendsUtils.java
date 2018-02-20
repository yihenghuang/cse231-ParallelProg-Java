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
package mapreduce.framework.warmup.friends;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;

import edu.wustl.cse231s.util.KeyValuePair;
import edu.wustl.cse231s.util.OrderedPair;
import mapreduce.apps.friends.core.Account;
import mapreduce.apps.friends.core.AccountId;
import mapreduce.apps.friends.core.MutualFriendIds;
import mapreduce.framework.warmup.friends.MutualFriendsConcreteStaticMapReduce;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class AccessWarmUpMutualFriendsUtils {
	public static void map(Account account, BiConsumer<OrderedPair<AccountId>, Set<AccountId>> keyValuePairConsumer) {
		MutualFriendsConcreteStaticMapReduce.map(account, keyValuePairConsumer);
	}

	public static List<KeyValuePair<OrderedPair<AccountId>, Set<AccountId>>>[] mapAll(Account[] input)
			throws InterruptedException, ExecutionException {
		return MutualFriendsConcreteStaticMapReduce.mapAll(input);
	}

	public static Map<OrderedPair<AccountId>, List<Set<AccountId>>> accumulateAll(
			List<KeyValuePair<OrderedPair<AccountId>, Set<AccountId>>>[] mapAllResults) {
		return MutualFriendsConcreteStaticMapReduce.accumulateAll(mapAllResults);
	}

	public static Map<OrderedPair<AccountId>, MutualFriendIds> finishAll(
			Map<OrderedPair<AccountId>, List<Set<AccountId>>> accumulateAllResult) throws InterruptedException, ExecutionException {
		return MutualFriendsConcreteStaticMapReduce.finishAll(accumulateAllResult);
	}
}
