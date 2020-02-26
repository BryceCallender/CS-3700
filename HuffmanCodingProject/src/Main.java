import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        File constitutionFile = new File("constitution.txt");

        System.out.println("Constitution file size: " + constitutionFile.length() + " bytes");

        HuffmanEncoding huffmanEncoding = new HuffmanEncoding(constitutionFile);

        huffmanEncoding.createHuffmanTree();
        File newFile = huffmanEncoding.encodeFile();

        huffmanEncoding.decodeFile();

        ParallelHuffmanEncoding parallelHuffmanEncoding = new ParallelHuffmanEncoding(constitutionFile);

        parallelHuffmanEncoding.createHuffmanTree();
        File parallelFile = parallelHuffmanEncoding.encodeFile();

        parallelHuffmanEncoding.decodeFile();

        System.out.println("Constitution compressed file size: " + (newFile.length()/8.0f) + " bytes");
        System.out.println("Compressed by " + ((newFile.length() / 8.0f)/constitutionFile.length() * 100) + "%");
        System.out.println("Constitution multithreaded compressed file size: " + (parallelFile.length()/8.0f) + " bytes");
        System.out.println("Compressed by " + ((parallelFile.length() / 8.0f)/constitutionFile.length() * 100) + "%");
    }
}
