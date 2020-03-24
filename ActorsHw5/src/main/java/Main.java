import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("ActorSystem");

        try {
            //ActorRef actor = actorSystem.actorOf(Props.create(SieveActor.class));
            ActorRef actor = actorSystem.actorOf(Props.create(BufferActor.class), "Buffer");
            actor.tell("Start", actor);
            //actor.tell(new SieveActor.PrimeContents(IntStream.rangeClosed(2, 1_000_000).boxed().collect(Collectors.toList())), actor);

            System.out.println("Press ENTER to exit the system");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            actorSystem.terminate();
        }

    }
}
