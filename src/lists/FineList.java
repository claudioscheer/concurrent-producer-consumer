/*
 * FineList.java
 *
 * Created on January 3, 2006, 6:50 PM
 * Updated on April 20, 2020, 9:02 PM, by Claudio Scheer
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures", by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */
package lists;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import interfaces.GenericListInterface;

/**
 * Fine-grained synchronization: lock coupling (hand-over-hand locking).
 * 
 * @param T Item type.
 * @author Maurice Herlihy
 */
public class FineList<T> implements GenericListInterface<T> {

  // First list entry.
  private Node head;

  public FineList() {
    // Add sentinels to start and end.
    head = new Node(Integer.MIN_VALUE);
    head.next = new Node(Integer.MAX_VALUE);
  }

  /**
   * Add an element.
   * 
   * @param item Element to add.
   * @return True iff element was not there already.
   * @throws InterruptedException
   */
  public boolean add(T item) throws InterruptedException {
    int key = item.hashCode();
    head.lock();
    Node pred = head;
    try {
      Node curr = pred.next;
      curr.lock();
      try {
        while (curr.key < key) {
          pred.unlock();
          pred = curr;
          curr = curr.next;
          curr.lock();
        }
        if (curr.key == key) {
          return false;
        }
        Node newNode = new Node(item);
        newNode.next = curr;
        pred.next = newNode;
        return true;
      } finally {
        curr.unlock();
      }
    } finally {
      pred.unlock();
    }
  }

  /**
   * Remove an element.
   * 
   * @param item Element to remove.
   * @return True iff element was present.
   * @throws InterruptedException
   */
  public boolean remove(T item) throws InterruptedException {
    Node pred = null, curr = null;
    int key = item.hashCode();
    head.lock();
    try {
      pred = head;
      curr = pred.next;
      curr.lock();
      try {
        while (curr.key < key) {
          pred.unlock();
          pred = curr;
          curr = curr.next;
          curr.lock();
        }
        if (curr.key == key) {
          pred.next = curr.next;
          return true;
        }
        return false;
      } finally {
        curr.unlock();
      }
    } finally {
      pred.unlock();
    }
  }

  public boolean contains(T item) {
    Node pred = null, curr = null;
    int key = item.hashCode();
    head.lock();
    try {
      pred = head;
      curr = pred.next;
      curr.lock();
      try {
        while (curr.key < key) {
          pred.unlock();
          pred = curr;
          curr = curr.next;
          curr.lock();
        }
        return (curr.key == key);
      } finally {
        curr.unlock();
      }
    } finally {
      pred.unlock();
    }
  }

  @Override
  public int size() {
    Node pred = head;
    Node curr = pred.next;
    int count = 0;
    while (curr.item != null) {
      ++count;
      pred = curr;
      curr = curr.next;
    }
    return count;
  }

  private class Node {

    // Actual item.
    T item;
    // Item's hash code.
    int key;
    // Next Node in list.
    Node next;
    // Synchronizes individual Node.
    Lock lock;

    /**
     * Constructor for usual Node.
     * 
     * @param item Element in list.
     */
    Node(T item) {
      this.item = item;
      this.key = item.hashCode();
      this.lock = new ReentrantLock();
    }

    /**
     * Constructor for sentinel Node.
     * 
     * @param key Should be min or max int value.
     */
    Node(int key) {
      this.item = null;
      this.key = key;
      this.lock = new ReentrantLock();
    }

    /**
     * Lock Node.
     */
    void lock() {
      lock.lock();
    }

    /**
     * Unlock Node.
     */
    void unlock() {
      lock.unlock();
    }
  }
}
