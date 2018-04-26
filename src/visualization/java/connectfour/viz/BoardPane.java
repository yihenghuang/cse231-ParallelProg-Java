package connectfour.viz;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import connectfour.core.Board;
import connectfour.core.BoardLocation;
import connectfour.core.Player;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public final class BoardPane extends GridPane {

	private volatile CompletableFuture<Integer> moveFuture;
	private final Circle[][] circles = new Circle[Board.HEIGHT][Board.WIDTH];

	public BoardPane() {

		setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));

		ColumnConstraints columnConstraints = new ColumnConstraints();
		columnConstraints.setPercentWidth(100.0 / Board.WIDTH);
		for (int column = 0; column < Board.WIDTH; column++)
			getColumnConstraints().add(columnConstraints);
		RowConstraints rowConstraints = new RowConstraints();
		rowConstraints.setPercentHeight(100.0 / Board.HEIGHT);
		for (int row = 0; row < Board.HEIGHT; row++)
			getRowConstraints().add(rowConstraints);

		for (int row = 0; row < Board.HEIGHT; row++)
			for (int column = 0; column < Board.WIDTH; column++) {
				int finalColumn = column;
				Circle child = new Circle(50.0, Color.WHITE);
				circles[row][column] = child;
				add(child, column, Board.HEIGHT - row - 1);
				setMargin(child, new Insets(SPACING));
				child.setOnMouseClicked(e -> handlePlayedMove(finalColumn));
			}

		requestLayout();
	}

	public int awaitMove() {
		moveFuture = new CompletableFuture<>();
		return moveFuture.join();
	}
	
	public void update(Board board) {
		for (int row = 0; row < Board.HEIGHT; row++)
			for (int column = 0; column < Board.WIDTH; column++) {
				Player color = board.get(BoardLocation.valueOf(row, column));
				if (color == Player.RED)
					circles[row][column].setFill(Color.RED);
				else if (color == Player.YELLOW)
					circles[row][column].setFill(Color.YELLOW);
				else 
					circles[row][column].setFill(Color.WHITE);
			}
	}

	@Override
	protected void layoutChildren() {
		super.layoutChildren();

		double width = (getWidth() - (Board.WIDTH - 1) * SPACING) / Board.WIDTH;
		double height = (getHeight() - (Board.HEIGHT - 1) * SPACING) / Board.HEIGHT;
		double radius = Math.min(width, height) / 2;

		Arrays.stream(circles).forEach(row -> Arrays.stream(row).forEach(circle -> circle.setRadius(radius)));

	}

	private void handlePlayedMove(int column) {
		if (moveFuture != null)
			moveFuture.complete(column);
		moveFuture = null;
	}

	private static final double SPACING = 10.0;

}
