import java.util.concurrent.BlockingQueue;

public class RankingThread extends Thread {
    public BlockingQueue<ElectedOfficial> officialsList;
    public ElectedOfficial leader;

    private int currentOfficials = 0;
    private int maxOfficials;

    RankingThread(BlockingQueue<ElectedOfficial> officialsList, int maxOfficials) {
        this.officialsList = officialsList;
        this.maxOfficials = maxOfficials;
    }

    @Override
    public void run() {
        while(currentOfficials < maxOfficials) {
            checkForNewOfficials();
        }
    }

    public synchronized void checkForNewOfficials() {
        if (Thread.interrupted()) {
            System.out.println("A new official has come into play!");
            currentOfficials++;
            for (ElectedOfficial eo : officialsList) {
                if (leader == null) {
                    leader = eo;
                } else {
                    if (leader.rank < eo.rank) {
                        System.out.println("Leader has changed from " + leader.rank + " to " + eo.rank);
                        leader = eo;
                        notifyAll();
                    }
                }
            }
        }
    }
}

