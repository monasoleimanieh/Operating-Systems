class BoundedBuffer {
    private final int[] buffer;
    private final int BUFFER_SIZE;

    // Index for producer adding items
    private int index_add; 

    // Index for consumer removing items
    private int index_remove; 

    // Number of items in the buffer
    private int counter; 

    public BoundedBuffer(int bufferSize) {
        this.BUFFER_SIZE = bufferSize;
        this.buffer = new int[BUFFER_SIZE];
        this.index_add = 0;
        this.index_remove = 0;
        this.counter = 0;
    }

    public void produce(int item) throws InterruptedException {
        int register1;
        
        synchronized (this) {
            // Wait while buffer is full
            while (counter == BUFFER_SIZE) {
                wait();
            }
            
            // Update number of items in buffer
            register1 = counter + 1;
            counter = register1;
            buffer[index_add] = item;
            index_add = (index_add + 1) % BUFFER_SIZE;

            System.out.println("Producer: " + item);

            // Notify the consumer there is new data in buffer
            notifyAll(); 
        }
    }

    public int consume() throws InterruptedException {
        int item;
        int register2;
        
        synchronized (this) {
            // Wait while buffer is full
            while (counter == 0) {
                wait();
            }
            
            // Update number of items in buffer
            register2 = counter - 1;
            counter = register2;
            item = buffer[index_remove];
            index_remove = (index_remove + 1) % BUFFER_SIZE;

            System.out.println("Consumer: " + item);

            // Notify the producer there is space in buffer
            notifyAll();
        }
        return item;
    }
}

class Producer implements Runnable {
    private final BoundedBuffer buffer;

    public Producer(BoundedBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        int i = 0;
        try {
            while (true) {
                buffer.produce(i++);
                // For time to produce
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Consumer implements Runnable {
    private final BoundedBuffer buffer;

    public Consumer(BoundedBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            while (true) {
                buffer.consume();
                // For time to consume
                Thread.sleep(1200);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        // Shared buffer size
        int bufferSize = 5;
        
        BoundedBuffer buffer = new BoundedBuffer(bufferSize);
        Producer producer = new Producer(buffer);
        Consumer consumer = new Consumer(buffer);

        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(consumer);

        producerThread.start();
        consumerThread.start();
    }
}
