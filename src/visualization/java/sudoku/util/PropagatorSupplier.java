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
package sudoku.util;

import java.util.function.Supplier;

import sudoku.core.ConstraintPropagator;
import sudoku.instructor.InstructorSudokuTestUtils;
import sudoku.lab.ConstraintPropagatorSupplier;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public enum PropagatorSupplier implements Supplier<ConstraintPropagator> {
	STUDENT_LAB("student's Propagator") {
		@Override
		public ConstraintPropagator get() {
			return new ConstraintPropagatorSupplier().get();
		}
	},
	INSTRUCTOR_UNPROPAGATED("instructor's UNPROPAGATED") {
		@Override
		public ConstraintPropagator get() {
			return null;
		}
	},
	INSTRUCTOR_PEER_ONLY("instructor's PEER_ONLY Propagator") {
		@Override
		public ConstraintPropagator get() {
			return InstructorSudokuTestUtils.createPeerOnlyConstraintPropagator();
		}
	},
	INSTRUCTOR_PEER_AND_UNIT("instructor's PEER_AND_UNIT Propagator") {
		@Override
		public ConstraintPropagator get() {
			return InstructorSudokuTestUtils.createPeerAndUnitConstraintPropagator();
		}
	};
	private final String repr;

	private PropagatorSupplier(String repr) {
		this.repr = repr;
	}

	@Override
	public String toString() {
		return this.repr;
	}
}
