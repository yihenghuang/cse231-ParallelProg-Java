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

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import mapreduce.apps.cholera.core.WaterPump;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class WaterPumpDistanceRow extends AbstractWaterPumpRow {
	private final DoubleProperty instructorValueProperty = new SimpleDoubleProperty(this,
			INSTRUCTOR_VALUE_PROPERTY_NAME);
	private final DoubleProperty studentValueProperty = new SimpleDoubleProperty(this, STUDENT_VALUE_PROPERTY_NAME);

	private static final double EPSILON = 0.000001;

	public WaterPumpDistanceRow(WaterPump waterPump, Number instructorValue, Number studentValue) {
		super(waterPump, Math.abs(studentValue.doubleValue() - instructorValue.doubleValue()) < EPSILON);
		instructorValueProperty.set(instructorValue.doubleValue());
		studentValueProperty.set(studentValue.doubleValue());
	}

	public DoubleProperty instructorValueProperty() {
		return instructorValueProperty;
	}

	public DoubleProperty studentValueProperty() {
		return studentValueProperty;
	}
}
