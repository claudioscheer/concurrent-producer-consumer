package main;

import interfaces.GenericListInterface;
import lists.FineList;
import lists.LazyList;
import lists.OptimisticList;
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
        int x3 = 2;

        GenericListInterface<Integer> lazyList = new LazyList<Integer>();
        lazyList.add(x1);
        lazyList.add(x2);
        lazyList.add(x3);
        lazyList.remove(x3);

        GenericListInterface<Integer> optimisticList = new OptimisticList<Integer>();
        optimisticList.add(x1);
        optimisticList.add(x2);
        optimisticList.add(x3);

        System.out.println(lazyList.size());
        System.out.println(optimisticList.size());
    }

    public static void main(String[] args) throws InterruptedException {
        // testRandomNumbersProbabilities();
        testListSize();
    }
}
