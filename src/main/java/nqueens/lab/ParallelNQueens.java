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
package nqueens.lab;

import static edu.wustl.cse231s.v5.V5.doWork;
import static edu.wustl.cse231s.v5.V5.finish;
import static edu.wustl.cse231s.v5.V5.forasync;
import static edu.wustl.cse231s.v5.V5.newIntegerFinishAccumulator;
import static edu.wustl.cse231s.v5.V5.register;

import java.util.concurrent.ExecutionException;

import edu.wustl.cse231s.IntendedForStaticAccessOnlyError;
import edu.wustl.cse231s.NotYetImplementedException;
import edu.wustl.cse231s.v5.api.FinishAccumulator;
import edu.wustl.cse231s.v5.api.NumberReductionOperator;
import nqueens.core.ImmutableQueenLocations;

/**
 * @author Yiheng Huang
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class ParallelNQueens {
	private ParallelNQueens() {
		throw new IntendedForStaticAccessOnlyError();
	}

	/**
	 * Like SequentialNQueens, implement the method placeQueenInRow. Note that
	 * countSolutions is called in TestNQueens, but not implemented for you here.
	 * There is no param row for the immutable version. Why not? How will you get
	 * the row? (Hint: look at the methods in ImmutableQueenLocations)
	 * 
	 * @param acc
	 *            An accumulator used to keep track of the number of board
	 *            arrangements found so far. Why are we using an accumulator instead
	 *            of an array?
	 * @param queenLocations
	 *            A representation of the chess board containing the queens.
	 *            However, note that it is immutable in the parallel version. Why is
	 *            it immutable? You must implement some methods in
	 *            ImmutableQueenLocations.java
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	private static void placeQueenInRow(FinishAccumulator<Integer> acc, ImmutableQueenLocations queenLocations)
			throws InterruptedException, ExecutionException {
		doWork(1);
		// TODO implement parallel placeQueenInRow
			// Note: implement the unfinished methods in ImmutableQueenLocations
			// first
			throw new NotYetImplementedException();
	}

	/**
	 * This method should return the number of solutions. Like the sequential
	 * version, it will contain an initial call to placeQueenInRow. (Hint: What will
	 * you need to do differently because you are working in parallel?)
	 * 
	 * @param size
	 *            The size of chess board, i.e. the "n" in n-queens.
	 * @return The number of solutions found for the given size.
	 */
	public static int countSolutions(ImmutableQueenLocations queenLocations)
			throws InterruptedException, ExecutionException {
		// TODO implement countSolutions
			throw new NotYetImplementedException();
	}
}
