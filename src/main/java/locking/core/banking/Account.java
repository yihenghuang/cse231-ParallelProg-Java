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

/**
 * @author Ben Choi (benjaminchoi@wustl.edu)
 */

public interface Account {

	/**
	 * Gets the unique ID number associated with the account
	 * 
	 * @return accountNumber, the unique ID associated with the account
	 */
	public int getUniqueIdNumber();

	/**
	 * Gets the balance associated with the account
	 * 
	 * @return balance, the monetary balance of the account
	 */
	public int getBalance();

	/**
	 * Deposits an amount of money into the associated account
	 * 
	 * @param amount,
	 *            the amount of money you wish to deposit
	 * @return the updated balance, after depositing the money
	 */
	public int deposit(int amount);

	/**
	 * Withdraws an amount of money from the associated account
	 * 
	 * @param amount,
	 *            the amount of money you wish to withdraw
	 * @return the updated balance, after withdrawing the money
	 */
	public int withdraw(int amount);
	
	public boolean isHeldByCurrentThread();
}
