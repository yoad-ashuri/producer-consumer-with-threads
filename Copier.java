/*
 *Yoad_Ashuri-311162606
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

public class Copier implements Runnable {

	static int COPY_BUFFER_SIZE = 4096;
	private SynchronizedQueue<File> resultsQueue;
	private File destination;

	public Copier(SynchronizedQueue<File> resultsQueue, File destination) {
		this.resultsQueue = resultsQueue;
		this.destination = destination;
	}

	public void run() {
		while(resultsQueue.getSize() != 0) {
			File file = resultsQueue.dequeue();
			if (file != null) {
				try {
					byte[] buff = new byte[COPY_BUFFER_SIZE];
					int len_read;
					File newFile = new File (destination, file.getName());

					FileInputStream in = new FileInputStream(file);
					FileOutputStream out = new FileOutputStream(newFile);

					len_read = in.read(buff);				//read first byte
					while (len_read > 0) {
						out.write(buff, 0, len_read);
						len_read = in.read(buff);
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}