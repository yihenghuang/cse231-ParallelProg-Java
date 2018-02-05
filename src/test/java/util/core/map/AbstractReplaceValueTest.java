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
package util.core.map;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public abstract class AbstractReplaceValueTest {
	protected abstract <K, V> Map<K, V> createMap();
	
	@Test
	public void testPutReplaceValue() {
		Map<String, String> mapNameToPlace = this.createMap();
		String bearName = "Paddington";
		mapNameToPlace.put(bearName, "Peru");
		mapNameToPlace.put(bearName, "London");
		Assert.assertEquals(1, mapNameToPlace.size());
		Assert.assertNotEquals("Peru", mapNameToPlace.get(bearName));
		Assert.assertEquals("London", mapNameToPlace.get(bearName));
	}

	@Test
	public void testPutReplaceWithSameValue() {
		Map<String, String> mapNameToPlace = this.createMap();
		String key = "A";
		String value = "B";
		
		mapNameToPlace.put(key, value);
		Assert.assertEquals(1, mapNameToPlace.size());
		Assert.assertEquals(value, mapNameToPlace.get(key));

		mapNameToPlace.put(key, value);
		Assert.assertEquals(1, mapNameToPlace.size());
		Assert.assertEquals(value, mapNameToPlace.get(key));
	}
}
