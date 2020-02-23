import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;

public class RPSThread2Game2 implements Runnable {
    String[] values = {"Rock", "Paper", "Scissors"};

    String name;
    String handGesture;
    BlockingQueue<RPSThread2Game2> rpsQueue;
    CyclicBarrier barrier;


    RPSThread2Game2(int index, BlockingQueue<RPSThread2Game2> rpsQueue, CyclicBarrier barrier) {
        name = "Thread" + index;
        this.rpsQueue = rpsQueue;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        int randomPick = ThreadLocalRandom.current().nextInt(values.length);

        handGesture = values[randomPick];

        System.out.println(name + ": used " + handGesture);

        try {
            rpsQueue.put(this);
            System.out.println(name + " is waiting at the barrier...");
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    public void redoGesture() {
        int randomPick = ThreadLocalRandom.current().nextInt(values.length);

        handGesture = values[randomPick];

        System.out.println(name + ": retried and got " + handGesture);
    }

    public void awaitOpponent() {
        try {
            System.out.println(name + " is waiting at the barrier for an opponent after winning...");
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
