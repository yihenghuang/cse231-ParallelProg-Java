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
package edu.wustl.cse231s.v5;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicReference;

import edu.wustl.cse231s.v5.api.AccumulatorReducer;
import edu.wustl.cse231s.v5.api.CheckedCallable;
import edu.wustl.cse231s.v5.api.CheckedConsumer;
import edu.wustl.cse231s.v5.api.CheckedIntConsumer;
import edu.wustl.cse231s.v5.api.CheckedIntIntConsumer;
import edu.wustl.cse231s.v5.api.CheckedRunnable;
import edu.wustl.cse231s.v5.api.ContentionLevel;
import edu.wustl.cse231s.v5.api.DoubleAccumulationDeterminismPolicy;
import edu.wustl.cse231s.v5.api.FinishAccumulator;
import edu.wustl.cse231s.v5.api.Metrics;
import edu.wustl.cse231s.v5.api.NumberReductionOperator;
import edu.wustl.cse231s.v5.api.PhaserPair;
import edu.wustl.cse231s.v5.impl.V5Impl;
import edu.wustl.cse231s.v5.impl.executor.ExecutorV5Impl;
import edu.wustl.cse231s.v5.options.AwaitFuturesOption;
import edu.wustl.cse231s.v5.options.ChunkedOption;
import edu.wustl.cse231s.v5.options.ObjectBasedIsolationOption;
import edu.wustl.cse231s.v5.options.PhasedEmptyOption;
import edu.wustl.cse231s.v5.options.PhasedOption;
import edu.wustl.cse231s.v5.options.RegisterAccumulatorsOption;
import edu.wustl.cse231s.v5.options.SingleOption;
import edu.wustl.cse231s.v5.options.SystemPropertiesOption;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class V5 {
	private static AtomicReference<V5Impl> implAtom = new AtomicReference<V5Impl>(null);

	private static V5Impl getImpl() {
		return implAtom.get();
	}

	private static void launch(CheckedRunnable body) throws InterruptedException, ExecutionException {
		getImpl().launch(body);
	}

	public static void launchApp(CheckedRunnable body) {
		launchApp(new ExecutorV5Impl(), body);
	}

	public static void launchApp(V5Impl impl, CheckedRunnable body) {
		if (implAtom.compareAndSet(null, impl)) {
			try {
				launch(body);
			} catch (InterruptedException | ExecutionException e) {
				throw new RuntimeException(e);
			} finally {
				implAtom.set(null);
			}
		} else {
			throw new RuntimeException();
		}
	}

	public static void launchApp(CheckedRunnable body, Runnable preFinalizeCallback) {
		throw new RuntimeException();
		// getImpl().launchHabaneroApp(body, preFinalizeCallback);
	}

	public static void launchApp(SystemPropertiesOption systemPropertiesOption, CheckedRunnable body) {
		launchApp(systemPropertiesOption, body, null);
	}

	public static void launchApp(SystemPropertiesOption systemPropertiesOption, CheckedRunnable body,
			Runnable preFinalizeCallback) {
		throw new RuntimeException();
		// getImpl().launchHabaneroApp(systemPropertiesOption, body,
		// preFinalizeCallback);
	}
	
	public static void finish(CheckedRunnable body) throws InterruptedException, ExecutionException {
		getImpl().finish(body);
	}

	public static void async(CheckedRunnable body) {
		getImpl().async(body);
	}

	public static <R> Future<R> future(CheckedCallable<R> body) {
		return getImpl().future(body);
	}

	public static void forseq(int min, int maxExclusive, CheckedIntConsumer body)
			throws InterruptedException, ExecutionException {
		getImpl().forseq(min, maxExclusive, body);
	}

	public static void forasync(int min, int maxExclusive, CheckedIntConsumer body)
			throws InterruptedException, ExecutionException {
		getImpl().forasync(min, maxExclusive, body);
	}

	public static void forall(int min, int maxExclusive, CheckedIntConsumer body)
			throws InterruptedException, ExecutionException {
		getImpl().forall(min, maxExclusive, body);
	}

	public static void forseq(ChunkedOption chunkedOption, int min, int maxExclusive, CheckedIntConsumer body)
			throws InterruptedException, ExecutionException {
		getImpl().forseq(chunkedOption, min, maxExclusive, body);
	}

	public static void forasync(ChunkedOption chunkedOption, int min, int maxExclusive, CheckedIntConsumer body)
			throws InterruptedException, ExecutionException {
		getImpl().forasync(chunkedOption, min, maxExclusive, body);
	}

	public static void forall(ChunkedOption chunkedOption, int min, int maxExclusive, CheckedIntConsumer body)
			throws InterruptedException, ExecutionException {
		getImpl().forall(chunkedOption, min, maxExclusive, body);
	}

	public static <T> void forseq(T[] array, CheckedConsumer<T> body) throws InterruptedException, ExecutionException {
		getImpl().forseq(array, body);
	}

	public static <T> void forasync(T[] array, CheckedConsumer<T> body)
			throws InterruptedException, ExecutionException {
		getImpl().forasync(array, body);
	}

	public static <T> void forall(T[] array, CheckedConsumer<T> body) throws InterruptedException, ExecutionException {
		getImpl().forall(array, body);
	}

	public static <T> void forseq(ChunkedOption chunkedOption, T[] array, CheckedConsumer<T> body)
			throws InterruptedException, ExecutionException {
		getImpl().forseq(chunkedOption, array, body);
	}

	public static <T> void forasync(ChunkedOption chunkedOption, T[] array, CheckedConsumer<T> body)
			throws InterruptedException, ExecutionException {
		getImpl().forasync(chunkedOption, array, body);
	}

	public static <T> void forall(ChunkedOption chunkedOption, T[] array, CheckedConsumer<T> body)
			throws InterruptedException, ExecutionException {
		getImpl().forall(chunkedOption, array, body);
	}

	public static <T> void forseq(Iterable<T> iterable, CheckedConsumer<T> body)
			throws InterruptedException, ExecutionException {
		getImpl().forseq(iterable, body);
	}

	public static <T> void forasync(Iterable<T> iterable, CheckedConsumer<T> body)
			throws InterruptedException, ExecutionException {
		getImpl().forasync(iterable, body);
	}

	public static <T> void forall(Iterable<T> iterable, CheckedConsumer<T> body)
			throws InterruptedException, ExecutionException {
		getImpl().forall(iterable, body);
	}

	public static <T> void forseq(ChunkedOption chunkedOption, Iterable<T> iterable, CheckedConsumer<T> body)
			throws InterruptedException, ExecutionException {
		getImpl().forseq(chunkedOption, iterable, body);
	}

	public static <T> void forasync(ChunkedOption chunkedOption, Iterable<T> iterable, CheckedConsumer<T> body)
			throws InterruptedException, ExecutionException {
		getImpl().forasync(chunkedOption, iterable, body);
	}

	public static <T> void forall(ChunkedOption chunkedOption, Iterable<T> iterable, CheckedConsumer<T> body)
			throws InterruptedException, ExecutionException {
		getImpl().forall(chunkedOption, iterable, body);
	}

	public static void forseq2d(int minA, int maxExclusiveA, int minB, int maxExclusiveB, CheckedIntIntConsumer body)
			throws InterruptedException, ExecutionException {
		getImpl().forseq2d(minA, maxExclusiveA, minB, maxExclusiveB, body);
	}

	public static void forasync2d(int minA, int maxExclusiveA, int minB, int maxExclusiveB, CheckedIntIntConsumer body)
			throws InterruptedException, ExecutionException {
		getImpl().forasync2d(minA, maxExclusiveA, minB, maxExclusiveB, body);
	}

	public static void forall2d(int minA, int maxExclusiveA, int minB, int maxExclusiveB, CheckedIntIntConsumer body)
			throws InterruptedException, ExecutionException {
		getImpl().forall2d(minA, maxExclusiveA, minB, maxExclusiveB, body);
	}

	public static void forseq2d(ChunkedOption chunkedOption, int minA, int maxExclusiveA, int minB, int maxExclusiveB,
			CheckedIntIntConsumer body) throws InterruptedException, ExecutionException {
		getImpl().forseq2d(chunkedOption, minA, maxExclusiveA, minB, maxExclusiveB, body);
	}

	public static void forasync2d(ChunkedOption chunkedOption, int minA, int maxExclusiveA, int minB, int maxExclusiveB,
			CheckedIntIntConsumer body) throws InterruptedException, ExecutionException {
		getImpl().forasync2d(chunkedOption, minA, maxExclusiveA, minB, maxExclusiveB, body);
	}

	public static void forall2d(ChunkedOption chunkedOption, int minA, int maxExclusiveA, int minB, int maxExclusiveB,
			CheckedIntIntConsumer body) throws InterruptedException, ExecutionException {
		getImpl().forall2d(chunkedOption, minA, maxExclusiveA, minB, maxExclusiveB, body);
	}

	public static void forseq(PhasedEmptyOption phasedEmptyOption, int min, int maxExclusive, CheckedIntConsumer body)
			throws InterruptedException, ExecutionException {
		getImpl().forseq(phasedEmptyOption, min, maxExclusive, body);
	}

	public static void forasync(PhasedEmptyOption phasedEmptyOption, int min, int maxExclusive, CheckedIntConsumer body)
			throws InterruptedException, ExecutionException {
		getImpl().forasync(phasedEmptyOption, min, maxExclusive, body);
	}

	public static void forall(PhasedEmptyOption phasedEmptyOption, int min, int maxExclusive, CheckedIntConsumer body)
			throws InterruptedException, ExecutionException {
		getImpl().forall(phasedEmptyOption, min, maxExclusive, body);
	}

	public static boolean isLaunched() {
		return getImpl() != null;
	}

	public static int numWorkerThreads() {
		return Runtime.getRuntime().availableProcessors();
	}

	public static void doWork(long n) {
		getImpl().doWork(n);
	}

	public static void dumpStatistics() {
		throw new RuntimeException();
	}

	public static Metrics abstractMetrics() {
		throw new RuntimeException();
	}

	@Deprecated
	public static Phaser newPhaser() {
		return new Phaser();
	}

	public static void phaserNext() {
		throw new RuntimeException();
	}

	public static void phaserAwait(Phaser phaser) {
		throw new RuntimeException();
	}

	public static void phaserSignal(Phaser phaser) {
		throw new RuntimeException();
	}

	public static AwaitFuturesOption await(Future<?> futureA, Future<?>... futuresBtoZ) {
		return new AwaitFuturesOption(futureA, futuresBtoZ);
	}

	public static ChunkedOption chunked() {
		return new ChunkedOption();
	}

	public static ChunkedOption chunked(int size) {
		return new ChunkedOption(size);
	}

	public static RegisterAccumulatorsOption register(FinishAccumulator<?> accumulatorA,
			FinishAccumulator<?>... accumulatorBtoZ) {
		return new RegisterAccumulatorsOption(accumulatorA, accumulatorBtoZ);
	}

	public static ObjectBasedIsolationOption objectBased(Object participant) {
		return new ObjectBasedIsolationOption(participant);
	}

	public static ObjectBasedIsolationOption objectBasedAll(Object participantA, Object participantB,
			Object... participantsCtoZ) {
		return new ObjectBasedIsolationOption(participantA, participantB, participantsCtoZ);
	}

	public static PhasedOption phased(PhaserPair phaserPairA, PhaserPair... phaserPairsBtoZ) {
		return new PhasedOption(phaserPairA, phaserPairsBtoZ);
	}

	public static PhasedEmptyOption phased() {
		return new PhasedEmptyOption();
	}

	public static SingleOption single(Runnable runnable) {
		return new SingleOption(runnable);
	}

	public static void async(PhasedOption phasedOption, CheckedRunnable body) {
		throw new RuntimeException();
	}

	public static void async(AwaitFuturesOption awaitFuturesOption, CheckedRunnable body) {
		getImpl().async(awaitFuturesOption, body);
	}

	public static <R> Future<R> future(AwaitFuturesOption awaitFuturesOption, CheckedCallable<R> body) {
		return getImpl().future(awaitFuturesOption, body);
	}

	public static void finish(RegisterAccumulatorsOption registerAccumulatorsOption, CheckedRunnable body)
			throws InterruptedException, ExecutionException {
		throw new RuntimeException();
		// getImplementation().finish(registerAccumulatorsOption.getAccumulators(),
		// body);
	}

	public static FinishAccumulator<Integer> newIntegerFinishAccumulator(NumberReductionOperator operator,
			ContentionLevel contentionLevel) {
		throw new RuntimeException();
		// return getImplementation().newIntegerFinishAccumulator(operator,
		// contentionLevel);
	}

	public static FinishAccumulator<Integer> newIntegerFinishAccumulator(NumberReductionOperator operator) {
		return newIntegerFinishAccumulator(operator, ContentionLevel.HIGH);
	}

	public static FinishAccumulator<Double> newDoubleFinishAccumulator(NumberReductionOperator operator,
			ContentionLevel contentionLevel, DoubleAccumulationDeterminismPolicy determinismPolicy) {
		throw new RuntimeException();
		// return getImplementation().newDoubleFinishAccumulator(operator,
		// contentionLevel, determinismPolicy);
	}

	public static FinishAccumulator<Double> newDoubleFinishAccumulator(NumberReductionOperator operator,
			ContentionLevel contentionLevel) {
		return newDoubleFinishAccumulator(operator, contentionLevel, DoubleAccumulationDeterminismPolicy.DETERMINISTIC);
	}

	public static FinishAccumulator<Double> newDoubleFinishAccumulator(NumberReductionOperator operator) {
		return newDoubleFinishAccumulator(operator, ContentionLevel.HIGH);
	}

	public static <T> FinishAccumulator<T> newReducerFinishAccumulator(AccumulatorReducer<T> reducer,
			ContentionLevel contentionLevel) {
		throw new RuntimeException();
		// return getImplementation().newReducerFinishAccumulator(reducer,
		// contentionLevel);
	}

	public static <T> FinishAccumulator<T> newReducerFinishAccumulator(AccumulatorReducer<T> reducer) {
		return newReducerFinishAccumulator(reducer, ContentionLevel.HIGH);
	}

	@Deprecated
	public static void isolated(Runnable body) {
		throw new RuntimeException();
	}

	@Deprecated
	public static void isolated(ObjectBasedIsolationOption objectBasedIsolationOption, Runnable body) {
		throw new RuntimeException();
	}

	@Deprecated
	public static <V> V isolatedWithReturn(ObjectBasedIsolationOption objectBasedIsolationOption, Callable<V> body) {
		throw new RuntimeException();
	}

	@Deprecated
	public static Object readMode(Object o) {
		throw new RuntimeException();
	}

	@Deprecated
	public static Object writeMode(Object o) {
		throw new RuntimeException();
	}
}
