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
package connectfour.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.ToIntBiFunction;

/**
 * @author Finn Voichick
 */
public final class WinLine implements Iterable<BoardLocation> {

	private final Iterable<BoardLocation> locations;

	private WinLine(Iterable<BoardLocation> locations) {
		this.locations = locations;
	}

	@Override
	public Iterator<BoardLocation> iterator() {
		return locations.iterator();
	}

	@Override
	public Spliterator<BoardLocation> spliterator() {
		return locations.spliterator();
	}

	public static Iterable<WinLine> values() {
		return VALUES;
	}

	private static final Collection<WinLine> VALUES;
	static {

		List<WinLine> values = new ArrayList<>(24 + 21 + 12 + 12);

		int numRows = Board.HEIGHT, numColumns = Board.WIDTH, length = 4;

		addWinLines(0, numRows, 0, numColumns - length + 1, (r, o) -> r, (c, o) -> c + o, values);
		addWinLines(0, numRows - length + 1, 0, numColumns, (r, o) -> r + o, (c, o) -> c, values);
		addWinLines(0, numRows - length + 1, 0, numColumns - length + 1, (r, o) -> r + o, (c, o) -> c + o, values);
		addWinLines(0, numRows - length + 1, length - 1, numColumns, (r, o) -> r + o, (c, o) -> c - o, values);

		VALUES = Collections.unmodifiableList(values);

	}

	private static void addWinLines(int minRow, int maxRow, int minColumn, int maxColumn,
			ToIntBiFunction<Integer, Integer> rowFunction, ToIntBiFunction<Integer, Integer> columnFunction,
			Collection<WinLine> values) {

		for (int startingRow = minRow; startingRow < maxRow; startingRow++)
			for (int startingColumn = minColumn; startingColumn < maxColumn; startingColumn++) {

				BoardLocation[] locations = new BoardLocation[4];
				for (int offset = 0; offset < 4; offset++) {
					int row = rowFunction.applyAsInt(startingRow, offset);
					int column = columnFunction.applyAsInt(startingColumn, offset);
					locations[offset] = BoardLocation.valueOf(row, column);
				}
				values.add(new WinLine(Arrays.asList(locations)));

			}

	}

}
