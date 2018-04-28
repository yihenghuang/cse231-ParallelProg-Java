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
package raytrace.viz;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Supplier;

import org.sunflow.SunflowAPI;
import org.sunflow.core.Display;
import org.sunflow.image.Color;

import edu.wustl.cse231s.color.ColorUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import raytrace.core.RayTraceTaskContext;
import raytrace.core.RayTraceUtils;
import raytrace.core.RayTracer;
import raytrace.core.runtime.RtImageSampler;
import raytrace.demo.SplitFourWayRayTracer;
import raytrace.fun.DivideAndConquerRayTracer;
import raytrace.fun.WorkStealingRayTracer;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class RaytraceVizApp extends Application {
	private static enum RayTracerSupplier implements Supplier<RayTracer> {
		SPLIT_4_WAY() {
			@Override
			public RayTracer get() {
				return new SplitFourWayRayTracer();
			}
		},
		DIVIDE_AND_CONQUER() {
			@Override
			public RayTracer get() {
				return new DivideAndConquerRayTracer();
			}
		},
		WORK_STEALING() {
			@Override
			public RayTracer get() {
				return new WorkStealingRayTracer();
			}
		};
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane root = new BorderPane();
		WritableImage rgbWritableImage = new WritableImage(640, 480);
		ImageView rgbImageView = new ImageView(rgbWritableImage);
		WritableImage idWritableImage = new WritableImage(640, 480);
		ImageView idImageView = new ImageView(idWritableImage);

		rgbImageView.setFitWidth(480);
		rgbImageView.setPreserveRatio(true);
		idImageView.setFitWidth(480);
		idImageView.setPreserveRatio(true);
		
		Scene scene = new Scene(root);
		HBox hBox = new HBox();
		hBox.getChildren().addAll(rgbImageView, idImageView);
		root.setCenter(hBox);

		Collection<Button> buttons = new LinkedList<>();
		VBox vBox = new VBox();
		for (RayTracerSupplier rayTracerSupplier : RayTracerSupplier.values()) {
			Button button = new Button(rayTracerSupplier.name());
			buttons.add(button);
			button.setOnAction((ActionEvent e) -> {
				for(Button b : buttons) {
					b.setDisable(true);
				}
				new Thread(() -> {
					URL url = RayTraceUtils.class.getResource("../bunny.sc");
					String path = url.getFile();
					SunflowAPI api = new SunflowAPI();
					api.parse(path);
					api.build();
					api.options(SunflowAPI.DEFAULT_OPTIONS);

					RayTracer rayTracer = rayTracerSupplier.get();
					RtImageSampler imageSampler = new RtImageSampler(rayTracer);

					javafx.scene.paint.Color[] colors = ColorUtil.getColorPalette();
					
					api.render(SunflowAPI.DEFAULT_OPTIONS, new Display() {

						@Override
						public void imageBegin(int w, int h, int bucketSize) {
							Platform.runLater(() -> {
								for (int j = 0; j < h; j++) {
									for (int i = 0; i < w; i++) {
										idWritableImage.getPixelWriter().setColor(i, j, javafx.scene.paint.Color.LIGHTGRAY);
										rgbWritableImage.getPixelWriter().setColor(i, j, javafx.scene.paint.Color.LIGHTGRAY);
									}
								}
							});
						}

						@Override
						public void imagePrepare(int x, int y, int w, int h, int id) {
							Platform.runLater(() -> {
								for (int j = 0; j < h; j++) {
									for (int i = 0; i < w; i++) {
										idWritableImage.getPixelWriter().setColor(x + i, y + j,
												id != RayTraceTaskContext.UNKNOWN_TASK_ID ? colors[id] : javafx.scene.paint.Color.GRAY);
									}
								}
							});
						}

						@Override
						public void imageUpdate(int x, int y, int w, int h, Color[] data) {
							Color[] copy = Arrays.copyOf(data, data.length);
							Platform.runLater(() -> {
								for (int j = 0, index = 0; j < h; j++) {
									for (int i = 0; i < w; i++, index++) {
										float[] rgb = copy[index].copy().toNonLinear().getRGB();
										rgbWritableImage.getPixelWriter().setColor(x + i, y + j,
												javafx.scene.paint.Color.color(rgb[0], rgb[1], rgb[2]));
									}
								}
							});
						}

						@Override
						public void imageFill(int x, int y, int w, int h, Color c) {
						}

						@Override
						public void imageEnd() {
							Platform.runLater(() -> {
								for(Button b : buttons) {
									b.setDisable(false);
								}
							});
						}

					}, imageSampler);

				}).start();
			});
			vBox.getChildren().add(button);
		}

		root.setRight(vBox);

		primaryStage.setTitle("Raytrace");
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
