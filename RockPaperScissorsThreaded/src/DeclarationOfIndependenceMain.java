import java.io.*;

public class DeclarationOfIndependenceMain {
    public static void main(String[] args) {
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(new File("independence.txt")));
            FileWriter outputFile = new FileWriter("1thread_backwards_independence.txt");

            String line;
            while((line = fileReader.readLine()) != null) {
                String[] words = line.split(" ");
                StringBuilder outputLine = new StringBuilder();
                for (int i = words.length - 1; i >= 0; i--) {
                    outputLine.append(words[i]).append(" ");
                }
                outputFile.write(outputLine.toString());
            }

            outputFile.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
