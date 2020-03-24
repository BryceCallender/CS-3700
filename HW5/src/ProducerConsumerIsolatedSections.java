import java.util.ArrayList;

class ProducerIsolated extends Thread {
    private final int PRODUCTION_COUNT = 100;
    private IsolatedBoundedBuffer buffer;
    int producerNumber;

    ProducerIsolated(int i, IsolatedBoundedBuffer buffer) {
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

class ConsumerIsolated extends Thread {
    private IsolatedBoundedBuffer buffer;
    int consumerNumber;

    ConsumerIsolated(int i, IsolatedBoundedBuffer buffer) {
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

public class ProducerConsumerIsolatedSections {
    public static void main(String[] args) {
        IsolatedBoundedBuffer buffer = new IsolatedBoundedBuffer();

        ArrayList<ProducerIsolated> producers = new ArrayList<>();
        ArrayList<ConsumerIsolated> consumers = new ArrayList<>();

        System.out.println("---------------START 5 PRODUCER, 2 CONSUMER---------------");
        long start = System.currentTimeMillis();
        //Part A
        for (int i = 0; i < 5; i++) {
            producers.add(new ProducerIsolated(i, buffer));
            producers.get(i).start();
        }

        for (int i = 0; i < 2; i++) {
            consumers.add(new ConsumerIsolated(i, buffer));
            consumers.get(i).start();
        }

        for (ProducerIsolated producer : producers) {
            try {
                producer.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (ConsumerIsolated consumer : consumers) {
            consumer.interrupt();
        }

        for (ConsumerIsolated consumer : consumers) {
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
            producers.add(new ProducerIsolated(i, buffer));
            producers.get(i).start();
        }

        for (int i = 0; i < 5; i++) {
            consumers.add(new ConsumerIsolated(i, buffer));
            consumers.get(i).start();
        }

        for (ProducerIsolated producer : producers) {
            try {
                producer.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (ConsumerIsolated consumer : consumers) {
            consumer.interrupt();
        }

        for (ConsumerIsolated consumer : consumers) {
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