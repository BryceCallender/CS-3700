import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class OrderedLeaderElection {
    public static void main(String[] args) {
        int N = 5;

        BlockingQueue<ElectedOfficial> officials = new ArrayBlockingQueue<>(N);
        ElectedOfficial[] electedOfficialThreads = new ElectedOfficial[N];

        RankingThread rankingThread = new RankingThread(officials, N);

        rankingThread.start();

        for(int i = 0; i < N; i++) {
            electedOfficialThreads[i] = new ElectedOfficial("Official" + (i + 1), rankingThread);
            electedOfficialThreads[i].start();
        }

        try {
            rankingThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        officials.forEach(Thread::interrupt);

        System.out.println("The results are in the winning official is....");
        System.out.println(rankingThread.leader.name + " with a rank of " + rankingThread.leader.rank);

        System.exit(0);
    }
}
