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

package mapreduce.apps.cards.studio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.LinkedList;

import org.apache.commons.lang3.mutable.MutableInt;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import mapreduce.apps.cards.core.Card;
import mapreduce.apps.cards.core.Deck;
import mapreduce.apps.cards.core.Rank;
import mapreduce.apps.cards.core.Suit;
import mapreduce.apps.cards.studio.CardMapper;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@RunWith(Parameterized.class)
public class CardMapperNumericOnlyTest {
	private final Suit suit;
	private final Rank rank;

	public CardMapperNumericOnlyTest(Suit suit, Rank rank) {
		this.suit = suit;
		this.rank = rank;
	}

	@Test
	public void test() {
		CardMapper cardMapper = new CardMapper();
		Deck deck = new Deck(new Card(this.suit, this.rank));

		MutableInt count = new MutableInt();
		cardMapper.map(deck, (key, value) -> {
			assertTrue(this.rank.isNumeric());
			assertEquals(suit, key);
			assertEquals(rank.getNumericValue(), value.intValue());
			count.increment();
		});
		
		if(this.rank.isNumeric()) {
			assertEquals(1, count.intValue());
		} else {
			assertEquals(0, count.intValue());
		}
		
	}

	@Parameters(name = "{0} {1}")
	public static Collection<Object[]> getConstructorArguments() {
		Collection<Object[]> result = new LinkedList<>();
		result.add(new Object[] { Suit.SPADES, Rank.ACE });
		result.add(new Object[] { Suit.HEARTS, Rank.QUEEN });
		result.add(new Object[] { Suit.DIAMONDS, Rank.SEVEN });
		result.add(new Object[] { Suit.CLUBS, Rank.TWO });
		return result;
	}
}
