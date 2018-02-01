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
package edu.wustl.cse231s.fx;

import java.nio.ByteBuffer;

import edu.wustl.cse231s.IntendedForStaticAccessOnlyError;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritablePixelFormat;

public class FxImageUtils {
	private FxImageUtils() {
		throw new IntendedForStaticAccessOnlyError();
	}

	public static byte[] getBuffer(PixelReader reader, int bytesPerPixel, int width, int height, WritablePixelFormat<ByteBuffer> pixelformat ) {
		int x = 0;
		int y = 0;
		byte[] buffer = new byte[width * height * bytesPerPixel];
		int offset = 0;
		int scanlineStride = width * bytesPerPixel;
		reader.getPixels(x, y, width, height, pixelformat, buffer, offset, scanlineStride);
		return buffer;
	}
	
	public static void copyBuffer(byte[] from, int bytesPerPixel, int width, int height, WritablePixelFormat<ByteBuffer> pixelformat, PixelWriter to) {
		int x = 0;
		int y = 0;
		int offset = 0;
		int scanlineStride = width * 4;
		to.setPixels(x, y, width, height, pixelformat, from, offset, scanlineStride);
	}
	
}
