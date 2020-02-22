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


            ArrayList<Future<String>> reverseStringTasks = new ArrayList<>();

            StringBuilder wholeFile = new StringBuilder();
            String line;
            while ((line = fileReader.readLine()) != null) {
                line = line.replaceAll("\\p{Punct}", "");
                wholeFile.append(line).append(" ").append(" newline ");
            }

            coreRanges.forEach((coreCount) -> {
                try {
                    FileWriter outputFile = new FileWriter(coreCount + "thread_multithreaded_backwards_independence.txt", true);
                    ExecutorService threadPool = Executors.newFixedThreadPool(coreCount);

                    reverseStringTasks.clear();

                    int lineCount = wholeFile.toString().split(" newline ").length;
                    for (int i = 0; i < coreCount; i++) {
                        int start = i *  Math.floorDiv(lineCount, coreCount);
                        int end = start + Math.floorDiv(lineCount, coreCount);

                        System.out.format("Start: %d, End: %d%n" ,start,end);

                        reverseStringTasks.add(threadPool.submit(new CallableStringParser(wholeFile.toString(), start, end)));
                    }

                    for(int i = reverseStringTasks.size() - 1; i >= 0; i--) {
                        try {
                            String fileData = reverseStringTasks.get(i).get();
//                            System.out.println(fileData);
                            outputContentToFile(outputFile, fileData);
//                          System.out.println(reverseStringTasks.get(i).get());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
//                        catch (IOException e) {

//                        }
                    }

                    outputFile.close();
                    threadPool.shutdown();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }catch(IOException e) {

        }
    }

    private static synchronized void outputContentToFile(FileWriter outputFile, String data) {
        System.out.println("-----------------WRITING TO FILE--------------------");
        System.out.println(data);
        try {
            outputFile.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
