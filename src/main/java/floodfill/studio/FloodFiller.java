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
package floodfill.studio;

import static edu.wustl.cse231s.v5.V5.async;
import static edu.wustl.cse231s.v5.V5.finish;

import java.util.concurrent.ExecutionException;

import edu.wustl.cse231s.NotYetImplementedException;
import edu.wustl.cse231s.pixels.MutablePixels;
import javafx.scene.paint.Color;

/**
 * @author __STUDENT_NAME__
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class FloodFiller {
	/**
	 * @param mutablePixels
	 *            The set of pixels that you are supposed to manipulate. See the
	 *            MutablePixels class for information about the methods that you
	 *            have access to.
	 * @param prevColor
	 *            The color targeted for replacement. Remember that the flood fill
	 *            algorithm changes one color to another. It should only change
	 *            pixels of this target color.
	 * @param nextColor
	 *            The replacement color the appropriate pixels should be set to.
	 * @param x
	 *            The x-coordinate of the pixel to examine.
	 * @param y
	 *            The y-coordinate of the pixel to examine.
	 */
	private static void floodFillKernel(MutablePixels mutablePixels, Color prevColor, Color nextColor, int x, int y) {
		throw new NotYetImplementedException();
	}

	/**
	 * Starts your recursive algorithm by finding the original color of the
	 * specified pixel and invoking floodFillKernel on that pixel to initiate
	 * replacing all of the connected pixels of the original prevColor to nextColor.
	 * 
	 * @param pixels
	 *            The original pixels to manipulate for the flood fill.
	 * @param nextColor
	 *            The new color to fill the area with.
	 * @param x
	 *            The x-coordinate of the flood fill starting point.
	 * @param y
	 *            The y-coordinate of the flood fill starting point.
	 */
	public static void floodFill(MutablePixels pixels, Color nextColor, int x, int y)
			throws InterruptedException, ExecutionException {
		Color prevColor = pixels.getColor(x, y);
		if (nextColor.equals(prevColor)) {
			// pass
		} else {
			finish(() -> {
				floodFillKernel(pixels, prevColor, nextColor, x, y);
			});
		}
	}
}
