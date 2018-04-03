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
package pipeline.cake.studio;

import static edu.wustl.cse231s.v5.V5.async;
import static edu.wustl.cse231s.v5.V5.finish;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Phaser;

import edu.wustl.cse231s.NotYetImplementedException;
import pipeline.cake.core.BakedCake;
import pipeline.cake.core.Baker;
import pipeline.cake.core.IcedCake;
import pipeline.cake.core.Icer;
import pipeline.cake.core.MixedIngredients;
import pipeline.cake.core.Mixer;

/**
 * @author __STUDENT_NAME__
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class CakePipeline {
	public static IcedCake[] mixBakeAndIceCakes(Mixer mixer, Baker baker, Icer icer, int cakeCount)
			throws InterruptedException, ExecutionException {
		MixedIngredients[] mixedIngredients = new MixedIngredients[cakeCount];
		BakedCake[] bakedCakes = new BakedCake[cakeCount];
		IcedCake[] icedCakes = new IcedCake[cakeCount];
		throw new NotYetImplementedException();
	}
}
