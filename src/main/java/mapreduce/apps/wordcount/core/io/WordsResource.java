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
package mapreduce.apps.wordcount.core.io;

import java.net.MalformedURLException;
import java.net.URL;

class Delimiter {
	static String CHAPTER = "\\R\\R\\R(?=CHAPTER )";
	static String Chapter = "\\R\\R\\R(?=Chapter )";
	static String Scene = "\\R\\R(?=Scene )";
}
/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public enum WordsResource {
	//A_TALE_OF_TWO_CITIES("https://www.gutenberg.org/files/98/98-0.txt", Delimiter.),
	ALICES_ADVENTURES_IN_WONDERLAND("https://www.gutenberg.org/files/11/11-0.txt", Delimiter.CHAPTER),
	//DON_QUIXOTE("http://www.gutenberg.org/cache/epub/2000/pg2000.txt", Delimiter.),
	PRIDE_AND_PREJUDICE("https://www.gutenberg.org/files/1342/1342-0.txt", Delimiter.Chapter),
	MACBETH("http://www.gutenberg.org/cache/epub/1533/pg1533.txt", Delimiter.Scene),
	THE_COUNT_OF_MONTE_CRISTO("https://www.gutenberg.org/files/1184/1184-0.txt", Delimiter.Chapter),
	THE_ADVENTURES_OF_TOM_SAWYER("https://www.gutenberg.org/files/74/74-0.txt", Delimiter.CHAPTER),
	WAR_AND_PEACE("https://www.gutenberg.org/files/2600/2600-0.txt", Delimiter.CHAPTER)
	;
	
	private final String urlSpec;
	private final String delimiter;
	
	private WordsResource(String urlSpec, String delimiter) {
		this.urlSpec = urlSpec;
		this.delimiter = delimiter;
	}

	public String getDelimiter() {
		return this.delimiter;
	}

	public URL getUrl() {
		try {
			return new URL(this.urlSpec);
		} catch (MalformedURLException murle) {
			throw new Error(murle);
		}
	}

}
