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
package mapreduce.apps.wordcount.studio;

import org.junit.Test;

import mapreduce.apps.wordcount.core.TextSection;
import mapreduce.core.AbstractMRTest;
import mapreduce.core.CollectorSolution;
import mapreduce.core.FrameworkSolution;
import mapreduce.core.MapperSolution;
import mapreduce.core.TestApplication;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link WordCountMapper}
 */
public class WordCountMapperLetterCaseTest<A> extends AbstractMRTest<TextSection, String, Integer, A, Integer> {
	public WordCountMapperLetterCaseTest() {
		super(TestApplication.WORD_COUNT, FrameworkSolution.INSTRUCTOR, MapperSolution.STUDENT,
				CollectorSolution.INSTRUCTOR);
	}

	private void testSingle(String text) {
		this.testInput(new TextSection[] { new TextSection(text) });
	}

	@Test
	public void testMixedCase() {
		this.testSingle("The the");
	}

	@Test
	public void testAllLowerCase() {
		this.testSingle("o beware my lord of jealousy");
	}

	@Test
	public void testAllLowerCaseWithDuplicates() {
		this.testSingle("to be or not to be");
	}

	@Test
	public void testAllUpperCase() {
		this.testSingle("ET TU BRUTE");
	}

	@Test
	public void testAllUpperCaseWithDuplicates() {
		this.testSingle("GOOD NIGHT GOOD NIGHT PARTING IS SUCH SWEET SORROW");
	}
}
