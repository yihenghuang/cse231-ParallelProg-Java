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
package tnx.lab.executor;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import edu.wustl.cse231s.executors.BookkeepingExecutorService;
import sort.core.RandomDataUtils;
import sort.core.SortUtils;
import sort.core.quick.PivotInitialIndexSelector;
import sort.core.quick.SequentialPartitioner;
import tnx.lab.executor.XQuicksort;
import tnx.lab.rubric.TnXRubric;

/**
 * @author Finn Voichick
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link XQuicksort#parallelQuicksort(java.util.concurrent.ExecutorService, int[], int, sort.core.quick.Partitioner)}
 */
@RunWith(Parameterized.class)
@TnXRubric(TnXRubric.Category.EXECUTOR_QUICKSORT)
public class QuicksortParallelTest extends AbstractExecutorTest {
	private final int length;
	private final int threshold;
	private final int hardCodedSpawnLimit;

	public QuicksortParallelTest(int length, int threshold, int hardCodedSpawnLimit) {
		this.length = length;
		this.threshold = threshold;
		this.hardCodedSpawnLimit = hardCodedSpawnLimit;
	}

	@Override
	protected List<Integer> getAcceptableSubmitCounts() {
		return Arrays.asList(this.hardCodedSpawnLimit, this.hardCodedSpawnLimit * 2);
	}

	@Override
	protected List<Integer> getInvokeAllSizes() {
		return Collections.emptyList();
	}

	@Override
	protected void accept(BookkeepingExecutorService executor) throws InterruptedException, ExecutionException {
		int[] original = RandomDataUtils.createRandomData(length, 231L);

		int[] sorted = Arrays.copyOf(original, original.length);
		Arrays.parallelSort(sorted);
		assertTrue(SortUtils.isInSortedOrder(sorted));

		int[] data = Arrays.copyOf(original, original.length);
		XQuicksort.parallelQuicksort(executor, data, threshold,
				new SequentialPartitioner(PivotInitialIndexSelector.DETERMINISTIC));
		assertEquals("All submitted tasks must have finished", 0, executor.getNotYetJoinedTaskCount());
		assertTrue(SortUtils.isInSortedOrder(data));
		assertArrayEquals("The array must be correctly sorted", sorted, data);

		assertEquals(0, executor.getInvokeAllCount());
		int taskCount = executor.getSubmitCount();
		assertNotEquals("parallelism not added at all.", 0, taskCount);
		assertThat("copy and paste from sequential error? Expected much larger taskCount", taskCount,
				Matchers.greaterThan(2));
		List<Integer> acceptableSubmitCounts = this.getAcceptableSubmitCounts();
		List<Matcher<? super Integer>> expectedSubmitCountMatchers = new LinkedList<>();
		for (int v : acceptableSubmitCounts) {
			expectedSubmitCountMatchers.add(is(v));
		}
		assertThat(taskCount, anyOf(expectedSubmitCountMatchers));
	}

	@Parameters(name = "length={0}; threshold={1}")
	public static Collection<Object[]> getConstructorArguments() {
		List<Object[]> result = new LinkedList<>();
		result.add(new Object[] { 42, 11, 4 });
		result.add(new Object[] { 10_000, 10_000 / 42, 71 });
		return result;
	}
}
