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

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import edu.wustl.cse231s.junit.JUnitUtils;
import mapreduce.apps.friends.core.Account;
import mapreduce.apps.friends.core.AccountId;
import mapreduce.apps.friends.util.AccountDatabase;
import mapreduce.framework.warmup.friends.MutualFriendsConcreteStaticMapReduce;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link MutualFriendsConcreteStaticMapReduce#reduceCombine(List, List)}
 */
public class ReduceCombineTest {
	@Rule
	public TestRule timeout = JUnitUtils.createTimeoutRule();

	@Test
	public void test() {
		Account[] accounts = AccountDatabase.MUTUAL_FRIENDS_STEVE_KRENZEL_EXAMPLE.getAccounts();
		List<Set<AccountId>> expected = new LinkedList<>();
		List<Set<AccountId>> front = new LinkedList<>();
		List<Set<AccountId>> back = new LinkedList<>();
		
		int mid = accounts.length/2;
		int i =0;
		for (Account account : accounts) {
			Set<AccountId> friendIds = account.getFriendIds();
			expected.add(friendIds);
			((i<mid)?front:back).add(friendIds);
			i++;
		}
		
		List<Set<AccountId>> actual = new LinkedList<>();
		MutualFriendsConcreteStaticMapReduce.reduceCombine(actual, front);
		MutualFriendsConcreteStaticMapReduce.reduceCombine(actual, back);

		assertEquals(expected.size(), actual.size());
		Iterator<Set<AccountId>> expectedIterator = expected.iterator();
		Iterator<Set<AccountId>> actualIterator = actual.iterator();
		while (expectedIterator.hasNext()) {
			assertEquals(expectedIterator.next(), actualIterator.next());
		}
	}
}
