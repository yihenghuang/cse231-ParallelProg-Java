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
package pipeline.cake.studio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.mutable.MutableObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import edu.wustl.cse231s.junit.JUnitUtils;
import edu.wustl.cse231s.v5.bookkeep.BookkeepingUtils;
import edu.wustl.cse231s.v5.impl.BookkeepingV5Impl;
import pipeline.cake.core.BakedCake;
import pipeline.cake.core.Baker;
import pipeline.cake.core.IcedCake;
import pipeline.cake.core.Icer;
import pipeline.cake.core.MixedIngredients;
import pipeline.cake.core.Mixer;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class CakePipelineCorrectnessTest {
	@Rule
	public TestRule timeout = JUnitUtils.createTimeoutRule();

	@Test
	public void test() {
		int desiredCakeCount = 100;
		MixedIngredients[] mixes = new MixedIngredients[desiredCakeCount];
		BakedCake[] bakedCakes = new BakedCake[desiredCakeCount];
		IcedCake[] icedCakes = new IcedCake[desiredCakeCount];

		Thread[] threads = new Thread[3];
		class TestMixer implements Mixer {
			private final AtomicInteger count = new AtomicInteger(0);

			@Override
			public MixedIngredients mix(int cakeIndex) {
				assertEquals(count.get(), cakeIndex);
				if (count.get() == 0) {
					threads[0] = Thread.currentThread();
				} else {
					assertSame(threads[0], Thread.currentThread());
				}
				MixedIngredients mixedIngredients = new MixedIngredients(cakeIndex);
				mixes[count.get()] = mixedIngredients;
				count.incrementAndGet();
				return mixedIngredients;
			}
		}

		class TestBaker implements Baker {
			private final AtomicInteger count = new AtomicInteger(0);

			@Override
			public BakedCake bake(int cakeIndex, MixedIngredients mixedIngredients) {
				assertEquals(count.get(), cakeIndex);
				if (count.get() == 0) {
					threads[1] = Thread.currentThread();
				} else {
					assertSame(threads[1], Thread.currentThread());
				}
				BakedCake bakedCake = new BakedCake(cakeIndex, mixedIngredients);
				bakedCakes[count.get()] = bakedCake;
				count.incrementAndGet();
				return bakedCake;
			}
		}

		class TestIcer implements Icer {
			private final AtomicInteger count = new AtomicInteger(0);

			@Override
			public IcedCake ice(int cakeIndex, BakedCake bakedCake) {
				assertEquals(count.get(), cakeIndex);
				if (count.get() == 0) {
					threads[2] = Thread.currentThread();
				} else {
					assertSame(threads[2], Thread.currentThread());
				}
				IcedCake icedCake = new IcedCake(cakeIndex, bakedCake);
				icedCakes[count.get()] = icedCake;
				count.incrementAndGet();
				return icedCake;
			}
		}

		TestMixer mixer = new TestMixer();
		TestBaker baker = new TestBaker();
		TestIcer icer = new TestIcer();

		MutableObject<IcedCake[]> result = new MutableObject<>();
		BookkeepingV5Impl bookkeep = BookkeepingUtils.bookkeep(() -> {
			result.setValue(CakePipeline.mixBakeAndIceCakes(mixer, baker, icer, desiredCakeCount));
		});
		assertEquals(desiredCakeCount, mixer.count.get());
		assertEquals(desiredCakeCount, baker.count.get());
		assertEquals(desiredCakeCount, icer.count.get());
		IcedCake[] resultIcedCakes = result.getValue();
		assertEquals(desiredCakeCount, resultIcedCakes.length);
		for (int i = 0; i < resultIcedCakes.length; i++) {
			assertNotNull(resultIcedCakes[i]);
		}
		assertEquals(1, bookkeep.getNonAccumulatorFinishInvocationCount());
		assertEquals(3, bookkeep.getTaskCount());
	}
}
