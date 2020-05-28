/*
 * @author Claudio Scheer
 */
package operators;

import java.util.ArrayList;
import java.util.List;

import enums.ListOperationType;
import utils.RandomNumbers;

public abstract class GenericOperator extends Thread {

    protected abstract boolean operateAdd() throws InterruptedException;

    protected abstract boolean operateRemove() throws InterruptedException;

    protected abstract boolean operateContains();

    protected abstract int operateListSize();

    public volatile static boolean WARMING_UP = true;
    private int[] operationsCount;
    private List<Integer> listSizes;

    public GenericOperator() {
        this.operationsCount = new int[ListOperationType.values().length];
        this.listSizes = new ArrayList<>();
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            try {
                ListOperationType operation = this.getRandomOperation();
                this.operate(operation);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void operate(ListOperationType operation) throws InterruptedException {
        switch (operation) {
            case add:
                this.operateAdd();
                break;
            case remove:
                this.operateRemove();
                break;
            case contains:
                this.operateContains();
                break;
            case listSize:
                if (!GenericOperator.WARMING_UP) {
                    // Add to the list if it is not warming-up.
                    this.listSizes.add(this.operateListSize());
                }
                break;
        }
        if (!GenericOperator.WARMING_UP) {
            operationsCount[operation.ordinal()] += 1;
        }
    }

    protected ListOperationType getRandomOperation() {
        int index = RandomNumbers.getRandomOperationIndex();
        return ListOperationType.values()[index];
    }

    public int[] getOperationsCount() {
        return operationsCount;
    }

    public List<Integer> getListSizes() {
        return listSizes;
    }
}