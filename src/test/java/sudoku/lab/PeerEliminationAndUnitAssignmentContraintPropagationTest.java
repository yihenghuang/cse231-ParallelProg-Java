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
package sudoku.lab;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import backtrack.lab.rubric.BacktrackRubric;
import sudoku.core.io.PuzzlesResource;
import sudoku.core.io.PuzzlesResourceUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link DefaultConstraintPropagator}
 */
@BacktrackRubric(BacktrackRubric.Category.CONTRAINT_PROPAGATOR)
@RunWith(Parameterized.class)
public class PeerEliminationAndUnitAssignmentContraintPropagationTest extends AbstractContraintPropagationTest {
	public PeerEliminationAndUnitAssignmentContraintPropagationTest(String givens) {
		super(givens);
	}

	@Parameters(name = "{0}")
	public static Collection<Object[]> getConstructorArguments() throws IOException {
		List<String> easyGivensList = PuzzlesResourceUtils.readGivens(PuzzlesResource.EASY50);
		List<String> hardestGivensList = PuzzlesResourceUtils.readGivens(PuzzlesResource.HARDEST);
		List<Integer> completelyConstrainableWithPeerEliminationAndUnitAssignmentIndices = Arrays.asList(0, 1, 2, 3, 4,
				7, 8, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34,
				35, 36, 37, 38, 39, 40, 43, 44, 45);

		Collection<Object[]> results = new LinkedList<>();
		for (int i : completelyConstrainableWithPeerEliminationAndUnitAssignmentIndices) {
			results.add(new Object[] { easyGivensList.get(i) });
		}
		results.add(new Object[] { hardestGivensList.get(4) });
		return results;
	}
}
