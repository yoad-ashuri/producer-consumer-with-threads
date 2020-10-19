/*
 *Yoad_Ashuri-311162606
 */
import java.io.File;

public class Scouter implements Runnable {

	private SynchronizedQueue<File> directoryQueue;
	private File root;

	public Scouter(SynchronizedQueue<File> directoryQueue, File root) {
		this.directoryQueue = directoryQueue;
		this.root = root;
	}

	@Override
	public void run() {
		this.directoryQueue.registerProducer();
		File[] filesList = root.listFiles();
		if(filesList == null) {
			return;
		}

		if(root.isDirectory()){
			directoryQueue.enqueue(this.root);
		}
		for (int i = 0; i<filesList.length; i++){
			this.root = filesList[i];

			if (this.root.isDirectory()){
				this.run();
			}
		}

		directoryQueue.unregisterProducer();
	}
}