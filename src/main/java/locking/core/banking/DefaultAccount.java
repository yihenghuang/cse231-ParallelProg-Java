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

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.jcip.annotations.NotThreadSafe;

/**
 * @author Ben Choi (benjaminchoi@wustl.edu)
 */
@NotThreadSafe
public class DefaultAccount implements AccountWithLock {

	private final int idNumber;
	private int balance;
	private final ReentrantLock lock;

	/**
	 * A bank account initialized with a given (and unique) account number and an
	 * initial balance
	 * 
	 * @param idNumber,
	 *            the unique ID associated with the account
	 * @param balance,
	 *            the initial balance of the account
	 */
	public DefaultAccount(int idNumber, int balance) {
		this.idNumber = idNumber;
		this.balance = balance;
		this.lock = new ReentrantLock();
	}

	@Override
	public int getUniqueIdNumber() {
		return idNumber;
	}

	@Override
	public int getBalance() {
		return balance;
	}

	@Override
	public Lock getLock() {
		return lock;
	}

	@Override
	public int deposit(int amount) {
		return balance += amount;
	}

	@Override
	public int withdraw(int amount) {
		return balance -= amount;
	}

	@Override
	public boolean isHeldByCurrentThread() {
		return Thread.holdsLock(this) || this.lock.isHeldByCurrentThread();
	}
}
