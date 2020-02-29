import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class ParallelHuffmanEncoding {
    private File file;
    private Map<Character, Integer> frequencyMap;
    private Map<Character, String> binaryRepresentations;

    private String fileData;
    private ExecutorService threadPool;
    private int coreCount;

    private PriorityQueue<HuffmanNode> priorityQueue;

    ParallelHuffmanEncoding(File file) {
        this.file = file;
        frequencyMap = new ConcurrentHashMap<>();
        priorityQueue = new PriorityQueue<>(1, new HuffmanComparator());
        binaryRepresentations = new HashMap<>();

        coreCount = Runtime.getRuntime().availableProcessors();

        threadPool = Executors.newFixedThreadPool(coreCount);
    }

    public void createHuffmanTree() throws IOException {
        try {
            long start = System.nanoTime();
            Path path = Paths.get("constitution.txt");
            StringBuilder fileContents = new StringBuilder();

            AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(path, EnumSet.of(StandardOpenOption.READ), threadPool);

            int bufferSize = (int)path.toFile().length() / coreCount + 50; // some padding from bad division offsets

            List<ByteBuffer> buffers = new ArrayList<>();
            List<Future<?>> futures = new ArrayList<>();
            
            long position = 0;
            for(int i = 0; i < coreCount; i++) {
                ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
                futures.add(asynchronousFileChannel.read(buffer, position));
                position += bufferSize;
                buffers.add(buffer);
            }

            for(Future<?> future: futures) {
                future.get();
            }

            for (ByteBuffer byteBuffer: buffers) {
                byteBuffer.position(0);
                CharBuffer charBuffer = Charset.defaultCharset().decode(byteBuffer);
                fileContents.append(charBuffer);
                for (char character: charBuffer.array()) {
                    if(!frequencyMap.containsKey(character)) {
                        frequencyMap.put(character, 1);
                    }else {
                        frequencyMap.put(character, frequencyMap.get(character) + 1);
                    }
                }
            }

            long end = System.nanoTime();
            System.out.println("It took " + (end-start) + "ns to create the frequency map");

            fileData = fileContents.toString();

            start = System.nanoTime();
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

            end = System.nanoTime();
            System.out.println("It took " + (end-start) + "ns to create the tree");

//            System.out.println("Encoding Key Output");
//            for (Map.Entry<Character, String> entry: binaryRepresentations.entrySet()) {
//                System.out.println(entry.getKey() + ": " + entry.getValue());
//            }

        } catch (FileNotFoundException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public File encodeFile() {
        long start = System.nanoTime();
        File outputFile = new File("constitution_multithread.txt");
        List<Future<String>> futures = new ArrayList<>();
        try {
            FileWriter fileWriter = new FileWriter(outputFile);

            for(int i = 0; i < coreCount; i++) {
                int startLine = i * Math.floorDiv(fileData.length(), coreCount);
                int endLine = startLine + Math.floorDiv(fileData.length(), coreCount);

                //System.out.printf("Start: %d End: %d%n", startLine, endLine);

                futures.add(threadPool.submit(new CallableEncoder(binaryRepresentations, fileData, startLine, endLine)));
            }

            for (Future<String> future: futures) {
                try {
                    fileWriter.write(future.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        threadPool.shutdown();

        long end = System.nanoTime();
        System.out.println("It took " + (end-start) + "ns to encode the file");

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

    public void decodeFile() {
        File fileToRead = new File("constitution_multithread.txt");
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileToRead));
            HuffmanNode current = priorityQueue.peek();

            int c;
            while((c = bufferedReader.read()) != -1) {
                Character character = (char) c;

                if(character.equals('0')) {
                    current = current.leftChild;
                }else {
                    current = current.rightChild;
                }

                if(current.leftChild == null && current.rightChild == null) {
                    //System.out.print(current.character);
                    current = priorityQueue.peek();
                }
            }
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}