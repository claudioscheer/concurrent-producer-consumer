/*
 * LazyList.java
 *
 * Created on January 4, 2006, 1:41 PM
 * Updated on May 07, 2020, 8:05 PM, by Claudio Scheer
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures", by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */
package lists;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import interfaces.GenericListInterface;

/**
 * Lazy list implementation: lock-free contains method.
 * 
 * @param T Item type.
 * @author Maurice Herlihy
 */
public class LazyList<T> implements GenericListInterface<T> {

  // First list Node.
  private Node head;

  public LazyList() {
    // Add sentinels to start and end.
    this.head = new Node(Integer.MIN_VALUE);
    this.head.next = new Node(Integer.MAX_VALUE);
  }

  /**
   * Check that prev and curr are still in list and adjacent.
   */
  private boolean validate(Node pred, Node curr) {
    return !pred.marked && !curr.marked && pred.next == curr;
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
      Node pred = this.head;
      Node curr = head.next;
      while (curr.key < key) {
        pred = curr;
        curr = curr.next;
      }
      pred.lock();
      try {
        curr.lock();
        try {
          if (validate(pred, curr)) {
            if (curr.key == key) { // present
              return false;
            } else { // not present
              Node Node = new Node(item);
              Node.next = curr;
              pred.next = Node;
              return true;
            }
          }
        } finally { // always unlock
          curr.unlock();
        }
      } finally { // always unlock
        pred.unlock();
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
      Node pred = this.head;
      Node curr = head.next;
      while (curr.key < key) {
        pred = curr;
        curr = curr.next;
      }
      pred.lock();
      try {
        curr.lock();
        try {
          if (validate(pred, curr)) {
            if (curr.key != key) { // present
              return false;
            } else { // absent
              curr.marked = true; // logically remove
              pred.next = curr.next; // physically remove
              return true;
            }
          }
        } finally { // always unlock curr
          curr.unlock();
        }
      } finally { // always unlock pred
        pred.unlock();
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
    Node curr = this.head;
    while (curr.key < key) {
      curr = curr.next;
    }
    return curr.key == key && !curr.marked;
  }

  @Override
  public int size() {
    Node pred = this.head;
    Node curr = pred.next;
    int count = 0;
    while (curr.item != null) {
      ++count;
      pred = curr;
      curr = curr.next;
    }
    return count;
  }

  /**
   * List Node.
   */
  private class Node {

    // Actual item.
    T item;
    // Item's hash code.
    int key;
    // Next Node in list.
    Node next;
    // If true, Node is logically deleted.
    boolean marked;
    // Synchronizes Node.
    Lock lock;

    /**
     * Constructor for usual Node.
     * 
     * @param item Element in list.
     */
    Node(T item) {
      this.item = item;
      this.key = item.hashCode();
      this.next = null;
      this.marked = false;
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
      this.next = null;
      this.marked = false;
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
