import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SieveActor extends AbstractActor {
    private boolean[] numbers;
    List<ActorRef> children;

    long start, end;

    SieveActor(int n, long time) {
        this.start = time;
        children = new ArrayList<>();
        this.numbers = new boolean[n + 1];

        Arrays.fill(numbers, true);
        getSelf().tell(2, getSelf());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Integer.class, nonPrime -> {
                    for(int i = nonPrime; i * i <= numbers.length; i++) {
                        ActorRef child = getContext().actorOf(Props.create(PrimeChecker.class, numbers.length - 1));
                        children.add(child);
                        child.tell(i, getSelf());
                    }
                })
                .match(PrimeChecker.NonPrime.class, np -> numbers[np.number] = false)
                .match(PrimeChecker.CheckPrime.class, p -> {
                    if(numbers[p.number]) {
                        System.out.println(p.number);
                    }
                })
                .match(ActorRef.class, primeChecker -> {
                    children.remove(getSender());
                    if(children.size() == 0) {
                        getContext().getSelf().tell("Finished", getSelf());
                    }
                })
                .matchEquals("Finished", f -> {
                    for(int i = (int)Math.sqrt(numbers.length - 1); i < numbers.length; i++) {
                        if(numbers[i]) {
                            System.out.println(i);
                        }
                    }
                    getContext().getSystem().terminate();

                    end = System.nanoTime();

                    System.out.format("%nIt took %dns to do the sieve and output results.%n", end-start);
                    System.out.format("Time in seconds: %f seconds%n", (double)(end-start) / 1_000_000_000);
                })
                .build();
    }
}
