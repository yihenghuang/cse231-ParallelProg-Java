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
package edu.wustl.cse231s.viz;

import static edu.wustl.cse231s.v5.V5.launchApp;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public abstract class VizApp extends Application {
	protected abstract void resetIfNecessary();

	protected abstract void solve() throws RuntimeInterruptedException;

	private void onSolve() {

		this.isBlocking.set(false);
		this.setCancelDisabled(false);
		this.isToBeCanceled = false;
		this.thread = new Thread(() -> {
			try {
				this.solve();
			} catch (RuntimeException re) {
				if(re.getCause() instanceof RuntimeInterruptedException) {
					System.out.println("consuming " + re);
					this.resetIfNecessary();
				} else {
					throw re;
				}
			}
			this.thread = null;
			Platform.runLater(() -> {
				this.setCancelDisabled(true);
				this.resumeButton.setDisable(true);
			});
		});
		this.thread.start();

	}

	private void onStep() {
		if (this.thread != null) {
			// pass
		} else {
			this.onSolve();
		}
		this.pauseButton.setDisable(true);
		this.resumeButton.setDisable(false);
		this.isBlocking.set(true);
		try {
			int unused = 0;
			this.queue.put(unused);
		} catch (InterruptedException ie) {
			Thread.currentThread().interrupt();
		}
	}

	private void onPause() {
		this.isBlocking.set(true);
		this.pauseButton.setDisable(true);
		this.resumeButton.setDisable(false);
	}

	private void onResume() {
		this.isBlocking.set(false);
		this.pauseButton.setDisable(false);
		this.resumeButton.setDisable(true);
		try {
			int unused = 0;
			this.queue.put(unused);
		} catch (InterruptedException ie) {
			Thread.currentThread().interrupt();
		}
	}

	protected void onCancel() {
		this.isToBeCanceled = true;
//		if (this.threadToInterruptOnCancel != null) {
//			this.threadToInterruptOnCancel.interrupt();
//		}
	}

	protected void takeFromQueueIfNecessary() {
		if (this.isBlocking.get()) {
			try {
				@SuppressWarnings("unused")
				int unused = this.queue.take();
			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
			}
		}
	}

	protected abstract Collection<Node> getBonusNodesToEnableOnThreadStart();

	protected abstract Collection<Node> getBonusNodesToDisableOnThreadStart();

	private final Iterable<Node> getNodesToEnableOnThreadStart() {
		Collection<Node> result = new LinkedList<>();
		result.add(getCancelButton());
		Collection<Node> bonus = getBonusNodesToEnableOnThreadStart();
		if (bonus != null) {
			result.addAll(bonus);
		}
		return result;
	}

	private final Iterable<Node> getNodesToDisableOnThreadStart() {
		Collection<Node> result = new LinkedList<>();
		result.add(getSolveButton());
		Collection<Node> bonus = getBonusNodesToDisableOnThreadStart();
		if (bonus != null) {
			result.addAll(bonus);
		}
		return result;
	}

	private void setCancelDisabled(boolean isDisabled) {
		boolean isCancelFound = false;
		for (Node node : this.getNodesToEnableOnThreadStart()) {
			node.setDisable(isDisabled);
			if (node == cancelButton) {
				isCancelFound = true;
			}
		}
		if (isCancelFound) {
			// pass
		} else {
			throw new RuntimeException("cancel button not found in getNodesToEnableAlongWithCancel() "
					+ this.getNodesToEnableOnThreadStart());
		}

		for (Node node : this.getNodesToDisableOnThreadStart()) {
			node.setDisable(isDisabled == false);
			if (node == cancelButton) {
				throw new RuntimeException("cancel button found in getNodesToDisableWhenCancelIsEnabled() "
						+ this.getNodesToDisableOnThreadStart());
			}
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		solveButton = new Button("solve puzzle");
		solveButton.setStyle("-fx-base: lightgreen;");
		stepButton = new Button("step");
		pauseButton = new Button("pause");
		resumeButton = new Button("resume");
		cancelButton = new Button("reset");
		cancelButton.setDisable(true);
		pauseButton.setDisable(true);
		cancelButton.setDisable(true);
		resumeButton.setDisable(true);
		solveButton.setOnAction((event) -> {
			this.onSolve();
		});
		cancelButton.setOnAction((event) -> {
			this.onCancel();
		});

		pauseButton.setOnAction((event) -> {
			this.onPause();
		});

		resumeButton.setOnAction((event) -> {
			this.onResume();
		});

		stepButton.setOnAction((event) -> {
			this.onStep();
		});
	}

	public Button getSolveButton() {
		return solveButton;
	}

	public Button getStepButton() {
		return stepButton;
	}

	public Button getPauseButton() {
		return pauseButton;
	}

	public Button getResumeButton() {
		return resumeButton;
	}

	public Button getCancelButton() {
		return cancelButton;
	}

	
	protected boolean isToBeCanceled() {
		return isToBeCanceled;
	}
	
	private final AtomicBoolean isBlocking = new AtomicBoolean(false);
	private final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();

	private Button solveButton;

	private Button stepButton;
	private Button resumeButton;
	private Button pauseButton;
	private Button cancelButton;
	private Thread thread;//ToInterruptOnCancel;
	private boolean isToBeCanceled = false;


	public static void checkHabaneroJavaagentArg() {
		launchApp(() -> {
		});
	}
}
