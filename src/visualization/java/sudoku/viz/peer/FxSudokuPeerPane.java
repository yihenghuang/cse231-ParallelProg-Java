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
package sudoku.viz.peer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import sudoku.core.ImmutableSudokuPuzzle;
import sudoku.core.Square;
import sudoku.viz.common.FxSudokuPane;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class FxSudokuPeerPane extends FxSudokuPane {
	@Override
	protected Node createSquareNode(Square square) {
		ToggleButton toggleButton = new ToggleButton();
		toggleButton.setToggleGroup(this.toggleGroup);

		this.squareToToggleButtonMap.put(square, toggleButton);
		this.toggleToSquareMap.put(toggleButton, square);
		return toggleButton;
	}

	private static void updateStyleClass(ObservableList<String> styleClass, boolean state, String s) {
		if (state) {
			if (styleClass.contains(s)) {
				// pass
			} else {
				styleClass.add(s);
			}
		} else {
			if (styleClass.contains(s)) {
				styleClass.remove(s);
			}
		}
	}

	@Override
	public void init() throws Exception {
		super.init();
		toggleGroup.selectedToggleProperty()
				.addListener((ObservableValue<? extends Toggle> observable, Toggle oldToggle, Toggle newToggle) -> {
					if (newToggle != null) {
						Square square = this.toggleToSquareMap.get(newToggle);
						Collection<Square> peers = square.getPeers();

						for (Square s : Square.values()) {
							ToggleButton toggleButton = this.squareToToggleButtonMap.get(s);
							ObservableList<String> styleClass = toggleButton.getStyleClass();
							boolean isPeer = square == s || peers.contains(s);
							updateStyleClass(styleClass, isPeer, "peerToggleButton");
						}
					}
				});

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
		ToggleButton toggleButton = this.squareToToggleButtonMap.get(square);
		toggleButton.setText(sb.toString());

		ObservableList<String> styleClass = toggleButton.getStyleClass();
		updateStyleClass(styleClass, puzzle.isSquareValueDetermined(square), "givenToggleButton");
	}

	private final ToggleGroup toggleGroup = new ToggleGroup();
	private final Map<Square, ToggleButton> squareToToggleButtonMap = new HashMap<>();
	private final Map<Toggle, Square> toggleToSquareMap = new HashMap<>();
}
