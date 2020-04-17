package main;

import interfaces.GenericListInterface;
import lists.CoarseList;
import lists.IntegerListOperator;

public class TestGuided {

    public static void main(String[] args) {
        IntegerListOperator.warmingUp = false;
        GenericListInterface<Double> list = new CoarseList<Double>();
        list.add(Math.random());
        double a = Math.random();
        list.add(a);
        list.remove(a);

        int currentMonitorSize = list.size();

        System.out.println(String.format("Current monitor size: %s", currentMonitorSize));
    }
}
