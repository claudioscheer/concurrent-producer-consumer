package operators;

import enums.ListOperationType;
import utils.RandomNumbers;

public abstract class GenericOperator extends Thread {

    protected abstract boolean operateAdd() throws InterruptedException;

    protected abstract boolean operateRemove() throws InterruptedException;

    protected abstract boolean operateContains();

    public volatile static boolean warmingUp = true;
    private int[] operationsCount;

    public GenericOperator() {
        this.operationsCount = new int[ListOperationType.values().length];
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
                return;
        }
        if (!GenericOperator.warmingUp) {
            operationsCount[operation.ordinal()] += 1;
        }
    }

    protected ListOperationType getRandomOperation() {
        // Generate random according to a distribuition frequency.
        int index = RandomNumbers.getRandomOperationIndex();
        return ListOperationType.values()[index];
    }

    public int[] getOperationsCount() {
        return operationsCount;
    }
}