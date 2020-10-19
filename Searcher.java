/*
 *Yoad_Ashuri-311162606
 */
import java.io.File;
import java.io.IOException;

public class Searcher implements Runnable {

	private SynchronizedQueue<File> directoryQueue;
	private SynchronizedQueue<File> resultsQueue;
	private String pattern;
	private String extension;

	public Searcher(SynchronizedQueue<File> directoryQueue, SynchronizedQueue<File> resultsQueue, String pattern, String extension) {
		this.directoryQueue = directoryQueue;
		this.resultsQueue = resultsQueue;
		this.pattern = pattern;
		this.extension = extension;
	}

	public void run() {
		resultsQueue.registerProducer();

		while (directoryQueue.getSize() != 0) {
			try {
				File dir = directoryQueue.dequeue();
				if (dir != null) {
					File[] filesList = dir.listFiles();

					for (int i = 0; i < filesList.length; i++){
						if (filesList[i].getName().contains(this.pattern) && filesList[i].getName().endsWith(this.extension)) {
							resultsQueue.enqueue(filesList[i]);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		resultsQueue.unregisterProducer();
	}
}