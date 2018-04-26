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
package connectfour.core;

import java.util.Objects;

/**
 * @author Finn Voichick
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class Config {
	public static class Builder {
		private Heuristic heuristic;
		private int maxDepth;
		private int maxParallelDepth;

		public Builder heuristic(Heuristic heuristic) {
			this.heuristic = heuristic;
			return this;
		}

		public Builder maxDepth(int maxDepth) {
			this.maxDepth = maxDepth;
			return this;
		}

		public Builder maxParallelDepth(int maxParallelDepth) {
			this.maxParallelDepth = maxParallelDepth;
			return this;
		}

		public Config build() {
			Objects.requireNonNull(heuristic);
			if (maxParallelDepth > maxDepth) {
				throw new RuntimeException(
						String.format("maxParallelDepth(%d) greater than maxDepth(%d)", maxParallelDepth, maxDepth));
			}
			return new Config(this);
		}
	}

	private final Heuristic heuristic;
	private final int maxDepth;
	private final int maxParallelDepth;

	private Config(Builder builder) {
		this.heuristic = builder.heuristic;
		this.maxDepth = builder.maxDepth;
		this.maxParallelDepth = builder.maxParallelDepth;
	}

	public Heuristic getHeuristic() {
		return heuristic;
	}

	public int getMaxDepth() {
		return maxDepth;
	}

	public int getMaxParallelDepth() {
		return maxParallelDepth;
	}
}
