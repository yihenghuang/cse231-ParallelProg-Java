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
package mapreduce.apps.friends.studio;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import edu.wustl.cse231s.NotYetImplementedException;
import mapreduce.apps.friends.core.AccountId;
import mapreduce.apps.friends.core.MutualFriendIds;
import mapreduce.collector.studio.ClassicReducer;

/**
 * A reducer for the mutual friends program. Given sets of account IDs, it finds
 * the intersection of those sets. In other words, given a set of A's friends
 * and a set of B's friends, it will find the accounts that are friends of both
 * A and B.
 * 
 * @author __STUDENT_NAME__
 * @author Finn Voichick
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class MutualFriendsClassicReducer implements ClassicReducer<Set<AccountId>, MutualFriendIds> {

	/**
	 * Creates a {@code MutualFriendsClassicReducer} using the given array of
	 * account IDs as the "universe" of accounts.
	 * 
	 * @param universe
	 */
	public MutualFriendsClassicReducer(AccountId[] universe) {
		this.universe = Collections.unmodifiableCollection(Arrays.asList(universe));
	}

	/**
	 * Converts several sets of account IDs into a single set of account IDs that is
	 * the intersection of all of the sets. In other words, the returned
	 * {@code MutualFriendIds} object represents all of the accounts that exist in
	 * all of the passed-in sets.
	 * 
	 * @see MutualFriendIds
	 */
	@Override
	public Function<List<Set<AccountId>>, MutualFriendIds> finisher() {

		return new Function<List<Set<AccountId>>, MutualFriendIds>() {

			@Override
			public MutualFriendIds apply(List<Set<AccountId>> t) {

				MutualFriendIds result = MutualFriendIds.createInitializedToUniverse(universe);
				throw new NotYetImplementedException();
			}

		};

	}

	private final Collection<AccountId> universe;
}
