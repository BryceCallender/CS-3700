public class SieveOfEratosthenes {
    public static void main(String[] args) {
        NormalSieveOfEratosthenes sieve = new NormalSieveOfEratosthenes(1_000_000);
        sieve.performSieve();
    }
}
