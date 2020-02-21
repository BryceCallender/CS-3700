import java.util.concurrent.Callable;

public class CallableStringParser implements Callable<String> {
    private String fileContents;
    private int start;
    private int end;

    CallableStringParser(String file, int start, int end) {
        this.fileContents = file;
        this.start = start;
        this.end = end;
    }

    @Override
    public String call() throws Exception {
        StringBuilder reversedString = new StringBuilder();
        String[] contents = fileContents.split(" ");

        return reversedString.toString();
    }
}
