/*
 * LockFreeList.java
 *
 * Created on January 4, 2006, 2:41 PM
 * Updated on May 19, 2020, 3:36 PM, by Claudio Scheer
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures", by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */
package lists;

import java.util.concurrent.atomic.AtomicMarkableReference;

import interfaces.GenericListInterface;

/**
 * Lock-free List based on M. Michael's algorithm.
 * 
 * @param T Item type.
 * @author Maurice Herlihy
 */
public class LockFreeList<T> implements GenericListInterface<T> {

  // First list node.
  private Node head;

  public LockFreeList() {
    this.head = new Node(Integer.MIN_VALUE);
    Node tail = new Node(Integer.MAX_VALUE);
    while (!head.next.compareAndSet(null, tail, false, false)) {
    }
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
      // Find predecessor and curren entries.
      Window window = find(this.head, key);
      Node pred = window.pred, curr = window.curr;
      // Is the key present?
      if (curr.key == key) {
        return false;
      } else {
        // Splice in new node.
        Node node = new Node(item);
        node.next = new AtomicMarkableReference(curr, false);
        if (pred.next.compareAndSet(curr, node, false, false)) {
          return true;
        }
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
    boolean snip;
    while (true) {
      // Find predecessor and curren entries.
      Window window = find(this.head, key);
      Node pred = window.pred, curr = window.curr;
      // Is the key present?
      if (curr.key != key) {
        return false;
      } else {
        // Snip out matching node.
        Node succ = curr.next.getReference();
        snip = curr.next.attemptMark(succ, true);
        if (!snip) {
          continue;
        }
        pred.next.compareAndSet(curr, succ, false, false);
        return true;
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
    // Find predecessor and curren entries.
    Window window = find(this.head, key);
    Node curr = window.curr;
    return (curr.key == key);
  }

  @Override
  public int size() {
    Node pred = this.head;
    Node curr = pred.next.getReference();
    int count = 0;
    while (curr.item != null) {
      ++count;
      pred = curr;
      curr = curr.next.getReference();
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
    AtomicMarkableReference<Node> next;

    /**
     * Constructor for usual Node.
     * 
     * @param item Element in list.
     */
    Node(T item) {
      this.item = item;
      this.key = item.hashCode();
      this.next = new AtomicMarkableReference<Node>(null, false);
    }

    /**
     * Constructor for sentinel Node.
     * 
     * @param key Should be min or max int value.
     */
    Node(int key) {
      this.item = null;
      this.key = key;
      this.next = new AtomicMarkableReference<Node>(null, false);
    }
  }

  /**
   * Pair of adjacent list entries.
   */
  class Window {
    // Earlier node.
    public Node pred;
    // Later node.
    public Node curr;

    Window(Node pred, Node curr) {
      this.pred = pred;
      this.curr = curr;
    }
  }

  /**
   * If element is present, returns Node and predecessor. If absent, returns Node
   * with least larger key.
   * 
   * @param head Start of list.
   * @param key  Key to search for.
   * @return If element is present, returns Node and predecessor. If absent,
   *         returns Node with least larger key.
   */
  public Window find(Node head, int key) {
    Node pred = null, curr = null, succ = null;
    // Is curr marked?
    boolean[] marked = { false };
    boolean snip;
    retry: while (true) {
      pred = head;
      curr = pred.next.getReference();
      while (true) {
        succ = curr.next.get(marked);
        // Replace curr if marked.
        while (marked[0]) {
          snip = pred.next.compareAndSet(curr, succ, false, false);
          if (!snip) {
            continue retry;
          }
          curr = pred.next.getReference();
          succ = curr.next.get(marked);
        }
        if (curr.key >= key) {
          return new Window(pred, curr);
        }
        pred = curr;
        curr = succ;
      }
    }
  }
}
