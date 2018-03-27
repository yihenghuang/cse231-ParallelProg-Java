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

package mapreduce.framework.lab.rubric;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import edu.wustl.cse231s.rubric.RubricCategory;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MapReduceRubric {
	public static enum SuperCatergory {
		SIMPLE_FRAMEWORK("Simple framework subtotal"),
		MATRIX_FRAMEWORK("Matrix framework subtotal"),
		SANITY_CHECK(null),
		UNCATEGORIZED(null);

		private final String title;

		private SuperCatergory(String title) {
			this.title = title;
		}

		public String getTitle() {
			return title;
		}
	}

	public static enum Category implements RubricCategory {
		SANITY_CHECK(SuperCatergory.SANITY_CHECK, null, 0.0) {
			@Override
			public boolean isVisible() {
				return false;
			}
		},
		SIMPLE_MAP_ALL(SuperCatergory.SIMPLE_FRAMEWORK, "Correct mapAll", 0.1),
		SIMPLE_ACCUMULATE_ALL(SuperCatergory.SIMPLE_FRAMEWORK, "Correct accumulateAll", 0.2),
		SIMPLE_FINISH_ALL(SuperCatergory.SIMPLE_FRAMEWORK, "Correct finishAll", 0.1),
		SIMPLE_UNCATEGORIZED(SuperCatergory.UNCATEGORIZED, null, 0.0),
		MATRIX_MAP_AND_ACCUMULATE_ALL(SuperCatergory.MATRIX_FRAMEWORK, "Correct mapAndAccumulateAll", 0.25),
		MATRIX_COMBINE_AND_FINISH_ALL(SuperCatergory.MATRIX_FRAMEWORK, "Correct combineAndFinishAll", 0.25),
		MATRIX_UNCATEGORIZED(SuperCatergory.UNCATEGORIZED, null, 0.0),
		UNCATEGORIZED(SuperCatergory.UNCATEGORIZED, null, 0.0);

		private final SuperCatergory superCatergory;
		private final String title;

		private final double portion;

		private Category(SuperCatergory superCatergory, String title, double portion) {
			this.superCatergory = superCatergory;
			this.title = title;
			this.portion = portion;
		}

		public SuperCatergory getSuperCatergory() {
			return superCatergory;
		}

		public String getTitle() {
			return title;
		}

		@Override
		public double getPortion() {
			return this.portion;
		}
	}

	Category[] value();
}
