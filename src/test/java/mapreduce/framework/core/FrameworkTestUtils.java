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

package mapreduce.framework.core;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import mapreduce.apps.friends.util.AccountDatabase;
import mapreduce.apps.wordcount.core.WordCountUtils;
import mapreduce.apps.wordcount.core.io.WordsResource;
import mapreduce.core.TestApplication;
import mapreduce.framework.warmup.wordcount.util.TextSectionResource;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class FrameworkTestUtils {
	@SuppressWarnings("unchecked")
	public static <E> E[] getInput(Object resource) throws IOException {
		if (resource instanceof TextSectionResource) {
			TextSectionResource textSectionResource = (TextSectionResource) resource;
			return (E[]) textSectionResource.getTextSections();
		} else if (resource instanceof WordsResource) {
			WordsResource wordsResource = (WordsResource) resource;
			return (E[]) WordCountUtils.readAllLinesOfWords(wordsResource);
		} else {
			AccountDatabase accountDatabase = (AccountDatabase) resource;
			return (E[]) accountDatabase.getAccounts();
		}
	}

	public static TestApplication getApplication(Object resource) {
		if (resource instanceof TextSectionResource) {
			return TestApplication.WORD_COUNT;
		} else if (resource instanceof WordsResource) {
			return TestApplication.WORD_COUNT;
		} else {
			return TestApplication.MUTUAL_FRIENDS;
		}
	}

	public static Collection<Object[]> getConstructorArguments(Object[] argAs) {
		List<Object[]> result = new LinkedList<>();
		for (Object argA : argAs) {
			for (TextSectionResource textSectionResource : TextSectionResource.values()) {
				result.add(new Object[] { argA, textSectionResource });
			}
			for (WordsResource wordsResource : WordsResource.values()) {
				result.add(new Object[] { argA, wordsResource });
			}
			for (AccountDatabase accountDatabase : AccountDatabase.values()) {
				result.add(new Object[] { argA, accountDatabase });
			}
		}
		return result;
	}

	public static Collection<Object[]> getConstructorArguments(Object[] argAs, Object[] argBs) {
		List<Object[]> result = new LinkedList<>();
		for (Object argA : argAs) {
			for (Object argB : argBs) {
				for (TextSectionResource textSectionResource : TextSectionResource.values()) {
					result.add(new Object[] { argA, argB, textSectionResource });
				}
				for (WordsResource wordsResource : WordsResource.values()) {
					result.add(new Object[] { argA, argB, wordsResource });
				}
				for (AccountDatabase accountDatabase : AccountDatabase.values()) {
					result.add(new Object[] { argA, argB, accountDatabase });
				}
			}
		}
		return result;
	}
}
