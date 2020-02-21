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

        String[] words = fileContents.split(" newline ");
        if(end >= words.length) {
            end = words.length - 1;
        }

        System.out.println("--------------I am responsible for these lines-------------");
        for(int i = end; i >= start; i--) {
            //System.out.println(words[i]);
            //reversedString.append("\n");
        }
        System.out.println("-----------------------------------------------------------");

        return reversedString.toString();
    }
}
