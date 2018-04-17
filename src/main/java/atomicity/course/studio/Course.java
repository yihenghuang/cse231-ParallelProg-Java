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

import java.util.Collection;

import com.google.common.base.Supplier;

import atomicity.course.core.Student;
import net.jcip.annotations.ThreadSafe;

/**
 * @author Yiheng Huang
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@ThreadSafe
public class Course {
	private final int limit;
	private final Collection<Student> students;

	public Course(int limit, Supplier<Collection<Student>> collectionSupplier) {
		this.limit = limit;
		this.students = collectionSupplier.get();
	}

	public int getLimit() {
		return this.limit;
	}

	public boolean addIfSpace(Student student) {
		synchronized (this.students) {
			if (this.students.size() < this.limit) {
				this.students.add(student);
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean drop(Student student) {
		synchronized (this.students) {
			return this.students.remove(student);
		}
	}
}
