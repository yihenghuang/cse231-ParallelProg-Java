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

import static edu.wustl.cse231s.v5.V5.forall;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;

import edu.wustl.cse231s.NotYetImplementedException;
import edu.wustl.cse231s.util.KeyValuePair;
import edu.wustl.cse231s.util.OrderedPair;
import mapreduce.apps.friends.core.Account;
import mapreduce.apps.friends.core.AccountId;
import mapreduce.apps.friends.core.MutualFriendIds;
import mapreduce.apps.friends.studio.MutualFriendsClassicReducer;
import mapreduce.apps.friends.studio.MutualFriendsMapper;
import mapreduce.collector.studio.ClassicReducer;
import mapreduce.framework.lab.simple.SimpleMapReduceFramework;

/**
 * @author Yiheng Huang
 * @author Finn Voichick
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class MutualFriendsConcreteStaticMapReduce {
	/**
	 * {@link MutualFriendsMapper#map(Account, BiConsumer)};
	 */
	static void map(Account account, BiConsumer<OrderedPair<AccountId>, Set<AccountId>> keyValuePairConsumer) {
		throw new NotYetImplementedException();
	}

	/**
	 * {@link ClassicReducer#supplier()};
	 */
	static List<Set<AccountId>> reduceCreateList() {
		throw new NotYetImplementedException();
	}

	/**
	 * {@link ClassicReducer#accumulator()};
	 */
	static void reduceAccumulate(List<Set<AccountId>> list, Set<AccountId> v) {
		throw new NotYetImplementedException();
	}

	/**
	 * NOTE: this method is not used for the warm-up or simple framework
	 * 
	 * {@link ClassicReducer#combiner()};
	 */
	static void reduceCombine(List<Set<AccountId>> a, List<Set<AccountId>> b) {
		throw new NotYetImplementedException();
	}

	/**
	 * {@link MutualFriendsClassicReducer#finisher()};
	 */
	static MutualFriendIds reduceFinish(List<Set<AccountId>> list) {
		Set<AccountId> universe = null; // imagine this being a set of a billion
										// account ids
		MutualFriendIds result = MutualFriendIds.createInitializedToUniverse(universe);
		throw new NotYetImplementedException();
	}

	/**
	 * {@link SimpleMapReduceFramework#mapAll(E[])};
	 */
	static List<KeyValuePair<OrderedPair<AccountId>, Set<AccountId>>>[] mapAll(Account[] input)
			throws InterruptedException, ExecutionException {
		throw new NotYetImplementedException();
	}

	/**
	 * {@link SimpleMapReduceFramework#accumulateAll(List<KeyValuePair<K,V>[])};
	 */
	static Map<OrderedPair<AccountId>, List<Set<AccountId>>> accumulateAll(
			List<KeyValuePair<OrderedPair<AccountId>, Set<AccountId>>>[] mapAllResults) {
		throw new NotYetImplementedException();
	}

	/**
	 * {@link SimpleMapReduceFramework#finishAll(Map)};
	 */
	static Map<OrderedPair<AccountId>, MutualFriendIds> finishAll(
			Map<OrderedPair<AccountId>, List<Set<AccountId>>> accumulateAllResult)
			throws InterruptedException, ExecutionException {
		throw new NotYetImplementedException();
	}

	public static Map<OrderedPair<AccountId>, MutualFriendIds> mapReduce(Account[] input)
			throws InterruptedException, ExecutionException {
		List<KeyValuePair<OrderedPair<AccountId>, Set<AccountId>>>[] mapAllResult = mapAll(input);
		Map<OrderedPair<AccountId>, List<Set<AccountId>>> accumulateAllResult = accumulateAll(mapAllResult);
		return finishAll(accumulateAllResult);
	}
}
