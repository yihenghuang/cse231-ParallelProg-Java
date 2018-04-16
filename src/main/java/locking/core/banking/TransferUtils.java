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

package locking.core.banking;

import java.util.Objects;

import edu.wustl.cse231s.IntendedForStaticAccessOnlyError;

/**
 * @author Ben Choi
 */
public class TransferUtils {
	private TransferUtils() {
		throw new IntendedForStaticAccessOnlyError();
	}

	/**
	 * This method checks whether the sender has enough money in her bank account to
	 * transfer the given amount to the recipient. It also checks to make sure that
	 * the sender and recipient are not the same people. If all of those conditions
	 * are met, it will successfully transfer the funds.
	 * 
	 * @param sender,
	 *            the Account instance of the sender
	 * @param recipient,
	 *            the Account instance of the recipient
	 * @param amount,
	 *            the amount the sender is trying to send to the recipient
	 * @return the result of this transaction
	 */
	public static TransferResult checkBalanceAndTransfer(Account sender, Account recipient, int amount) {
		Objects.requireNonNull(sender);
		Objects.requireNonNull(recipient);

		if (sender.isHeldByCurrentThread()) {
			// pass
		} else {
			throw new IllegalStateException("current thread does not hold lock for sender: " + sender);
		}
		if (recipient.isHeldByCurrentThread()) {
			// pass
		} else {
			throw new IllegalStateException("current thread does not hold lock for recipient: " + recipient);
		}
		if (sender.getBalance() < amount) {
			return TransferResult.INSUFFICIENT_FUNDS;
		} else if (sender.getUniqueIdNumber() == recipient.getUniqueIdNumber()) {
			return TransferResult.INTRA_ACCOUNT_TRANSFER_OMITTED;
		} else {
			sender.withdraw(amount);
			recipient.deposit(amount);
			return TransferResult.SUCCESS;
		}
	}
}
