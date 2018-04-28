package connectfour.viz;

import java.net.URL;
import java.util.List;

import connectfour.core.BitBoard;
import connectfour.core.Board;
import connectfour.core.PositionExpectedScorePair;
import connectfour.core.PositionExpectedScorePairs;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public final class ConnectFourPositionViz extends Application {
	private ComboBox<PositionExpectedScorePair> positionComboBox;

	@Override
	public void start(Stage primaryStage) throws Exception {
		List<PositionExpectedScorePair> positionExpectedScorePairs = PositionExpectedScorePairs
				.readAll(new URL("http://blog.gamesolver.org/data/Test_L3_R1"));

		BorderPane borderPane = new BorderPane();
		BoardPane boardPane = new BoardPane();

		ObservableList<PositionExpectedScorePair> positionOptions = FXCollections
				.observableArrayList(positionExpectedScorePairs);
		this.positionComboBox = new ComboBox<>(positionOptions);
		this.positionComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			if (newValue != null) {
				Board board = new BitBoard(newValue.getPosition());
				boardPane.update(board);
			}
		});
		this.positionComboBox.setStyle("-fx-font: 18px \"Monospaced\";");

		borderPane.setCenter(boardPane);
		HBox hBox = new HBox();
		hBox.getChildren().add(this.positionComboBox);
		borderPane.setTop(hBox);
		Scene scene = new Scene(borderPane);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
