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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.LinkedList;

import org.junit.Test;

import atomicity.course.core.Student;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class CoursePointedTest {
	@Test
	public void testAdd() {
		Collection<Student> collection = new LinkedList<>();
		Course course = new Course(2, () -> {
			return collection;
		});

		Student student0 = new Student(0);
		Student student1 = new Student(1);
		Student student2 = new Student(2);

		assertEquals(0, collection.size());
		assertFalse(course.drop(student0));
		assertEquals(0, collection.size());
		assertFalse(course.drop(student1));
		assertEquals(0, collection.size());
		assertFalse(course.drop(student2));
		assertEquals(0, collection.size());

		assertTrue(course.addIfSpace(student0));
		assertEquals(1, collection.size());
		
		assertTrue(course.addIfSpace(student1));
		assertEquals(2, collection.size());

		assertFalse(course.addIfSpace(student2));
		assertEquals(2, collection.size());

		assertFalse(course.drop(student2));
		assertEquals(2, collection.size());
		
		assertTrue(course.drop(student1));
		assertEquals(1, collection.size());

		assertTrue(course.addIfSpace(student2));
		assertEquals(2, collection.size());
	}
}
