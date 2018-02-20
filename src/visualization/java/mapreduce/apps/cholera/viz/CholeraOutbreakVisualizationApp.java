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
package mapreduce.apps.cholera.viz;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.MapDifference;
import com.google.common.collect.MapDifference.ValueDifference;
import com.google.common.collect.Maps;

import edu.wustl.cse231s.NotYetImplementedException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import mapreduce.apps.cholera.core.CholeraDeath;
import mapreduce.apps.cholera.core.Location;
import mapreduce.apps.cholera.core.SohoCholeraOutbreak1854;
import mapreduce.apps.cholera.core.WaterPump;
import mapreduce.apps.cholera.studio.CholeraMapper;
import mapreduce.core.InstructorMapReduceTestUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class CholeraOutbreakVisualizationApp extends Application {
	private static Point2D transformLocationToImageSpace(Location location, double imageScale) {
		// convert to close to 0.0-1.0
		// as you can tell from the overlay, these constants do not line things
		// up
		// perfectly
		double xMin = 3.0;
		double xMax = 19.0;
		double yMin = 3.0;
		double yMax = 19.0;
		double xRange = xMax - xMin;
		double yRange = yMax - yMin;

		double x = ((location.getX() - xMin) / xRange);
		double y = ((location.getY() - yMin) / yRange);
		y = 1.0 - y;

		// scale to image
		x *= imageScale;
		y *= imageScale;

		return new Point2D(x, y);
	}

	private static double calculateFontSizeBasedOnDeathCount(Integer deathCount) {
		double size = 8.0;
		if (deathCount != null) {
			size += deathCount / 12;
		}
		return size;
	}

	private static double calculateCircleRadiusBasedOnDeathCount(Integer deathCount) {
		double size = 4.0;
		if (deathCount != null) {
			size += deathCount / 12;
		}
		return size;
	}

	private static List<WaterPumpDeathCountRow> createEntriesSortedByDeathCount(
			Map<WaterPump, Integer> instructorPumpToDeathCountDictionary,
			Map<WaterPump, Integer> studentPumpToDeathCountDictionary) {

		MapDifference<WaterPump, Integer> difference = Maps.difference(instructorPumpToDeathCountDictionary,
				studentPumpToDeathCountDictionary);

		List<WaterPumpDeathCountRow> result = new LinkedList<>();
		for (Entry<WaterPump, Integer> entry : difference.entriesInCommon().entrySet()) {
			result.add(new WaterPumpDeathCountRow(entry.getKey(), entry.getValue(), entry.getValue()));
		}
		for (Entry<WaterPump, ValueDifference<Integer>> entry : difference.entriesDiffering().entrySet()) {
			result.add(new WaterPumpDeathCountRow(entry.getKey(), entry.getValue().leftValue(),
					entry.getValue().rightValue()));
		}
		for (Entry<WaterPump, Integer> entry : difference.entriesOnlyOnLeft().entrySet()) {
			result.add(new WaterPumpDeathCountRow(entry.getKey(), entry.getValue(), 0));
		}
		for (Entry<WaterPump, Integer> entry : difference.entriesOnlyOnRight().entrySet()) {
			result.add(new WaterPumpDeathCountRow(entry.getKey(), 0, entry.getValue()));
		}
		result.sort((entryA, entryB) -> {
			int comp = entryB.instructorDeathCountProperty().intValue()
					- entryA.instructorDeathCountProperty().intValue();
			if (comp != 0) {
				return comp;
			} else {
				return entryB.studentDeathCountProperty().intValue() - entryA.studentDeathCountProperty().intValue();
			}
		});
		return result;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		CholeraMapper mapper = new CholeraMapper();
		boolean isImplemented;
		try {
			mapper.map(SohoCholeraOutbreak1854.getDeaths()[0], (k, v) -> {

			});
			isImplemented = true;

		} catch (NotYetImplementedException nyie) {
			nyie.printStackTrace();
			isImplemented = false;
		}

		Map<WaterPump, Integer> studentPumpToDeathCountDictionary;
		if (isImplemented) {
			try {
				studentPumpToDeathCountDictionary = InstructorMapReduceTestUtils.mapReduceCholeraStudent(this,
						primaryStage);
			} catch (Throwable t) {
				studentPumpToDeathCountDictionary = Collections.emptyMap();
			}
		} else {
			studentPumpToDeathCountDictionary = Collections.emptyMap();
		}
		double scale = 800;

		Map<WaterPump, Integer> instructorPumpToDeathCountDictionary = InstructorMapReduceTestUtils
				.mapReduceCholeraInstructor(this, primaryStage);

		BorderPane pane = new BorderPane();

		Image originalImage = new Image("https://upload.wikimedia.org/wikipedia/commons/2/27/Snow-cholera-map-1.jpg");
		ImageView imageView = new ImageView(originalImage);
		imageView.setFitHeight(scale);
		imageView.setSmooth(true);
		imageView.setPreserveRatio(true);

		TableView<WaterPumpDeathCountRow> table = new TableView<>();

		List<WaterPumpDeathCountRow> rows = createEntriesSortedByDeathCount(instructorPumpToDeathCountDictionary,
				studentPumpToDeathCountDictionary);

		ObservableList<WaterPumpDeathCountRow> observableList = FXCollections.observableArrayList();
		for (WaterPumpDeathCountRow row : rows) {
			observableList.add(row);
		}
		table.setItems(observableList);

		int columnCount = 4;
		TableColumn<WaterPumpDeathCountRow, Boolean> isCorrectCol = new TableColumn<>("correct?");
		isCorrectCol.setCellValueFactory(new PropertyValueFactory<>("isCorrect"));
		isCorrectCol.prefWidthProperty().bind(table.widthProperty().divide(columnCount));
		isCorrectCol.setCellFactory((column) -> {
			return new CheckBoxTableCell<WaterPumpDeathCountRow, Boolean>() {
				@Override
				public void updateItem(Boolean item, boolean isEmpty) {
					super.updateItem(item, isEmpty);
					if (isEmpty) {
						// pass
					} else {
						if (item) {
							// pass
						} else {
							setTextFill(Color.RED);
							setText("ERROR");
						}
					}
				}
			};
		});

		TableColumn<WaterPumpDeathCountRow, WaterPump> waterPumpCol = new TableColumn<>("Water Pump");
		waterPumpCol.setCellValueFactory(new PropertyValueFactory<>("waterPump"));
		waterPumpCol.prefWidthProperty().bind(table.widthProperty().divide(columnCount));
		TableColumn<WaterPumpDeathCountRow, Integer> instructorDeathCountCol = new TableColumn<>(
				"(Instructor Result) Death Count");
		instructorDeathCountCol.setCellValueFactory(new PropertyValueFactory<>("instructorDeathCount"));
		instructorDeathCountCol.prefWidthProperty().bind(table.widthProperty().divide(columnCount));
		TableColumn<WaterPumpDeathCountRow, Integer> studentDeathCountCol = new TableColumn<>(
				"(Student Result) Death Count");
		studentDeathCountCol.setCellValueFactory(new PropertyValueFactory<>("studentDeathCount"));
		studentDeathCountCol.prefWidthProperty().bind(table.widthProperty().divide(columnCount));

		table.getColumns().add(waterPumpCol);
		table.getColumns().add(instructorDeathCountCol);
		table.getColumns().add(studentDeathCountCol);
		table.getColumns().add(isCorrectCol);
		table.setPrefWidth(300);

		pane.setLeft(imageView);
		pane.setCenter(table);

		for (WaterPump pump : WaterPump.values()) {
			Integer deathCount = studentPumpToDeathCountDictionary.get(pump);
			double fontSize = calculateFontSizeBasedOnDeathCount(deathCount);
			double circleRadius = calculateCircleRadiusBasedOnDeathCount(deathCount);

			Point2D center = transformLocationToImageSpace(pump.getLocation(), scale);
			Circle circle = new Circle(center.getX(), center.getY(), circleRadius);
			circle.setFill(new Color(0.0, 0.0, 1.0, 0.5));

			Text text = new Text();
			text.setFont(Font.font("Serif", FontWeight.BOLD, fontSize));
			text.setText(pump.name());
			text.setX(center.getX() + circleRadius);
			text.setY(center.getY());
			text.setFill(Color.DARKBLUE);
			text.setTextOrigin(VPos.CENTER);
			pane.getChildren().addAll(circle, text);
		}

		for (CholeraDeath deaths : SohoCholeraOutbreak1854.getDeaths()) {
			Location location = deaths.getLocation();
			Point2D center = transformLocationToImageSpace(location, scale);
			Circle circle = new Circle(center.getX(), center.getY(), 4.0);
			circle.setFill(new Color(1.0, 0.0, 0.0, 0.25));
			circle.setStroke(Color.BLACK);
			pane.getChildren().add(circle);
		}

		Scene scene = new Scene(pane);
		primaryStage.setTitle("1854 Cholera Outbreak");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
