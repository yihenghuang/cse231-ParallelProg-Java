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
package lambda.demo.compute;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class LambdasMapComputeExample {
	public static void main(final String[] args) {
		String text = "abracadabra";
		// NOTE: we use a TreeMap<K,V> since it is sorted producing consistent output
		Map<Character, Integer> mapCharacterToCount = new TreeMap<>();
		for( char ch : text.toCharArray() ) {
			// https://docs.oracle.com/javase/8/docs/api/java/util/Map.html#compute-K-java.util.function.BiFunction-
			mapCharacterToCount.compute(ch, (Character character, Integer count)-> {
				if( count != null ) {
					//we have associated character before
					return count+1;
				} else {
					//first encounter with character
					return 1;
				}
			});
		}
		for( Entry<Character, Integer> entry : mapCharacterToCount.entrySet() ) {
			System.out.println(entry);
		}
	}
}
