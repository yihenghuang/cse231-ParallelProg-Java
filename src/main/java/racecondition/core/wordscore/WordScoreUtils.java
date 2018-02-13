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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class WordScoreUtils {
	
	/**
	 * Creates a map of the letters associated with a given point value
	 * @return map of scores and their associated letters
	 */
	public static Map<Integer, String> createScoreToLettersMap() {
		Map<Integer, String> result = new HashMap<>();
		result.put(10, "qz");
		result.put(8, "jx");
		result.put(5, "k");
		result.put(4, "fhvwy");
		result.put(3, "bcmp");
		result.put(2, "dg");
		result.put(1, "eaionrtlsu");
		return result;
	}

	/**
	 * Creates a map of scores associated with a given letter
	 * @param scoreToLettersMap, a map of scores and their associated letters
	 * @return map of letters and their associated scores
	 */
	public static Map<Character, Integer> createLetterToScoreMap(Map<Integer, String> scoreToLettersMap) {
		Map<Character, Integer> result = new HashMap<>();
		for (Entry<Integer, String> entry : scoreToLettersMap.entrySet()) {
			int score = entry.getKey();
			String letters = entry.getValue();
			for (char letter : letters.toCharArray()) {
				result.put(letter, score);
			}
		}
		return result;
	}

	/**
	 * Cleans up words by making them lowercase and replacing special characters
	 * @param sourceLine, the string/word to consider
	 * @return the cleaned string
	 */
	public static String toCleanedWord( String sourceLine ) {
		String word = sourceLine.toLowerCase();
		return word.replaceAll("[^a-z]", "");
	}
	
	public static boolean isCleanedWord(String word) {
		//TODO: investigate further testing
		return StringUtils.isAllLowerCase(word);
	}

	/**
	 * Calculates the score associated with a given word
	 * @param word, the word to examine
	 * @param mapLetterToScore, a map of letters and their associated scores
	 * @return the score of the given word
	 */
	public static int calculateScore(String word, Map<Character, Integer> mapLetterToScore) {
		int sum = 0;
		for( char letter : word.toCharArray() ) {
			sum += mapLetterToScore.get(letter);
//			char lowerCaseLetter = Character.toLowerCase(letter);
//			Integer score = mapLetterToScore.get(lowerCaseLetter);
//			if( score != null ) {
//				sum += score;
//			} else {
//				//pass
//			}
		}
		return sum;
	}
}
