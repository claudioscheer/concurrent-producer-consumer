package main;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
    This monitor implementation follows the book The Art of Multiprocessor Programming, Figure 8.5 (section 8.2.2, chapter 8), and section A.2.2 (Apendix A).
*/
public class Monitor<T> {
    // Only one process can enqueue/dequeue at a time.
    private final Lock lock = new ReentrantLock();
    // Wait while the queue is full.
    private final Condition notFull = lock.newCondition();
    // Wait while the queue is empty.
    private final Condition notEmpty = lock.newCondition();
    private final T[] items;
    private int tail, head, count = 0;

    @SuppressWarnings("unchecked")
    public Monitor(int maxCapacity) {
        items = (T[]) new Object[maxCapacity];
    }

    public int getCurrentSize() {
        return count;
    }

    public void enq(T x) throws InterruptedException {
        lock.lock();
        try {
            // Wait while the queue is full.
            while (count == items.length) {
                notFull.await();
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
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public T deq() throws InterruptedException {
        lock.lock();
        try {
            // Wait while the queue is empty.
            while (count == 0) {
                notEmpty.await();
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
            notFull.signal();
            return x;
        } finally {
            lock.unlock();
        }
    }
}