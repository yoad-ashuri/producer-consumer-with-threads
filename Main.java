/*
 *Yoad_Ashuri-311162606
 */
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {

        List<String> lines = getLinesFromFile();
        System.out.println("Number of lines found: " + lines.size());
        System.out.println("Starting to process");

        long startTimeWithoutThreads = System.currentTimeMillis();
        workWithoutThreads(lines);
        long elapsedTimeWithoutThreads = (System.currentTimeMillis() - startTimeWithoutThreads);
        System.out.println("Execution time: " + elapsedTimeWithoutThreads);


        long startTimeWithThreads = System.currentTimeMillis();
        workWithThreads(lines);
        long elapsedTimeWithThreads = (System.currentTimeMillis() - startTimeWithThreads);
        System.out.println("Execution time: " + elapsedTimeWithThreads);

    }

    private static void workWithThreads(List<String> lines) {
        //Your code:
        //Get the number of available cores
        //Assuming X is the number of cores - Partition the data into x data sets
        int x = Runtime.getRuntime().availableProcessors();
        //Create a fixed thread pool of size X
        ExecutorService threadPool = Executors.newFixedThreadPool(x);

        //Submit X workers to the thread pool
        for (int i=0; i < x; i++){
            int start = i * (lines.size() / x);
            int end = (i + 1)*(lines.size() / x) - 1;
            Worker worker;
            if (i != (x-1)) {
                worker = new Worker(lines.subList(start, end));
            }else {
                worker = new Worker(lines.subList(start, lines.size()));
            }
           threadPool.execute(worker);
        }
		//Wait for termination
        threadPool.shutdown();
    }

    private static void workWithoutThreads(List<String> lines) {
        Worker worker = new Worker(lines);
        worker.run();
    }

    private static List<String> getLinesFromFile() {
        ArrayList<String> result = new ArrayList<>();
        Scanner in = null;
            try {
                in = new Scanner(new FileReader("C:\\Temp\\shakespeare.txt")); // open the file to read
                //start reading the file - line by line
                while (in.hasNext()) {
                    result.add(in.nextLine());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {

                    in.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        return result;
    }
}
