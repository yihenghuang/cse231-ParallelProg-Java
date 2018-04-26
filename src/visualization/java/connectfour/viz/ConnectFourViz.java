package connectfour.viz;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import connectfour.challenge.OpenEndedHeuristic;
import connectfour.core.BitBoard;
import connectfour.core.Board;
import connectfour.core.Heuristic;
import connectfour.core.Player;
import connectfour.core.controllers.ComputerController;
import connectfour.core.Controller;
import connectfour.studio.WinOrLoseHeuristic;
import connectfour.studio.chooseyourownadventure.Adventure;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public final class ConnectFourViz extends Application {
	private static enum HeuristicOption {
		WIN_OR_LOSE(WinOrLoseHeuristic.INSTANCE), OPEN_ENDED(OpenEndedHeuristic.INSTANCE);

		private final Heuristic heuristic;

		private HeuristicOption(Heuristic heuristic) {
			this.heuristic = heuristic;
		}

		public Heuristic getHeuristic() {
			return heuristic;
		}
	}

	private static enum GameState {
		RED_TURN, YELLOW_TURN, RED_WINS, YELLOW_WINS, DRAW;
	}

	private Spinner<Integer> maxDepthSpinner;
	private ComboBox<HeuristicOption> heuristicComboBox;
	private ComboBox<Adventure> adventureComboBox;
	private Label gameStateLabel;

	@Override
	public void start(Stage primaryStage) throws Exception {

		BoardPane pane = new BoardPane();
		ObservableList<HeuristicOption> heuristicOptions = FXCollections.observableArrayList(HeuristicOption.values());
		this.heuristicComboBox = new ComboBox<>(heuristicOptions);
		this.heuristicComboBox.setValue(HeuristicOption.WIN_OR_LOSE);

		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 12, 6);

		maxDepthSpinner = new Spinner<>();
		maxDepthSpinner.setValueFactory(valueFactory);

		gameStateLabel = new Label();

		ObservableList<Adventure> adventureOptions = FXCollections.observableArrayList(Adventure.values());
		this.adventureComboBox = new ComboBox<>(adventureOptions);
		this.adventureComboBox.setValue(Adventure.SEQUENTIAL_ONLY);

		FlowPane controls = new FlowPane();
		controls.setHgap(2.0);
		controls.getChildren().addAll(new Label("game state:"), gameStateLabel, new Label("heuristic:"),
				heuristicComboBox, new Label("maximum depth:"), maxDepthSpinner, new Label("adventure:"),
				adventureComboBox);

		BorderPane borderPane = new BorderPane();
		borderPane.setCenter(pane);
		borderPane.setTop(controls);
		Scene scene = new Scene(borderPane);
		primaryStage.setScene(scene);
		// primaryStage.setOnCloseRequest(e -> System.exit(0));

		primaryStage.show();

		ExecutorService ex = Executors.newSingleThreadExecutor();
		try {

			ex.submit(() -> {

				try {

					Board board = new BitBoard();

					Map<Player, Controller> controllers = new EnumMap<>(Player.class);
					controllers.put(Player.RED, new UIController(Player.RED, pane));
					controllers.put(Player.YELLOW,
							new ComputerController(Player.YELLOW, () -> heuristicComboBox.getValue().getHeuristic(),
									() -> 2, () -> maxDepthSpinner.getValue(), () -> adventureComboBox.getValue()));

					for (Player current = Player.RED; !board.isDone(); current = current.getOpponent()) {
						GameState gameState = current == Player.RED ? GameState.RED_TURN : GameState.YELLOW_TURN;
						String labelText = gameState.name();
						Platform.runLater(() -> {
							gameStateLabel.setText(labelText);
						});

						board = board.createNextBoard(controllers.get(current).selectColumn(board));
						pane.update(board);
					}
					GameState gameState = GameState.DRAW;
					Player pieceColor = board.getWinner();
					if (pieceColor != null) {
						switch (pieceColor) {
						case RED:
							gameState = GameState.RED_WINS;
							break;
						case YELLOW:
							gameState = GameState.YELLOW_WINS;
							break;
						}
					}
					String labelText = gameState.name();
					Platform.runLater(() -> {
						gameStateLabel.setText(labelText);
					});

				} catch (Throwable e) {
					e.printStackTrace();
				}

				return null;
			});

		} finally {
			ex.shutdown();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
