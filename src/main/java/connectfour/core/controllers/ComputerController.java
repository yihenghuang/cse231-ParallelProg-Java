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

package connectfour.core.controllers;

import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

import connectfour.core.Board;
import connectfour.core.ColumnEvaluationPair;
import connectfour.core.Config;
import connectfour.core.Controller;
import connectfour.core.Heuristic;
import connectfour.core.Player;
import connectfour.studio.chooseyourownadventure.Adventure;

/**
 * @author Finn Voichick
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public final class ComputerController implements Controller {

	private final Player player;
	private final Supplier<Heuristic> heuristicSupplier;
	private final Supplier<Integer> maximumParallelDepthSupplier;
	private final Supplier<Integer> maximumDepthSupplier;
	private final Supplier<Adventure> adventureSupplier;

	public ComputerController(Player player, Supplier<Heuristic> heuristicSupplier,
			Supplier<Integer> maximumParallelDepthSupplier, Supplier<Integer> maximumDepthSupplier,
			Supplier<Adventure> adventureSupplier) {
		this.player = player;
		this.heuristicSupplier = heuristicSupplier;
		this.maximumParallelDepthSupplier = maximumParallelDepthSupplier;
		this.maximumDepthSupplier = maximumDepthSupplier;
		this.adventureSupplier = adventureSupplier;
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	@Override
	public int selectColumn(Board board) throws InterruptedException, ExecutionException {
		Config config = new Config.Builder().heuristic(heuristicSupplier.get())
				.maxParallelDepth(maximumParallelDepthSupplier.get()).maxDepth(maximumDepthSupplier.get()).build();
		Adventure adventure = this.adventureSupplier.get();
		ColumnEvaluationPair columnEvaluationPair = adventure.chooseAdventure(board, config);
		return columnEvaluationPair.getColumn();
	}

	@Override
	public String toString() {
		return player + "-ComputerPlayer";
	}

}
