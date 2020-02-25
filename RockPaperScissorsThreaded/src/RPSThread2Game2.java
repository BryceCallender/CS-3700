import java.util.concurrent.*;

public class RPSThread2Game2 implements Runnable {
    String[] values = {"Rock", "Paper", "Scissors"};

    String name;
    String handGesture;
    BlockingQueue<RPSThread2Game2> rpsQueue;

    CountDownLatch countDownLatch;

    RPSThread2Game2(int index, BlockingQueue<RPSThread2Game2> rpsQueue, CountDownLatch countDownLatch) {
        name = "Thread" + index;
        this.rpsQueue = rpsQueue;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        int randomPick = ThreadLocalRandom.current().nextInt(values.length);

        handGesture = values[randomPick];

        System.out.println(name + ": used " + handGesture);

        try {
            rpsQueue.put(this);
            countDownLatch.countDown();
            System.out.println("Counted down");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void redoGesture() {
        int randomPick = ThreadLocalRandom.current().nextInt(values.length);

        handGesture = values[randomPick];

        System.out.println(name + ": retried and got " + handGesture);
    }
}
