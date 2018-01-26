/*******************************************************************************
 * Copyright (C) 2016-2018 Dennis Cosgrove
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
package count.assignment;

import static edu.wustl.cse231s.v5.V5.async;
import static edu.wustl.cse231s.v5.V5.finish;

import java.util.concurrent.ExecutionException;

import edu.wustl.cse231s.NotYetImplementedException;
import edu.wustl.cse231s.bioinformatics.Nucleobase;
import edu.wustl.cse231s.util.IntegerRange;
import midpoint.assignment.MidpointUtils;

/**
 * @author Yiheng Huang
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class NucleobaseCounting {
	/**
	 * Should sequentially count all of the instances of a specific nucleobase that
	 * are within the given range. This method should involve no parallelism. The
	 * range of numbers to search includes all indices in the array that are equal
	 * to or greater than the min index and less than the max index.
	 * 
	 * @param chromosome
	 *            The chromosome to examine, represented as an array of bytes. Each
	 *            byte in the sequence represents one nucleobase--that is, cytosine
	 *            (C), guanine (G), adenine (A), thymine (T), or unknown (N). A byte
	 *            is used (rather than char, int, String, or enum, for example)
	 *            because a byte is a primitive data type that takes up very little
	 *            memory.
	 * @param targetNucleobase
	 *            The nucleobase to look for in the chromosome. The byte value
	 *            representing this nucleobase occurs some number of times in the
	 *            chromosome. In other words, if you call nucleobase.toByte(), it
	 *            will be equal to of the bytes in the chromosome array.
	 * @param min
	 *            the lowest array index in the range to search, inclusive
	 * @param maxExclusive
	 *            the highest array index in the range to search, exclusive
	 * @return the total number of times that the given nucleobase occurs in the
	 *         given chromosome
	 */
	public static int countRangeSequential(byte[] chromosome, Nucleobase targetNucleobase, int min, int maxExclusive) {
		int count = 0;
		for(int i=min; i<maxExclusive; i++) {
			if ( chromosome[i]==targetNucleobase.toByte() ) count++; 
		}
		return count;
	}

	/**
	 * Sequentially counts all of the instances of a specific nucleobase. This
	 * method is very simple in that all it does is call
	 * {@link #countRangeSequential(byte[], Nucleobase, int, int)}. You do not need
	 * to change this method at all, but you will need to implement the
	 * {@code countRangeSequential} method.
	 * 
	 * @param chromosome
	 *            the chromosome to examine, as an array of bytes
	 * @param targetNucleobase
	 *            the nucleobase to look for in the chromosome
	 * @return The total number of times that the given nucleobase occurs in the
	 *         given chromosome.
	 */
	public static int countSequential(byte[] chromosome, Nucleobase targetNucleobase) {
		return countRangeSequential(chromosome, targetNucleobase, 0, chromosome.length);
	}

	/**
	 * This method should asynchronously count all of the instances of a specific
	 * nucleobase, creating two tasks. The chromosome should be split into two
	 * halves, and the "upper" half should be counted at the same time
	 * (asynchronously) as the "lower" half. You will need async and finish for this
	 * method.
	 * 
	 * @param chromosome
	 *            The chromosome to examine, represented as an array of bytes.
	 * @param targetNucleobase
	 *            The nucleobase to look for in the chromosome.
	 * @return The total number of times that the given nucleobase occurs in the
	 *         given chromosome.
	 * @throws InterruptedException,
	 *             ExecutionException
	 */
	public static int countParallelLowerUpperSplit(byte[] chromosome, Nucleobase targetNucleobase)
			throws InterruptedException, ExecutionException {
		int mid = chromosome.length/2;
		int [] arr = {0,0};
		finish(()->{
			async(() -> {
				for (int upperIndex=0; upperIndex<mid; upperIndex++) {
					if ( chromosome[upperIndex]==targetNucleobase.toByte() ) arr[0]++; 			
				}
			});
			for (int lowerIndex=mid; lowerIndex<chromosome.length; lowerIndex++) {
				if ( chromosome[lowerIndex]==targetNucleobase.toByte() ) arr[1]++;
			}
		});
		int count = arr[0]+arr[1];
		return count;
	}

	/**
	 * This method should asynchronously count all of the instances of a specific
	 * nucleobase, creating the given number of tasks. In other words, you should
	 * spawn n tasks, each of which counts for 1/n of the chromosome. For example,
	 * if numTasks is 8, the chromosome should be divided into 8 pieces, and each of
	 * these pieces should be counted in a separate asynchronous task. You should
	 * enclose each of these tasks in an async block, so that each task can run in
	 * parallel. Note: if numTasks is 2, the behavior of this method will be the
	 * same as countParallelLowerUpperSplit.
	 * 
	 * @param chromosome
	 *            The chromosome to examine, represented as an array of bytes.
	 * @param targetNucleobase
	 *            The nucleobase to look for in the chromosome.
	 * @param numTasks
	 *            The number of tasks to create.
	 * @return The total number of times that the given nucleobase occurs in the
	 *         given chromosome.
	 * @throws InterruptedException,
	 *             ExecutionException
	 */
	public static int countParallelNWaySplit(byte[] chromosome, Nucleobase targetNucleobase, int numTasks)
			throws InterruptedException, ExecutionException {
		int []sub = new int [numTasks+1];
		int sliceLength = chromosome.length/numTasks;
		int mode = chromosome.length%numTasks;
	
		finish(()->{
			int min = 0;
			for (int taskIndex : new IntegerRange(0,numTasks)) {
				int max = min+sliceLength;
				final int _min = min;
				async(()->{
					for (int arrayIndex = _min; arrayIndex<max; arrayIndex++) {
						if (chromosome[arrayIndex]==targetNucleobase.toByte() ) sub[taskIndex]++;
					}
				});
					min = max;
			}
		});
		for (int arrayIndex = numTasks*sliceLength; arrayIndex<chromosome.length; arrayIndex++) {
			if (chromosome[arrayIndex]==targetNucleobase.toByte()) sub[numTasks]++;
		}
		
		int total = 0;
		for (int subs : sub) {
			total+=subs;
		}
		return total;
	}

	/**
	 * This method should count all of the instances of a target nucleobase in a
	 * chromosome.
	 * 
	 * @see #countParallelDivideAndConquerKernel(byte[],Nucleobase,int,int,int)
	 * 
	 * @param chromosome
	 *            The chromosome to examine, represented as an array of bytes.
	 * @param targetNucleobase
	 *            The nucleobase to look for in the chromosome.
	 * @param threshold
	 *            The number of tasks to create.
	 * @return The total number of times that the given nucleobase occurs in the
	 *         given chromosome.
	 * @throws InterruptedException,
	 *             ExecutionException
	 */
	public static int countParallelDivideAndConquer(byte[] chromosome, Nucleobase targetNucleobase, int threshold)
			throws InterruptedException, ExecutionException {
		return countParallelDivideAndConquerKernel(chromosome, targetNucleobase, threshold, 0, chromosome.length);
	}

	/**
	 * This method should count all of the instances of a target nucleobase in a
	 * chromosome between [min,maxExclusive).
	 * 
	 * This method should perform each lower and upper half in parallel, creating
	 * tasks until the length between min and maxExclusive falls below threshold. At
	 * that point it should invoke countRangeSequential to do the rest of the work.
	 * 
	 * @see #countRangeSequential(byte[],Nucleobase,int,int)
	 * 
	 * @param chromosome
	 *            The chromosome to examine, represented as an array of bytes.
	 * @param targetNucleobase
	 *            The nucleobase to look for in the chromosome.
	 * @param threshold
	 *            The number of tasks to create.
	 * @param min
	 *            the lowest array index in the range to search, inclusive
	 * @param maxExclusive
	 *            the highest array index in the range to search, exclusive
	 * 
	 * @return The total number of times that the given nucleobase occurs in the
	 *         given chromosome.
	 * @throws InterruptedException,
	 *             ExecutionException
	 */
	static int countParallelDivideAndConquerKernel(byte[] chromosome, Nucleobase targetNucleobase, int threshold,
			int min, int maxExclusive) throws InterruptedException, ExecutionException {
		int sliceLength = maxExclusive - min;
		if (sliceLength > threshold) {
			int [] sub = {0,0};
			int mid = ( maxExclusive + min )/2;
			finish(()->{
				async(()->{
					sub[0] = countParallelDivideAndConquerKernel(chromosome, targetNucleobase, threshold, min, mid);
				});
				sub[1] = countParallelDivideAndConquerKernel(chromosome, targetNucleobase, threshold, mid, maxExclusive);
			});
			return sub[0]+sub[1];
		}else {
			return countRangeSequential(chromosome, targetNucleobase, min, maxExclusive);
		}
	}
}
