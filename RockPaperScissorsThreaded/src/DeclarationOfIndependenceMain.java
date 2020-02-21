import java.io.*;

public class DeclarationOfIndependenceMain {
    public static void main(String[] args) {
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(new File("independence.txt")));
            FileWriter outputFile = new FileWriter("1thread_backwards_independence.txt");

            StringBuilder wholeFile = new StringBuilder();
            String line;
            while((line = fileReader.readLine()) != null) {
                System.out.println(line);
                line = line.replaceAll("\\p{Punct}", "");
                wholeFile.append(line).append("\n ");
            }

            String[] words = wholeFile.toString().split(" ");
            for(int i = words.length - 1; i >= 0; i--) {
                System.out.println(words[i]);
                if(i == words.length - 1) {
                    words[i] = words[i].replace("\n", "");
                }
                outputFile.write(words[i] + " ");
            }

            outputFile.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
