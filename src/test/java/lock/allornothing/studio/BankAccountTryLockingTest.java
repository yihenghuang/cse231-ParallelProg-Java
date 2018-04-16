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

import static edu.wustl.cse231s.v5.V5.async;
import static edu.wustl.cse231s.v5.V5.finish;
import static edu.wustl.cse231s.v5.V5.launchApp;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import lock.allornothing.studio.BankAccountLockTrying;
import locking.core.banking.AccountWithLock;
import locking.core.banking.DefaultAccount;
import locking.core.banking.TransferResult;

/**
 * @author Ben Choi (benjaminchoi@wustl.edu)
 * 
 *         {@link BankAccountLockTrying#transferMoneyAndWait(AccountWithLock, AccountWithLock, int)}
 */
public class BankAccountTryLockingTest {
	@Test(timeout = 5000)
	public void test() throws InterruptedException {
		DefaultAccount[] accounts = new DefaultAccount[20];
		for (int i = 0; i < 20; ++i) {
			accounts[i] = new DefaultAccount(i, 5000);
		}
		for (int i = 0; i < 20; ++i) {
			for (int j = 19; j >= 0; --j) {
				if (i != j) {
					int iBalance = accounts[i].getBalance();
					int jBalance = accounts[j].getBalance();
					assertEquals("The transfer should have been successful, but it was not",
							BankAccountLockTrying.transferMoney(accounts[i], accounts[j], 50), TransferResult.SUCCESS);
					assertEquals("The sender's balance did not update properly", iBalance - 50,
							accounts[i].getBalance());
					assertEquals("The recipient's balance did not update properly", jBalance + 50,
							accounts[j].getBalance());
				} else {
					assertEquals("The transfer should been ommitted as the sender and recipient are the same account",
							BankAccountLockTrying.transferMoney(accounts[i], accounts[j], 50),
							TransferResult.INTRA_ACCOUNT_TRANSFER_OMITTED);
				}
			}
		}
		for (int i = 0; i < 20; ++i) {
			assertEquals("The account balances did not update properly", 5000, accounts[i].getBalance());
		}
		launchApp(() -> {
			finish(() -> {
				for (int i = 0; i < 1000; ++i) {
					async(() -> {
						assertEquals("The transfer failed to work asynchronously",
								BankAccountLockTrying.transferMoney(accounts[0], accounts[1], 2),
								TransferResult.SUCCESS);
						assertEquals("The transfer failed to work asynchronously",
								BankAccountLockTrying.transferMoney(accounts[1], accounts[0], 1),
								TransferResult.SUCCESS);
					});
				}
			});
		});
		assertEquals("The account balances did not update properly in parallel", 4000, accounts[0].getBalance());
		assertEquals("The account balances did not update properly in parallel", 6000, accounts[1].getBalance());
	}
}
