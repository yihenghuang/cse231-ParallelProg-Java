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
package mapreduce.apps.wordcount.studio;

import java.util.function.BiConsumer;

import edu.wustl.cse231s.NotYetImplementedException;
import mapreduce.apps.wordcount.core.TextSection;
import mapreduce.framework.core.Mapper;

/**
 * An implementation of the {@code Mapper} interface that maps TextSection
 * objects to key-value pairs, where the keys are words and the values are
 * counts. This is used as the mapper for word count.
 * 
 * @author Yiheng Huang
 * @author Finn Voichick
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class WordCountMapper implements Mapper<TextSection, String, Integer> {

	/**
	 * Given a section of text, should find the words in the section and write key
	 * value pairs to the given {@code BiConsumer}. You should only write words that
	 * are of positive length. Also, this mapper should be case-insensitive, meaning
	 * that all words should be converted to lower case. You shouldn't do any
	 * reducing in this phase, meaning that the values of all of the key-value pairs
	 * will be zero.
	 * 
	 * It might make sense for this method to return a {@code Collection} of
	 * key-value pairs, but this is not the case. This method is more general than
	 * that. Instead of returning the key-value pairs, you should be writing them to
	 * the {@code BiConsumer}.
	 * 
	 * @param textSection
	 *            a section of text
	 * @param keyValuePairConsumer
	 *            the context used to write the key value pairs
	 * @see String#toLowerCase()
	 */
	@Override
	public void map(TextSection textSection, BiConsumer<String, Integer> keyValuePairConsumer) {
		throw new NotYetImplementedException();
	}

}
