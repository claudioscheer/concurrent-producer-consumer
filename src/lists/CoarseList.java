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

import java.util.concurrent.locks.Condition;
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

  // List capacity.
  private int capacity;
  // First list Node.
  private Node head;
  // Last list Node.
  private Node tail;
  // Synchronizes access to list.
  private Lock lock = new ReentrantLock();
  // Wait while the queue is full.
  private final Condition notFull = lock.newCondition();
  // Wait while the queue is empty.
  private final Condition notEmpty = lock.newCondition();

  public CoarseList(int capacity) {
    // Add sentinels to start and end.
    this.head = new Node(Integer.MIN_VALUE);
    this.tail = new Node(Integer.MAX_VALUE);
    head.next = this.tail;
    this.capacity = capacity;
  }

  /**
   * Add an element.
   * 
   * @param item Element to add.
   * @return True iff element was not there already.
   * @throws InterruptedException
   */
  @Override
  public boolean add(T item) throws InterruptedException {
    Node pred, curr;
    int key = item.hashCode();
    lock.lock();
    try {
      // Wait while the list is full. Using .size() may not be a
      // good solution.
      while (this.size() == this.capacity) {
        notFull.await();
      }

      pred = this.head;
      curr = pred.next;
      while (curr.key < key) {
        pred = curr;
        curr = curr.next;
      }
      // By default, the element was already present.
      boolean response = false;
      if (key != curr.key) {
        // Add element.
        Node node = new Node(item);
        node.next = curr;
        pred.next = node;
        response = true;
      }
      // Notifies that the list is not empty.
      notEmpty.signal();
      return response;
    } finally {
      lock.unlock();
    }
  }

  /**
   * Remove an element.
   * 
   * @param item Element to remove.
   * @return True iff element was present.
   * @throws InterruptedException
   */
  @Override
  public boolean remove(T item) throws InterruptedException {
    Node pred, curr;
    int key = item.hashCode();
    lock.lock();
    try {
      // Wait while the list is empty.
      while (this.size() == 0) {
        notEmpty.await();
      }

      pred = this.head;
      curr = pred.next;
      while (curr.key < key) {
        pred = curr;
        curr = curr.next;
      }
      // By default, the element was not present.
      boolean response = false;
      if (key == curr.key) {
        // Element is present.
        pred.next = curr.next;
        response = true;
      }
      // Notifies that the list is not full.
      notFull.signal();
      return response;
    } finally {
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
      pred = this.head;
      curr = pred.next;
      while (curr.key < key) {
        pred = curr;
        curr = curr.next;
      }
      return (key == curr.key);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public int size() {
    Node pred, curr;
    lock.lock();
    try {
      int count = 0;
      pred = this.head;
      curr = pred.next;
      while (curr.item != null) {
        ++count;
        pred = curr;
        curr = curr.next;
      }
      return count;
    } finally {
      lock.unlock();
    }
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
