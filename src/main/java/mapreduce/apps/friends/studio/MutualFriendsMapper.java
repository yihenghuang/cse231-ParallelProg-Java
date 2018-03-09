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

import java.util.Set;
import java.util.function.BiConsumer;

import edu.wustl.cse231s.util.OrderedPair;
import mapreduce.apps.friends.core.Account;
import mapreduce.apps.friends.core.AccountId;
import mapreduce.framework.core.Mapper;

/**
 * A {@code Mapper} for the mutual friends program. Remember the example covered
 * in the slides. Given a single user, you're creating many key-value pairs. For
 * each key-value pair, the key is an {@code OrderedPair} of the original
 * account's ID and one of their friends. The values for all of these key-value
 * pairs are the same: a list of all of the account's friends.
 * 
 * As an example, say you call the map method on account A, who is friends with
 * B, C, and D. The key-value pairs written by this method would be:
 * 
 * [((A, B), [B, C, D]), ((A, C), [B, C, D]), ((A, D), [B, C, D]))
 * 
 * Review the slides for more information.
 * 
 * @author Yiheng Huang
 * @author Finn Voichick
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class MutualFriendsMapper implements Mapper<Account, OrderedPair<AccountId>, Set<AccountId>> {

	/**
	 * Given and account, produces key-value pairs where the key is a pair of
	 * account IDs, and each value is a list of all of the original account's
	 * friends.
	 * 
	 * @see Account
	 * @see AccountId
	 * @see OrderedPair
	 */
	@Override
	public void map(Account account, BiConsumer<OrderedPair<AccountId>, Set<AccountId>> keyValuePairConsumer) {
		Set<AccountId> friends = account.getFriendIds();
		for (AccountId id : friends) {

			keyValuePairConsumer.accept(new OrderedPair<AccountId>(account.getId(), id), friends);
		}
	}
}
