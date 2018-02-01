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

import static edu.wustl.cse231s.v5.V5.isLaunched;
import static edu.wustl.cse231s.v5.V5.launchApp;

import java.net.URL;

import org.apache.commons.lang3.mutable.MutableInt;

import edu.wustl.cse231s.color.ColorUtil;
import edu.wustl.cse231s.pixels.MutablePixels;
import floodfill.studio.FloodFiller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class FloodFillVizApp extends Application {
	private static void clampToBlackAndWhite(WritableImage image) {
		PixelReader pixelReader = image.getPixelReader();
		PixelWriter pixelWriter = image.getPixelWriter();
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				Color color = pixelReader.getColor(x, y);
				double opacity = color.getOpacity();
				pixelWriter.setColor(x, y, opacity < 0.5 ? Color.WHITE : Color.BLACK);
			}
		}
	}

	// TODO: reduce visibility
	public static WritableImage loadClampedToBlackAndWhiteWritableImage() {
		// source:
		// "https://sites.wustl.edu/publicaffairs/files/2015/07/Washington_University_Monogram_Open1c200-01-17zuofc.png";
		URL url = FloodFillVizApp.class.getResource("WashULogo.png");
		Image image = new Image(url.toExternalForm());
		int width = (int) image.getWidth();
		int height = (int) image.getHeight();
		WritableImage writableImage = new WritableImage(image.getPixelReader(), width, height);
		clampToBlackAndWhite(writableImage);
		return writableImage;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane root = new BorderPane();
		WritableImage writableImage = loadClampedToBlackAndWhiteWritableImage();

		MutablePixels pixels = new FxMutablePixels(writableImage, FxThreadConfinementPolicy.CONFINED_TO_JAVAFX_THREAD);

		ImageView imageView = new ImageView(writableImage);

		Color[] colors = ColorUtil.getColorPalette();

		MutableInt count = new MutableInt();

		root.setOnMousePressed((MouseEvent e) -> {
			if (isLaunched()) {
				System.err.println(
						"Previous flood fill still processing.  Ignoring click at " + e.getX() + " " + e.getY() + ".");
			} else {
				int colorIndex = count.intValue() % colors.length;
				Color color = colors[colorIndex];
				int x = (int) e.getX();
				int y = (int) e.getY();

				new Thread(() -> {
					launchApp(() -> {
						FloodFiller.floodFill(pixels, color, x, y);
					});
				}).start();
				count.increment();
			}
		});
		Scene scene = new Scene(root);
		root.setCenter(imageView);

		primaryStage.setTitle("Flood Fill");
		primaryStage.setScene(scene);
		primaryStage.show();

		Platform.runLater(() -> primaryStage.sizeToScene());

		primaryStage.setOnCloseRequest(e -> {
			Platform.exit();
			System.exit(0);
		});
	}

	public static void main(String[] args) throws Exception {
		launch(args);
	}
}
