import java.util.List;
import java.util.concurrent.BlockingQueue;

public class RankingThread extends Thread {
    public BlockingQueue<ElectedOfficial> officalsList;
    private ElectedOfficial leader;

    RankingThread(BlockingQueue<ElectedOfficial> officalsList) {
        this.officalsList = officalsList;
    }

    @Override
    public void run() {
        while(true) {
            try {
                if(Thread.interrupted()) {
                    for (ElectedOfficial eo : officalsList) {
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
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
