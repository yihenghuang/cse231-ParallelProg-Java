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
package connectfour.studio;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import connectfour.core.BitBoard;
import connectfour.core.Board;
import connectfour.core.ColumnEvaluationPair;
import connectfour.core.Config;
import connectfour.core.PositionExpectedScorePair;
import connectfour.core.PositionExpectedScorePairs;
import connectfour.studio.chooseyourownadventure.Adventure;
import connectfour.studio.chooseyourownadventure.ParallelAdventureSupplier;

@RunWith(Parameterized.class)
public class WinDrawLossTest {
	private final PositionExpectedScorePair positionExpectedScorePair;
	private final Adventure adventure;

	public WinDrawLossTest(PositionExpectedScorePair positionExpectedScorePair, Adventure adventure) {
		this.positionExpectedScorePair = positionExpectedScorePair;
		this.adventure = adventure;
	}

	@Test
	public void test() {
		Board board = new BitBoard(this.positionExpectedScorePair.getPosition());
		int maxDepth = 42 - board.getTurnsPlayed();
		int maxParallelDepth = this.adventure.isParallel() ? Math.min(2, maxDepth) : 0;
		Config config = new Config.Builder().heuristic(WinOrLoseHeuristic.INSTANCE).maxParallelDepth(maxParallelDepth)
				.maxDepth(maxDepth).build();
		ColumnEvaluationPair columnEvaluationPair = this.adventure.chooseAdventure(board, config);
		int expectedWinDrawLoss = toWinDrawLoss(positionExpectedScorePair);
		int actualWinDrawLoss = toWinDrawLoss(columnEvaluationPair);
		assertEquals(expectedWinDrawLoss, actualWinDrawLoss);
	}

	@Parameters(name = "{0}; {1}")
	public static Collection<Object[]> getConstructorArguments() throws MalformedURLException, IOException {
		List<PositionExpectedScorePair> positionExpectedScorePairs = PositionExpectedScorePairs
				.readAll(new URL("http://blog.gamesolver.org/data/Test_L3_R1"));

		positionExpectedScorePairs = positionExpectedScorePairs.subList(0, 71);

		List<Object[]> result = new ArrayList<>(positionExpectedScorePairs.size());
		Adventure parallelAdventure = new ParallelAdventureSupplier().get();
		for (PositionExpectedScorePair positionExpectedScorePair : positionExpectedScorePairs) {
			result.add(new Object[] { positionExpectedScorePair, Adventure.SEQUENTIAL_ONLY });
			result.add(new Object[] { positionExpectedScorePair, parallelAdventure });
		}
		return result;
	}

	private static int toWinDrawLoss(ColumnEvaluationPair columnEvaluationPair) {
		double evaluation = columnEvaluationPair.getEvaluation();
		if (evaluation < 0.0) {
			return -1;
		} else if (evaluation > 0.0) {
			return 1;
		} else {
			return 0;
		}
	}

	private static int toWinDrawLoss(PositionExpectedScorePair positionExpectedScorePair) {
		int expectedScore = positionExpectedScorePair.getExpectedScore();
		if (expectedScore < 0) {
			return -1;
		} else if (expectedScore > 0) {
			return 1;
		} else {
			return 0;
		}
	}
}
