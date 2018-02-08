package tnx.lab.executor;
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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import count.assignment.NucleobaseCounting;
import edu.wustl.cse231s.NotYetImplementedException;
import edu.wustl.cse231s.bioinformatics.Nucleobase;
import edu.wustl.cse231s.bioinformatics.io.resource.ChromosomeResource;
import edu.wustl.cse231s.timing.ImmutableTimer;
import tnx.lab.executor.XNucleobaseCounting;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class NucleobaseCountTiming {
	private static void timeCountSequential(byte[] chromosome, Nucleobase targetNucleobase, int expectedCount) {
		try {
			ImmutableTimer timer = new ImmutableTimer(String.format("%48s", "NucleobaseCounting.countSequential"));
			int count = NucleobaseCounting.countSequential(chromosome, targetNucleobase);
			long dt = timer.mark();
			if (count == expectedCount) {
				timer.printResults(dt);
			} else {
				throw new RuntimeException(count + " does not equal expect count " + expectedCount);
			}
		} catch (NotYetImplementedException nyie) {
			System.out.println("XNucleobaseCount.countSequential: NOT YET IMPLEMENTED");
		}
	}

	private static void timeCountUpperLowerSplit(ExecutorService executor, byte[] chromosome,
			Nucleobase targetNucleobase, int expectedCount) throws InterruptedException, ExecutionException {
		try {
			ImmutableTimer timer = new ImmutableTimer(
					String.format("%48s", "XNucleobaseCount.countParallelUpperLowerSplit"));
			int count = XNucleobaseCounting.countLowerUpperSplit(executor, chromosome, targetNucleobase);
			long dt = timer.mark();
			if (count == expectedCount) {
				timer.printResults(dt);
			} else {
				throw new RuntimeException(count + " does not equal expect count " + expectedCount);
			}
		} catch (NotYetImplementedException nyie) {
			System.out.println("XNucleobaseCount.count2WaySplit: NOT YET IMPLEMENTED");
		}
	}

	private static void timeCountNWaySplit(ExecutorService executor, byte[] chromosome, Nucleobase targetNucleobase,
			int numTasks, int expectedCount) throws InterruptedException, ExecutionException {
		try {
			ImmutableTimer timer = new ImmutableTimer(
					String.format("%48s", "XNucleobaseCount.countParallelNWaySplit(" + numTasks + ")"));
			int count = XNucleobaseCounting.countNWaySplit(executor, chromosome, targetNucleobase, numTasks);
			long dt = timer.mark();
			if (count == expectedCount) {
				timer.printResults(dt);
			} else {
				throw new RuntimeException(count + " does not equal expect count " + expectedCount);
			}
		} catch (NotYetImplementedException nyie) {
			System.out.println("XNucleobaseCount.countNWaySplit: NOT YET IMPLEMENTED");
		}
	}

	private static void timeCountDivideAndConquer(ExecutorService executor, byte[] chromosome,
			Nucleobase targetNucleobase, int threshold, int expectedCount)
			throws InterruptedException, ExecutionException {
		try {
			ImmutableTimer timer = new ImmutableTimer(
					String.format("%48s", "XNucleobaseCount.countDivideAndConquer(" + threshold + ")"));
			int count = XNucleobaseCounting.countDivideAndConquer(executor, chromosome, targetNucleobase, threshold);
			long dt = timer.mark();
			if (count == expectedCount) {
				timer.printResults(dt);
			} else {
				throw new RuntimeException(count + " does not equal expect count " + expectedCount);
			}
		} catch (NotYetImplementedException nyie) {
			System.out.println("XNucleobaseCount.countDivideAndConquer: NOT YET IMPLEMENTED");
		}
	}

	public static void main(String[] args) throws Exception {
		byte[] chromosome = ChromosomeResource.HOMO_SAPIENS_X.getData();

		Nucleobase targetNucleobase = Nucleobase.ADENINE;
		int expectedCount = NucleobaseCounting.countSequential(chromosome, targetNucleobase);

		int numProcessors = Runtime.getRuntime().availableProcessors();

		ExecutorService executor = Executors.newCachedThreadPool();

		try {
			final int ITERATION_COUNT = 10;
			for (int iteration = 0; iteration < ITERATION_COUNT; iteration++) {

				timeCountSequential(chromosome, targetNucleobase, expectedCount);
				timeCountUpperLowerSplit(executor, chromosome, targetNucleobase, expectedCount);
				timeCountNWaySplit(executor, chromosome, targetNucleobase, numProcessors, expectedCount);
				timeCountNWaySplit(executor, chromosome, targetNucleobase, numProcessors * 2, expectedCount);
				timeCountNWaySplit(executor, chromosome, targetNucleobase, numProcessors * 10, expectedCount);
				timeCountNWaySplit(executor, chromosome, targetNucleobase, numProcessors * 71, expectedCount);

				timeCountDivideAndConquer(executor, chromosome, targetNucleobase, chromosome.length / 2, expectedCount);
				timeCountDivideAndConquer(executor, chromosome, targetNucleobase, chromosome.length / 10,
						expectedCount);
				timeCountDivideAndConquer(executor, chromosome, targetNucleobase, chromosome.length / 100,
						expectedCount);
				timeCountDivideAndConquer(executor, chromosome, targetNucleobase, chromosome.length / 1_000,
						expectedCount);
				System.out.println();
			}
		} finally {
			executor.shutdown();
		}

	}

}
