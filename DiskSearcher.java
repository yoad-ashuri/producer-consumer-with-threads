/*
 *Yoad_Ashuri-311162606
 */
import java.io.File;

public class DiskSearcher {

	static int DIRECTORY_QUEUE_CAPACITY = 50;
	static int RESULTS_QUEUE_CAPACITY = 50;

	public static void main(String[] args) {

		String pattern = args[0];
		String extension = args[1];
		File root = new File(args[2]);
		File destination = new File(args[3]);
		int numOfSearchers =  Integer.parseInt(args[4]);
		int numOfCopiers = Integer.parseInt(args[5]);

		Thread[] searcherArry = new Thread[numOfSearchers];
		Thread[] copiersArry = new Thread[numOfCopiers];

		SynchronizedQueue<File> directoryQueue = new SynchronizedQueue<File>(DIRECTORY_QUEUE_CAPACITY);
		SynchronizedQueue<File> resultsQueue = new SynchronizedQueue<File>(RESULTS_QUEUE_CAPACITY);

		Scouter newS = new Scouter(directoryQueue, root);
		Thread scouter = new Thread(newS);
		scouter.start();

		try {
			scouter.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		for(int i = 0; i <  numOfSearchers; i++) {
			searcherArry[i] = new Thread(new Searcher(directoryQueue, resultsQueue, pattern, extension));
			searcherArry[i].start();
		}

		for(int i = 0; i <  numOfCopiers; i++) {
			copiersArry[i] = new Thread(new Copier(resultsQueue, destination));
			copiersArry[i].start();
		}



		for(int i = 0; i < numOfSearchers; i++) {
			try {
				searcherArry[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		for(int i = 0; i < numOfCopiers; i++) {
			try {
				copiersArry[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}