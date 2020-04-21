package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import enums.ListOperationType;
import interfaces.GenericListInterface;
import lists.FineList;
import operators.IntegerListOperator;
import utils.RandomNumbers;

public class Test {

    private static List<IntegerListOperator> getAllThreads(int numberThreads, GenericListInterface<Integer> list) {
        List<IntegerListOperator> threads = new ArrayList<>();
        for (int i = 0; i < numberThreads; i++) {
            threads.add(new IntegerListOperator(list));
        }
        return threads;
    }

    private static int[] getOperationsCountSum(List<IntegerListOperator> threads) {
        int[] count = new int[ListOperationType.values().length];
        for (IntegerListOperator thread : threads) {
            for (int i = 0; i < count.length; i++) {
                count[i] += thread.getOperationsCount()[i];
            }
        }
        return count;
    }

    public static void main(String[] args) throws InterruptedException {
        int listInitialSize = Integer.parseInt(args[0]);
        int listCapacity = Integer.parseInt(args[1]);
        int numberThreads = Integer.parseInt(args[2]);

        RandomNumbers.MAX = listCapacity;

        System.out.println();
        System.out.println(String.format("Initial list size: %s", listInitialSize));
        System.out.println(String.format("List capacity: %s", listCapacity));
        System.out.println(String.format("Number of threads: %s", numberThreads));
        System.out.println();
        System.out.println("Starting test...");

        // It is necessary to limit the size of the list.
        GenericListInterface<Integer> list = new FineList<Integer>(listCapacity);
        for (int i = 1; i <= listInitialSize; i++) {
            list.add(i);
        }

        System.out.println(String.format("List size after initialization: %s", list.size()));

        System.out.println("Starting producers and consumers...");
        List<IntegerListOperator> threads = getAllThreads(numberThreads, list);
        for (IntegerListOperator thread : threads) {
            thread.start();
        }

        // Use a period of warm-up. During this period, operations are not counted.
        System.out.println("Warming-up...");
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
        }
        System.out.println(String.format("List size after warm-up: %s", list.size()));

        IntegerListOperator.warmingUp = false;

        long startTime = System.nanoTime();
        try {
            System.out.println("Producing and consuming...");
            TimeUnit.SECONDS.sleep(60);
        } catch (InterruptedException e) {
        }

        System.out.println("Interrupting threads...");
        for (IntegerListOperator thread : threads) {
            try {
                thread.interrupt();
                thread.join();
            } catch (InterruptedException e) {
            }
        }
        long endTime = System.nanoTime();
        double durationSeconds = (endTime - startTime) / 1_000_000_000d;

        List<Integer> allListSizes = new ArrayList<>();
        for (IntegerListOperator thread : threads) {
            allListSizes.addAll(thread.getListSizes());
        }

        int currentMonitorSize = list.size();
        int[] operationsCount = getOperationsCountSum(threads);
        int operationsCountSum = Arrays.stream(operationsCount).sum();
        double averageListSizes = allListSizes.stream().mapToInt(x -> x).average().getAsDouble();
        double operationsThroughput = (operationsCountSum / durationSeconds);
        double addOperationsThroughput = (operationsCount[ListOperationType.add.ordinal()] / durationSeconds);
        double removeOperationsThroughput = (operationsCount[ListOperationType.remove.ordinal()] / durationSeconds);
        double containsOperationsThroughput = (operationsCount[ListOperationType.contains.ordinal()] / durationSeconds);
        double listSizeOperationsThroughput = (operationsCount[ListOperationType.listSize.ordinal()] / durationSeconds);

        System.out.println();
        System.out.println("--------------------------------------------------");
        System.out.println();

        System.out.println(String.format("Duration in seconds: %s", durationSeconds));
        System.out.println(String.format("Current list size: %s", currentMonitorSize));
        System.out.println(String.format("Average list size: %s", averageListSizes));
        System.out.println();
        System.out.println(String.format("Total of operations: %s", operationsCountSum));
        System.out.println(String.format("Operations throughput per second: %s", operationsThroughput));
        System.out.println();
        System.out.println(
                String.format("Total of add operations: %s", operationsCount[ListOperationType.add.ordinal()]));
        System.out.println(String.format("add operations throughput per second: %s", addOperationsThroughput));
        System.out.println();
        System.out.println(
                String.format("Total of remove operations: %s", operationsCount[ListOperationType.remove.ordinal()]));
        System.out.println(String.format("remove operations throughput per second: %s", removeOperationsThroughput));
        System.out.println();
        System.out.println(String.format("Total of contains operations: %s",
                operationsCount[ListOperationType.contains.ordinal()]));
        System.out
                .println(String.format("contains operations throughput per second: %s", containsOperationsThroughput));
        System.out.println();
        System.out.println(String.format("Total of listSize operations: %s",
                operationsCount[ListOperationType.listSize.ordinal()]));
        System.out
                .println(String.format("listSize operations throughput per second: %s", listSizeOperationsThroughput));
        System.out.println();
        System.out.println("Finished.");
    }
}