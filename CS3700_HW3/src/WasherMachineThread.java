import java.util.concurrent.BlockingQueue;

public class WasherMachineThread extends Thread {
    private BlockingQueue<String> washingQueue;

    WasherMachineThread(BlockingQueue<String> washingQueue) {
        this.washingQueue = washingQueue;
    }

    @Override
    public void run() {
        while(true) {
            try {
                String color = washingQueue.take();
                System.out.format("Washer Thread: Destroyed %s socks.%n", color);
            }
            catch(InterruptedException e) {
            }
        }
    }

}
