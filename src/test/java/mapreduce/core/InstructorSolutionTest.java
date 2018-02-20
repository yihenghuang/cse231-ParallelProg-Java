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
package mapreduce.core;

import java.io.IOException;

import org.junit.Test;

import mapreduce.apps.wordcount.core.TextSection;
import mapreduce.apps.wordcount.core.WordCountUtils;
import mapreduce.apps.wordcount.core.io.WordsResource;
import mapreduce.core.InstructorMapReduceTestUtils;
import mapreduce.framework.lab.rubric.MapReduceRubric;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@MapReduceRubric(MapReduceRubric.Category.SANITY_CHECK)
public class InstructorSolutionTest {
	@Test
	public void test() throws IOException {
		TextSection[] data = WordCountUtils.readAllLinesOfWords(WordsResource.ALICES_ADVENTURES_IN_WONDERLAND);
		InstructorMapReduceTestUtils.checkInstructorWordCountSolution(data);
	}
}
