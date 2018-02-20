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
package mapreduce.apps.cholera.core;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public enum WaterPump {
	// source: 
	// https://www1.udel.edu/johnmack/frec682/cholera/pumps.txt
	// https://www1.udel.edu/johnmack/frec682/cholera/cholera2.html
	OXFORD_MARKET(8.6512012, 17.8915997), 
	CASTLE_STREET_EAST(10.9847803, 18.5178509), 
	OXFORD_STREET_AND_BEANERS_STREET(13.3781900, 17.3945408), 
	NEWMAN_STREET(14.8798304, 17.8099194), 
	MARLBOROUGH_MEWS(8.6947680, 14.9054699), 
	CARNABY_STREET(8.8644161, 12.7535400), 
	BROAD_STREET(12.5713596, 11.7271700), 
	WARWICK_STREET(10.6609697, 7.4286470), 
	BRIDLE_STREET(13.5214596, 7.9582500), 
	RUPERT_STREET(16.4348907, 9.2521296), 
	DEAN_STREET(18.9143906, 9.7378187), 
	TIGHBORNE_STREET(16.0051098, 5.0468378), 
	SAVILLE_ROW_AND_VICO_STREET(8.9994402, 5.1010232);

	private WaterPump(double x, double y) {
		this.location = new Location(x, y);
	}
	
	public Location getLocation() {
		return this.location;
	}
	
	private final Location location;
}
