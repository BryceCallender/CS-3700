import java.util.concurrent.BlockingQueue;

public class RankingThread extends Thread {
    public BlockingQueue<ElectedOfficial> officialsList;
    public ElectedOfficial leader;

    RankingThread(BlockingQueue<ElectedOfficial> officialsList) {
        this.officialsList = officialsList;
    }

    @Override
    public void run() {
        while(true) {
            if (Thread.interrupted()) {
                for (ElectedOfficial eo : officialsList) {
                    if (leader == null) {
                        leader = eo;
                    } else {
                        if (leader.rank < eo.rank) {
                            System.out.println("Leader has changed from" + leader.rank + " to " + eo.rank);
                            leader = eo;
                            //notifyAll();
                        }
                    }
                }

                System.out.println("a new official has been made");
            }
        }
    }
}

