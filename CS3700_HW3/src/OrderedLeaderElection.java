import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class OrderedLeaderElection {
    public static void main(String[] args) {
        Random random = new Random();
        int N = random.nextInt(10) + 1;

        System.out.println("There will be " + N + " officials made for this program");

        //Stores the officials being made
        BlockingQueue<ElectedOfficial> officials = new ArrayBlockingQueue<>(N);
        ElectedOfficial[] electedOfficialThreads = new ElectedOfficial[N];

        //Made and start the thread responsible for listening to the new threads being made
        RankingThread rankingThread = new RankingThread(officials, N);
        rankingThread.start();

        //So this is going to synchronize a lock on the ranking thread that
        //has to deal with the checking of a official if they have surpassed anyone
        synchronized (rankingThread.lock) {
            for (int i = 0; i < N; i++) {
                electedOfficialThreads[i] = new ElectedOfficial("Official" + (i + 1), rankingThread);
                electedOfficialThreads[i].start();
                try {
                    //wait on the lock so we know that the ranking thread so we know that when it is notified
                    //it will be done checking!
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
