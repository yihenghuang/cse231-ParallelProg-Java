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

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface UtilRubric {
	public static enum SuperCatergory {
		LIST("SinglyLinkedList subtotal"), MAP("ArrayOfBucketsHashMap subtotal");

		private final String title;

		private SuperCatergory(String title) {
			this.title = title;
		}

		public String getTitle() {
			return title;
		}
	}

	public static enum Category {
		LIST_SIZE(SuperCatergory.LIST, "Correct size"),
		LIST_IS_EMPTY(SuperCatergory.LIST, "Correct isEmpty"),
		LIST_ADD_FIRST(SuperCatergory.LIST, "Correct addFirst"),
		LIST_REMOVE(SuperCatergory.LIST, "Correct remove"),
		LIST_GET(SuperCatergory.LIST, "Correct get"),
		LIST_INDEX_OF(SuperCatergory.LIST, "Correct indexOf"),
		LIST_UNCATEGORIZED(SuperCatergory.LIST, null),
		MAP_SIZE(SuperCatergory.MAP, "Correct size"),
		MAP_IS_EMPTY(SuperCatergory.MAP, "Correct isEmpty"),
		MAP_PUT(SuperCatergory.MAP, "Correct put"),
		MAP_REMOVE(SuperCatergory.MAP, "Correct remove"),
		MAP_GET(SuperCatergory.MAP, "Correct get"),
		MAP_COMPUTE(SuperCatergory.MAP, "Correct compute"),
		MAP_UNCATEGORIZED(SuperCatergory.MAP, null);

		private final SuperCatergory superCatergory;
		private final String title;

		private Category(SuperCatergory superCatergory, String title) {
			this.superCatergory = superCatergory;
			this.title = title;
		}

		public SuperCatergory getSuperCatergory() {
			return superCatergory;
		}

		public String getTitle() {
			return title;
		}
	}

	Category[] value();
}
