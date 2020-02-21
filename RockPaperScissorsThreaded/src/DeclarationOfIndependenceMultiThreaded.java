import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.stream.IntStream;

public class DeclarationOfIndependenceMultiThreaded {
    public static void main(String[] args) {

        int coreCount = Runtime.getRuntime().availableProcessors();
        System.out.println(coreCount + " processor"
                + (coreCount != 1 ? "s are " : " is ")
                + "available");

        IntStream coreRanges = IntStream.rangeClosed(1,coreCount);

        ExecutorService threadPool;

        for(int i = 0; i < coreCount; i++) {
            threadPool = Executors.newFixedThreadPool(i + 1);
            FutureTask[] reverseStringTasks = new FutureTask[i + 1];

            //reverseStringTasks[i] = new FutureTask(new CallableStringParser());
        }
    }
}
