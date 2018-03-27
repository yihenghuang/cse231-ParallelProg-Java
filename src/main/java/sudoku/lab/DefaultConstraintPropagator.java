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

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.wustl.cse231s.NotYetImplementedException;
import net.jcip.annotations.Immutable;
import sudoku.core.ConstraintPropagator;
import sudoku.core.Square;
import sudoku.core.Units;
import sudoku.core.io.PuzzlesResourceUtils;

/**
 * Constraint propagation approach and terminology adopted from
 * <a href="http://norvig.com/sudoku.html">Peter Norvig's essay</a>.
 * 
 * @author Yiheng Huang
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@Immutable
public final class DefaultConstraintPropagator implements ConstraintPropagator {
	@Override
	public Map<Square, SortedSet<Integer>> createOptionSetsFromGivens(String givens) {
		int[][] values = PuzzlesResourceUtils.parseGivens(givens);
		throw new NotYetImplementedException();
	}

	public Map<Square, SortedSet<Integer>> createNextOptionSets(Map<Square, SortedSet<Integer>> otherOptionSets,
			Square square, int value) {
		throw new NotYetImplementedException();
	}

	/**
	 * @throws IllegalArgumentException if the given value is not an option for the given square
	 */
	private static void assign(Map<Square, SortedSet<Integer>> optionSets, Square square, int value) {
		throw new NotYetImplementedException();
	}

	private static void eliminate(Map<Square, SortedSet<Integer>> optionSets, Square square, int value) {
		throw new NotYetImplementedException();
	}

	private static SortedSet<Integer> allOptions() {
		return new TreeSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
	}

	private static SortedSet<Integer> copyOf(SortedSet<Integer> other) {
		return new TreeSet<>(other);
	}

	private static Map<Square, SortedSet<Integer>> deepCopyOf(Map<Square, SortedSet<Integer>> other) {
		Map<Square, SortedSet<Integer>> result = new EnumMap<>(Square.class);
		for (Entry<Square, SortedSet<Integer>> entry : other.entrySet()) {
			result.put(entry.getKey(), copyOf(entry.getValue()));
		}
		return result;
	}
}
