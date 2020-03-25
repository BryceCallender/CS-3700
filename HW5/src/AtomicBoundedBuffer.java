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
        while(true) {
            if(buffer.compareAndSet(buffer.get(), buffer.get()-1)) {
                System.out.println("Consumer"+ consumerNumber + " consumed an item");
                buffer.decrementAndGet();
                return true;
            }
        }

//        if(consumerNumber == validConsumerNumber && buffer.get() > 0) {
//            validConsumerNumber = ++validConsumerNumber % maxConsumers;
//            buffer.decrementAndGet();
//
//            return true;
//        }
    }

    public boolean put(int producerNumber) {
        while(true) {
            if (buffer.compareAndSet(buffer.get(), buffer.get() + 1)) {
                System.out.println("Producer" + producerNumber + " has produced an item");
                buffer.incrementAndGet();
                return true;
            }
        }
//        if(producerNumber == validProducerNumber && buffer.get() < 10) {
//            validProducerNumber = ++validProducerNumber % maxProducers;
//            buffer.incrementAndGet();
//
//            return true;
//        }
    }
}
