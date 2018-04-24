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
package raytrace.core;

import org.sunflow.image.Color;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public final class Section {
	public Section(int xMin, int yMin, int xMaxExclusive, int yMaxExclusive) {
		this.xMin = xMin;
		this.yMin = yMin;
		this.xMaxExclusive = xMaxExclusive;
		this.yMaxExclusive = yMaxExclusive;
	}

	public int getXMin() {
		return this.xMin;
	}

	public int getYMin() {
		return this.yMin;
	}

	public int getXMaxExclusive() {
		return this.xMaxExclusive;
	}

	public int getYMaxExclusive() {
		return this.yMaxExclusive;
	}
	
	public int getWidth() {
		return this.xMaxExclusive-this.xMin;
	}

	public int getHeight() {
		return this.yMaxExclusive-this.yMin;
	}
	
	public void render( RayTraceTaskContext taskContext, Color[] colorBuffer, int id ) {
		int index = 0;
		for (int y = this.getYMin(); y < this.getYMaxExclusive(); y++) {
			for (int x = this.getXMin(); x < this.getXMaxExclusive(); x++) {
				colorBuffer[index] = taskContext.castRay(x, y);
				index ++;
			}
		}
		taskContext.emit(this, colorBuffer, id);
	}

	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getSimpleName());
		sb.append("[xMin:");
		sb.append(this.xMin);
		sb.append(";yMin:");
		sb.append(this.yMin);
		sb.append(";xMaxExclusive:");
		sb.append(this.xMaxExclusive);
		sb.append(";yMaxExclusive:");
		sb.append(this.yMaxExclusive);
		sb.append("]");
		return sb.toString();
	}

	private final int xMin;
	private final int yMin;
	private final int xMaxExclusive;
	private final int yMaxExclusive;
}
