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

//        synchronized (this.rankingThread.lock) {
//            try {
                rankingThread.officialsList.add(this);
                rankingThread.interrupt();
//                this.rankingThread.lock.wait();
//            }catch(InterruptedException e) {
//
//            }
//        }
    }

    @Override
    public void run() {
        while(true) {
            synchronized (rankingThread) {
                try {
                    rankingThread.wait();
                } catch (InterruptedException e) {
                    return;
                }
                System.out.format("%s: %s was who I thought was leading the election, but it is really %s now!%n", name, leader, rankingThread.leader.name);
            }
        }
    }
}
