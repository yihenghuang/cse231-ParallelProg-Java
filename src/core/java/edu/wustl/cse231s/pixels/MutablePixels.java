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
package edu.wustl.cse231s.pixels;

import javafx.scene.paint.Color;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public interface MutablePixels {
	int getWidth();
	int getHeight();

	/**
	 * Checks to see whether this image contains a pixel at the given
	 * coordinates.
	 * 
	 * @param x
	 *            The x-coordinate of the desired pixel.
	 * @param y
	 *            The y-coordinate of the desired pixel.
	 * @return true if the given pixel coordinates are within the bounds of the
	 *         image, otherwise false.
	 */
	boolean isInBounds(int x, int y);

	/**
	 * Gets the Color of the pixel at the given coordinates.
	 * 
	 * @param x
	 *            The x-coordinate of the desired pixel.
	 * @param y
	 *            The y-coordinate of the desired pixel.
	 * @return The color of the image at the given pixel.
	 */
	Color getColor(int x, int y);

	/**
	 * Sets the pixel at the given coordinates to a replacement color.
	 * 
	 * @param x
	 *            The x-coordinate of the desired pixel.
	 * @param y
	 *            The y-coordinate of the desired pixel.
	 * @param color
	 *            The new color for the pixel.
	 */
	void setColor(int x, int y, Color color);
}
