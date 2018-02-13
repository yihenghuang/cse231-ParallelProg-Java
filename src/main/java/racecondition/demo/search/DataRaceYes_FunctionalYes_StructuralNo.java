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
package racecondition.demo.search;

import static edu.wustl.cse231s.v5.V5.async;
import static edu.wustl.cse231s.v5.V5.doWork;
import static edu.wustl.cse231s.v5.V5.finish;
import static edu.wustl.cse231s.v5.V5.launchApp;

import java.util.concurrent.ExecutionException;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/) 
 * credit: Vivek Sarkar (vsarkar@rice.edu)
 */
public class DataRaceYes_FunctionalYes_StructuralNo {
	public static boolean searchPar(final char[] pattern, final char[] text)
			throws InterruptedException, ExecutionException {
		final int M = pattern.length;
		final int N = text.length;
		final boolean[] found = { false };
		finish(() -> {
			for (int i = 0; i <= N - M; i++) {
				// data race
				if (found[0]) {
					// different computation graph
					break;
				}
				final int ii = i;
				async(() -> {
					int j;
					for (j = 0; j < M; j++) {
						doWork(1);
						if (text[ii + j] != pattern[j]) {
							break;
						}
					}
					if (j == M) {
						// data race
						// same output, however
						found[0] = true;
					}
				});
			}
		});
		return found[0];
	}

	public static void main(String[] args) {
		char[] pattern = "ab".toCharArray();
		char[] text = "abacadabrabracabracadababacadabrabracabracadabrabrabracad".toCharArray();
		for (int testIteration = 0; testIteration < 100; testIteration++) {
			launchApp(() -> {
				boolean isFound = searchPar(pattern, text);
				System.out.println(isFound);
			});
		}
	}
}
