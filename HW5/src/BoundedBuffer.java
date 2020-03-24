import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedBuffer {
    ArrayList<String> buffer = new ArrayList<>(10);
    Lock lock = new ReentrantLock();
    Condition bufferNotFull = lock.newCondition();
    Condition bufferNotEmpty = lock.newCondition();


    public void take(int consumerNumber, boolean producersDone) {
        try {
            lock.lock();

            //Consumers are just finishing the job
            if(buffer.size() == 0 && producersDone) {
                return;
            }

            while(buffer.size() == 0) {
                bufferNotFull.await();
            }
            buffer.remove(0);
            bufferNotEmpty.signalAll();
            System.out.println("Consumer" + consumerNumber + " consumed an item...");
        } catch (InterruptedException e) {

        } finally {
            lock.unlock();
        }
    }

    public void put(int producerNumber, String item) {
        try {
            lock.lock();
            while(buffer.size() == 10) {
                bufferNotEmpty.await();
            }
            buffer.add(item);
            System.out.println("Producer" + producerNumber + " added an Item to the buffer...");
            bufferNotFull.signalAll();
        }catch(Exception e) {

        } finally {
            lock.unlock();
        }
    }
}
