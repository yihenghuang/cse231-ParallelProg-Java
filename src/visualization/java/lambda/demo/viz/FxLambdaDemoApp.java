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
package lambda.demo.viz;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class FxLambdaDemoApp extends Application {
	private Pane createPane(int n) {
		System.out.println("entering createPane");
		Pane pane = new HBox();
		for (int i = 0; i < n; i++) {
			
			// final accessible_within_lambda_i in order to access the value of i within action handler
			final int accessible_within_lambda_i = i;
			Button buttonI = new Button(Integer.toString(i));
			buttonI.setFont(Font.font(64.0));
			buttonI.setOnAction((ActionEvent e) -> {
				System.out.println("entering lambda");
				System.out.println("    " + accessible_within_lambda_i + "            (note: runtime stack with local variable i long since popped)");
				System.out.println(" leaving lambda");
				System.out.println();
			});
			pane.getChildren().add(buttonI);
		}
		System.out.println("leaving  createPane");
		System.out.println();
		return pane;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane root = new BorderPane();

		Scene scene = new Scene(root);
		Pane pane = createPane(8);
		root.setCenter(pane);

		primaryStage.setTitle("Lambda Demo");
		primaryStage.setScene(scene);
		primaryStage.show();

		Platform.runLater(() -> primaryStage.sizeToScene());

		primaryStage.setOnCloseRequest(e -> {
			Platform.exit();
			System.exit(0);
		});
	}

	public static void main(String[] args) throws Exception {
		launch(args);
	}
}
