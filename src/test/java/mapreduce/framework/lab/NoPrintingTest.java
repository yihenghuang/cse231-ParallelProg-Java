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
package mapreduce.framework.lab;

import static edu.wustl.cse231s.v5.V5.launchAppWithReturn;
import static org.junit.Assert.assertNotNull;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import edu.wustl.cse231s.print.AbstractNoPrintingTest;
import mapreduce.apps.wordcount.core.TextSection;
import mapreduce.apps.wordcount.studio.WordCountMapper;
import mapreduce.framework.core.MapReduceFramework;
import mapreduce.framework.core.Mapper;
import mapreduce.framework.lab.matrix.MatrixMapReduceFramework;
import mapreduce.framework.lab.simple.SimpleMapReduceFramework;
import mapreduce.framework.warmup.wordcount.util.TextSectionResource;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link SimpleMapReduceFramework} {@link MatrixMapReduceFramework}
 */
public class NoPrintingTest extends AbstractNoPrintingTest {
	@Override
	protected void testKernel() {
		TextSection[] textSections = TextSectionResource.ODE_ON_A_GRECIAN_URN.getTextSections();
		Mapper<TextSection, String, Integer> mapper = new WordCountMapper();
		Collector<Integer, ?, Integer> collector = Collectors.summingInt((v) -> v);
		MapReduceFramework<TextSection, String, Integer, ?, Integer> simple = new SimpleMapReduceFramework<>(mapper,
				collector);
		MapReduceFramework<TextSection, String, Integer, ?, Integer> matrix = new MatrixMapReduceFramework<>(mapper,
				collector);
		@SuppressWarnings("unchecked")
		MapReduceFramework<TextSection, String, Integer, ?, Integer>[] frameworks = new MapReduceFramework[] { simple,
				matrix };
		for (MapReduceFramework<TextSection, String, Integer, ?, Integer> framework : frameworks) {
			Map<String, Integer> mapSimple = launchAppWithReturn(() -> {
				return framework.mapReduceAll(textSections);
			});
			assertNotNull(mapSimple);
		}
	}
}
