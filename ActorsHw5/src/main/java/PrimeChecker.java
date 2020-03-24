import akka.actor.AbstractActor;

public class PrimeChecker extends AbstractActor {
    private int MAX;

    PrimeChecker(int max) {
        this.MAX = max;
    }

    static class NonPrime {
        final int number;

        NonPrime(int number) {
            this.number = number;
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Integer.class, num -> {
                    //System.out.println(num + " check");
                    if(num >= Math.sqrt(MAX)) {
                        getContext().getParent().tell(getSelf(), getSelf());
                        //System.out.println("Finished");
                        //getContext().getParent().tell("Finished", getSelf());
                    } else {
                        for (int i = num * num; i <= MAX; i += num) {
                            getContext().getParent().tell(new NonPrime(i), getSelf());
                        }
                        getContext().getParent().tell(getSelf(), getSelf());
                    }
                })
                .build();
    }
}
