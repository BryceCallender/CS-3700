import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

class Sock {
    public String color;

    Sock(String color) {
        this.color = color;
    }
}

public class SockThread extends Thread {
    public String sockColor;
    private BlockingQueue<Sock> blockingQueue;

    SockThread(String sockColor, BlockingQueue<Sock> blockingQueue) {
        this.sockColor = sockColor;
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        int sockAmount = ThreadLocalRandom.current().nextInt(100) + 1;

        for(int i = 0; i < sockAmount; i++) {
            try {
                System.out.format("%s Sock: Produced %d of %d %n", sockColor, i + 1, sockAmount);
                blockingQueue.put(new Sock(sockColor));
            }catch(InterruptedException e) {

            }
        }
    }

}
