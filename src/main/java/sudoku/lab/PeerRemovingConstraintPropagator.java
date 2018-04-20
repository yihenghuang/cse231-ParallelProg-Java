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

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;

import sudoku.core.ConstraintPropagator;
import sudoku.core.Square;
import sudoku.core.io.PuzzlesResourceUtils;

/**
 * @author Yiheng Huang
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class PeerRemovingConstraintPropagator implements ConstraintPropagator {
	@Override
	public Map<Square, SortedSet<Integer>> createOptionSetsFromGivens(String givens) {
		int[][] values = PuzzlesResourceUtils.parseGivens(givens);

		Map<Square, SortedSet<Integer>> map = new EnumMap<>(Square.class);

		int len = values.length;
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++) {
				Square each = Square.valueOf(i, j);
				map.put(each, allOptions());
			}
		}

		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++) {
				Square each = Square.valueOf(i, j);
				if (values[i][j] != 0) {
					associateValueWithSquareAndRemoveFromPeers(map, each, values[i][j]);
				}
			}
		}

		return map;
	}

	@Override
	public Map<Square, SortedSet<Integer>> createNextOptionSets(Map<Square, SortedSet<Integer>> otherOptionSets,
			Square square, int value) {
		Map<Square, SortedSet<Integer>> map = deepCopyOf(otherOptionSets);
		associateValueWithSquareAndRemoveFromPeers(map, square, value);
		return map;
	}

	private static void associateValueWithSquareAndRemoveFromPeers(Map<Square, SortedSet<Integer>> resultOptionSets,
			Square square, int value) {

		Collection<Square> peers = square.getPeers();
		resultOptionSets.put(square, singleOption(value));

		for (Square each : peers) {

			if (resultOptionSets.get(each).contains(value)) {
				resultOptionSets.get(each).remove(value);
				int size = resultOptionSets.get(each).size();
				if (size == 1) {
					int first = resultOptionSets.get(each).first();
					associateValueWithSquareAndRemoveFromPeers(resultOptionSets, each, first);
				}
			}
		}
	}

	private static SortedSet<Integer> singleOption(int value) {
		return new TreeSet<>(Arrays.asList(value));
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
