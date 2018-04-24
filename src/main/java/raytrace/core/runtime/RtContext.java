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
import org.sunflow.core.Options;
import org.sunflow.core.Scene;

import raytrace.core.RayTraceContext;
import raytrace.core.RayTraceTaskContext;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class RtContext implements RayTraceContext {
	public RtContext(Options options, Scene scene, int width, int height, Display display) {
		this.options = options;
		this.scene = scene;
		this.display = display;
		this.width = width;
		this.height = height;
	}

	/*package-private*/ Options getOptions() {
		return this.options;
	}
	/*package-private*/ Scene getScene() {
		return this.scene;
	}
	/*package-private*/ Display getDisplay() {
		return this.display;
	}
	
	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	@Override
	public RayTraceTaskContext createTaskContext() {
		return new RtTaskContext(this);
	}

	private final Options options;
	private final Scene scene;
	private final Display display;
	private final int width;
	private final int height;
}
