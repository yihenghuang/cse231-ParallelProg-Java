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
package sudoku.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import edu.wustl.cse231s.lazy.Lazy;
import net.jcip.annotations.Immutable;

/**
 * A single square location in a {@link SudokuPuzzle}. Note that this enum
 * represents only a location on the board; it does not store the number written
 * there. To get the value (written number) at a given square, use
 * {@link SudokuPuzzle#getOptions(Square)}.
 * 
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * @author Finn Voichick
 * @see <a href="http://norvig.com/sudoku.html">Peter Norvig's board layout</a>
 */
@Immutable
public enum Square {
	A1, A2, A3, A4, A5, A6, A7, A8, A9, //
	B1, B2, B3, B4, B5, B6, B7, B8, B9, //
	C1, C2, C3, C4, C5, C6, C7, C8, C9, //
	D1, D2, D3, D4, D5, D6, D7, D8, D9, //
	E1, E2, E3, E4, E5, E6, E7, E8, E9, //
	F1, F2, F3, F4, F5, F6, F7, F8, F9, //
	G1, G2, G3, G4, G5, G6, G7, G8, G9, //
	H1, H2, H3, H4, H5, H6, H7, H8, H9, //
	I1, I2, I3, I4, I5, I6, I7, I8, I9; //

	private final int row;
	private final int column;
	private final Lazy<Collection<Square>> peers;

	private Square() {
		row = this.name().charAt(0) - 'A';
		column = this.name().charAt(1) - '1';
		peers = new Lazy<>(() -> {
			Set<Square> set = new HashSet<>(20);
			set.addAll(Units.getRowUnit(row));
			set.addAll(Units.getColumnUnit(column));
			set.addAll(Units.getBoxUnit(row, column));
			set.remove(this);
			assert set.size() == 20;
			return Collections.unmodifiableCollection(set);
		});
	}

	/**
	 * Gets the zero-indexed row of this board location. The first row is 0, the
	 * next row is 1, and so on. So, for example, square B4 would return 1, because
	 * "B" is the second row.
	 * 
	 * @return the row of this square
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Gets the zero-indexed column of this board location. The first column is 0,
	 * the next column is 1, and so on. So, for example, square B4 would return 3,
	 * because the fourth column becomes 3 when zero-indexed.
	 * 
	 * @return the column of this square
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * Gets a collection of all of this square's peers. This includes all squares in
	 * the same row as this square, in the same column as this square, and in the
	 * same box (3-by-3) as this square. In other words, this is a collection of all
	 * squares that cannot have the same value as this one.
	 * 
	 * @return all of this square's peers
	 */
	public Collection<Square> getPeers() {
		return this.peers.get();
	}

	/**
	 * Gets the square with the given row and column. This square's
	 * {@link #getRow()} and {@link #getColumn()} methods will return the same
	 * values passed into this method. For example, if row is 1 and column is 3,
	 * this method will return square B4.
	 * 
	 * @param row
	 *            the row of the desired square's location
	 * @param column
	 *            the column of the desired square's location
	 * @return the square at the given location
	 */
	public static Square valueOf(int row, int column) {
		StringBuilder sb = new StringBuilder();
		sb.append((char) ('A' + row));
		sb.append((char) ('1' + column));
		return valueOf(sb.toString());
	}

}
