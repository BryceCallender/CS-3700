import java.util.Map;
import java.util.concurrent.Callable;

public class CallableEncoder implements Callable<String> {
    private Map<Character, String> binaryRepresentations;

    private String fileData;
    private int start;
    private int end;

    CallableEncoder(Map<Character, String> binaryRepresentations, String fileData, int startLine, int endLine) {
        this.binaryRepresentations = binaryRepresentations;
        this.fileData = fileData;
        this.start = startLine;
        this.end = endLine;
    }

    @Override
    public String call() throws Exception {
        StringBuilder data = new StringBuilder();
        for(int i = start; i < end; i++) {
            char character = fileData.charAt(i);
            data.append(binaryRepresentations.get(character));
        }
        return data.toString();
    }
}
