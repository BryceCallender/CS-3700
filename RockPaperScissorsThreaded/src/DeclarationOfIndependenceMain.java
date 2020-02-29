import java.io.*;

public class DeclarationOfIndependenceMain {
    public static void main(String[] args) {
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(new File("independence.txt")));
            PrintWriter outputFile = new PrintWriter("1thread_backwards_independence.txt");

            StringBuilder wholeFile = new StringBuilder();
            String line;
            long startTime = System.nanoTime();
            while((line = fileReader.readLine()) != null) {
                line = line.replaceAll("\\p{Punct}", "");
                wholeFile.append(line).append(" ").append(" newline ");
            }

            String[] words = wholeFile.toString().split("\\s+");
            for(int i = words.length - 1; i >= 0; i--) {
                if(words[i].equals("newline")) {
                    outputFile.println();
                    continue;
                }
                outputFile.write(words[i] + " ");
            }

            outputFile.close();
            long endTime = System.nanoTime();

            System.out.println("It took " + (endTime-startTime) + "ns to read the file and then reverse it with single thread");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
