/*
 * @author Claudio Scheer
 */
package main;

import interfaces.GenericListInterface;
import lists.LockFreeList;
import utils.RandomNumbers;

public class TestGuided {

    private static void testRandomNumbersProbabilities() {
        int iterations = 300000;
        int[] count = new int[4];
        for (int i = 0; i < iterations; i++) {
            int p = RandomNumbers.getRandomOperationIndex();
            count[p] += 1;
        }

        for (int i = 0; i < count.length; i++) {
            System.out.println(String.format("%s - %s", count[i], (count[i] / Double.valueOf(iterations)) * 100));
        }
    }

    private static void testListSize() throws InterruptedException {
        int x1 = 1;
        int x2 = 2;
        int x3 = 3;

        GenericListInterface<Integer> lockFreeList = new LockFreeList<Integer>();
        lockFreeList.add(x1);
        lockFreeList.add(x2);
        lockFreeList.add(x3);
        lockFreeList.remove(x3);

        System.out.println(lockFreeList.size());
    }

    public static void main(String[] args) throws InterruptedException {
        // testRandomNumbersProbabilities();
        testListSize();
    }
}
