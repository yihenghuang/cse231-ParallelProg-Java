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

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Assert;
import org.junit.Test;

import tnx.lab.rubric.TnXRubric;
import tnx.lab.thread.SimpleThreadFactory;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@TnXRubric(TnXRubric.Category.THREAD_NEW)
public class SimpleThreadFactoryTest {
	@Test
	public void test() throws InterruptedException {
		ThreadFactory threadFactory = new SimpleThreadFactory();

		AtomicBoolean isTargetRun = new AtomicBoolean(false);
		Runnable target = () -> {
			isTargetRun.set(true);
		};

		Thread thread = threadFactory.newThread(target);
		Assert.assertNotNull(thread);
		Assert.assertEquals("Do NOT start newly created threads.  ThreadState: ", Thread.State.NEW, thread.getState());

		Assert.assertFalse("Do NOT run newly created threads.", isTargetRun.get());
		thread.run();
		Assert.assertTrue(isTargetRun.get());

		isTargetRun.set(false);
		thread.start();
		thread.join();
		Assert.assertTrue(isTargetRun.get());
	}
}
