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
package edu.wustl.cse231s.junit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class JUnitUtils {
	//public static final long DEFAULT_TIMEOUT = 1000L;
	
	public static TestRule createTimeoutRule() {
		return new DisableOnDebug(new Timeout(1, TimeUnit.SECONDS));
	}
	
	public static <A> Collection<Object[]> toParameterizedArguments(Object argA0, Object... argAs) {
		List<Object[]> result = new ArrayList<>();
		result.add(new Object[] { argA0 });
		for (Object argA : argAs) {
			result.add(new Object[] { argA });
		}
		return result;
	}

	public static <A> Collection<Object[]> toParameterizedArguments(Iterable<A> argumentAs) {
		List<Object[]> result = new ArrayList<>();
		for (A argumentA : argumentAs) {
			result.add(new Object[] { argumentA });
		}
		return result;
	}

	public static <A> Collection<Object[]> toParameterizedArguments(A[] argumentAs) {
		List<Object[]> result = new ArrayList<>();
		for (A argumentA : argumentAs) {
			result.add(new Object[] { argumentA });
		}
		return result;
	}

	public static <A, B> Collection<Object[]> toParameterizedArguments2(A[] argumentAs, B[] argumentBs) {
		List<Object[]> result = new ArrayList<>();
		for (A argumentA : argumentAs) {
			for (B argumentB : argumentBs) {
				result.add(new Object[] { argumentA, argumentB });
			}
		}
		return result;
	}

	public static <A, B, C> Collection<Object[]> toParameterizedArguments3(A[] argumentAs, B[] argumentBs, C[] argumentCs) {
		List<Object[]> result = new ArrayList<>();
		for (A argumentA : argumentAs) {
			for (B argumentB : argumentBs) {
				for (C argumentC : argumentCs) {
					result.add(new Object[] { argumentA, argumentB, argumentC });
				}
			}
		}
		return result;
	}

	public static <A, B, C, D> Collection<Object[]> toParameterizedArguments4(A[] argumentAs, B[] argumentBs, C[] argumentCs, D[] argumentDs) {
		List<Object[]> result = new ArrayList<>();
		for (A argumentA : argumentAs) {
			for (B argumentB : argumentBs) {
				for (C argumentC : argumentCs) {
					for (D argumentD : argumentDs) {
						result.add(new Object[] { argumentA, argumentB, argumentC, argumentD });
					}
				}
			}
		}
		return result;
	}
}
