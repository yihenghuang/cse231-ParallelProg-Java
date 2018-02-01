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
package floodfill;

import static edu.wustl.cse231s.v5.V5.launchApp;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.wustl.cse231s.color.ColorUtil;
import edu.wustl.cse231s.fx.FxImageUtils;
import edu.wustl.cse231s.junit.JUnitUtils;
import edu.wustl.cse231s.pixels.MutablePixels;
import floodfill.studio.FloodFiller;
import floodfill.viz.FloodFillVizApp;
import floodfill.viz.FxMutablePixels;
import floodfill.viz.FxThreadConfinementPolicy;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.paint.Color;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class FloodFillTest {
	@Test(timeout = JUnitUtils.DEFAULT_TIMEOUT)
	public void test() {
		WritableImage original = FloodFillVizApp.loadClampedToBlackAndWhiteWritableImage();

		int x = 100;
		int y = 100;

		Color[] colors = ColorUtil.getColorPalette();
		Color replacementColor = colors[0];

		int bytesPerPixel = 4;
		WritablePixelFormat<ByteBuffer> pixelformat = PixelFormat.getByteBgraInstance();
		int width = (int) original.getWidth();
		int height = (int) original.getHeight();

		byte[] originalBytes = FxImageUtils.getBuffer(original.getPixelReader(), bytesPerPixel, width, height,
				pixelformat);

		WritableImage studentImage = new WritableImage(width, height);
		FxImageUtils.copyBuffer(originalBytes, bytesPerPixel, width, height, pixelformat,
				studentImage.getPixelWriter());

		MutablePixels pixels = new FxMutablePixels(studentImage,
				FxThreadConfinementPolicy.NOT_CONFINED_TO_JAVAFX_THREAD);
		launchApp(() -> {
			FloodFiller.floodFill(pixels, replacementColor, x, y);
		});
		Assert.assertTrue(isCorrect(original, studentImage, replacementColor, x, y));
	}

	@BeforeClass
	public static void initializeJavaFx() throws InterruptedException {
		// start up a JavaFx App so that Images are supported
		new Thread(() -> {
			Application.launch(FxStartLatchApp.class, new String[] {});
		}).start();
		FxStartLatchApp.awaitStarted();
	}

	private static class Pixel {
		private final MutablePixels pixels;
		private final int x;
		private final int y;

		public Pixel(MutablePixels pixels, int x, int y) {
			this.pixels = pixels;
			this.x = x;
			this.y = y;
		}

		public Pixel createNeighbor(int dx, int dy) {
			return new Pixel(this.pixels, this.x + dx, this.y + dy);
		}

		public Color getColor() {
			return pixels.getColor(x, y);
		}

		public void setColor(Color color) {
			pixels.setColor(this.x, this.y, color);
		}

		public boolean isInBounds() {
			return pixels.isInBounds(x, y);
		}
	}

	private static void floodFillViaQueue(Color replacementColor, Pixel p0) {
		Color originalColor = p0.getColor();
		Queue<Pixel> queue = new LinkedList<>();
		queue.add(p0);
		while (queue.isEmpty() == false) {
			Pixel pixel = queue.poll();
			if (pixel.isInBounds()) {
				if (Objects.equals(originalColor, pixel.getColor())) {
					pixel.setColor(replacementColor);
					queue.add(pixel.createNeighbor(1, 0));
					queue.add(pixel.createNeighbor(-1, 0));
					queue.add(pixel.createNeighbor(0, 1));
					queue.add(pixel.createNeighbor(0, -1));
				}
			}
		}
	}

	private static boolean isCorrect(Image original, Image studentResult, Color replacementColor, int x, int y) {
		int bytesPerPixel = 4;
		WritablePixelFormat<ByteBuffer> pixelformat = PixelFormat.getByteBgraInstance();
		int width = (int) original.getWidth();
		int height = (int) original.getHeight();

		byte[] originalBytes = FxImageUtils.getBuffer(original.getPixelReader(), bytesPerPixel, width, height,
				pixelformat);

		byte[] studentBytes = FxImageUtils.getBuffer(studentResult.getPixelReader(), bytesPerPixel, width, height,
				pixelformat);

		WritableImage instructorImage = new WritableImage(width, height);
		FxImageUtils.copyBuffer(originalBytes, bytesPerPixel, width, height, pixelformat,
				instructorImage.getPixelWriter());

		FxMutablePixels instructorPixels = new FxMutablePixels(instructorImage,
				FxThreadConfinementPolicy.NOT_CONFINED_TO_JAVAFX_THREAD);
		floodFillViaQueue(replacementColor, new Pixel(instructorPixels, x, y));

		byte[] instructorBytes = FxImageUtils.getBuffer(instructorImage.getPixelReader(), bytesPerPixel, width, height,
				pixelformat);

		return Arrays.equals(instructorBytes, studentBytes);
	}
}
