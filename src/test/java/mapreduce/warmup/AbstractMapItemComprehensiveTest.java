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
package mapreduce.warmup;

import static edu.wustl.cse231s.v5.V5.launchApp;

import org.junit.Test;

import mapreduce.core.InstructorMapReduceTestUtils;
import mapreduce.core.TestApplication;
import mapreduce.framework.core.Mapper;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public abstract class AbstractMapItemComprehensiveTest<E, K, V> {
	private final E[] input;
	private final TestApplication application;
	private final Mapper<E, K, V> mapper;

	public AbstractMapItemComprehensiveTest(E[] input, TestApplication application, Mapper<E, K, V> mapper) {
		this.input = input;
		this.application = application;
		this.mapper = mapper;
	}

	@Test
	public void test() {
		launchApp(() -> {
			InstructorMapReduceTestUtils.checkWarmUpMapItems(input, application, mapper);
		});
	}
}
