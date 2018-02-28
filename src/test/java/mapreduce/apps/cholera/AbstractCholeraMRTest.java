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

package mapreduce.apps.cholera;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import edu.wustl.cse231s.junit.JUnitUtils;
import mapreduce.apps.cholera.core.CholeraDeath;
import mapreduce.apps.cholera.core.SohoCholeraOutbreak1854;
import mapreduce.apps.cholera.core.WaterPump;
import mapreduce.apps.cholera.studio.CholeraApp;
import mapreduce.apps.cholera.studio.CholeraAppValueRepresentation;
import mapreduce.core.AbstractMRTest;
import mapreduce.core.CollectorSolution;
import mapreduce.core.FrameworkSolution;
import mapreduce.core.InstructorMapReduceTestUtils;
import mapreduce.core.MapperSolution;
import mapreduce.core.TestApplication;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public abstract class AbstractCholeraMRTest<A> extends AbstractMRTest<CholeraDeath, WaterPump, Integer, A, Integer> {
	public AbstractCholeraMRTest(FrameworkSolution frameworkSolution, MapperSolution mapperSolution,
			CollectorSolution collectorSolution) {
		super(TestApplication.CHOLERA, frameworkSolution, mapperSolution, collectorSolution);
	}

	@Rule
	public TestRule timeout = JUnitUtils.createTimeoutRule();

	@Test
	public void test() {
		this.testInput(SohoCholeraOutbreak1854.getDeaths());
	}

	@Test
	public void testMostSuspectPump() {
		Map<WaterPump, Number> map = InstructorMapReduceTestUtils.mapReduceCholeraStudent();

		Collection<Entry<WaterPump, Number>> entries;
		if (map instanceof SortedMap) {
			entries = map.entrySet();
		} else {
			List<Entry<WaterPump, Number>> list = new ArrayList<>(map.entrySet());
			list.sort((a, b) -> {
				Number aValue = a.getValue();
				Number bValue = b.getValue();
				int result;
				if (aValue instanceof Integer && bValue instanceof Integer) {
					result = Integer.compare(aValue.intValue(), bValue.intValue());
				} else {
					result = Double.compare(aValue.doubleValue(), bValue.doubleValue());
				}
				if (CholeraApp.getValueRepresentation() == CholeraAppValueRepresentation.HIGH_NUMBERS_SUSPECT) {
					result *= -1;
				}
				return result;
			});
			entries = list;
		}
		assertTrue(entries.size() > 0);
		assertSame(WaterPump.BROAD_STREET, entries.iterator().next().getKey());

		final int MINIMUM_ENTRY_COUNT = 4;
		assertTrue(entries.size() >= MINIMUM_ENTRY_COUNT);
	}
}
