/*******************************************************************************
 * Copyright (C) 2016-2018 Dennis Cosgrove
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
import mapreduce.apps.cholera.studio.CholeraApp;
import mapreduce.apps.cholera.studio.CholeraAppValueRepresentation;
import mapreduce.core.InstructorMapReduceTestUtils;
import mapreduce.framework.core.Mapper;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class CholeraOutbreakVisualizationApp extends Application {
	private static Point2D transformLocationToImageSpace(Location location, double imageScale) {
		// convert to close to 0.0-1.0 as you can tell from the overlay,
		// these constants sadly do not line things up perfectly
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

	private static double calculateFontSize(Number value, double smallValue, double largeValue) {
		double size = 8.0;
		if (value != null) {
			double v;
			if (smallValue > largeValue) {
				double range = smallValue - largeValue;
				v = (value.doubleValue() - largeValue) / range;
			} else {
				double range = largeValue - smallValue;
				v = 1.0 - ((value.doubleValue() - smallValue) / range);
			}
			size += 24 * v;
		}
		return size;
	}

	private static double calculateCircleRadius(Number value, double smallValue, double largeValue) {
		double size = 4.0;
		if (value != null) {
			double v;
			if (smallValue > largeValue) {
				double range = smallValue - largeValue;
				v = (value.doubleValue() - largeValue) / range;
			} else {
				double range = largeValue - smallValue;
				v = 1.0 - ((value.doubleValue() - smallValue) / range);
			}
			size += 24 * v;
		}
		return size;

	}

	private static List<WaterPumpDeathCountRow> createDeathCountRows(Map<WaterPump, Number> instructorDictionary,
			Map<WaterPump, Number> studentDictionary) {

		MapDifference<WaterPump, Number> difference = Maps.difference(instructorDictionary, studentDictionary);

		List<WaterPumpDeathCountRow> result = new LinkedList<>();
		for (Entry<WaterPump, Number> entry : difference.entriesInCommon().entrySet()) {
			result.add(new WaterPumpDeathCountRow(entry.getKey(), entry.getValue(), entry.getValue()));
		}
		for (Entry<WaterPump, ValueDifference<Number>> entry : difference.entriesDiffering().entrySet()) {
			result.add(new WaterPumpDeathCountRow(entry.getKey(), entry.getValue().leftValue(),
					entry.getValue().rightValue()));
		}
		for (Entry<WaterPump, Number> entry : difference.entriesOnlyOnLeft().entrySet()) {
			result.add(new WaterPumpDeathCountRow(entry.getKey(), entry.getValue(), 0));
		}
		for (Entry<WaterPump, Number> entry : difference.entriesOnlyOnRight().entrySet()) {
			result.add(new WaterPumpDeathCountRow(entry.getKey(), 0, entry.getValue()));
		}
		result.sort((entryA, entryB) -> {
			int comp = entryB.instructorValueProperty().intValue() - entryA.instructorValueProperty().intValue();
			if (comp != 0) {
				return comp;
			} else {
				return entryB.studentValueProperty().intValue() - entryA.studentValueProperty().intValue();
			}
		});
		return result;
	}

	private static List<WaterPumpDistanceRow> createDistanceRows(Map<WaterPump, Number> instructorDictionary,
			Map<WaterPump, Number> studentDictionary) {

		MapDifference<WaterPump, Number> difference = Maps.difference(instructorDictionary, studentDictionary);

		List<WaterPumpDistanceRow> result = new LinkedList<>();
		for (Entry<WaterPump, Number> entry : difference.entriesInCommon().entrySet()) {
			result.add(new WaterPumpDistanceRow(entry.getKey(), entry.getValue(), entry.getValue()));
		}
		for (Entry<WaterPump, ValueDifference<Number>> entry : difference.entriesDiffering().entrySet()) {
			result.add(new WaterPumpDistanceRow(entry.getKey(), entry.getValue().leftValue(),
					entry.getValue().rightValue()));
		}
		for (Entry<WaterPump, Number> entry : difference.entriesOnlyOnLeft().entrySet()) {
			result.add(new WaterPumpDistanceRow(entry.getKey(), entry.getValue(), 0));
		}
		for (Entry<WaterPump, Number> entry : difference.entriesOnlyOnRight().entrySet()) {
			result.add(new WaterPumpDistanceRow(entry.getKey(), 0, entry.getValue()));
		}
		result.sort((entryA, entryB) -> {
			int comp = -Double.compare(entryB.instructorValueProperty().doubleValue(),
					entryA.instructorValueProperty().doubleValue());
			if (comp != 0) {
				return comp;
			} else {
				return -Double.compare(entryB.studentValueProperty().doubleValue(),
						entryA.studentValueProperty().doubleValue());
			}
		});
		return result;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Mapper<CholeraDeath, WaterPump, Number> mapper = CholeraApp.createMapper();
		boolean isImplemented;
		try {
			mapper.map(SohoCholeraOutbreak1854.getDeaths()[0], (k, v) -> {

			});
			isImplemented = true;

		} catch (NotYetImplementedException nyie) {
			nyie.printStackTrace();
			isImplemented = false;
		}

		Map<WaterPump, Number> studentPumpToDeathCountDictionary;
		if (isImplemented) {
			try {
				studentPumpToDeathCountDictionary = InstructorMapReduceTestUtils.mapReduceCholeraStudent();
			} catch (Throwable t) {
				studentPumpToDeathCountDictionary = Collections.emptyMap();
			}
		} else {
			studentPumpToDeathCountDictionary = Collections.emptyMap();
		}
		double scale = 800;

		BorderPane pane = new BorderPane();

		Image originalImage = new Image("https://upload.wikimedia.org/wikipedia/commons/2/27/Snow-cholera-map-1.jpg");
		ImageView imageView = new ImageView(originalImage);
		imageView.setFitHeight(scale);
		imageView.setSmooth(true);
		imageView.setPreserveRatio(true);

		TableView<AbstractWaterPumpRow> table = new TableView<>();

		int intCount = 0;
		for (Number value : studentPumpToDeathCountDictionary.values()) {
			if (value instanceof Integer) {
				intCount++;
			}
		}

		int columnCount = 4;
		TableColumn<AbstractWaterPumpRow, Boolean> isMatchCol = new TableColumn<>("match?");
		isMatchCol.setCellValueFactory(new PropertyValueFactory<>(AbstractWaterPumpRow.IS_MATCH_PROPERTY_NAME));
		isMatchCol.prefWidthProperty().bind(table.widthProperty().divide(columnCount));
		isMatchCol.setCellFactory((column) -> {
			return new CheckBoxTableCell<AbstractWaterPumpRow, Boolean>() {
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
							setText("!!!");
						}
					}
				}
			};
		});

		TableColumn<AbstractWaterPumpRow, WaterPump> waterPumpCol = new TableColumn<>("Water Pump");
		waterPumpCol.setCellValueFactory(new PropertyValueFactory<>(AbstractWaterPumpRow.WATER_PUMP_PROPERTY_NAME));
		waterPumpCol.prefWidthProperty().bind(table.widthProperty().divide(columnCount));
		TableColumn<AbstractWaterPumpRow, Integer> instructorValueCol = new TableColumn<>("Instructor Result");
		instructorValueCol
				.setCellValueFactory(new PropertyValueFactory<>(AbstractWaterPumpRow.INSTRUCTOR_VALUE_PROPERTY_NAME));
		instructorValueCol.prefWidthProperty().bind(table.widthProperty().divide(columnCount));
		instructorValueCol.setStyle("-fx-alignment: center-right;");

		TableColumn<AbstractWaterPumpRow, Integer> studentValueCol = new TableColumn<>("Student Result");
		studentValueCol
				.setCellValueFactory(new PropertyValueFactory<>(AbstractWaterPumpRow.STUDENT_VALUE_PROPERTY_NAME));
		studentValueCol.prefWidthProperty().bind(table.widthProperty().divide(columnCount));
		studentValueCol.setStyle("-fx-alignment: center-right;");

		table.getColumns().add(waterPumpCol);
		table.getColumns().add(instructorValueCol);
		table.getColumns().add(studentValueCol);
		table.getColumns().add(isMatchCol);

		List<? extends AbstractWaterPumpRow> rows;
		if (intCount > 0) {
			Map<WaterPump, Number> instructorPumpToDeathCountDictionary = InstructorMapReduceTestUtils
					.mapReduceCholeraInstructorDeathCount();
			rows = createDeathCountRows(instructorPumpToDeathCountDictionary, studentPumpToDeathCountDictionary);
		} else {
			Map<WaterPump, Number> instructorPumpToDeathCountDictionary = InstructorMapReduceTestUtils
					.mapReduceCholeraInstructorDistance(CholeraApp
							.getValueRepresentation() == CholeraAppValueRepresentation.LOW_NUMBERS_SUSPECT_SQUARED);
			rows = createDistanceRows(instructorPumpToDeathCountDictionary, studentPumpToDeathCountDictionary);
		}
		ObservableList<AbstractWaterPumpRow> observableList = FXCollections.observableArrayList();
		for (AbstractWaterPumpRow row : rows) {
			observableList.add(row);
		}
		table.setItems(observableList);
		table.setPrefWidth(300);

		pane.setLeft(imageView);
		pane.setCenter(table);

		double min = Double.MAX_VALUE;
		double max = -Double.MAX_VALUE;
		for (Number value : studentPumpToDeathCountDictionary.values()) {
			min = Math.min(min, value.doubleValue());
			max = Math.max(max, value.doubleValue());
		}

		double big;
		double small;
		if (CholeraApp.getValueRepresentation() == CholeraAppValueRepresentation.HIGH_NUMBERS_SUSPECT) {
			big = max;
			small = min;
		} else {
			big = min;
			small = max;
		}

		for (WaterPump pump : WaterPump.values()) {
			Number deathCount = studentPumpToDeathCountDictionary.get(pump);
			double fontSize = calculateFontSize(deathCount, big, small);
			double circleRadius = calculateCircleRadius(deathCount, big, small);

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
