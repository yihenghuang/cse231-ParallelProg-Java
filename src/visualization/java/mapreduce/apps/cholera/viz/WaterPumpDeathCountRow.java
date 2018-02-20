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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import mapreduce.apps.cholera.core.WaterPump;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class WaterPumpDeathCountRow {
	private final BooleanProperty isCorrectProperty = new SimpleBooleanProperty(this, "isCorrect");
	private final ObjectProperty<WaterPump> waterPumpProperty = new SimpleObjectProperty<>(this, "waterPump");
	private final IntegerProperty instructorDeathCountProperty = new SimpleIntegerProperty(this,
			"instructorDeathCountProperty");
	private final IntegerProperty studentDeathCountProperty = new SimpleIntegerProperty(this, "studentDeathCount");

	public WaterPumpDeathCountRow(WaterPump waterPump, int instructorDeathCount, int studentDeathCount) {
		isCorrectProperty.set(studentDeathCount == instructorDeathCount);
		waterPumpProperty.set(waterPump);
		instructorDeathCountProperty.set(instructorDeathCount);
		studentDeathCountProperty.set(studentDeathCount);
	}

	public BooleanProperty isCorrectProperty() {
		return isCorrectProperty;
	}

	public ObjectProperty<WaterPump> waterPumpProperty() {
		return waterPumpProperty;
	}

	public IntegerProperty instructorDeathCountProperty() {
		return instructorDeathCountProperty;
	}

	public IntegerProperty studentDeathCountProperty() {
		return studentDeathCountProperty;
	}
}
