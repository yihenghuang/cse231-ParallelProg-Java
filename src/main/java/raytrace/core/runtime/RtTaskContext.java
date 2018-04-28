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

import org.sunflow.core.IntersectionState;
import org.sunflow.core.ShadingState;
import org.sunflow.image.Color;

import raytrace.core.RayTraceTaskContext;
import raytrace.core.Section;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class RtTaskContext implements RayTraceTaskContext {
	public RtTaskContext(RtContext context) {
		this.context = context;
	}

	@Override
	public void emit( Section section, Color[] colors, int id ) {
		int x = section.getXMin();
		int y = section.getYMin();
		int width = section.getWidth();
		int height = section.getHeight();
		this.context.getDisplay().imagePrepare(x, y, width, height, id);
		this.context.getDisplay().imageUpdate(x, y, width, height, colors);
	}
	
	@Override
	public Color castRay(int x, int y) {
		ShadingState state = this.context.getScene().getRadiance(intersectionState, x, this.context.getHeight() - 1 - y, 0.0, 0.0, 0.0, 0);
		return (state != null) ? state.getResult() : Color.BLACK;
	}

	private final RtContext context;
	private final IntersectionState intersectionState = new IntersectionState();
}
