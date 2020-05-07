/*
 * CoarseList.java
 *
 * Created on January 3, 2006, 5:02 PM
 * Updated on April 14, 2020, 1:13 PM, by Claudio Scheer
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures", by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */
package lists;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import interfaces.GenericListInterface;

/**
 * List using coarse-grained synchronization.
 * 
 * @param T Item type.
 * @author Maurice Herlihy
 */
public class CoarseList<T> implements GenericListInterface<T> {

  // First list Node.
  private Node head;
  // Last list Node.
  private Node tail;
  // Synchronizes access to list.
  private Lock lock = new ReentrantLock();

  public CoarseList() {
    // Add sentinels to start and end.
    this.head = new Node(Integer.MIN_VALUE);
    this.tail = new Node(Integer.MAX_VALUE);
    head.next = this.tail;
  }

  /**
   * Add an element.
   * 
   * @param item Element to add.
   * @return True iff element was not there already.
   */
  @Override
  public boolean add(T item) {
    Node pred, curr;
    int key = item.hashCode();
    lock.lock();
    try {
      pred = head;
      curr = pred.next;
      while (curr.key < key) {
        pred = curr;
        curr = curr.next;
      }
      if (key == curr.key) {
        return false;
      } else {
        Node node = new Node(item);
        node.next = curr;
        pred.next = node;
        return true;
      }
    } finally {
      lock.unlock();
    }
  }

  /**
   * Remove an element.
   * 
   * @param item Element to remove.
   * @return True iff element was present.
   */
  @Override
  public boolean remove(T item) {
    Node pred, curr;
    int key = item.hashCode();
    lock.lock();
    try {
      pred = this.head;
      curr = pred.next;
      while (curr.key < key) {
        pred = curr;
        curr = curr.next;
      }
      if (key == curr.key) { // present
        pred.next = curr.next;
        return true;
      } else {
        return false; // not present
      }
    } finally { // always unlock
      lock.unlock();
    }
  }

  /**
   * Test whether element is present.
   * 
   * @param item Element to test.
   * @return True iff element is present.
   */
  @Override
  public boolean contains(T item) {
    Node pred, curr;
    int key = item.hashCode();
    lock.lock();
    try {
      pred = head;
      curr = pred.next;
      while (curr.key < key) {
        pred = curr;
        curr = curr.next;
      }
      return (key == curr.key);
    } finally { // always unlock
      lock.unlock();
    }
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

  private class Node {

    // Actual item.
    T item;
    // Item's hash code.
    int key;
    // Next Node in list.
    Node next;

    /**
     * Constructor for usual Node.
     * 
     * @param item Element in list.
     */
    Node(T item) {
      this.item = item;
      this.key = item.hashCode();
    }

    /**
     * Constructor for sentinel Node.
     * 
     * @param key Should be min or max int value.
     */
    Node(int key) {
      this.item = null;
      this.key = key;
    }
  }
}
