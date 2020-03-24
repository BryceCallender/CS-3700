import java.util.concurrent.atomic.AtomicInteger;

public class AtomicBoundedBuffer {
    AtomicInteger buffer = new AtomicInteger(0);

    int validConsumerNumber = 0;
    int validProducerNumber = 0;

    int maxConsumers;
    int maxProducers;

    AtomicBoundedBuffer(int numberProducers, int numberConsumers) {
        this.maxConsumers = numberConsumers;
        this.maxProducers = numberProducers;
    }

    public boolean take(int consumerNumber) {
        if(consumerNumber == validConsumerNumber && buffer.get() > 0) {
            validConsumerNumber = ++validConsumerNumber % maxConsumers;
            buffer.decrementAndGet();
            System.out.println("Consumer"+ consumerNumber + " consumed an item");
            return true;
        }
        return false;
    }

    public boolean put(int producerNumber) {
        if(producerNumber == validProducerNumber && buffer.get() < 10) {
            validProducerNumber = ++validProducerNumber % maxProducers;
            buffer.incrementAndGet();
            System.out.println("Producer" + producerNumber + " has produced an item");
            return true;
        }
        return false;
    }
}
