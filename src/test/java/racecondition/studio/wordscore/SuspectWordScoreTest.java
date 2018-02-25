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
package racecondition.studio.wordscore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import edu.wustl.cse231s.download.DownloadUtils;
import edu.wustl.cse231s.junit.JUnitUtils;
import edu.wustl.cse231s.v5.bookkeep.BookkeepingUtils;
import edu.wustl.cse231s.v5.impl.BookkeepingV5Impl;
import racecondition.core.wordscore.DataCleaningUtils;
import racecondition.core.wordscore.WordScoreUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 * {@link SuspectWordScore#toCleanedWordsViaArray(java.util.Collection)
 */
public class SuspectWordScoreTest {
	private final List<String> sourceLines;

	public SuspectWordScoreTest() throws IOException {
		URL wordsUrl = new URL("https://users.cs.duke.edu/~ola/ap/linuxwords");
		File file = DownloadUtils.getDownloadedFile(wordsUrl);
		List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
		this.sourceLines = DataCleaningUtils.toUnique(lines);
	}

	@Rule
	public TestRule timeout = JUnitUtils.createTimeoutRule();

	@Test
	public void testSuspectClean() {
		BookkeepingV5Impl bookkeeping = BookkeepingUtils.bookkeep(() -> {
			List<String> actual = SuspectWordScore.toCleanedWordsViaArray(this.sourceLines);
			for (String cleanedWord : actual) {
				assertTrue(WordScoreUtils.isCleanedWord(cleanedWord));
			}
		});
		assertNotEquals("parallelism must not be removed", 0, bookkeeping.getTaskCount());
		assertEquals("unexpected task count", this.sourceLines.size(), bookkeeping.getTaskCount());
	}
}
