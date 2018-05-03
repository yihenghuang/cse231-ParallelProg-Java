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

import java.util.Set;

/**
 * A Connect Four board, containing information about who has played at every
 * location.
 * 
 * @author Finn Voichick
 */
public interface Board {

	/**
	 * Gets the player who has played at the given location. If nobody has played at
	 * the given location, this method returns null.
	 * 
	 * @param location
	 *            the location in the board to examine
	 * @return the player who played there, or null if the location is empty
	 */
	public Player get(BoardLocation location);

	/**
	 * Gets the player who is currently up to play. This will be the opponent of the
	 * player who has just played.
	 * 
	 * @return the player whose turn it is
	 */
	public Player getCurrentPlayer();

	/**
	 * Determines if it is legal for the current player to play a piece in the given
	 * column. A column is a legal play if it is not full.
	 * 
	 * @param column
	 *            the column that is questionably full
	 * @return true if the given column is not full, otherwise false
	 */
	public boolean canPlay(int column);

	/**
	 * Gets a set of columns that are valid plays for the current player. In other
	 * words, this set contains all integers for which the {@link #canPlay(int)}
	 * method would return true.
	 * 
	 * @return the set of all valid moves for the current player
	 */
	public Set<Integer> getValidPlays();

	/**
	 * Creates a new Board object that contains one additional piece. The current
	 * player of this next board will be the opponent of the current board.
	 * 
	 * @param column
	 *            the column in which to play
	 * @return
	 */
	public Board createNextBoard(int column);

	/**
	 * Determines whether or not this board is done. A board is done if a player has
	 * won (and {@link #getWinner()} would return a non-null value), or if the board
	 * is full (and {@link #isFull()} would return true).
	 * 
	 * @return true if the game is over, otherwise false
	 */
	public boolean isDone();

	/**
	 * Determines whether or not this board is completely full. A board is full if
	 * each of its columns is full, and {@link #getValidPlays()} would return an
	 * empty set.
	 * 
	 * @return true if the board is completely filled in othewise false
	 */
	public boolean isFull();

	/**
	 * Gets the player who has won the game. If nobody has won, null is returned.
	 * 
	 * @return the player who has won, or null if nobody has won
	 */
	public Player getWinner();

	/**
	 * Gets the number of turns that have been played since the start of the game.
	 * For an empty board, this will return 0. If one player has played, this will
	 * return 1. In other words, this is equivalent to the number of pieces on the
	 * board.
	 * 
	 * @return the number of turns that have been played since the start of the game
	 */
	public int getTurnsPlayed();

	/** The height of any board, equivalent to the number of rows. */
	public static final int HEIGHT = 6;
	/** The width of any board, equivalent to the number of columns. */
	public static final int WIDTH = 7;

}
