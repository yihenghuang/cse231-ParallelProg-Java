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
package backtrack.lab;

import static edu.wustl.cse231s.v5.V5.launchApp;

import backtrack.lab.rubric.BacktrackRubric;
import edu.wustl.cse231s.print.AbstractNoPrintingTest;
import nqueens.core.DefaultMutableQueenLocations;
import nqueens.lab.DefaultImmutableQueenLocations;
import nqueens.lab.ParallelNQueens;
import nqueens.lab.SequentialNQueens;
import sudoku.core.ImmutableSudokuPuzzle;
import sudoku.lab.ConstraintPropagatorSupplier;
import sudoku.lab.DefaultImmutableSudokuPuzzle;
import sudoku.lab.ParallelSudoku;
import sudoku.lab.RowMajorSquareSearchAlgorithm;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@BacktrackRubric(BacktrackRubric.Category.NO_PRINTING)
public class NoPrintingTest extends AbstractNoPrintingTest {
	@Override
	protected void testKernel() {
		SequentialNQueens.countSolutions(new DefaultMutableQueenLocations(8));
		launchApp(() -> {
			ParallelNQueens.countSolutions(new DefaultImmutableQueenLocations(8));
			// source: http://norvig.com/hardest.txt
			String givens = "85...24..72......9..4.........1.7..23.5...9...4...........8..7..17..........36.4.";
			ImmutableSudokuPuzzle puzzle = new DefaultImmutableSudokuPuzzle(new ConstraintPropagatorSupplier().get(),
					givens);
			ParallelSudoku.solve(puzzle, new RowMajorSquareSearchAlgorithm());

		});
	}
}
