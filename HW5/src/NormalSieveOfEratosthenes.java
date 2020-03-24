import java.util.Arrays;

public class NormalSieveOfEratosthenes {
    private int n;
    private boolean[] numberList;

    NormalSieveOfEratosthenes(int highestNumber) {
        this.n = highestNumber;
        this.numberList = new boolean[n + 1]; //2 to n

        Arrays.fill(numberList, true); //Sets everything to true
    }

    void performSieve() {
        long start = System.nanoTime();
        //starting from 2 all the way to halfway of n is where we stop
        for(int i = 2; i * i < n; i++) {
            if(numberList[i]) {
                for (int j = i * i; j <= n; j += i) {
                    numberList[j] = false;
                }
            }
        }

        //output everything that is true in the list
        for(int i = 2; i < n; i++) {
            if(numberList[i]) {
                System.out.println(i + " is prime");
            }
        }

        long end = System.nanoTime();

        System.out.format("%nIt took %dns to do the sieve and output results.%n", end-start);
        System.out.format("Time in seconds: %f seconds%n", (double)(end-start) / 1_000_000_000);
    }
}
