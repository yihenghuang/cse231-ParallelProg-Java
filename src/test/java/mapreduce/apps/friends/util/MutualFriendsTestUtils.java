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
package mapreduce.apps.friends.util;

import edu.wustl.cse231s.IntendedForStaticAccessOnlyError;
import mapreduce.apps.friends.core.Account;
import mapreduce.apps.friends.core.AccountId;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class MutualFriendsTestUtils {

	private MutualFriendsTestUtils() {
		throw new IntendedForStaticAccessOnlyError();
	}

	//
	// // credit: http://stevekrenzel.com/finding-friends-with-mapreduce
	//
	// private static Account[] steveKrenzelExampleData =
	// DatabaseUtils.createAccounts(
	// /* A-> */"BCD",
	// /* B-> */"ACDE",
	// /* C-> */"ABDE",
	// /* D-> */"ABCE",
	// /* E-> */"BCD"
	// );
	//
	// private static Account[] otherData = DatabaseUtils.createAccounts(
	// /* A-> */"BCDF",
	// /* B-> */"ADEF",
	// /* C-> */"A",
	// /* D-> */"ABEF",
	// /* E-> */"BD",
	// /* F-> */"ABD"
	// );
	//
	//
	// public static Account[] getSteveKrenzelExampleData() {
	// return steveKrenzelExampleData;
	// }
	//
	// public static Account[] getOtherData() {
	// return otherData;
	// }
	//
	public static AccountId[] getUniverseOfIds(Account[] accounts) {
		AccountId[] result = new AccountId[accounts.length];
		for (int i = 0; i < accounts.length; i++) {
			result[i] = accounts[i].getId();
		}
		return result;
	}
}
