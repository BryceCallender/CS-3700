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
                    log.info(getSelf().path().name() + " consumed an item.");
                    log.info("Sleeping for 1 second...");
                    //getContext().getParent().tell("Removed", getSelf());
                    getContext().setReceiveTimeout(Duration.ofSeconds(1));

                })
                .match(ReceiveTimeout.class, t -> {
                    getContext().cancelReceiveTimeout();
                    log.info(getSelf().path().name() + " has woken up");
                    getContext().getParent().tell("Ready", getSelf());
                })
                .build();
    }
}
