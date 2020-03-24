import java.util.ArrayList;

public class IsolatedBoundedBuffer {
    ArrayList<String> buffer = new ArrayList<>(10);

    public synchronized void take(int consumerNumber, boolean producersDone) {
        try {
            //Consumers are just finishing the job
            if(buffer.size() == 0 && producersDone) {
                return;
            }

            while(buffer.size() == 0) {
                wait();
            }
            buffer.remove(0);
            notifyAll();
            System.out.println("Consumer" + consumerNumber + " consumed an item...");
        } catch (InterruptedException e) {

        }
    }

    public synchronized void put(int producerNumber, String item) {
        try {
            while(buffer.size() == 10) {
                wait();
            }
            buffer.add(item);
            System.out.println("Producer" + producerNumber + " added an Item to the buffer...");
            notifyAll();
        }catch(Exception e) {

        }
    }
}
