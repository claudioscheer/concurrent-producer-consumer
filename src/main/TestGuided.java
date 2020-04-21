package main;

import interfaces.GenericListInterface;
import lists.FineList;
import operators.IntegerListOperator;
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

    private static void testListCapacity() throws InterruptedException {
        IntegerListOperator.WARMING_UP = false;
        GenericListInterface<Double> list = new FineList<Double>(1);
        double a = Math.random();
        list.add(a);
        // list.remove(a);
        list.add(Math.random());

        int currentMonitorSize = list.size();
        System.out.println(String.format("Current monitor size: %s", currentMonitorSize));
    }

    public static void main(String[] args) throws InterruptedException {
        testRandomNumbersProbabilities();
        testListCapacity();
    }
}
