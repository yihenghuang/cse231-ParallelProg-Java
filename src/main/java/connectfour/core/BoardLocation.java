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

import net.jcip.annotations.Immutable;

/**
 * An immutable location on the board, with both a row and a column. There are a
 * limited number of BoardLocations, so they must be accessed using the
 * {@link #valueOf(int, int)} and {@link #values()} methods. Both row and column
 * are zero-indexed, with location (0, 0) representing the bottom-left corner.
 * 
 * @author Finn Voichick
 */
@Immutable
public final class BoardLocation {

	private final int row;
	private final int column;

	private BoardLocation(int row, int column) {
		this.row = row;
		this.column = column;
	}

	/**
	 * Gets the row of this BoardLocation object. The bottom of the board is row 0,
	 * and the top row is row 5.
	 * 
	 * @return the row of this location
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Gets the column of this BoardLocation object. The left-most column is column
	 * 0, and the right-most column is column 6.
	 * 
	 * @return the column of this location
	 */
	public int getColumn() {
		return column;
	}

	@Override
	public String toString() {
		return "(" + row + ", " + column + ")";
	}

	/**
	 * Gets the BoardLocation object representing the given row and column. Note
	 * that locations are zero-indexed, with location (0, 0) representing the
	 * bottom-left corner.
	 * 
	 * @param row
	 *            the row of the desired location
	 * @param column
	 *            the column of the desired location
	 * @return the BoardLocation representing the given row and column
	 */
	public static BoardLocation valueOf(int row, int column) {
		if (row < 0 || row >= Board.HEIGHT || column < 0 || column >= Board.WIDTH)
			throw new IllegalArgumentException("Location out of bounds: (" + row + ", " + column + ')');
		return allLocations[row][column];
	}

	/**
	 * Returns an iterable that can be used to iterate over all of the locations on
	 * the board. Locations will be iterated over in row-major order, starting with
	 * location (0, 0).
	 * 
	 * @return an iterable that can iterate over all possible board locations
	 */
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
		return Arrays.stream(allLocations).flatMap(row -> Arrays.stream(row));
	}

	private static BoardLocation[][] generateLocations() {
		int height = Board.HEIGHT, width = Board.WIDTH;
		BoardLocation[][] tempLocations = new BoardLocation[height][width];
		for (int row = 0; row < height; row++)
			for (int column = 0; column < width; column++)
				tempLocations[row][column] = new BoardLocation(row, column);
		return tempLocations;
	}

	private static final BoardLocation[][] allLocations = generateLocations();

}
