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

package atomicity.course.studio;

import static edu.wustl.cse231s.v5.V5.forall;
import static edu.wustl.cse231s.v5.V5.launchApp;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.apache.commons.lang3.mutable.MutableObject;
import org.junit.Test;

import atomicity.course.core.Student;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class CourseHoldsLockTest {
	@Test
	public void test() {
		final int N = 100;
		MutableObject<Course> reference = new MutableObject<>();
		Course course = new Course(N, () -> {
			return new LinkedList<Student>() {
				private static final long serialVersionUID = 1L;

				@Override
				public boolean add(Student student) {
					Course course = reference.getValue();
					assertTrue(Thread.holdsLock(course) || Thread.holdsLock(this));
					return super.add(student);
				}

				@Override
				public boolean remove(Object o) {
					Course course = reference.getValue();
					assertTrue(Thread.holdsLock(course) || Thread.holdsLock(this));
					return super.remove(o);
				}

				@Override
				public int size() {
					Course course = reference.getValue();
					assertTrue(Thread.holdsLock(course) || Thread.holdsLock(this));
					return super.size();
				}
			};
		});
		reference.setValue(course);

		launchApp(() -> {
			forall(0, N, (id) -> {
				Student student = new Student(id);
				assertTrue(course.addIfSpace(student));
				assertTrue(course.drop(student));
			});
		});
	}
}
