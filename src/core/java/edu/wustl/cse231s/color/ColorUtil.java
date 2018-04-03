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
package edu.wustl.cse231s.color;

import javafx.scene.paint.Color;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class ColorUtil {
	public static Color[] getColorPalette() {
		// chosen from conservative 7-color palette: http://mkweb.bcgsc.ca/colorblind/
		return new Color[] { /* Color.BLACK, */Color.rgb(230, 159, 0), Color.rgb(86, 180, 233), Color.rgb(0, 158, 115),
				Color.rgb(240, 228, 66), Color.rgb(0, 114, 178), Color.rgb(213, 94, 0), Color.rgb(204, 121, 167) };
	}

	public static Color[] getColorPalette12() {
		return new Color[] { Color.web("006e82"), Color.web("0a64a0"), Color.web("005ac8"),
				Color.web("00a0fa"), Color.web("00c8fa"), Color.web("14d2dc"), Color.web("aa0a3c"), Color.web("fa2800"),
				Color.web("fa5078"), Color.web("f0f032"), Color.web("fae68c"), Color.web("fae6be") };
	}

	public static Color[] getColorPalette15() {
		// NOTE: all colors do not seem to line up
		// http://www.somersault1824.com/tips-for-designing-scientific-figures-for-color-blind-readers/
		return new Color[] { Color.rgb(0, 0, 0), Color.rgb(0, 73, 73), Color.rgb(0, 146, 146), Color.rgb(255, 109, 182),
				Color.rgb(255, 182, 119), Color.rgb(73, 0, 146), Color.rgb(0, 109, 219), Color.rgb(182, 109, 255),
				Color.rgb(109, 182, 255), Color.rgb(182, 219, 255), Color.rgb(146, 0, 0), Color.rgb(146, 73, 0),
				Color.rgb(219, 209, 0), Color.rgb(36, 255, 36), Color.rgb(255, 255, 109) };
	}

	public static String toWeb(Color color) {
		return String.format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255),
				(int) (color.getBlue() * 255));
	}
}
