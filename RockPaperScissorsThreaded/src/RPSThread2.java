import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;

public class RPSThread2 implements Runnable {
    String[] values = {"Rock", "Paper", "Scissors"};

    String name;
    String handGesture;

    private CyclicBarrier barrier;

    RPSThread2(int index, CyclicBarrier barrier) {
        name = "Thread" + index;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        int randomPick = ThreadLocalRandom.current().nextInt(values.length);

        handGesture = values[randomPick];

        System.out.println(name + ": used " + handGesture);

        try {
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
