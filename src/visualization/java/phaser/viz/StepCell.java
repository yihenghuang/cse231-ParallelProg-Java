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

package phaser.viz;

import java.util.concurrent.Phaser;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class StepCell extends ProgressBar {
	private final Supplier<Duration> durationSupplier;
	private final BooleanSupplier isToBeCanceledSupplier;

	public StepCell(Supplier<Duration> durationSupplier, BooleanSupplier isToBeCanceledSupplier) {
		this.durationSupplier = durationSupplier;
		this.isToBeCanceledSupplier = isToBeCanceledSupplier;
		this.nodeOrientationProperty().set(NodeOrientation.LEFT_TO_RIGHT);
		this.reset();
	}

	public void reset() {
		this.setProgress(0.0);
	}

	public void setTaken() {
		if (isToBeCanceledSupplier.getAsBoolean()) {
			Thread.currentThread().interrupt();
		} else {
			Phaser phaser = new Phaser(1);
			Platform.runLater(() -> {
				Duration duration = durationSupplier.get();
				Timeline timeline = new Timeline();
				KeyFrame keyFrame = new KeyFrame(duration, (e) -> {
					phaser.arrive();
				}, new KeyValue(this.progressProperty(), 1.0));
				timeline.getKeyFrames().add(keyFrame);
				timeline.play();
			});
			try {
				phaser.awaitAdvanceInterruptibly(0);
			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
			}
		}
	}
}
