import akka.actor.AbstractActor;
import akka.actor.ReceiveTimeout;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.time.Duration;

public class ConsumerActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private ConsumerActor() {
        getContext().parent().tell("Ready", getSelf());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("Remove", s -> {
                    System.out.println(getSelf().path().name() + " consumed an item.");
                    System.out.println("Sleeping for 1 second...");
                    getContext().setReceiveTimeout(Duration.ofSeconds(1));
                })
                .match(ReceiveTimeout.class, t -> {
                    getContext().cancelReceiveTimeout();
                    System.out.println(getSelf().path().name() + " has woken up");
                    getContext().getParent().tell("Ready", getSelf());
                })
                .build();
    }
}
