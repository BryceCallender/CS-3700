import java.util.ArrayList;

class Producer extends Thread {
    private final int PRODUCTION_COUNT = 100;
    private BoundedBuffer buffer;
    int producerNumber;

    Producer(int i, BoundedBuffer buffer) {
        this.producerNumber = i;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        for(int i = 0; i < PRODUCTION_COUNT; i++) {
            buffer.put(producerNumber, "Item");
        }
    }
}

class Consumer extends Thread {
    private BoundedBuffer buffer;
    int consumerNumber;

    Consumer(int i, BoundedBuffer buffer) {
        this.consumerNumber = i;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while(true) {
            try {
                buffer.take(consumerNumber, false);
                System.out.println("Going to sleep for a second...");
                //Sleep for a second
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Getting rid of remaining units");
                while(buffer.buffer.size() > 0) {
                    try {
                        buffer.take(consumerNumber, true);
                        System.out.println("Going to sleep for a second...");
                        //Sleep for a second
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

public class ProducerConsumerLock {
    public static void main(String[] args) {
        BoundedBuffer buffer = new BoundedBuffer();

        ArrayList<Producer> producers = new ArrayList<>();
        ArrayList<Consumer> consumers = new ArrayList<>();

        System.out.println("---------------START 5 PRODUCER, 2 CONSUMER---------------");
        long start = System.currentTimeMillis();
        //Part A
        for (int i = 0; i < 5; i++) {
            producers.add(new Producer(i, buffer));
            producers.get(i).start();
        }

        for (int i = 0; i < 2; i++) {
            consumers.add(new Consumer(i, buffer));
            consumers.get(i).start();
        }

        for (Producer producer : producers) {
            try {
                producer.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (Consumer consumer : consumers) {
            consumer.interrupt();
        }

        for (Consumer consumer : consumers) {
            try {
                consumer.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("---------------END 5 PRODUCER, 2 CONSUMER---------------");
        System.out.println("Program is finished");
        System.out.format("5 producer, 2 consumer took: %dms", end-start);

        System.out.println("\n\n---------------START 2 PRODUCER, 5 CONSUMER---------------");
        producers.clear();
        consumers.clear();

        start = System.currentTimeMillis();
        //Part B
        for (int i = 0; i < 2; i++) {
            producers.add(new Producer(i, buffer));
            producers.get(i).start();
        }

        for (int i = 0; i < 5; i++) {
            consumers.add(new Consumer(i, buffer));
            consumers.get(i).start();
        }

        for (Producer producer : producers) {
            try {
                producer.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (Consumer consumer : consumers) {
            consumer.interrupt();
        }

        for (Consumer consumer : consumers) {
            try {
                consumer.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        end = System.currentTimeMillis();
        System.out.println("---------------END 2 PRODUCER, 5 CONSUMER---------------");
        System.out.println("Program is finished");
        System.out.format("2 producer, 5 consumer took: %dms", end-start);
    }
}