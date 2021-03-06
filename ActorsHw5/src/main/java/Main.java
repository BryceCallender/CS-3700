import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        long start = System.nanoTime();
        ActorSystem actorSystem = ActorSystem.create("ActorSystem");

        try {
            //ActorRef actor = actorSystem.actorOf(Props.create(BufferActor.class));
            ActorRef actor = actorSystem.actorOf(Props.create(SieveActor.class, 1_000_000, start));
        } finally {
            //actorSystem.terminate();
        }

    }
}
