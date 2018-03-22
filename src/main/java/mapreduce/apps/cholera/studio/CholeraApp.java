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
package mapreduce.apps.cholera.studio;

import java.util.function.BiConsumer;
import java.util.stream.Collector;

import mapreduce.apps.cholera.core.CholeraDeath;
import mapreduce.apps.cholera.core.WaterPump;
import mapreduce.collector.intsum.studio.IntSumCollector;
import mapreduce.framework.core.Mapper;

/**
 * @author Yiheng Huang
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class CholeraApp {

	public static CholeraAppValueRepresentation getValueRepresentation() {
		return CholeraAppValueRepresentation.HIGH_NUMBERS_SUSPECT;
	}

	public static Mapper<CholeraDeath, WaterPump, Number> createMapper() {
		WaterPump[] p = WaterPump.values();

		Mapper<CholeraDeath, WaterPump, Number> m = new Mapper<CholeraDeath, WaterPump, Number>() {
			@Override
			public void map(CholeraDeath item, BiConsumer<WaterPump, Number> keyValuePairConsumer) {

				double minDist = p[0].getLocation().getDistanceTo(item.getLocation());
				WaterPump min = p[0];

				for (WaterPump pump : WaterPump.values()) {
					if (pump.getLocation().getDistanceTo(item.getLocation()) < minDist) {
						minDist = pump.getLocation().getDistanceTo(item.getLocation());
						min = pump;
					}
				}

				keyValuePairConsumer.accept(min, 1);
			}

		};
		return m;
	}

	public static Collector<? extends Number, ?, ? extends Number> createCollector() {
		return new IntSumCollector();

	}
}
