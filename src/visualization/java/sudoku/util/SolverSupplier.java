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
package sudoku.util;

import java.util.concurrent.ExecutionException;

import sudoku.core.ImmutableSudokuPuzzle;
import sudoku.core.SquareSearchAlgorithm;
import sudoku.instructor.InstructorSudokuTestUtils;
import sudoku.lab.ParallelSudoku;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
// NOTE: not a supplier at the moment
public enum SolverSupplier {
	STUDENT("student's Solver") {
		@Override
		public ImmutableSudokuPuzzle solve(ImmutableSudokuPuzzle puzzle, SquareSearchAlgorithm squareSearchAlgorithm)
				throws InterruptedException, ExecutionException {
			return ParallelSudoku.solve(puzzle, squareSearchAlgorithm);
		}
	},
	INSTRUCTOR("instructor's Solver") {
		@Override
		public ImmutableSudokuPuzzle solve(ImmutableSudokuPuzzle puzzle, SquareSearchAlgorithm squareSearchAlgorithm)
				throws InterruptedException, ExecutionException {
			return InstructorSudokuTestUtils.solve(puzzle, squareSearchAlgorithm);
		}
	};

	private final String repr;

	private SolverSupplier(String repr) {
		this.repr = repr;
	}

	public abstract ImmutableSudokuPuzzle solve(ImmutableSudokuPuzzle puzzle,
			SquareSearchAlgorithm squareSearchAlgorithm) throws InterruptedException, ExecutionException;

	@Override
	public String toString() {
		return this.repr;
	}
}
