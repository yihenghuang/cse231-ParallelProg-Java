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
package sudoku.viz.option;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import sudoku.core.ImmutableSudokuPuzzle;
import sudoku.core.Square;
import sudoku.viz.common.FxSudokuPane;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class FxSudokuOptionPane extends FxSudokuPane {
	@Override
	protected Node createSquareNode(Square square) {
		Button toggleButton = new Button();
		this.squareToButtonMap.put(square, toggleButton);
		return toggleButton;
	}

	
	@Override
	protected void updateSquare(ImmutableSudokuPuzzle puzzle, Square square) {
		Collection<Integer> options = puzzle.getOptions(square);
		StringBuilder sb = new StringBuilder();
		String prefix = "";
		for (int option : options) {
			sb.append(prefix);
			sb.append(option);
			prefix = ",";
		}
		Button button = this.squareToButtonMap.get(square);
		button.setText(sb.toString());

		
		double expectedMax = 7.0;
		Color color = Color.GREEN.darker().darker();
		color = color.interpolate(Color.WHITE, options.size() / expectedMax);
		button.setStyle("-fx-base: " + String.format(
				"#%02X%02X%02X", 
				(int) (color.getRed() * 255),
				(int) (color.getGreen() * 255), 
				(int) (color.getBlue() * 255)
				) + ";");
	}

	private final Map<Square, Button> squareToButtonMap = new HashMap<>();
}
