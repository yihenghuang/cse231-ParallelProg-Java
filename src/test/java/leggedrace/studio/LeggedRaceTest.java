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

import static edu.wustl.cse231s.v5.V5.launchApp;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;

import edu.wustl.cse231s.junit.JUnitUtils;
import edu.wustl.cse231s.v5.bookkeep.BookkeepingUtils;
import edu.wustl.cse231s.v5.impl.BookkeepingV5Impl;
import edu.wustl.cse231s.v5.impl.V5Impl;
import edu.wustl.cse231s.v5.impl.executor.ExecutorV5Impl;
import leggedrace.core.LeggedRace;
import leggedrace.core.Participant;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@RunWith(Parameterized.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class LeggedRaceTest {
	private final Solution solution;
	private final int numSteps;
	private final int numParticipants;

	public LeggedRaceTest(Solution solution, int numSteps, int numParticipants) {
		this.solution = solution;
		this.numSteps = numSteps;
		this.numParticipants = numParticipants;
	}

	@Test
	public void testCorrectness() {
		LeggedRace leggedRace = solution.createLeggedRace();
		boolean[][] isStepTakenMatrix = new boolean[numSteps][numParticipants];
		Participant[] participants = new Participant[numParticipants];
		for (int _participantIndex = 0; _participantIndex < participants.length; _participantIndex++) {
			final int participantIndex = _participantIndex;
			participants[_participantIndex] = new Participant() {
				@Override
				public void takeStep(int stepIndex) {
					solution.test(stepIndex, participantIndex, isStepTakenMatrix);
					assertTrue("step already taken.  step=" + stepIndex + ";participant=" + participantIndex,
							isStepTakenMatrix[stepIndex][participantIndex] == false);
					isStepTakenMatrix[stepIndex][participantIndex] = true;
				}
			};
		}
		ExecutorService executorService = Executors.newFixedThreadPool(participants.length);
		V5Impl impl = new ExecutorV5Impl(executorService);
		launchApp(impl, () -> {
			leggedRace.takeSteps(participants, numSteps);
		});

		for (int stepIndex = 0; stepIndex < numSteps; stepIndex++) {
			for (int participantIndex = 0; participantIndex < participants.length; participantIndex++) {
				assertTrue("step never taken.  step=" + stepIndex + ";participant=" + participantIndex,
						isStepTakenMatrix[stepIndex][participantIndex]);
			}
		}
	}

	@Test
	public void testParallelism() {
		Participant[] participants = new Participant[numParticipants];
		for (int participantIndex = 0; participantIndex < participants.length; participantIndex++) {
			participants[participantIndex] = new Participant() {
				@Override
				public void takeStep(int stepIndex) {
				}
			};
		}
		ExecutorService executorService = Executors.newFixedThreadPool(participants.length);
		BookkeepingV5Impl bookkeep = BookkeepingUtils.bookkeep(executorService, () -> {
			solution.createLeggedRace().takeSteps(participants, numSteps);
		});

		solution.checkParallelism(bookkeep, participants, numSteps);
	}

	private static void testAllPreviousRows(int currStepIndex, boolean[][] isStepTakenMatrix) {
		for (int row = 0; row < currStepIndex; row++) {
			for (int col = 0; col < isStepTakenMatrix[row].length; col++) {
				assertTrue("previous step not yet taken.  currStep=" + currStepIndex + ";prevStep=" + row
						+ ";participant=" + col, isStepTakenMatrix[row][col]);
			}
		}
	}

	protected static enum Solution {
		SEQUENTIAL() {
			@Override
			public LeggedRace createLeggedRace() {
				return new SequentialLeggedRace();
			}

			@Override
			public void test(int stepIndex, int participantIndex, boolean[][] isStepTakenMatrix) {
				testAllPreviousRows(stepIndex, isStepTakenMatrix);
				for (int col = 0; col < participantIndex; col++) {
					assertTrue("previous participant for this step not yet taken.  currStep=" + stepIndex
							+ ";participant=" + col, isStepTakenMatrix[stepIndex][col]);
				}
			}

			@Override
			public void checkParallelism(BookkeepingV5Impl bookkeep, Participant[] participants, int stepCount) {
				assertEquals(0, bookkeep.getTaskCount());
			}
		},
		FORALL() {
			@Override
			public LeggedRace createLeggedRace() {
				return new ForallLeggedRace();
			}

			@Override
			public void test(int stepIndex, int participantIndex, boolean[][] isStepTakenMatrix) {
				testAllPreviousRows(stepIndex, isStepTakenMatrix);
			}

			@Override
			public void checkParallelism(BookkeepingV5Impl bookkeep, Participant[] participants, int stepCount) {
				assertEquals(participants.length * stepCount, bookkeep.getTaskCount());
				assertEquals(stepCount, bookkeep.getForasyncTotalInvocationCount());
				assertEquals(stepCount, bookkeep.getNonAccumulatorFinishInvocationCount());
			}
		},
		FORALL_PHASED() {
			@Override
			public LeggedRace createLeggedRace() {
				return new ForallPhasedLeggedRace();
			}

			@Override
			public void test(int stepIndex, int participantIndex, boolean[][] isStepTakenMatrix) {
				testAllPreviousRows(stepIndex, isStepTakenMatrix);
			}

			@Override
			public void checkParallelism(BookkeepingV5Impl bookkeep, Participant[] participants, int stepCount) {
				assertEquals(participants.length, bookkeep.getTaskCount());
				assertEquals(1, bookkeep.getForasyncTotalInvocationCount());
				assertEquals(1, bookkeep.getNonAccumulatorFinishInvocationCount());
			}
		},
		FORALL_POINT_TO_POINT() {
			@Override
			public LeggedRace createLeggedRace() {
				return new ForallPhasedPointToPointLeggedRace();
			}

			@Override
			public void test(int stepIndex, int participantIndex, boolean[][] isStepTakenMatrix) {
				int partnerIndex = getPartnerIndex(participantIndex);
				for (int row = 0; row < stepIndex; row++) {
					assertTrue("previous step not yet taken for participant.  currStep=" + stepIndex + ";prevStep="
							+ row + ";participant=" + participantIndex, isStepTakenMatrix[row][participantIndex]);
					assertTrue(
							"previous step not yet taken for partner.  currStep=" + stepIndex + ";prevStep=" + row
									+ ";participant=" + participantIndex + ";partner=" + partnerIndex,
							isStepTakenMatrix[row][partnerIndex]);
				}
			}

			private int getPartnerIndex(int index) {
				boolean isOddIndex = (index & 0x1) == 0x1;
				if (isOddIndex) {
					return index - 1;
				} else {
					return index + 1;
				}
			}

			@Override
			public void checkParallelism(BookkeepingV5Impl bookkeep, Participant[] participants, int stepCount) {
				assertEquals(participants.length, bookkeep.getTaskCount());
				assertEquals(1, bookkeep.getForasyncTotalInvocationCount());
				assertEquals(1, bookkeep.getNonAccumulatorFinishInvocationCount());
			}
		};

		public abstract LeggedRace createLeggedRace();

		public abstract void test(int stepIndex, int participantIndex, boolean[][] isStepTakenMatrix);

		public abstract void checkParallelism(BookkeepingV5Impl bookkeep, Participant[] participants, int stepCount);

		public static List<Solution> getPartialCreditSolutions() {
			List<Solution> result = new LinkedList<>();
			result.add(SEQUENTIAL);
			result.add(FORALL);
			result.add(FORALL_PHASED);
			return result;
		}

		public static List<Solution> getFullCreditSolutions() {
			List<Solution> result = new LinkedList<>();
			result.addAll(Arrays.asList(values()));
			result.removeAll(getPartialCreditSolutions());
			return result;
		}
	}

	protected static Collection<Object[]> getConstructorArguments(List<Solution> solutions) {
		return JUnitUtils.toParameterizedArguments3(solutions.toArray(), new Integer[] { 1, 4, 71 },
				new Integer[] { 2, 8, 32 });
	}
}
