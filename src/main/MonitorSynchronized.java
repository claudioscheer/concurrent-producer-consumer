package main;

/*
    This monitor implementation follows the book The Art of Multiprocessor Programming, Figure 8.5 (section 8.2.2, chapter 8), and section A.2.2 (Apendix A).
*/
public class MonitorSynchronized<T> {
    private final T[] items;
    private int tail, head, count = 0;

    @SuppressWarnings("unchecked")
    public MonitorSynchronized(int maxCapacity) {
        items = (T[]) new Object[maxCapacity];
    }

    public int getCurrentSize() {
        return count;
    }

    public synchronized void enq(T x) throws InterruptedException {
        // Wait while the queue is full.
        while (count == items.length) {
            wait();
        }
        // If the queue is not full, put the item in the tail position.
        items[tail] = x;
        /*
         * It is a circular list. If tail is at the end on the list, it will need to go
         * to the first position in the list.
         */
        if (++tail == items.length) {
            tail = 0;
        }
        ++count;
        // Notifies that the queue is not empty.
        notifyAll();
    }

    public synchronized T deq() throws InterruptedException {
        // Wait while the queue is empty.
        while (count == 0) {
            wait();
        }
        // If the list is not empty, get the item in the head position.
        T x = items[head];
        /*
         * It is a circular list. If head is at the end on the list, it will need to go
         * to the first position in the list.
         */
        if (++head == items.length) {
            head = 0;
        }
        --count;
        // Notifies that the queue is not full.
        notifyAll();
        return x;
    }
}