import java.util.Map;

public class HuffmanFrequencyThread implements Runnable {
    private Map<Character, Integer> frequencyMap;

    private String contents;
    private int start;
    private int end;

    HuffmanFrequencyThread(Map<Character, Integer> frequencyMap, String contents, int startLine, int endLine) {
        this.frequencyMap = frequencyMap;
        this.contents = contents;
        this.start = startLine;
        this.end = endLine;
    }

    @Override
    public void run() {
        for(int i = start; i < end; i++) {
            char character = contents.charAt(i);
            frequencyMap.computeIfPresent(character, (key, value) -> value + 1);
            frequencyMap.putIfAbsent(character,1);
        }
    }
}
