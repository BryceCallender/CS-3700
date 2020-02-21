import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class DeclarationOfIndependenceMultiThreaded {
    public static void main(String[] args) {

        int processors = Runtime.getRuntime().availableProcessors();
        System.out.println(processors + " processor"
                + (processors != 1 ? "s are " : " is ")
                + "available");

        IntStream coreRanges = IntStream.rangeClosed(1,processors);

        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(new File("independence.txt")));


            ArrayList<Future<?>> reverseStringTasks = new ArrayList<>();

            StringBuilder wholeFile = new StringBuilder();
            String line;
            while ((line = fileReader.readLine()) != null) {
                line = line.replaceAll("\\p{Punct}", "");
                wholeFile.append(line).append(" ").append(" newline ");
            }

            coreRanges.forEach((coreCount) -> {
                try {
                    PrintWriter outputFile = new PrintWriter(coreCount + "thread_multithreaded_backwards_independence.txt");
                    ExecutorService threadPool = Executors.newFixedThreadPool(coreCount);

                    reverseStringTasks.clear();

                    int lineCount = wholeFile.toString().split(" newline ").length;
                    for (int i = 0; i < coreCount; i++) {
                        int start = i * lineCount/coreCount;
                        int end = start + lineCount/coreCount;

                        System.out.format("Start: %d, End: %d%n" ,start,end);

                        reverseStringTasks.add(threadPool.submit(new CallableStringParser(wholeFile.toString(), start, end)));
                    }

                    for(int i = 0; i < reverseStringTasks.size(); i++) {
                        try {
//                            String fileData = (String) reverseStringTasks.get(i).get();
                            System.out.println(reverseStringTasks.get(i).get());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }

                    threadPool.shutdown();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });
        }catch(IOException e) {

        }
    }
}
