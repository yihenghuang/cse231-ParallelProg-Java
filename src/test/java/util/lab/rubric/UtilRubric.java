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
package util.lab.rubric;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import edu.wustl.cse231s.rubric.RubricCategory;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface UtilRubric {
	public static enum SuperCatergory {
		ITERATOR("LinkedNodesIterator subtotal"),
		COLLECTION("LinkedNodesCollection subtotal"),
		MAP("BucketsHashMap subtotal");

		private final String title;

		private SuperCatergory(String title) {
			this.title = title;
		}

		public String getTitle() {
			return title;
		}
	}

	public static enum Category implements RubricCategory {
		ITERATOR_HAS_NEXT(SuperCatergory.ITERATOR, "Correct hasNext", 0.05),
		ITERATOR_NEXT(SuperCatergory.ITERATOR, "Correct next", 0.1),
		ITERATOR_REMOVE(SuperCatergory.ITERATOR, "Correct remove", 0.1),
		COLLECTION_ITERATOR(SuperCatergory.COLLECTION, "Correct iterator", 0.05),
		COLLECTION_SIZE(SuperCatergory.COLLECTION, "Correct size", 0.05),
		COLLECTION_ADD(SuperCatergory.COLLECTION, "Correct add", 0.1),
		COLLECTION_UNCATEGORIZED(SuperCatergory.COLLECTION, null, 0.0),
		MAP_HASH(SuperCatergory.MAP, "Correct hash", 0.025),
		MAP_GET_BUCKET_FOR(SuperCatergory.MAP, "Correct getBucketFor", 0.025),
		MAP_SIZE(SuperCatergory.MAP, "Correct size", 0.05),
		MAP_PUT(SuperCatergory.MAP, "Correct put", 0.15),
		MAP_REMOVE(SuperCatergory.MAP, "Correct remove", 0.1),
		MAP_GET(SuperCatergory.MAP, "Correct get", 0.1),
		MAP_UNCATEGORIZED(SuperCatergory.MAP, null, 0.0);

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
