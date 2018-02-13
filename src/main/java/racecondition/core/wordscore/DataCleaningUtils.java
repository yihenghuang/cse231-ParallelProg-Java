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
package racecondition.core.wordscore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class DataCleaningUtils {
	
	//sadly, in order to give the list to list conversion something to do
	//we need to get rid of any duplicates that would be lost in the map later
	/**
	 * Goes through the given list of sourceLines and gets rid of any duplicates
	 * to return a list of only unique values
	 * @param sourceLines, the list of strings
	 * @return new list of strings without duplicates
	 */
	public static List<String> toUnique( List<String> sourceLines ) {
		Set<String> set = new HashSet<>();
		List<String> result = new ArrayList<>( sourceLines.size() );
		for( String sourceLine : sourceLines ) {
			String word = WordScoreUtils.toCleanedWord(sourceLine);
			if( set.contains(word) ) {
				//pass
			} else {
				set.add(word);
				result.add(sourceLine);
			}
		}
		return result;
	}
}
