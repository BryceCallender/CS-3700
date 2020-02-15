import java.util.concurrent.BlockingQueue;

public class RankingThread extends Thread {
    public BlockingQueue<ElectedOfficial> officialsList;
    public ElectedOfficial leader;

    private int currentOfficials = 0;
    private int maxOfficials;

    public final Object lock = new Object();

    RankingThread(BlockingQueue<ElectedOfficial> officialsList, int maxOfficials) {
        this.officialsList = officialsList;
        this.maxOfficials = maxOfficials;
    }

    @Override
    public synchronized void run() {
        while(currentOfficials < maxOfficials) {
            try {
                wait();
            } catch (InterruptedException e) {
                checkForNewOfficials();
            }
        }
    }

    public synchronized void checkForNewOfficials() {
        System.out.println("A new official has come into play!");
        currentOfficials++;
        for (ElectedOfficial eo : officialsList) {
            if (leader == null) {
                leader = eo;
                System.out.println("Leader set to " + leader.rank + " since they are the first!");
            } else {
                if (leader.rank < eo.rank) {
                    System.out.println("Leader has changed from " + leader.rank + " to " + eo.rank);
                    leader = eo;
                    notifyAll();
                    System.out.println("Notified");
                }
            }
        }
        synchronized (lock) {
            lock.notify();
        }
    }
}

