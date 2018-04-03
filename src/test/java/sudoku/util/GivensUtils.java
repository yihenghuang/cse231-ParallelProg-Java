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
package sudoku.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import sudoku.core.io.PuzzlesResource;
import sudoku.core.io.PuzzlesResourceUtils;
import sudoku.util.SearchSupplier;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class GivensUtils {
	private static Map<SearchSupplier, List<String>> givensToTest = new EnumMap<>(
			SearchSupplier.class);

	private static Collection<Integer> createIndicesToSkipFromTop95ForRowOrder() {
		List<Integer> indicesThatTookTooLongOrCrashedOnProfsMachine = Arrays.asList(0, 4, 7, 22, 35, 40, 70, 94);
		List<Integer> indicesThatTookLongerThan10Seconds = Arrays.asList(88, 45, 36, 10, 49, 27, 61);
		List<Integer> indicesThatTookLongerThan1Second = Arrays.asList(20, 15, 14, 6, 9, 75, 8, 1, 17, 16, 50, 69, 63,
				5);

		Collection<Integer> indicesToSkipFromTop95ForRowOrder = new LinkedList<>();
		indicesToSkipFromTop95ForRowOrder.addAll(indicesThatTookTooLongOrCrashedOnProfsMachine);
		indicesToSkipFromTop95ForRowOrder.addAll(indicesThatTookLongerThan10Seconds);
		indicesToSkipFromTop95ForRowOrder.addAll(indicesThatTookLongerThan1Second);

		return indicesToSkipFromTop95ForRowOrder;
	}

	public static List<String> getGivensToTest(SearchSupplier squareSearchAlgorithmSupplier) {
		return givensToTest.get(squareSearchAlgorithmSupplier);
	}

	static {
		Collection<Integer> indicesToSkipFromTop95ForRowOrder = createIndicesToSkipFromTop95ForRowOrder();
		try {
			List<String> all = new LinkedList<>();
			List<String> some = new LinkedList<>();
			for (PuzzlesResource puzzlesResource : PuzzlesResource.values()) {
				List<String> givensList = PuzzlesResourceUtils.readGivens(puzzlesResource);

				if (puzzlesResource == PuzzlesResource.TOP95) {
					int index = 0;
					for (String givens : givensList) {
						if (indicesToSkipFromTop95ForRowOrder.contains(index)) {
							// pass
						} else {
							some.add(givens);
						}
						index++;
					}
				} else {
					some.addAll(givensList);
				}

				all.addAll(givensList);
			}

			List<String> fewestOptionsGivens;
			final boolean IS_FEWEST_OPTIONS_FIRST_TESTING_ALL = false;
			if (IS_FEWEST_OPTIONS_FIRST_TESTING_ALL) {
				fewestOptionsGivens = all;
			} else {
				List<Integer> indices = Arrays.asList(0, 1, /* 1, */2, 3, 5, 8, 13, 21, 34, 55, 89);
				fewestOptionsGivens = new LinkedList<>();
				for (int i = 0; i < all.size(); i++) {
					if (indices.contains(i)) {
						fewestOptionsGivens.add(all.get(i));
					}
				}
			}

			givensToTest.put(SearchSupplier.STUDENT_FEWEST_OPTIONS_FIRST, fewestOptionsGivens);
			givensToTest.put(SearchSupplier.INSTRUCTOR_FEWEST_OPTIONS_FIRST, fewestOptionsGivens);
			final boolean IS_MORE_THAN_TWO_ROW_MAJOR_DESIRED = false;

			if (IS_MORE_THAN_TWO_ROW_MAJOR_DESIRED) {
				// pass
			} else {
				some = some.subList(0, 2);
			}
			givensToTest.put(SearchSupplier.STUDENT_ROW_MAJOR, some);
			givensToTest.put(SearchSupplier.INSTRUCTOR_ROW_MAJOR, some);

		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}
	
	public static String createEmptyGivens() {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<81; i++) {
			sb.append(".");
		}
		return sb.toString();
	}
}
