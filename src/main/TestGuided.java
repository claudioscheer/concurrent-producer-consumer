package main;

import interfaces.GenericListInterface;
import lists.CoarseList;
import operators.IntegerListOperator;

public class TestGuided {

    public static void main(String[] args) throws InterruptedException {
        IntegerListOperator.warmingUp = false;
        GenericListInterface<Double> list = new CoarseList<Double>(1);
        double a = Math.random();
        list.add(a);
        list.remove(a);
        list.add(Math.random());

        int currentMonitorSize = list.size();

        System.out.println(String.format("Current monitor size: %s", currentMonitorSize));
    }
}
