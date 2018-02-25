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
package mapreduce.apps.cards;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import edu.wustl.cse231s.junit.JUnitUtils;
import mapreduce.apps.cards.core.Deck;
import mapreduce.apps.cards.core.Suit;
import mapreduce.apps.cards.util.DeckTestUtils;
import mapreduce.core.AbstractMRTest;
import mapreduce.core.CollectorSolution;
import mapreduce.core.FrameworkSolution;
import mapreduce.core.MapperSolution;
import mapreduce.core.TestApplication;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public abstract class AbstractCardMRTest<A> extends AbstractMRTest<Deck, Suit, Integer, A, Integer> {
	private final int numDecks;

	public AbstractCardMRTest(FrameworkSolution frameworkSolution, MapperSolution mapperSolution, CollectorSolution collectorSolution, int numDecks) {
		super(TestApplication.CARDS, frameworkSolution, mapperSolution, collectorSolution);
		this.numDecks = numDecks;
	}
	@Rule
	public TestRule timeout = JUnitUtils.createTimeoutRule();

	@Test
	public void testShuffledDecks() {
		this.testInput(DeckTestUtils.createShuffledDecks(this.numDecks));
	}
}
