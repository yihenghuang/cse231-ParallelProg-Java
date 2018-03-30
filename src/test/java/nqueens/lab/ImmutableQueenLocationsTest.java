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
package nqueens.lab;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import backtrack.lab.rubric.BacktrackRubric;
import edu.wustl.cse231s.junit.JUnitUtils;
import nqueens.lab.DefaultImmutableQueenLocations;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link DefaultImmutableQueenLocations}
 */
@BacktrackRubric(BacktrackRubric.Category.IMMUTABLE_QUEEN_LOCATIONS)
public class ImmutableQueenLocationsTest {
	@Rule
	public TestRule timeout = JUnitUtils.createTimeoutRule();

	@Test
	public void test0Rows() {
		int truthAndBeautyBoardSize = 7;
		DefaultImmutableQueenLocations locations = new DefaultImmutableQueenLocations(truthAndBeautyBoardSize);
		Assert.assertEquals("Your getBoardSize method is incorrect, it should just return the board size",
				truthAndBeautyBoardSize, locations.getBoardSize());
		Assert.assertEquals("Your getRowCount method is incorrect, check the locations instance variable", 0,
				locations.getRowCount());
	}

	@Test
	public void test1Row() {
		int truthAndBeautyBoardSize = 7;
		DefaultImmutableQueenLocations locations = new DefaultImmutableQueenLocations(truthAndBeautyBoardSize);
		DefaultImmutableQueenLocations next = locations.createNext(1);
		Assert.assertEquals("Your getBoardSize method is not updating with createNext", truthAndBeautyBoardSize,
				next.getBoardSize());
		Assert.assertEquals("Your getRowCount method is not updating with createNext", 1, next.getRowCount());
		Assert.assertEquals("You are not correctly updating the queen location when you go to the next row", 1,
				next.getColumnOfQueenInRow(0));
		Assert.assertTrue("The column should not be threat free because of a previously placed queen",
				next.isNextRowThreatFree(0) == false);
		Assert.assertTrue("The column should not be threat free because of a previously placed queen",
				next.isNextRowThreatFree(1) == false);
		Assert.assertTrue("The column should not be threat free because of a previously placed queen",
				next.isNextRowThreatFree(2) == false);
		for (int column = 3; column < truthAndBeautyBoardSize; column++) {
			Assert.assertTrue("The column should be threat free despite the previously placed queen, but is not",
					next.isNextRowThreatFree(column));
		}
	}
}
