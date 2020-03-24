import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ProducerActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private final Integer MAX_ITEMS = 5;
    private Integer numberItemsMade = 0;

    static class ProducerResponse {}
    static class ProducerFinished {}

    ProducerActor() {
        getContext().getParent().tell("Start", getSelf());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("Request", s -> {
                    log.info(getSender().path().name() + " has requested an item");
                    if(numberItemsMade < MAX_ITEMS) {
                        numberItemsMade++;
                        getSender().tell(new ProducerResponse(), getSelf());
                    }else {
                        getSender().tell(new ProducerFinished(), getSelf());
                    }
                })
                .build();
    }
}
