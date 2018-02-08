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

package sort.core.quick;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public enum PivotInitialIndexSelector {
	RANDOM {
		@Override
		public int selectInitialIndex(int[] data, int min, int maxExclusive) {
			int range = maxExclusive - min;
			return min + ThreadLocalRandom.current().nextInt(range);
		}
	},
	DETERMINISTIC {
		@Override
		public int selectInitialIndex(int[] data, int min, int maxExclusive) {
			int range = maxExclusive - min;
			if (range > 2) {
				int mid = (min + maxExclusive) / 2;
				int max = maxExclusive - 1;
				int valueAtMin = data[min];
				int valueAtMid = data[mid];
				int valueAtMax = data[max];

				if (valueAtMin > valueAtMid) {
					if (valueAtMid > valueAtMax) {
						return mid;
					} else if (valueAtMin > valueAtMax) {
						return max;
					} else {
						return min;
					}
				} else {
					if (valueAtMin > valueAtMax) {
						return mid;
					} else if (valueAtMid > valueAtMax) {
						return max;
					} else {
						return mid;
					}
				}
			} else {
				return min;
			}
		}
	};
	public abstract int selectInitialIndex(int[] data, int min, int maxExclusive);
}
