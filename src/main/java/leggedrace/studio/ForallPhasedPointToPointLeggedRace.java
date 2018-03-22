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
package leggedrace.studio;

import static edu.wustl.cse231s.v5.V5.forall;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Phaser;

import leggedrace.core.LeggedRace;
import leggedrace.core.Participant;

/**
 * @author Yiheng Huang
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class ForallPhasedPointToPointLeggedRace implements LeggedRace {
	/**
	 * The parallel solution to the simulated legged race which does make use of
	 * phasers, including an array of phasers and the next() barrier.
	 */
	@Override
	public void takeSteps(Participant[] participants, int stepCount) throws InterruptedException, ExecutionException {
		Phaser[] p = new Phaser[participants.length];
		for (int i = 0; i < p.length; i++) {
			p[i] = new Phaser();
			p[i].register();
		}
		forall(0, participants.length, (j) -> {
			int temp = j;
			for (int k = 0; k < stepCount; k++) {
				participants[temp].takeStep(k);
				p[temp].arrive();

				p[getPartnerIndex(j)].awaitAdvance(k);
			}
		});
	}

	/**
	 * Used to access the correct phaser in the array of phasers. Every participant
	 * has an associated partner index which waits for a signal from the participant
	 * index. This method gets the value of the partner index from the participant
	 * index.
	 * 
	 * @param index,
	 *            more specifically that of the participant
	 * @return partner index, given the index of the associated participant
	 */
	private static int getPartnerIndex(int index) {
		boolean isOddIndex = (index & 0x1) == 0x1;
		if (isOddIndex) {
			return index - 1;
		} else {
			return index + 1;
		}
	}
}
