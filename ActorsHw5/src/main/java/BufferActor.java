import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.ArrayList;
import java.util.Random;

public class BufferActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    ArrayList<ActorRef> producerActors = new ArrayList<>();
    ArrayList<ActorRef> consumerActors = new ArrayList<>();

    ArrayList<ActorRef> readyConsumers = new ArrayList<>();

    private final Integer MAX = 10;
    private Integer numberOfItems = 0;
    private Integer producerNumber = 0;
    private Integer consumerNumber = 0;

    private Integer necessaryProducersToStop;
    private Integer producersFinished = 0;

    long start,end;

    static class FinishLeftovers {

    }

    @Override
    public void preStart() throws Exception {
        System.out.println("Init");

        start = System.nanoTime();

        for(int i = 0; i < 5; i++) {
            producerActors.add(getContext().actorOf(Props.create(ProducerActor.class), "Producer" + i));
        }

        for (int i = 0; i < 2; i++) {
            consumerActors.add(getContext().actorOf(Props.create(ConsumerActor.class), "Consumer" + i));
        }

//        for(int i = 0; i < 2; i++) {
//            producerActors.add(getContext().actorOf(Props.create(ProducerActor.class), "Producer" + i));
//        }
//
//        for (int i = 0; i < 5; i++) {
//            consumerActors.add(getContext().actorOf(Props.create(ConsumerActor.class), "Consumer" + i));
//        }

        necessaryProducersToStop = producerActors.size();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("Start", s-> {
                    getSender().tell("Request", getSelf());
                })
                .match(ProducerActor.ProducerResponse.class, r -> {
                    if(numberOfItems < MAX) {
                        System.out.println(getSelf().path().name() + " has received an item from " + getSender().path().name());
                        numberOfItems++;
                        System.out.println("Buffer size: " + numberOfItems);
                        if (readyConsumers.size() > 0) {
                            int randomConsumer = new Random().nextInt(readyConsumers.size());
                            ActorRef consumer = readyConsumers.get(randomConsumer);
                            System.out.println("Removing " + consumer.path().name() + "!");
                            numberOfItems--;
                            consumer.tell("Remove", getSender());
                            readyConsumers.remove(consumer);
                        }
                    }
                })
                .match(ProducerActor.ProducerFinished.class, p -> {
                    producersFinished++;

                    if(producersFinished.equals(necessaryProducersToStop)) {
                        System.out.println("Finished");
                        getSelf().tell(new FinishLeftovers(), getSelf());
                    }
                })
                .match(FinishLeftovers.class, f -> {
                    if (numberOfItems > 0 && readyConsumers.size() > 0) {
                        int randomConsumer = new Random().nextInt(readyConsumers.size());
                        ActorRef consumer = readyConsumers.get(randomConsumer);
                        System.out.println("Removing " + consumer.path().name() + "!");
                        numberOfItems--;
                        consumer.tell("Remove", getSelf());
                        readyConsumers.remove(consumer);
                    }

                    if(numberOfItems == 0) {
                        getContext().getSystem().terminate();
                        end = System.nanoTime();

                        System.out.format("%nIt took %dns to produce and consume everything.%n", end-start);
                        System.out.format("Time in seconds: %f seconds%n", (double)(end-start) / 1_000_000_000);
                    }
                })
                .matchEquals("Ready", s -> {
                    if(numberOfItems > 0) {
                        numberOfItems--;
                        getSender().tell("Remove", getSelf());
                    } else {
                        if(!readyConsumers.contains(getSender()))
                            readyConsumers.add(getSender());
                    }

                    producerNumber = ++producerNumber % producerActors.size();
                    producerActors.get(producerNumber).tell("Request", getSelf());
                })
                .build();
    }
}
