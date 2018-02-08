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

package sort.core.quick;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class SequentialPartitioner implements Partitioner {
	private final PivotInitialIndexSelector pivotValueSelector;

	public SequentialPartitioner(PivotInitialIndexSelector pivotValueSelector) {
		this.pivotValueSelector = pivotValueSelector;
	}

	public SequentialPartitioner() {
		this(PivotInitialIndexSelector.RANDOM);
	}

	@Override
	public PivotLocation partitionRange(int[] data, int min, int maxExclusive) {
		int initialIndexOfPivotValue = this.pivotValueSelector.selectInitialIndex(data, min, maxExclusive);
		int pivotValue = data[initialIndexOfPivotValue];
		int maxInclusive = maxExclusive - 1;

		// move pivot value to end
		swap(data, initialIndexOfPivotValue, maxInclusive);

		// move all values less than or equal to pivot value to lower end of array
		int currentPivotIndex = min;
		for (int i = min; i < maxInclusive; i++) {
			if (data[i] <= pivotValue) {
				swap(data, i, currentPivotIndex);
				currentPivotIndex++;
			}
		}

		// move pivot value to pivot
		swap(data, currentPivotIndex, maxInclusive);

		int leftSidesUpperInclusive;
		int rightSidesLowerInclusive;
		if (currentPivotIndex == min) {
			leftSidesUpperInclusive = min;
			rightSidesLowerInclusive = currentPivotIndex + 1;
		} else if (currentPivotIndex == maxInclusive) {
			leftSidesUpperInclusive = currentPivotIndex - 1;
			rightSidesLowerInclusive = maxInclusive;
		} else {
			leftSidesUpperInclusive = currentPivotIndex - 1;
			rightSidesLowerInclusive = currentPivotIndex + 1;
		}
		return new PivotLocation(leftSidesUpperInclusive + 1, rightSidesLowerInclusive);
	}

	private static void swap(int[] array, int a, int b) {
		int temp = array[a];
		array[a] = array[b];
		array[b] = temp;
	}
}
