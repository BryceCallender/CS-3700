import java.util.concurrent.ThreadLocalRandom;

public class ElectedOfficial extends Thread {
    public String name;
    public String leader;
    public int rank;

    private final RankingThread rankingThread;

    ElectedOfficial(String name, RankingThread rankingThread) {
        this.name = name;
        this.rank = ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.leader = name;
        this.rankingThread = rankingThread;

        System.out.format("%s has been created with a rank of %d and I think I am the leader!%n", name, rank);

        //Add new object to the list and interrupt to signal a new creation
        rankingThread.officialsList.add(this);
        rankingThread.interrupt();
    }

    @Override
    public void run() {
        //Run forever essentially will be killed when the N officials have all been made and notify is all done
        while(true) {
            //Notify lock is used to make sure all the notifies are received
            synchronized (rankingThread.notifyLock) {
                try {
                    //Wait on a signal (notifyAll caused)
                    rankingThread.notifyLock.wait();
                } catch (InterruptedException e) {
                    return;
                }

                //Print out the new leader to the console
                if(!leader.equals(rankingThread.leader.name)) {
                    System.out.format("Thread %s: %s was who I thought was leading the election, but it is really %s now!%n", name, leader, rankingThread.leader.name);
                }
            }
        }
    }
}
