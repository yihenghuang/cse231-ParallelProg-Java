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
package sudoku.lab;

import static edu.wustl.cse231s.v5.V5.doWork;
import static edu.wustl.cse231s.v5.V5.finish;
import static edu.wustl.cse231s.v5.V5.forasync;

import java.util.SortedSet;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.mutable.MutableObject;

import edu.wustl.cse231s.IntendedForStaticAccessOnlyError;
import edu.wustl.cse231s.v5.V5;
import sudoku.core.ImmutableSudokuPuzzle;
import sudoku.core.Square;
import sudoku.core.SquareSearchAlgorithm;

/**
 * Your sudoku-solving algorithm. This is where you will write your recursive
 * algorithm for solving an {@link ImmutableSudokuPuzzle}. The only public
 * method in this class is the
 * {@link #solve(ImmutableSudokuPuzzle, SquareSearchAlgorithm)} method, which
 * should call the (private)
 * {@link #solveKernel(MutableObject, ImmutableSudokuPuzzle, SquareSearchAlgorithm)}
 * method, starting off the recursion.
 * 
 * @author Yiheng Huang
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class ParallelSudoku {
	private ParallelSudoku() {
		throw new IntendedForStaticAccessOnlyError();
	}

	/**
	 * Should solve a sudoku puzzle given a square search algorithm. The board
	 * returned should have a value in every {@link Square} and include every value
	 * in the original puzzle. This method should not be recursive.
	 */
	public static ImmutableSudokuPuzzle solve(ImmutableSudokuPuzzle puzzle, SquareSearchAlgorithm squareSearchAlgorithm)
			throws InterruptedException, ExecutionException {
		MutableObject<ImmutableSudokuPuzzle> solution = new MutableObject<>(null);

		finish(() -> {
			solveKernel(solution, puzzle, squareSearchAlgorithm);
		});
		return solution.getValue();
	}

	/**
	 * Should recursively solve the given sudoku board. This method should
	 * recursively call itself on a partial solution of the board. If the solution
	 * is found, this method should set solution to reference the solved board.
	 * <p>
	 * A couple of things to note:
	 * <ul>
	 * <li>This will be easier if you do it sequentially first.
	 * <li>This method should eventually be running on multiple threads at the same
	 * time.
	 * <li>This problem is very similar to the n queens problem. A row on the
	 * chessboard is analogous to a {@link Square} in a sudoku puzzle; what is the
	 * equivalent of a chessboard's column?
	 * <li>You should not call
	 * {@link V5#finish(edu.wustl.cse231s.v5.api.CheckedRunnable)} anywhere in this
	 * method.
	 * <li>A puzzle has only one solution.
	 * <li>Once one tasks finds a solution, the others should cease searching
	 * further.
	 * </ul>
	 * 
	 * @param solution
	 *            a mutable reference used to store the solved board
	 * @param puzzle
	 *            the incomplete puzzle to solve
	 * @param squareSearchAlgorithm
	 *            the algorithm to use for picking the next square to examine
	 * @see Square
	 */
	private static void solveKernel(MutableObject<ImmutableSudokuPuzzle> solution, ImmutableSudokuPuzzle puzzle,
			SquareSearchAlgorithm squareSearchAlgorithm) throws InterruptedException, ExecutionException {
		doWork(1);

		if (squareSearchAlgorithm.selectNextUnfilledSquare(puzzle) != null) {

			Square sq = squareSearchAlgorithm.selectNextUnfilledSquare(puzzle);
			SortedSet<Integer> num = puzzle.getOptions(sq);
			forasync(num, (Integer each) -> {
				ImmutableSudokuPuzzle newPuzzle = puzzle.createNext(sq, each);
				solveKernel(solution, newPuzzle, squareSearchAlgorithm);
			});
		} else {
			solution.setValue(puzzle);
		}
	}

}
