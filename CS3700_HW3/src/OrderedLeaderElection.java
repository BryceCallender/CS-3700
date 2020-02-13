import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class OrderedLeaderElection {
    public static void main(String[] args) {
        int N = 5;

        BlockingQueue<ElectedOfficial> officials = new ArrayBlockingQueue<>(N);

        RankingThread rankingThread = new RankingThread(officials);

        rankingThread.start();

        for(int i = 0; i <= N; i++) {
            new ElectedOfficial("Official" + i, rankingThread).start();
        }
    }
}
