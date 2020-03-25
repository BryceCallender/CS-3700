import java.util.ArrayList;

class AtomicProducer extends Thread {
    private final int PRODUCTION_COUNT = 5;
    private AtomicBoundedBuffer buffer;
    int producerNumber;
    int numberItemsProduced = 0;

    AtomicProducer(int i, AtomicBoundedBuffer buffer) {
        this.producerNumber = i;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while(numberItemsProduced < PRODUCTION_COUNT) {
            numberItemsProduced = buffer.put(producerNumber)? ++numberItemsProduced : numberItemsProduced;
        }
    }
}

class AtomicConsumer extends Thread {
    private AtomicBoundedBuffer buffer;
    int consumerNumber;

    AtomicConsumer(int i, AtomicBoundedBuffer buffer) {
        this.consumerNumber = i;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while(true) {
            try {
                if(buffer.take(consumerNumber)) {
                    System.out.println("Going to sleep for a second...");
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                while(buffer.buffer.get() > 0) {
                    try {
                        buffer.take(consumerNumber);
                        System.out.println("Going to sleep for a second...");
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            }
        }
    }
}

public class ProducerConsumerAtomics {
    public static void main(String[] args) {
        AtomicBoundedBuffer atomicBoundedBuffer = new AtomicBoundedBuffer(5,2);

        ArrayList<AtomicProducer> producers = new ArrayList<>();
        ArrayList<AtomicConsumer> consumers = new ArrayList<>();

        System.out.println("---------------START 5 PRODUCER, 2 CONSUMER---------------");
        long start = System.currentTimeMillis();
        //Part A
        for (int i = 0; i < 5; i++) {
            producers.add(new AtomicProducer(i, atomicBoundedBuffer));
            producers.get(i).start();
        }

        for (int i = 0; i < 2; i++) {
            consumers.add(new AtomicConsumer(i, atomicBoundedBuffer));
            consumers.get(i).start();
        }

        for (AtomicProducer producer : producers) {
            try {
                producer.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (AtomicConsumer consumer : consumers) {
            consumer.interrupt();
        }

        for (AtomicConsumer consumer : consumers) {
            try {
                consumer.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long end = System.currentTimeMillis();
//        System.out.println("---------------END 5 PRODUCER, 2 CONSUMER---------------");
//        System.out.println("Program is finished");
//        System.out.format("5 producer, 2 consumer took: %dms", end-start);
//
//        System.out.println("\n\n---------------START 2 PRODUCER, 5 CONSUMER---------------");
//        producers.clear();
//        consumers.clear();
//
//        atomicBoundedBuffer = new AtomicBoundedBuffer(2,5);
//
//        start = System.currentTimeMillis();
//        //Part B
//        for (int i = 0; i < 2; i++) {
//            producers.add(new AtomicProducer(i, atomicBoundedBuffer));
//            producers.get(i).start();
//        }
//
//        for (int i = 0; i < 5; i++) {
//            consumers.add(new AtomicConsumer(i, atomicBoundedBuffer));
//            consumers.get(i).start();
//        }
//
//        for (AtomicProducer producer : producers) {
//            try {
//                producer.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        for (AtomicConsumer consumer : consumers) {
//            consumer.interrupt();
//        }
//
//        for (AtomicConsumer consumer : consumers) {
//            try {
//                consumer.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        end = System.currentTimeMillis();
        System.out.println("---------------END 2 PRODUCER, 5 CONSUMER---------------");
        System.out.println("Program is finished");
        System.out.format("2 producer, 5 consumer took: %dms", end-start);
    }
}
