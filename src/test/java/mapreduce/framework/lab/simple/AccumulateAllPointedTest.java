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
package mapreduce.framework.lab.simple;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

import edu.wustl.cse231s.util.KeyValuePair;
import mapreduce.framework.lab.rubric.MapReduceRubric;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link SimpleMapReduceFramework#accumulateAll(List[])}
 */
@MapReduceRubric(MapReduceRubric.Category.SIMPLE_ACCUMULATE_ALL)
public class AccumulateAllPointedTest extends AbstractAccumulateAllTest {
	@Override
	protected void execute(Collector<Boolean, List<Boolean>, Void> collector,
			List<KeyValuePair<String, Boolean>>[] mapAllResult) {
		Map<String, List<Boolean>> accumulateAllResult = AccessSimpleFrameworkUtils.accumulateAllOnly(mapAllResult,
				collector);
		assertEquals(2, accumulateAllResult.size());
		assertEquals(Arrays.asList(true, false), accumulateAllResult.get("a"));
		assertEquals(Arrays.asList(true), accumulateAllResult.get("b"));
	}
}
