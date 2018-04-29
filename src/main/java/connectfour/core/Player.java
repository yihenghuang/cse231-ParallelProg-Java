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

/**
 * An enum representing the two players in the game. These are used to
 * distinguish between pieces on the board.
 * 
 * @author Finn Voichick
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public enum Player {

	/** The red player in the game. */
	RED() {

		@Override
		public Player getOpponent() {
			return YELLOW;
		}

		@Override
		public char toLetterRepresentation() {
			return 'R';
		}

		@Override
		public String toString() {
			return "Red player";
		}

	},

	/** The yellow player in the game. */
	YELLOW() {

		@Override
		public Player getOpponent() {
			return RED;
		}

		@Override
		public char toLetterRepresentation() {
			return 'Y';
		}

		@Override
		public String toString() {
			return "Yellow player";
		}

	};

	/**
	 * Gets this player's opponent. {@link #RED} and {@link #YELLOW} are each
	 * other's opponent.
	 * 
	 * @return a player's opponent in the game
	 */
	public abstract Player getOpponent();

	/**
	 * Gets a single-letter representation of this player, either 'R' or 'Y'. This
	 * is used to display the board in text in a matrix form.
	 * 
	 * @return a single character representing this player
	 */
	public abstract char toLetterRepresentation();
}
