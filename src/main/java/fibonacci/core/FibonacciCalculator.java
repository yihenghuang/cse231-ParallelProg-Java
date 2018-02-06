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
package fibonacci.core;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import edu.wustl.cse231s.asymptoticanalysis.OrderOfGrowth;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public interface FibonacciCalculator {
	/**
	 * Calculates the nth Fibonacci number
	 * 
	 * Fn = Fn-1 + Fn-2.
	 * F0 = 0
	 * F1 = 1
	 * 
	 * n=0: 0
	 * n=1: 1
	 * n=2: 1
	 * n=3: 2
	 * n=4: 3
	 * n=5: 5
	 * n=6: 8
	 * n=7: 13
	 * n=8: 21
	 * ... and so on.
	 * 
	 * @param n index in the Fibonacci sequence to calculate
	 * @return the BigInteger representation of the Fibonacci number at n
	 * 
	 */
	BigInteger fibonacci(int n) throws InterruptedException, ExecutionException;

	OrderOfGrowth getOrderOfGrowth();

	boolean isSusceptibleToStackOverflowError();
}
