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

package backtrack.lab.rubric;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import edu.wustl.cse231s.rubric.RubricCategory;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface BacktrackRubric {
	public static enum SuperCatergory {
		N_QUEENS("N-Queens subtotal"), SUDOKU("Sudoku subtotal"), UNCATEGORIZED(null);

		private final String title;

		private SuperCatergory(String title) {
			this.title = title;
		}

		public String getTitle() {
			return title;
		}
	}

	public static enum Category implements RubricCategory {
		IMMUTABLE_QUEEN_LOCATIONS(SuperCatergory.N_QUEENS, "Correct DefaultImmutableQueenLocations", 0.05),
		SEQUENTIAL_N_QUEENS(SuperCatergory.N_QUEENS, "Correct SequentialNQueens", 0.1),
		PARALLEL_N_QUEENS_CORRECTNESS(SuperCatergory.N_QUEENS, "Correct ParallelNQueens", 0.1),
		PARALLEL_N_QUEENS_PARALLELISM(SuperCatergory.N_QUEENS, "Parallel ParallelNQueens", 0.1),
		IMMUTABLE_SUDOKU_PUZZLE(SuperCatergory.SUDOKU, "Correct ImmutableSudokuPuzzle", 0.05),
		CONTRAINT_PROPAGATOR(SuperCatergory.SUDOKU, "Correct ContraintPropagator", 0.15),
		ROW_MAJOR_SEARCH(SuperCatergory.SUDOKU, "Correct RowMajorSearch", 0.15),
		FEWEST_OPTIONS_FIRST_SEARCH(SuperCatergory.SUDOKU, "Correct FewestOptionsFirstSearch", 0.15),
		PARALLEL_SUDOKU_CORRECTNESS(SuperCatergory.SUDOKU, "Correct ParallelSudoku", 0.1),
		PARALLEL_SUDOKU_PARALLELISM(SuperCatergory.SUDOKU, "Parallel ParallelSudoku", 0.1),
		NO_PRINTING(null, null, 0.0);

		private final SuperCatergory superCatergory;
		private final String title;
		private final double portion;

		private Category(SuperCatergory superCatergory, String title, double portion) {
			this.superCatergory = superCatergory;
			this.title = title;
			this.portion = portion;
		}

		public SuperCatergory getSuperCatergory() {
			return superCatergory;
		}

		public String getTitle() {
			return title;
		}

		@Override
		public double getPortion() {
			return portion;
		}
	}

	Category[] value();
}
