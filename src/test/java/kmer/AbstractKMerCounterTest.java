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
package kmer;

import static edu.wustl.cse231s.v5.V5.launchApp;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.junit.Test;

import kmer.core.KMerCount;
import kmer.core.KMerCounter;
import kmer.instructor.InstructorKMerTestUtils;
import kmer.util.KMerResource;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public abstract class AbstractKMerCounterTest {
	private final KMerResource resource;
	private final int k;
	private final CheckEntent checkEntent;

	public AbstractKMerCounterTest(KMerResource resource, int k, CheckEntent checkEntent) {
		Objects.requireNonNull(resource);
		Objects.requireNonNull(checkEntent);
		this.resource = resource;
		this.k = k;
		this.checkEntent = checkEntent;
	}

	protected abstract KMerCounter createKMerCounter();

	@Test
	public void test() {
		KMerCounter kMerCounter = this.createKMerCounter();
		List<byte[]> sequences = resource.getSubSequences();
		launchApp(() -> {
			KMerCount actualKMerCount = kMerCounter.parse(sequences, k);
			if (this.checkEntent == CheckEntent.CHECK_ALL) {
				InstructorKMerTestUtils.checkAllKMers(sequences, k, actualKMerCount);
			} else {
				InstructorKMerTestUtils.checkSampleKMers(sequences, k, 1_000, actualKMerCount);
			}
		});
	}

	protected static enum CheckEntent {
		CHECK_ALL, CHECK_SAMPLES
	}

	private static Collection<Object[]> addToConstructorArguments(Collection<Object[]> results, KMerResource resource,
			int k, CheckEntent checkEntent) {
		results.add(new Object[] { resource, k, checkEntent });
		return results;
	}

	private static Iterable<KMerResource> getResources(KMerResource... yChromosomeChoices) {
		List<KMerResource> result = new LinkedList<>();
		result.add(KMerResource.CHOLERAE_ORI_C);
		result.add(KMerResource.MITOCHONDRION);
		for(KMerResource yChromosomeChoice : yChromosomeChoices ) {
			result.add(yChromosomeChoice);
		}
		return result;
	}

	protected static Collection<Object[]> createConstructorArgumentsForIndexableImplementations() {
		Collection<Object[]> results = new LinkedList<>();
		for (KMerResource resource : getResources(KMerResource.Y_CHROMOSOME_TINY, KMerResource.Y_CHROMOSOME_COMPLETE)) {
			addToConstructorArguments(results, resource, 5, CheckEntent.CHECK_ALL);
		}
		for (KMerResource resource : getResources(KMerResource.Y_CHROMOSOME_TINY, KMerResource.Y_CHROMOSOME_COMPLETE)) {
			addToConstructorArguments(results, resource, 12, CheckEntent.CHECK_SAMPLES);
		}
		return results;
	}

	protected static Collection<Object[]> createConstructorArgumentsForMappableImplementations() {
		Collection<Object[]> results = new LinkedList<>();
		for (KMerResource resource : getResources(KMerResource.Y_CHROMOSOME_TINY, KMerResource.Y_CHROMOSOME_COMPLETE)) {
			addToConstructorArguments(results, resource, 5, CheckEntent.CHECK_ALL);
		}
		for (KMerResource resource : getResources(KMerResource.Y_CHROMOSOME_TINY)) {
			addToConstructorArguments(results, resource, 23, CheckEntent.CHECK_SAMPLES);
		}
		return results;
	}

}
