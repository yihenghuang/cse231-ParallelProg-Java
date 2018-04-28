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
package raytrace.core.runtime;

import org.sunflow.core.Display;
import org.sunflow.core.ImageSampler;
import org.sunflow.core.Options;
import org.sunflow.core.Scene;

import edu.wustl.cse231s.timing.ImmutableTimer;
import raytrace.core.RayTracer;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class RtImageSampler implements ImageSampler {

	public RtImageSampler(RayTracer rayTracer) {
		this.rayTracer = rayTracer;
	}

	@Override
	public boolean prepare(Options options, Scene scene, int w, int h) {
		this.options = options;
		this.scene = scene;
		this.w = w;
		this.h = h;
		return true;
	}

	@Override
	public void render(Display display) {
		display.imageBegin(this.w, this.h, 1);
		try {
			RtContext context = new RtContext(this.options, this.scene, this.w, this.h, display);
			ImmutableTimer timer = new ImmutableTimer(this.rayTracer.getClass().getSimpleName());
			this.rayTracer.rayTrace(context);
			timer.markAndPrintResults();
		} finally {
			display.imageEnd();
		}
	}

	private final RayTracer rayTracer;
	private Options options;
	private Scene scene;
	private int w;
	private int h;
};
