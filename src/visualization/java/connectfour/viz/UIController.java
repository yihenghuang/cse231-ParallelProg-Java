package connectfour.viz;

import connectfour.core.Board;
import connectfour.core.Player;
import connectfour.core.Controller;

public final class UIController implements Controller {

	private final Player color;
	private final BoardPane pane;

	public UIController(Player color, BoardPane pane) {
		this.color = color;
		this.pane = pane;
	}

	@Override
	public Player getPlayer() {
		return color;
	}

	@Override
	public int selectColumn(Board board) {
		int column;
		do {
			column = pane.awaitMove();
		} while (!board.canPlay(column));
		return column;
	}

}
