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

package sudoku.lab;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;

import backtrack.lab.rubric.BacktrackRubric;
import sudoku.core.ConstraintPropagator;
import sudoku.core.Square;
import sudoku.util.GivensUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@BacktrackRubric(BacktrackRubric.Category.CONTRAINT_PROPAGATOR)
public class ConstraintPropagationDeepCopyTest {
	@Test
	public void test() {
		ConstraintPropagator constraintPropagator = new ConstraintPropagatorSupplier().get();
		String givens = GivensUtils.createEmptyGivens();
		Map<Square, SortedSet<Integer>> mapOriginal = constraintPropagator.createOptionSetsFromGivens(givens);
		assertNotNull(mapOriginal);
		assertEquals(81, mapOriginal.size());

		Square squareToAssign = Square.A1;
		int valueToAssign = 5;
		SortedSet<Integer> expected5Set = new TreeSet<>(Arrays.asList(5));
		SortedSet<Integer> expectedAllBut5Set = new TreeSet<>(Arrays.asList(1, 2, 3, 4, 6, 7, 8, 9));
		SortedSet<Integer> expectedAllSet = new TreeSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));

		Map<Square, SortedSet<Integer>> mapShallowCopy = new EnumMap<>(Square.class);
		for (Square square : Square.values()) {
			SortedSet<Integer> actualSet = mapOriginal.get(square);
			assertNotNull(actualSet);
			assertEquals(expectedAllSet, actualSet);
			mapShallowCopy.put(square, Collections.unmodifiableSortedSet(actualSet));
		}

		Map<Square, SortedSet<Integer>> mapUnmodifiableShallowCopy = Collections.unmodifiableMap(mapShallowCopy);
		Map<Square, SortedSet<Integer>> mapNext = constraintPropagator.createNextOptionSets(mapUnmodifiableShallowCopy,
				squareToAssign, valueToAssign);
		assertNotSame(mapNext, mapShallowCopy);
		assertEquals(81, mapNext.size());

		for (Square square : Square.values()) {
			SortedSet<Integer> unexpectedSet = mapUnmodifiableShallowCopy.get(square);
			SortedSet<Integer> actualSet = mapNext.get(square);
			assertNotNull(actualSet);
			assertNotSame(unexpectedSet, actualSet);
			if (square == squareToAssign) {
				assertEquals(expected5Set, actualSet);
			} else if (squareToAssign.getPeers().contains(square)) {
				assertEquals(expectedAllBut5Set, actualSet);
			} else {
				assertEquals(expectedAllSet, actualSet);
			}
		}
	}
}