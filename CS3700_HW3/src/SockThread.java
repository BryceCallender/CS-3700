import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
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
    private ConcurrentHashMap<String, Integer> sockMap;

    SockThread(String sockColor, BlockingQueue<Sock> blockingQueue, ConcurrentHashMap<String, Integer> sockMap) {
        this.sockColor = sockColor;
        this.blockingQueue = blockingQueue;
        this.sockMap = sockMap;
    }

    @Override
    public void run() {
        int sockAmount = ThreadLocalRandom.current().nextInt(100) + 1;

        for(int i = 0; i < sockAmount; i++) {
            try {
                System.out.format("%s Sock: Produced %d of %d %n", sockColor, i + 1, sockAmount);
                Sock sock = new Sock(sockColor);
                blockingQueue.put(sock);

                int currentCount = sockMap.get(sockColor);
                sockMap.replace(sockColor, ++currentCount);

            }catch(InterruptedException e) {

            }
        }
    }

}
