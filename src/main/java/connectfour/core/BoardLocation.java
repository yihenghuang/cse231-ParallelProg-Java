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

import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.stream.Stream;

/**
 * @author Finn Voichick
 */
public final class BoardLocation {

	private final int row;
	private final int column;

	private BoardLocation(int row, int column) {
		this.row = row;
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	@Override
	public String toString() {
		return "BoardLocation[row=" + row + ",column=" + column + ']';
	}

	public static BoardLocation valueOf(int row, int column) {
		if (row < 0 || row >= Board.HEIGHT || column < 0 || column >= Board.WIDTH)
			throw new IllegalArgumentException("Location out of bounds: (" + row + ", " + column + ')');
		return ALL_LOCATIONS[row][column];
	}

	public static Iterable<BoardLocation> values() {
		return new Iterable<BoardLocation>() {

			@Override
			public Iterator<BoardLocation> iterator() {
				return streamLocations().iterator();
			}

			@Override
			public Spliterator<BoardLocation> spliterator() {
				return streamLocations().spliterator();
			}

		};
	}

	private static Stream<BoardLocation> streamLocations() {
		return Arrays.stream(ALL_LOCATIONS).flatMap(row -> Arrays.stream(row));
	}

	private static final BoardLocation[][] ALL_LOCATIONS = new BoardLocation[Board.HEIGHT][Board.WIDTH];
	static {
		for (int row = 0; row < Board.HEIGHT; row++)
			for (int column = 0; column < Board.WIDTH; column++) {
				BoardLocation location = new BoardLocation(row, column);
				ALL_LOCATIONS[row][column] = location;
			}
	}

}
