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
        System.out.println(name);
        this.barrier = barrier;
    }

    @Override
    public void run() {
        //System.out.println(name);

        int randomPick = ThreadLocalRandom.current().nextInt(values.length);

        handGesture = values[randomPick];

        System.out.println(name + ": used " + handGesture);

        try {
            System.out.println(name + " is waiting for others to reach the barrier");
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
