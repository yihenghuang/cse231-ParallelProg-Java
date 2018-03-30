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
package mapreduce.framework.lab.matrix;

import static edu.wustl.cse231s.v5.V5.launchApp;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collector;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import edu.wustl.cse231s.junit.JUnitUtils;
import mapreduce.framework.core.Mapper;
import mapreduce.framework.core.NoOpCollector;
import mapreduce.framework.lab.matrix.AccessMatrixFrameworkUtils;
import mapreduce.framework.lab.matrix.MatrixMapReduceFramework;
import mapreduce.framework.lab.rubric.MapReduceRubric;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link MatrixMapReduceFramework#mapAndAccumulateAll(Object[])}
 */
@MapReduceRubric(MapReduceRubric.Category.MATRIX_MAP_AND_ACCUMULATE_ALL)
public class MapAccumulateAllPointedTest {
	private static boolean areDistinctObjects(Object[][] matrix) {
		int n = 0;
		int rowLength = 0;
		for (Object[] row : matrix) {
			if (n == 0) {
				rowLength = row.length;
			} else {
				assertEquals(rowLength, row.length);
			}
			n += row.length;

		}
		for (int i = 0; i < n; i++) {
			int rowI = i / rowLength;
			int colI = i % rowLength;
			for (int j = i + 1; j < n; j++) {
				int rowJ = j / rowLength;
				int colJ = j % rowLength;
				if (matrix[rowI][colI] == matrix[rowJ][colJ]) {
					return false;
				}
			}
		}
		return true;
	}

	@Rule
	public TestRule timeout = JUnitUtils.createTimeoutRule();

	@Test
	public void testResultArrayNotNull() {
		class Input {
		}
		class MapOutputKey {
		}
		class MapOutputValue {
		}

		int length = 10;
		Input[] data = new Input[length];
		Mapper<Input, MapOutputKey, MapOutputValue> doNothingMapper = (Input item,
				BiConsumer<MapOutputKey, MapOutputValue> keyValuePairConsumer) -> {
		};

		Collector<MapOutputValue, ?, ?> doNothingCollector = new NoOpCollector<>();

		launchApp(() -> {
			Map<MapOutputKey, ?>[][] mapAllResult = AccessMatrixFrameworkUtils.mapAndAccumulateAll(data,
					doNothingMapper, doNothingCollector);
			for (Map<MapOutputKey, ?>[] row : mapAllResult) {
				for (Map<MapOutputKey, ?> map : row) {
					Assert.assertNotNull(map);
				}
			}
			assertTrue(areDistinctObjects(mapAllResult));
		});
	}
}
