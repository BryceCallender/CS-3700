import java.io.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

class HuffmanNode {
    int frequency;
    char character;

    HuffmanNode leftChild;
    HuffmanNode rightChild;
}

class HuffmanComparator implements Comparator<HuffmanNode> {
    @Override
    public int compare(HuffmanNode huffmanNode1, HuffmanNode huffmanNode2) {
        return Integer.compare(huffmanNode1.frequency, huffmanNode2.frequency);
    }
}

public class HuffmanEncoding {
    private File file;
    private Map<Character, Integer> frequencyMap;
    private Map<Character, String> binaryRepresentations;

    PriorityQueue<HuffmanNode> priorityQueue;

    HuffmanEncoding(File file) {
        this.file = file;
        frequencyMap = new HashMap<>();
        priorityQueue = new PriorityQueue<>(1, new HuffmanComparator());
        binaryRepresentations = new HashMap<>();
    }

    public void createHuffmanTree() throws IOException {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            int c;
            while((c = bufferedReader.read()) != -1) {
                Character character = (char) c;
                if(!frequencyMap.containsKey(character)) {
                    frequencyMap.put(character, 1);
                }else {
                    frequencyMap.put(character, frequencyMap.get(character) + 1);
                }
            }

            for (Map.Entry<Character,Integer> entry: frequencyMap.entrySet()) {
                HuffmanNode huffmanNode = new HuffmanNode();

                huffmanNode.character = entry.getKey();
                huffmanNode.frequency = entry.getValue();

                huffmanNode.leftChild = null;
                huffmanNode.rightChild = null;

                priorityQueue.add(huffmanNode);
            }

            while(priorityQueue.size() > 1) {
                HuffmanNode node1 = priorityQueue.poll();
                HuffmanNode node2 = priorityQueue.poll();

                HuffmanNode newNode = new HuffmanNode();

                newNode.frequency = node1.frequency + node2.frequency;
                newNode.character = '_'; //Means that this node just holds the 2 nodes and is not an actual value node

                newNode.leftChild = node1;
                newNode.rightChild = node2;

                priorityQueue.add(newNode);
            }

            generateBinaryCodes(priorityQueue.peek(), "");

            System.out.println("Encoding Key Output");
            for (Map.Entry<Character, String> entry: binaryRepresentations.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public File encodeFile() {
        File outputFile = new File("compressed_constitution.txt");
        StringBuilder string = new StringBuilder();
        try {
            FileWriter fileWriter = new FileWriter(outputFile);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            int c;
            while((c = bufferedReader.read()) != -1) {
                Character character = (char) c;
                fileWriter.write(binaryRepresentations.get(character));
                string.append(binaryRepresentations.get(character));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputFile;
    }

    private void generateBinaryCodes(HuffmanNode root, String binary) {
        if(root == null) {
            return;
        }

        if(root.character != '_') {
            binaryRepresentations.put(root.character, binary);
        }
        generateBinaryCodes(root.leftChild, binary + "0");
        generateBinaryCodes(root.rightChild, binary + "1");
    }

}
