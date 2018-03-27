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
package sudoku.core.io;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Objects;

import edu.wustl.cse231s.download.DownloadUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class PuzzlesResourceUtils {
	public static int[][] parseGivens(String givens) {
		int[][] values = new int[9][9];
		int row = 0;
		int column = 0;
		for (char c : givens.toCharArray()) {
			int value;
			if (c == '.') {
				value = 0;
			} else {
				value = c - '0';
			}

			values[row][column] = value;
			column++;
			if (column == 9) {
				row++;
				column = 0;
			}
		}
		return values;
	}

	public static List<String> readGivens(PuzzlesResource puzzlesResource) throws IOException {
		File file = DownloadUtils.getDownloadedFile(puzzlesResource.getUrl());
		List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
		if (lines.get(0).length() == 81) {
			return lines;
		} else {
			List<String> result = new LinkedList<>();
			Iterator<String> iterator = lines.iterator();
			while (iterator.hasNext()) {
				StringBuilder sb = new StringBuilder();
				for (int row = 0; row < 9; row++) {
					assert iterator.hasNext();
					String line = iterator.next();
					sb.append(line.replaceAll("0", "."));
				}
				result.add(sb.toString());
				if (iterator.hasNext()) {
					String line = iterator.next();
					assert Objects.equal("========", line);
				}
			}
			return result;
		}
	}
}
