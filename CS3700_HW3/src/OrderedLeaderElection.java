import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class OrderedLeaderElection {
    public static void main(String[] args) {
        int N = 5;

        BlockingQueue<ElectedOfficial> officials = new ArrayBlockingQueue<>(N);
        ElectedOfficial[] electedOfficialThreads = new ElectedOfficial[N];

        RankingThread rankingThread = new RankingThread(officials, N);

        rankingThread.start();

        synchronized (rankingThread.lock) {
            for (int i = 0; i < N; i++) {
                electedOfficialThreads[i] = new ElectedOfficial("Official" + (i + 1), rankingThread);
                electedOfficialThreads[i].start();
                try {
                    rankingThread.lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            rankingThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        officials.forEach(Thread::interrupt);

        System.out.format("The results are in the winning official is....%n%s with a rank of %d%n", rankingThread.leader.name,rankingThread.leader.rank);

        //System.exit(0);
    }
}
