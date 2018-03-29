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
package sudoku.viz.solution;

import java.util.Arrays;

import com.google.common.base.Supplier;

import sudoku.core.SquareSearchAlgorithm;
import sudoku.instructor.InstructorSudokuTestUtils;
import sudoku.lab.FewestOptionsFirstSquareSearchAlgorithm;
import sudoku.lab.RowMajorSquareSearchAlgorithm;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public enum SquareSearchAlgorithmSupplier implements Supplier<SquareSearchAlgorithm> {
	STUDENT_ROW_MAJOR {
		@Override
		public SquareSearchAlgorithm get() {
			return new RowMajorSquareSearchAlgorithm();
		}
	},
	STUDENT_FEWEST_OPTIONS_FIRST {
		@Override
		public SquareSearchAlgorithm get() {
			return new FewestOptionsFirstSquareSearchAlgorithm();
		}
	},
	INSTRUCTOR_ROW_MAJOR {
		@Override
		public SquareSearchAlgorithm get() {
			return InstructorSudokuTestUtils.createRowMajorSearch();
		}
	},
	INSTRUCTOR_FEWEST_OPTIONS_FIRST {
		@Override
		public SquareSearchAlgorithm get() {
			return InstructorSudokuTestUtils.createFewestOptionsFirstSearch();
		}
	};

	public static Iterable<SquareSearchAlgorithmSupplier> studentValues() {
		return Arrays.asList(STUDENT_ROW_MAJOR, STUDENT_FEWEST_OPTIONS_FIRST);
	}

	public static Iterable<SquareSearchAlgorithmSupplier> instructorFewestOptionsFirstPlusStudentValues() {
		return Arrays.asList(INSTRUCTOR_FEWEST_OPTIONS_FIRST, STUDENT_ROW_MAJOR, STUDENT_FEWEST_OPTIONS_FIRST);
	}

	public static Iterable<SquareSearchAlgorithmSupplier> instructorValues() {
		return Arrays.asList(INSTRUCTOR_ROW_MAJOR, INSTRUCTOR_FEWEST_OPTIONS_FIRST);
	}
}
