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

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import edu.wustl.cse231s.sleep.SleepUtils;
import lock.allornothing.studio.BankAccountLockTrying;
import locking.core.banking.AccountWithLock;
import locking.core.banking.DefaultAccount;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link BankAccountLockTrying#transferMoneyAndWait(AccountWithLock, AccountWithLock, int)}
 */
@RunWith(Parameterized.class)
public class BankAccountLockInitiallyHeldTest {
	private final List<AccountToInitiallyLock> accountsToInitiallyLock;

	public BankAccountLockInitiallyHeldTest(List<AccountToInitiallyLock> accountsToInitiallyLock) {
		this.accountsToInitiallyLock = accountsToInitiallyLock;
	}

	@Test(timeout = 5000)
	public void test() throws InterruptedException {
		int startingSenderBalance = 1000;
		int transferAmount = 400;
		int id = 71;
		AccountWithLock sender = new DefaultAccount(id++, startingSenderBalance);
		AccountWithLock receiver = new DefaultAccount(id++, 0);

		CountDownLatch transferLatch = new CountDownLatch(1);
		CountDownLatch initialLockLatch = new CountDownLatch(1);

		startThread(() -> {
			try {
				transferLatch.await();
			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
			}

			if (accountsToInitiallyLock.contains(AccountToInitiallyLock.SENDER)) {
				sender.getLock().lock();
			}
			if (accountsToInitiallyLock.contains(AccountToInitiallyLock.RECEIVER)) {
				receiver.getLock().lock();
			}
			initialLockLatch.countDown();
			SleepUtils.sleep(1000);
			if (accountsToInitiallyLock.contains(AccountToInitiallyLock.SENDER)) {
				sender.getLock().unlock();
			}
			if (accountsToInitiallyLock.contains(AccountToInitiallyLock.RECEIVER)) {
				receiver.getLock().unlock();
			}
		});

		startAndJoinThread(() -> {
			transferLatch.countDown();
			try {
				initialLockLatch.await();
			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
			}

			try {
				BankAccountLockTrying.transferMoney(sender, receiver, transferAmount);
			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
			}
		});

		assertEquals(startingSenderBalance - transferAmount, sender.getBalance());
		assertEquals(transferAmount, receiver.getBalance());
	}

	private static enum AccountToInitiallyLock {
		SENDER, RECEIVER;

	}

	@Parameters(name = "accountsToInitiallyLock={0}")
	public static Collection<Object[]> getConstructorArguments() {
		List<Object[]> result = new LinkedList<>();
		result.add(new Object[] { Arrays.asList(AccountToInitiallyLock.SENDER) });
		result.add(new Object[] { Arrays.asList(AccountToInitiallyLock.RECEIVER) });
		result.add(new Object[] { Arrays.asList(AccountToInitiallyLock.SENDER, AccountToInitiallyLock.RECEIVER) });
		return result;
	}

	private static Thread startThread(Runnable body) {
		Thread thread = new Thread(body);
		thread.start();
		return thread;
	}

	private static Thread startAndJoinThread(Runnable body) throws InterruptedException {
		Thread thread = startThread(body);
		thread.join();
		return thread;
	}
}
