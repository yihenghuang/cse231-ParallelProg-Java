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
package sudoku.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class Units {
	@SuppressWarnings("unchecked")
	private static Collection<Square>[][] boxUnitMatrix = new Collection[3][3];
	@SuppressWarnings("unchecked")
	private static Collection<Square>[] rowUnits = new Collection[9];
	@SuppressWarnings("unchecked")
	private static Collection<Square>[] columnUnits = new Collection[9];
	static {
		int i = 0;
		for (String boxRows : new String[] { "ABC", "DEF", "GHI" }) {
			int j = 0;
			for (String boxCols : new String[] { "123", "456", "789" }) {
				List<Square> list = new ArrayList<>(9);
				for (char cr : boxRows.toCharArray()) {
					for (char cc : boxCols.toCharArray()) {
						StringBuilder sb = new StringBuilder();
						sb.append(cr);
						sb.append(cc);
						list.add(Square.valueOf(sb.toString()));
					}
				}
				boxUnitMatrix[i][j] = Collections.unmodifiableCollection(list);
				j++;
			}
			i++;
		}

		for (int row = 0; row < 9; row++) {
			List<Square> list = new ArrayList<>(9);
			for (int column = 0; column < 9; column++) {
				list.add(Square.valueOf(row, column));
			}
			rowUnits[row] = Collections.unmodifiableCollection(list);
		}

		for (int column = 0; column < 9; column++) {
			List<Square> list = new ArrayList<>(9);
			for (int row = 0; row < 9; row++) {
				list.add(Square.valueOf(row, column));
			}
			columnUnits[column] = Collections.unmodifiableCollection(list);
		}
	}

	public static Collection<Square> getBoxUnit(int row, int column) {
		int boxI = row / 3;
		int boxJ = column / 3;
		return boxUnitMatrix[boxI][boxJ];
	}

	public static Collection<Square> getRowUnit(int row) {
		return rowUnits[row];
	}

	public static Collection<Square> getColumnUnit(int column) {
		return columnUnits[column];
	}

	public static Iterable<Collection<Square>> allUnits() {
		List<Collection<Square>> result = new ArrayList<>(9 * 3);
		result.addAll(Arrays.asList(rowUnits));
		result.addAll(Arrays.asList(columnUnits));
		for (Collection<Square>[] boxUnits : boxUnitMatrix) {
			result.addAll(Arrays.asList(boxUnits));
		}
		return result;
	}
}
