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
package raytrace.fun;

import static edu.wustl.cse231s.v5.V5.async;
import static edu.wustl.cse231s.v5.V5.finish;
import static edu.wustl.cse231s.v5.V5.launchApp;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinWorkerThread;

import org.sunflow.image.Color;

import edu.wustl.cse231s.NotYetImplementedException;
import edu.wustl.cse231s.v5.impl.executor.ExecutorV5Impl;
import edu.wustl.cse231s.v5.options.SystemPropertiesOption;
import raytrace.core.RayTraceContext;
import raytrace.core.RayTraceTaskContext;
import raytrace.core.RayTraceUtils;
import raytrace.core.RayTracer;
import raytrace.core.Section;

/**
 * @author Yiheng Huang
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class DivideAndConquerRayTracer implements RayTracer {
	private static int getTaskId() {
		Thread thread = Thread.currentThread();
		if (thread instanceof ForkJoinWorkerThread) {
			ForkJoinWorkerThread forkJoinWorkerThread = (ForkJoinWorkerThread) thread;
			return forkJoinWorkerThread.getPoolIndex();
		} else {
			return RayTraceTaskContext.UNKNOWN_TASK_ID;
		}
	}

	// invoke this method when you get to the base case
	private static void renderSection(RayTraceContext context, int xMin, int yMin, int xMax, int yMax) {
		int xLength = xMax - xMin;
		int yLength = yMax - yMin;
		int arrayLength = xLength * yLength;
		Color[] colors = new Color[arrayLength];
		Section section = new Section(xMin, yMin, xMax, yMax);
		section.render(context.createTaskContext(), colors, getTaskId());
	}

	private void rayTraceKernel(RayTraceContext context, int xMin, int yMin, int xMax, int yMax, int areaThreshold) {
		// TODO: divide and conquer until you reach areaThreshold
		throw new NotYetImplementedException();
	}

	@Override
	public void rayTrace(RayTraceContext context) {
		final int areaThreshold = 400;
		ExecutorService executorService = Executors.newWorkStealingPool(4);
		launchApp(new ExecutorV5Impl(executorService), () -> {
			finish(() -> {
				rayTraceKernel(context, 0, 0, context.getWidth(), context.getHeight(), areaThreshold);
			});
		});
	}
}
