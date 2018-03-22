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
package edu.wustl.cse231s.v5.options;

import java.io.File;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class SystemPropertiesOption {
	public static class Builder {
		public Builder isLinearized(boolean isLinearized) {
			this.isLinearized = isLinearized;
			return this;
		}

		public Builder isAbstractMetricsDesired(boolean isAbstractMetricsDesired) {
			this.isAbstractMetricsDesired = isAbstractMetricsDesired;
			return this;
		}

		public Builder isDumpStatisticsDesired(boolean isDumpStatisticsDesired) {
			this.isDumpStatisticsDesired = isDumpStatisticsDesired;
			if (this.isDumpStatisticsDesired) {
				this.isAbstractMetricsDesired = true;
			}
			return this;
		}

		public Builder eventLogFile(File eventLogFile) {
			this.eventLogFile = eventLogFile;
			return this;
		}

		public Builder numWorkerThreads(int numWorkerThreads) {
			this.numWorkerThreads = numWorkerThreads;
			return this;
		}

		public SystemPropertiesOption build() {
			return new SystemPropertiesOption(this);
		}

		private boolean isLinearized;
		private boolean isDumpStatisticsDesired;
		private boolean isAbstractMetricsDesired;
		private Integer numWorkerThreads;
		private File eventLogFile;
	}

	private final boolean isLinearized;
	private final boolean isDumpStatisticsDesired;
	private final boolean isAbstractMetricsDesired;
	private final File eventLogFile;
	private final Integer numWorkerThreads;

	private SystemPropertiesOption(Builder builder) {
		this.isLinearized = builder.isLinearized;
		this.isDumpStatisticsDesired = builder.isDumpStatisticsDesired;
		this.isAbstractMetricsDesired = builder.isAbstractMetricsDesired;
		this.eventLogFile = builder.eventLogFile;
		this.numWorkerThreads = builder.numWorkerThreads;
	}

	public boolean isLinearized() {
		return this.isLinearized;
	}

	public boolean isDumpStatisticsDesired() {
		return this.isDumpStatisticsDesired;
	}

	public boolean isAbstractMetricsDesired() {
		return this.isAbstractMetricsDesired;
	}

	public File getEventLogFile() {
		return this.eventLogFile;
	}

	public Integer getNumWorkerThreads() {
		return this.numWorkerThreads;
	}
}
