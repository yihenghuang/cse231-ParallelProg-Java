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
package edu.wustl.cse231s.download;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import edu.wustl.cse231s.sleep.SleepUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class DownloadUtils {
	public static File getDownloadsDirectory() {
		File homeDirectory = new File(System.getProperty("user.home"));
		File downloadsDirectory = new File(homeDirectory, "Downloads");
		if (downloadsDirectory.exists()) {
			// pass
		} else {
			if (downloadsDirectory.mkdir()) {
				// pass
			} else {
				throw new RuntimeException(downloadsDirectory.getAbsolutePath() + " could not make directory.");
			}
		}
		if (downloadsDirectory.exists()) {
			if (downloadsDirectory.isDirectory()) {
				return downloadsDirectory;
			} else {
				throw new RuntimeException(downloadsDirectory.getAbsolutePath() + " is not a directory.");
			}
		} else {
			throw new RuntimeException(downloadsDirectory.getAbsolutePath() + " does not exist.");
		}
	}

	public static File getDownloadsDirectory(String downloadsSubpath) {
		File directory = getDownloadsDirectory();
		if (downloadsSubpath != null) {
			directory = new File(directory, downloadsSubpath);
		}
		return directory;
	}

	private static SwingWorker<Void, Void> progress(Object message, String note) {
		MutableObject<SwingWorker<Void, Void>> mutableWorker = new MutableObject<>();
		try {
			SwingUtilities.invokeAndWait(() -> {
				Component parentComponent = null;
				ProgressMonitor progressMonitor = new ProgressMonitor(parentComponent, message, note, 0, 100);
				progressMonitor.setProgress(0);
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

					@Override
					protected Void doInBackground() throws Exception {
						int i = 0;
						while (isCancelled() == false) {
							i++;
							i = i % progressMonitor.getMaximum();
							progressMonitor.setProgress(i);
							SleepUtils.sleep(50);
						}
						progressMonitor.setProgress(progressMonitor.getMaximum());
						return null;
					}

				};
				worker.execute();
				mutableWorker.setValue(worker);
			});
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		return mutableWorker.getValue();
	}

	public static File getDownloadedFile(URL url, String downloadsSubpath) throws IOException {
		String name = FilenameUtils.getName(url.getFile());
		File directory = getDownloadsDirectory(downloadsSubpath);
		File file = new File(directory, name);
		if (file.exists()) {
			// pass
		} else {
			String baseName = FilenameUtils.getBaseName(name);
			String extension = FilenameUtils.getExtension(name);
			File tempFile = File.createTempFile(baseName + "-", "." + extension);
			SwingWorker<Void, Void> worker = progress("Downloading from " + url.getHost(), url.getFile());
			FileUtils.copyURLToFile(url, tempFile);
			tempFile.renameTo(file);
			worker.cancel(false);
		}
		return file;
	}

	public static File getDownloadedFile(URL url) throws IOException {
		return getDownloadedFile(url, null);
	}

	private static List<URL> listAll(URL urlDirectory) throws IOException {
		String directoryPath = urlDirectory.toExternalForm();
		List<URL> result = new LinkedList<>();
		Document doc = Jsoup.connect(directoryPath).get();
		for (Element file : doc.select("a[href]")) {
			String href = file.attr("href");
			String hrefAbs = file.attr("abs:href");
			if (href.startsWith("?")) {
				// pass
				// System.out.println("skipping: " + hrefAbs);
			} else {
				if (hrefAbs.startsWith(directoryPath)) {
					try {
						URL uri = new URL(hrefAbs);
						result.add(uri);
					} catch (MalformedURLException murle) {
						throw new RuntimeException(murle);
					}
				} else {
					// System.out.println("skipping: " + hrefAbs);
				}
			}
		}
		return result;
	}

	public static List<File> getDownloadedFiles(URL urlDirectory, String downloadsSubpath) throws IOException {
		List<URL> urls = listAll(urlDirectory);
		List<File> result = new ArrayList<>(urls.size());
		for (URL url : urls) {
			result.add(getDownloadedFile(url, downloadsSubpath));
		}
		return result;
	}
}
