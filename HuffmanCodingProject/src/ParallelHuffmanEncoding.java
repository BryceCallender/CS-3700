import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
//            Path path = Paths.get("constitution.txt");
//            AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ);
//
//            ByteBuffer buffer = ByteBuffer.allocate((int)path.toFile().length());
//            long position = 0;
//
//            Future<Integer> operation = asynchronousFileChannel.read(buffer, position);
//
//            while(!operation.isDone());
//
//            buffer.flip();
//            byte[] data = new byte[buffer.limit()];
//            buffer.get(data);
//            System.out.println(new String(data));
//            buffer.clear();


            long start = System.currentTimeMillis();
            StringBuilder fileContents = new StringBuilder();
            String line;
            while((line = bufferedReader.readLine()) != null) {
                fileContents.append(line).append('\n');
            }

            List<Future<?>> future = new ArrayList<>();

            int fileLength = fileContents.length();
            fileData = fileContents.toString();

            for(int i = 0; i < coreCount; i++) {
                int startLine = i * Math.floorDiv(fileLength, coreCount);
                int endLine = startLine + Math.floorDiv(fileLength, coreCount);

                //System.out.printf("Start: %d End: %d%n", startLine, endLine);

                HuffmanFrequencyThread freqCalc = new HuffmanFrequencyThread(frequencyMap, fileData, startLine, endLine);
                future.add(threadPool.submit(freqCalc));
            }

            future.forEach((value) -> {
                try {
                    value.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });
            long end = System.currentTimeMillis();

            System.out.println("It took " + (end-start) + "ms to create the frequency map");

//            for (Map.Entry<Character,Integer> entry: frequencyMap.entrySet()) {
//                System.out.println(entry.getKey() + ": " + entry.getValue());
//            }

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
//            for (Map.Entry<Character, String> entry: binaryRepresentations.entrySet()) {
//                System.out.println(entry.getKey() + ": " + entry.getValue());
//            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public File encodeFile() {
        long start = System.currentTimeMillis();
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

        long end = System.currentTimeMillis();
        System.out.println("It took " + (end-start) + "ms to encode the file");

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
        File fileToRead = new File("compressed_constitution_multithread.txt");
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