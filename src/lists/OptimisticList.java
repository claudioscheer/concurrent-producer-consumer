/*
 * OptimisticList.java
 *
 * Created on January 4, 2006, 1:49 PM
 * Updated on April 24, 2020, 4:28 PM, by Claudio Scheer
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures", by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */
package lists;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import interfaces.GenericListInterface;

/**
 * Optimistic List implementation.
 * 
 * @param T Item type.
 * @author Maurice Herlihy
 */
public class OptimisticList<T> implements GenericListInterface<T> {

  // List capacity.
  private int capacity;
  // First list entry.
  private Entry head;

  public OptimisticList(int capacity) {
    this.head = new Entry(Integer.MIN_VALUE);
    this.head.next = new Entry(Integer.MAX_VALUE);
    this.capacity = capacity;
  }

  /**
   * Add an element.
   * 
   * @param item Element to add.
   * @return True iff element was not there already.
   */
  public boolean add(T item) {
    int key = item.hashCode();
    while (true) {
      Entry pred = this.head;
      Entry curr = pred.next;
      while (curr.key <= key) {
        pred = curr;
        curr = curr.next;
      }
      pred.lock();
      curr.lock();
      try {
        if (this.validate(pred, curr)) {
          // By default, the element was already present.
          boolean response = false;
          if (key != curr.key) {
            // Add element.
            Entry entry = new Entry(item);
            entry.next = curr;
            pred.next = entry;
            response = true;
          }
          return response;
        }
      } finally {
        pred.unlock();
        curr.unlock();
      }
    }
  }

  /**
   * Remove an element.
   * 
   * @param item Element to remove.
   * @return True iff element was present.
   */
  public boolean remove(T item) {
    int key = item.hashCode();
    while (true) {
      Entry pred = this.head;
      Entry curr = pred.next;
      while (curr.key < key) {
        pred = curr;
        curr = curr.next;
      }
      pred.lock();
      curr.lock();
      try {
        if (this.validate(pred, curr)) {
          if (curr.key == key) {
            // Present in list.
            pred.next = curr.next;
            return true;
          } else {
            // Not present in list.
            return false;
          }
        }
      } finally {
        pred.unlock();
        curr.unlock();
      }
    }
  }

  /**
   * Test whether element is present.
   * 
   * @param item Element to test.
   * @return True iff element is present.
   */
  public boolean contains(T item) {
    int key = item.hashCode();
    while (true) {
      // Sentinel node.
      Entry pred = this.head;
      Entry curr = pred.next;
      while (curr.key < key) {
        pred = curr;
        curr = curr.next;
      }
      try {
        pred.lock();
        curr.lock();
        if (this.validate(pred, curr)) {
          return (curr.key == key);
        }
      } finally {
        pred.unlock();
        curr.unlock();
      }
    }
  }

  @Override
  public int size() {
    return 0;
  }

  /**
   * Check that prev and curr are still in list and adjacent.
   * 
   * @param pred Predecessor node.
   * @param curr Current node.
   * @return Whether predecessor and current have changed.
   */
  private boolean validate(Entry pred, Entry curr) {
    Entry entry = head;
    while (entry.key <= pred.key) {
      if (entry == pred) {
        return pred.next == curr;
      }
      entry = entry.next;
    }
    return false;
  }

  // List entry.
  private class Entry {

    // Actual item.
    T item;
    // Item's hash code.
    int key;
    // Next entry in list.
    Entry next;
    // Synchronizes entry.
    Lock lock;

    /**
     * Constructor for usual Entry.
     * 
     * @param item Element in list.
     */
    Entry(T item) {
      this.item = item;
      this.key = item.hashCode();
      lock = new ReentrantLock();
    }

    /**
     * Constructor for sentinel Entry.
     * 
     * @param key Should be min or max int value.
     */
    Entry(int key) {
      this.key = key;
      lock = new ReentrantLock();
    }

    /**
     * Lock Entry.
     */
    void lock() {
      lock.lock();
    }

    /**
     * Unlock Entry.
     */
    void unlock() {
      lock.unlock();
    }
  }
}
