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
package lock.order.studio;

import edu.wustl.cse231s.IntendedForStaticAccessOnlyError;
import edu.wustl.cse231s.NotYetImplementedException;
import locking.core.banking.Account;
import locking.core.banking.TransferResult;
import locking.core.banking.TransferUtils;

/**
 * @author Yiheng Huang
 * @author Ben Choi (benjaminchoi@wustl.edu)
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class BankAccountLockOrdering {
	private BankAccountLockOrdering() {
		throw new IntendedForStaticAccessOnlyError();
	}

	/**
	 * Transfers a given amount of money from the sender to the recipient. This
	 * method should be made thread-safe with the use of synchronized and a lock
	 * ordering based on account numbers. In other words, compare the account
	 * numbers of the sender and recipient and decide which account to synchronize
	 * first depending on those account numbers. Make use of the
	 * checkBalanceAndTransfer method given to you to quickly check if there is
	 * enough money in the sender's account to transfer the given amount to the
	 * recipient. The method should return the result of the transfer.
	 * 
	 * Note: the idea for this method was taken from Java Concurrency in Practice
	 * (Chapter 10, pg. 209) and was slightly modified to fit our purposes
	 * 
	 * @param sender,
	 *            the Account instance of the sender
	 * @param recipient,
	 *            the Account instance of the recipient
	 * @param amount,
	 *            the amount the sender is trying to send to the recipient
	 * @return result of the transfer
	 */
	public static TransferResult transferMoney(Account sender, Account recipient, int amount) {
		throw new NotYetImplementedException();
	}

}
