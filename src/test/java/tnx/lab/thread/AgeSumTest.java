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
package tnx.lab.thread;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.function.IntFunction;

import org.junit.Assert;
import org.junit.Test;

import edu.wustl.cse231s.sleep.SleepUtils;
import tnx.lab.rubric.TnXRubric;
import tnx.lab.thread.TAgeSum;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@TnXRubric(TnXRubric.Category.THREAD_UPPER_LOWER)
public class AgeSumTest {
	private static final class TestThreadFactory implements ThreadFactory {
		public TestThreadFactory(IntFunction<Integer> preRunSleepMSec, IntFunction<Integer> postRunSleepMSec) {
			this.preRunSleepMSec = preRunSleepMSec;
			this.postRunSleepMSec = postRunSleepMSec;
		}

		@Override
		public synchronized Thread newThread(Runnable target) {
			int threadIndex = this.threads.size();
			int preMSec = this.preRunSleepMSec.apply(threadIndex);
			int postMSec = this.postRunSleepMSec.apply(threadIndex);
			Runnable runnable = preMSec == 0 && postMSec == 0 ? target : () -> {
				if (preMSec > 0) {
					SleepUtils.sleep(preMSec);
				}
				target.run();
				if (postMSec > 0) {
					SleepUtils.sleep(postMSec);
				}
			};
			Thread thread = new Thread(runnable);
			this.threads.add(thread);
			return thread;
		}
		
		public synchronized List<Thread> getThreads() {
			return Collections.unmodifiableList(this.threads);
		}

		private final List<Thread> threads = new LinkedList<>();
		private final IntFunction<Integer> preRunSleepMSec;
		private final IntFunction<Integer> postRunSleepMSec;
	}

	@Test
	public void test() throws InterruptedException {
		IntFunction<Integer> preRunSleepMSec = (index) -> 1000;
		IntFunction<Integer> postRunSleepMSec = (index) -> 0;
		TestThreadFactory threadFactory = new TestThreadFactory(preRunSleepMSec, postRunSleepMSec);

		
		int[] ages = { 1, 2, 3, 4 };
		int expected = 10;
		int actual = TAgeSum.sumUpperLowerSplit(ages, threadFactory);


		List<Thread> threads = threadFactory.getThreads();
		Assert.assertFalse("neglected to create any threads", threads.isEmpty());
		Assert.assertThat("created an unexpected number of threads", threads.size(), anyOf(is(1), is(2)));
		for (Thread thread : threads) {
			Thread.State threadState = thread.getState();
			Assert.assertNotEquals("negelcted to start thread", threadState, Thread.State.NEW);
			Assert.assertEquals("negelcted to join thread", threadState, Thread.State.TERMINATED);
		}

		Assert.assertEquals(expected, actual);
	}
}
