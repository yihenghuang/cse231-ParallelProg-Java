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

package edu.wustl.cse231s.util;

import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.common.collect.MapDifference.ValueDifference;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class MapUtils {
	private static <K, V> void appendMap(StringBuilder sb, Map<K, V> map, String format) {
		if (map.size() > 0) {
			final int CAP = 20;
			sb.append("\n");
			sb.append(String.format(format, map.size()));
			if (map.size() > CAP) {
				sb.append("(the first ").append(CAP).append(" listed)");
			}
			sb.append(":\n");
			SortedMap<K, V> sortedMap = new TreeMap<>(map);
			int i = 0;
			for (Entry<K, V> entry : sortedMap.entrySet()) {
				sb.append("    ").append(entry).append("\n");
				i++;
				if (i >= CAP) {
					break;
				}
			}
		}
	}

	private static <K, V> void appendMapValueDifference(StringBuilder sb, Map<K, ValueDifference<V>> map,
			String format) {
		if (map.size() > 0) {
			final int CAP = 20;
			sb.append("\n");
			sb.append(String.format(format, map.size()));
			if (map.size() > CAP) {
				sb.append("(the first ").append(CAP).append(" listed)");
			}
			sb.append(":\n");
			SortedMap<K, ValueDifference<V>> sortedMap = new TreeMap<>(map);
			int i = 0;
			for (Entry<K, ValueDifference<V>> entry : sortedMap.entrySet()) {
				sb.append("    ").append(entry.getKey()).append(" expected: ").append(entry.getValue().leftValue())
						.append("; actual: ").append(entry.getValue().rightValue()).append("\n");
				i++;
				if (i >= CAP) {
					break;
				}
			}
		}
	}

	public static <K, V> void assertMapsAreEquivalent(Map<K, V> expected, Map<K, V> actual) {
		if (Objects.equals(expected, actual)) {
			// pass
		} else {
			MapDifference<K, V> mapDifference = Maps.difference(expected, actual);
			StringBuilder sb = new StringBuilder();
			appendMap(sb, mapDifference.entriesOnlyOnLeft(), "ERROR. %d expected key(s) not found in student result");
			appendMap(sb, mapDifference.entriesOnlyOnRight(), "ERROR. %d unexpected key(s) found in student result");
			appendMapValueDifference(sb, mapDifference.entriesDiffering(),
					"ERROR. %d key(s) associated with incorrect value(s)");
			appendMap(sb, mapDifference.entriesInCommon(), "SUCCESS. %d key value pair(s) matched");
			assertTrue(sb.toString(), false);
		}
	}

}
