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
package lock.allornothing.studio;

import edu.wustl.cse231s.IntendedForStaticAccessOnlyError;
import edu.wustl.cse231s.NotYetImplementedException;
import edu.wustl.cse231s.concurrent.Interruptible;
import locking.core.banking.AccountWithLock;
import locking.core.banking.TransferResult;
import locking.core.banking.TransferUtils;

/**
 * @author __STUDENT_NAME__
 * @author Ben Choi (benjaminchoi@wustl.edu)
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class BankAccountLockTrying {

	private BankAccountLockTrying() {
		throw new IntendedForStaticAccessOnlyError();
	}

	/**
	 * Transfers a given amount of money from the sender to the recipient. This
	 * method should be made thread-safe with the use of the ReentrantLock
	 * associated with the sender and recipient. Make use of the
	 * checkBalanceAndTransfer method given to you to quickly check if there is
	 * enough money in the sender's account to transfer the given amount to the
	 * recipient. The method should return result of the attempted transaction.
	 * 
	 * Note: the idea for this method was taken from Java Concurrency in Practice
	 * (Chapter 13, pg. 280) and was slightly modified to fit our purposes
	 * 
	 * @param sender,
	 *            the AccountWithLock instance of the sender
	 * @param recipient,
	 *            the AccountWithLock instance of the recipient
	 * @param amount,
	 *            the amount the sender is trying to send to the recipient
	 * @return the result of the attempted transaction
	 */
	public static TransferResult tryTransferMoney(AccountWithLock sender, AccountWithLock recipient, int amount) {
		throw new NotYetImplementedException();
	}

	/**
	 * Transfers money from the specified sender account to the recipient account,
	 * repeatedly if necessary.
	 * 
	 * @param sender,
	 *            the AccountWithLock instance of the sender
	 * @param recipient,
	 *            the AccountWithLock instance of the recipient
	 * @param amount,
	 *            the amount the sender is trying to send to the recipient
	 * @param failureAction
	 *            action to take if acquiring both locks fails
	 * @return the result of the attempted transaction
	 */
	public static TransferResult transferMoney(AccountWithLock sender, AccountWithLock recipient, int amount,
			Interruptible failureAction) throws InterruptedException {
		while (true) {
			TransferResult transferResult = tryTransferMoney(sender, recipient, amount);
			if (transferResult != TransferResult.UNABLE_TO_ATTEMPT_AT_THIS_TIME) {
				return transferResult;
			} else {
				failureAction.run();
			}
		}
	}

	/**
	 * Transfers money from the specified sender account to the recipient account,
	 * repeatedly if necessary with default failure action.
	 * 
	 * @param sender,
	 *            the AccountWithLock instance of the sender
	 * @param recipient,
	 *            the AccountWithLock instance of the recipient
	 * @param amount,
	 *            the amount the sender is trying to send to the recipient
	 * @return the result of the attempted transaction
	 */
	public static TransferResult transferMoney(AccountWithLock sender, AccountWithLock recipient, int amount)
			throws InterruptedException {
		return transferMoney(sender, recipient, amount, () -> {
			if (Thread.currentThread().isInterrupted()) {
				throw new InterruptedException();
			}
			Thread.yield();
		});
	}
}
