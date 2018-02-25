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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import edu.wustl.cse231s.junit.JUnitUtils;
import mapreduce.apps.friends.core.Account;
import mapreduce.apps.friends.core.AccountId;
import mapreduce.apps.friends.core.MutualFriendIds;
import mapreduce.apps.friends.util.AccountDatabase;
import mapreduce.apps.friends.util.MutualFriendsTestUtils;
import mapreduce.framework.warmup.friends.MutualFriendsConcreteStaticMapReduce;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link MutualFriendsConcreteStaticMapReduce#reduceFinish(List)}
 */
public class ReduceFinishTest {
	@Rule
	public TestRule timeout = JUnitUtils.createTimeoutRule();

	@Test
	public void test() {
		Account[] accounts = AccountDatabase.MUTUAL_FRIENDS_STEVE_KRENZEL_EXAMPLE.getAccounts();
		MutualFriendIds expected = MutualFriendIds
				.createInitializedToUniverse(Arrays.asList(MutualFriendsTestUtils.getUniverseOfIds(accounts)));
		expected.intersectWith(accounts[0].getFriendIds());
		expected.intersectWith(accounts[1].getFriendIds());

		List<Set<AccountId>> list = new LinkedList<>();
		list.add(accounts[0].getFriendIds());
		list.add(accounts[1].getFriendIds());
		MutualFriendIds actual = MutualFriendsConcreteStaticMapReduce.reduceFinish(list);
		assertEquals(expected, actual);
	}
}
