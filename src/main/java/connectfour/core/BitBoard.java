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

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.jcip.annotations.Immutable;

/**
 * An immutable implementation of the Board interface that uses bit-shifting to
 * store the whole board state in two longs. It takes up very little memory and
 * most of its operations are very fast.
 * 
 * @author Finn Voichick
 */
@Immutable
public class BitBoard implements Board {
	/*
	 * This class is based heavily on the C++ bitboard implementation created by
	 * Pascal Pons and described at
	 * http://blog.gamesolver.org/solving-connect-four/06-bitboard/.
	 */

	private final long currentPosition;
	private final long mask;

	/**
	 * Constructs an empty BitBoard, with no pieces on the board.
	 */
	public BitBoard() {
		currentPosition = 0;
		mask = 0;
	}

	private BitBoard(long currentPosition, long mask) {
		this.currentPosition = currentPosition;
		this.mask = mask;
	}

	/**
	 * Constructs a BitBoard with the moves represented by the given string. The
	 * moves are represented as described by Pascal Pons here:
	 * http://blog.gamesolver.org/solving-connect-four/02-test-protocol/
	 * 
	 * @param moves
	 *            a string representing the moves that have been played so far
	 */
	public BitBoard(String moves) {
		long tempPosition = 0;
		long tempMask = 0;
		for (char move : moves.toCharArray()) {
			tempPosition ^= tempMask;
			tempMask |= (tempMask + bottomMask(move - '1'));
		}
		currentPosition = tempPosition;
		mask = tempMask;
	}

	@Override
	public Player get(BoardLocation location) {
		long locationMask = bottomMask(location.getColumn()) << location.getRow();
		if ((mask & locationMask) == 0)
			return null;
		Player currentPlayer = getCurrentPlayer();
		return (currentPosition & locationMask) == 0 ? currentPlayer.getOpponent() : currentPlayer;
	}

	@Override
	public Player getCurrentPlayer() {
		switch (getTurnsPlayed() & 1) {
		case 0:
			return Player.RED;
		case 1:
			return Player.YELLOW;
		default:
			throw new AssertionError();
		}
	}

	@Override
	public boolean canPlay(int column) {
		return (mask & topMask(column)) == 0;
	}

	@Override
	public Set<Integer> getValidPlays() {
		return IntStream.range(0, WIDTH).filter(this::canPlay).boxed().collect(Collectors.toSet());
	}

	@Override
	public Board createNextBoard(int column) {
		return new BitBoard(currentPosition ^ mask, mask | (mask + bottomMask(column)));
	}

	@Override
	public boolean isDone() {
		return getWinner() != null || isFull();
	}

	@Override
	public Player getWinner() {

		long opponentPosition = currentPosition ^ mask;

		// horizontal
		long m = opponentPosition & (opponentPosition >> (HEIGHT + 1));
		if ((m & (m >> (2 * (HEIGHT + 1)))) != 0)
			return getCurrentPlayer().getOpponent();

		// diagonal 1
		m = opponentPosition & (opponentPosition >> (HEIGHT));
		if ((m & (m >> (2 * HEIGHT))) != 0)
			return getCurrentPlayer().getOpponent();

		// diagonal 2
		m = opponentPosition & (opponentPosition >> (HEIGHT + 2));
		if ((m & (m >> (2 * (HEIGHT + 2)))) != 0)
			return getCurrentPlayer().getOpponent();

		// vertical
		m = opponentPosition & (opponentPosition >> 1);
		if ((m & (m >> 2)) != 0)
			return getCurrentPlayer().getOpponent();

		return null;

	}

	@Override
	public int getTurnsPlayed() {
		return Long.bitCount(mask);
	}

	@Override
	public boolean isFull() {
		return getTurnsPlayed() >= HEIGHT * WIDTH;
	}

	/**
	 * Gets a key that uniquely identifies this board.
	 * 
	 * @return a unique long representing this board
	 */
	public long getKey() {
		return currentPosition + mask;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (int row = Board.HEIGHT - 1; row >= 0; row--) {
			for (int column = 0; column < Board.WIDTH; column++) {
				Player color = get(BoardLocation.valueOf(row, column));
				result.append(color == null ? "." : color.toLetterRepresentation());
			}
			result.append('\n');
		}
		return result.toString();
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (!(o instanceof Board))
			return false;
		if (o instanceof BitBoard)
			return getKey() == ((BitBoard) o).getKey();

		Board board = (Board) o;
		for (BoardLocation location : BoardLocation.values())
			if (!Objects.equals(get(location), board.get(location)))
				return false;
		return true;

	}

	@Override
	public int hashCode() {
		return Long.hashCode(getKey());
	}

	private long topMask(int column) {
		return bottomMask(column) << (HEIGHT - 1);
	}

	private long bottomMask(int column) {
		return 1L << column * (HEIGHT + 1);
	}
}