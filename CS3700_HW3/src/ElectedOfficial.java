import java.util.concurrent.ThreadLocalRandom;

public class ElectedOfficial extends Thread {
    private String name;
    private String leader;
    public int rank;
    private final RankingThread rankingThread;

    ElectedOfficial(String name, RankingThread rankingThread) {
        this.name = name;
        this.rank = ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.leader = name;
        this.rankingThread = rankingThread;

        System.out.format("%s has been created with a rank of %d and I think I am the leader!%n", name, rank);
    }

    @Override
    public void run() {
        try {
            rankingThread.officialsList.put(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        rankingThread.interrupt();
    }
}
