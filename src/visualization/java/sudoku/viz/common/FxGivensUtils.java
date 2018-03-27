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
package sudoku.viz.common;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import sudoku.core.io.PuzzlesResource;
import sudoku.core.io.PuzzlesResourceUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class FxGivensUtils {
	public static ComboBox<String> createGivensComboBox(ChangeListener<String> changeListener) throws IOException {
		List<String> givensList = new LinkedList<>();
		givensList.addAll(PuzzlesResourceUtils.readGivens(PuzzlesResource.HARDEST));
		List<String> easyGivensList = PuzzlesResourceUtils.readGivens(PuzzlesResource.EASY50);

		List<Integer> completelyConstrainableWithPeerEliminationOnlyIndices = Arrays.asList(0, 4, 7, 11, 15, 16, 18, 19,
				33, 35, 37, 39);

		givensList.add(easyGivensList.get(completelyConstrainableWithPeerEliminationOnlyIndices.get(0)));
		ObservableList<String> givensOptions = FXCollections.observableArrayList(givensList);
		ComboBox<String> givensComboBox = new ComboBox<>(givensOptions);
		givensComboBox.setVisibleRowCount(givensList.size());
		// givensComboBox.setCellFactory((ListView<String> listView) -> {
		// return new ListCell<String>() {
		// @Override
		// public void updateItem(String item, boolean empty) {
		// super.updateItem(item, empty);
		// this.setText("givens: [" + item + "]");
		// }
		// };
		// });
		givensComboBox.setStyle("-fx-font: 11 monospace;");
		givensComboBox.valueProperty().addListener(changeListener);
		return givensComboBox;
	}
}
