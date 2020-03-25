import java.util.concurrent.atomic.AtomicInteger;

public class AtomicBoundedBuffer {
    AtomicInteger buffer = new AtomicInteger(0);

    int maxConsumers;
    int maxProducers;

    AtomicBoundedBuffer(int numberProducers, int numberConsumers) {
        this.maxConsumers = numberConsumers;
        this.maxProducers = numberProducers;
    }

    public boolean take(int consumerNumber) {
        while(true) {
            if(buffer.get() > 0) {
                if (buffer.compareAndSet(buffer.get(), buffer.get() - 1)) {
                    System.out.println("Consumer" + consumerNumber + " consumed an item");
                    buffer.decrementAndGet();
                    return true;
                }
            }
        }
    }

    public boolean put(int producerNumber) {
        while(true) {
            if(buffer.get() < 10) {
                if (buffer.compareAndSet(buffer.get(), buffer.get() + 1)) {
                    System.out.println("Producer" + producerNumber + " has produced an item");
                    buffer.incrementAndGet();
                    return true;
                }
            }
        }
    }
}
