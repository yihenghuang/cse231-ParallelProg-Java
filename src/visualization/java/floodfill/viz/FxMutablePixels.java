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
package floodfill.viz;

import java.nio.ByteBuffer;

import edu.wustl.cse231s.fx.FxUtils;
import edu.wustl.cse231s.pixels.MutablePixels;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.paint.Color;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class FxMutablePixels implements MutablePixels {
	public FxMutablePixels(Image image, FxThreadConfinementPolicy fxThreadConfinementPolicy) {
		if (image instanceof WritableImage) {
			this.writableImage = (WritableImage) image;
		} else {
			this.writableImage = new WritableImage(image.getPixelReader(), (int) image.getWidth(),
					(int) image.getHeight());
		}
		this.fxThreadConfinementPolicy = fxThreadConfinementPolicy;
	}

	@Override
	public int getWidth() {
		return (int) this.writableImage.getWidth();
	}

	@Override
	public int getHeight() {
		return (int) this.writableImage.getHeight();
	}

	@Override
	public boolean isInBounds(int x, int y) {
		return x >= 0 && x < this.writableImage.getWidth() && y >= 0 && y < this.writableImage.getHeight();
	}

	@Override
	public Color getColor(int x, int y) {
		return this.writableImage.getPixelReader().getColor(x, y);
	}

	@Override
	public void setColor(int x, int y, Color color) {
		Runnable runnable = ()->{
			this.writableImage.getPixelWriter().setColor(x, y, color);
		};
		if (this.fxThreadConfinementPolicy == FxThreadConfinementPolicy.NOT_CONFINED_TO_JAVAFX_THREAD || Platform.isFxApplicationThread()) {
			runnable.run();
		} else {
			try {
				FxUtils.runAndWait(runnable);				
			} catch( InterruptedException ie ) {
				Thread.currentThread().interrupt();
			}
		}
	}

	private final WritableImage writableImage;
	private final FxThreadConfinementPolicy fxThreadConfinementPolicy;
	
	
	//TODO: remove below
	
	private static void copyAllColors(Image from, PixelWriter to) {
		final int bytesPerPixel = 4;
		int x = 0;
		int y = 0;
		int width = (int) from.getWidth();
		int height = (int) from.getHeight();
		byte[] buffer = new byte[width * height * bytesPerPixel];
		WritablePixelFormat<ByteBuffer> pixelformat = PixelFormat.getByteBgraInstance();
		int offset = 0;
		int scanlineStride = width * 4;
		from.getPixelReader().getPixels(x, y, width, height, pixelformat, buffer, offset, scanlineStride);
		to.setPixels(x, y, width, height, pixelformat, buffer, offset, scanlineStride);
	}

	public WritableImage createWritableImage() {
		return new WritableImage(this.writableImage.getPixelReader(), (int) this.writableImage.getWidth(),
				(int) this.writableImage.getHeight());
	}

	public void copyAllColorsTo(WritableImage other) {
		copyAllColors(this.writableImage, other.getPixelWriter());
	}

	public void setAllColorsFrom(Image image) {
		copyAllColors(image, this.writableImage.getPixelWriter());
	}
	
}
