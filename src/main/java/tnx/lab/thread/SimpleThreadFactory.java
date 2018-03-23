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
package tnx.lab.thread;

import java.util.concurrent.ThreadFactory;

/**
 * The simplest {@code ThreadFactory} imaginable. This class exists just so that
 * you understand what a {@code ThreadFactory} is, and so that you understand
 * what code would be necessary for you to write if you aren't given a
 * ThreadFactory.
 * 
 * @author Yiheng Huang
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class SimpleThreadFactory implements ThreadFactory {

	/**
	 * Should construct a new {@code Thread}. This thread should not be running, and
	 * should not have already run. You are just setting up a thread with the given
	 * runnable so that another class can use it.
	 * 
	 * @return constructed thread
	 */
	@Override
	public Thread newThread(Runnable target) {
		return new Thread(target);

	}

}
