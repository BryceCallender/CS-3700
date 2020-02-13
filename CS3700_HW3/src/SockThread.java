import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

class Sock {
    public String color;

    Sock(String color) {
        this.color = color;
    }
}

public class SockThread extends Thread {
    public String sockColor;
    private BlockingDeque<Sock> blockingDeque;
    private AtomicInteger atomicInteger;

    SockThread(String sockColor, BlockingDeque<Sock> blockingDeque, AtomicInteger atomicInteger) {
        this.sockColor = sockColor;
        this.blockingDeque = blockingDeque;
        this.atomicInteger = atomicInteger;
    }

    @Override
    public void run() {
        int sockAmount = ThreadLocalRandom.current().nextInt(100) + 1;

        for(int i = 0; i < sockAmount; i++) {
            try {
                System.out.format("%s Sock: Produced %d of %d %n", sockColor, i + 1, sockAmount);
                Sock sock = new Sock(sockColor);
                blockingDeque.put(sock);
            }catch(InterruptedException e) {

            }
        }

        atomicInteger.incrementAndGet();
    }

}
