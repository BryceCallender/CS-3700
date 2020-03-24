import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SieveActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    ActorRef child;

    public static class PrimeContents {
        private final List<Integer> values;

        public PrimeContents(List<Integer> numberList) {
            values = Collections.unmodifiableList(numberList);
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(PrimeContents.class, prime -> {
                    if(!prime.values.isEmpty()) {
                        int step = prime.values.get(0);
                        log.info("{}", step);
                        List<Integer> nextNumbers = new ArrayList<>();
                        for(int i = 1; i < prime.values.size(); i++) {
                            if(prime.values.get(i) % step != 0) {
                                nextNumbers.add(prime.values.get(i));
                            }
                        }
                        child = getContext().actorOf(Props.create(SieveActor.class));
                        child.tell(new PrimeContents(nextNumbers), child);
                    }
                })
                .build();
    }
}
