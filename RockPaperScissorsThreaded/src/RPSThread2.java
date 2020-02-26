import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class RPSThread2 implements Runnable {
    String[] values = {"Rock", "Paper", "Scissors"};

    String name;
    String handGesture;

    AtomicInteger counter;

    RPSThread2(int index, AtomicInteger counter) {
        name = "Thread" + index;
        this.counter = counter;
    }

    @Override
    public void run() {
        //System.out.println(name);

        int randomPick = ThreadLocalRandom.current().nextInt(values.length);

        handGesture = values[randomPick];

        System.out.println(name + ": used " + handGesture);

        counter.incrementAndGet();
    }

    public void resetCounter(AtomicInteger newCounter) {
        this.counter = newCounter;
    }
}
