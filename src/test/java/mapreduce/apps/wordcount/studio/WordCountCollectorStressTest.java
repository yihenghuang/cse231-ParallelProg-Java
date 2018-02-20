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

import java.io.IOException;
import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import edu.wustl.cse231s.junit.JUnitUtils;
import mapreduce.apps.wordcount.AbstractWordCountStressTest;
import mapreduce.apps.wordcount.core.io.WordsResource;
import mapreduce.collector.studio.ClassicReducer;
import mapreduce.core.CollectorSolution;
import mapreduce.core.FrameworkSolution;
import mapreduce.core.MapperSolution;
import mapreduce.framework.lab.rubric.MapReduceRubric;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         After INSTRUCTOR and STUDENT_FINISHER_ONLY is passing and
 *         STUDENT_COMPLETE is failing, move on to {@link ClassicReducer}
 */
@RunWith(Parameterized.class)
@MapReduceRubric(MapReduceRubric.Category.COLLECTOR_UNCATEGORIZED)
public class WordCountCollectorStressTest<A> extends AbstractWordCountStressTest<A> {
	public WordCountCollectorStressTest(CollectorSolution collectorSolution, WordsResource wordsResource)
			throws IOException {
		super(FrameworkSolution.INSTRUCTOR, MapperSolution.INSTRUCTOR, collectorSolution, wordsResource);
	}

	@Parameters(name = "collector={0}; {1}")
	public static Collection<Object[]> getConstructorArguments() {
		return JUnitUtils.toParameterizedArguments2(CollectorSolution.getNonWarmUpValues(), WordsResource.values());
	}
}
