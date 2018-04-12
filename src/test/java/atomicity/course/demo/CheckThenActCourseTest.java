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

package atomicity.course.demo;

import static edu.wustl.cse231s.v5.V5.forall;
import static edu.wustl.cse231s.v5.V5.launchApp;
import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import atomicity.course.core.Student;
import edu.wustl.cse231s.junit.JUnitUtils;
import edu.wustl.cse231s.sleep.SleepUtils;
import edu.wustl.cse231s.v5.impl.executor.ExecutorV5Impl;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link CheckThenActCourse}
 */
public class CheckThenActCourseTest {
	@Rule
	public TestRule timeout = JUnitUtils.createTimeoutRule(4);

	@Test
	public void test() {
		final int SEAT_LIMIT = 1;
		final int TASK_COUNT = 8;

		Collection<Student> collection = new LinkedList<Student>();
		CheckThenActCourse checkThenActCourse = new CheckThenActCourse(SEAT_LIMIT, () -> collection);

		ExecutorService executorService = Executors.newCachedThreadPool();
		ExecutorV5Impl impl = new ExecutorV5Impl(executorService);
		Phaser phaser = new Phaser();
		phaser.bulkRegister(TASK_COUNT);
		launchApp(impl, () -> {
			forall(0, TASK_COUNT, (id) -> {
				phaser.arriveAndAwaitAdvance();
				Student student = new Student(id);
				if (checkThenActCourse.isSpaceRemaining()) {
					SleepUtils.sleep(100);
					checkThenActCourse.add(student);
				}
			});
		});

		assertEquals(checkThenActCourse.getLimit(), collection.size());
	}

}
