package lists;

import java.util.concurrent.ThreadLocalRandom;

import enums.ListOperationType;

public abstract class GenericOperator extends Thread {

    protected abstract boolean operateAdd();

    protected abstract boolean operateRemove();

    protected abstract boolean operateContains();

    public volatile static boolean warmingUp = true;
    private int[] operationsCount;

    public GenericOperator() {
        this.operationsCount = new int[ListOperationType.values().length];
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            ListOperationType operation = this.getRandomOperation();
            this.operate(operation);
        }
    }

    public void operate(ListOperationType operation) {
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
        int index = ThreadLocalRandom.current().nextInt(0, 2);
        return ListOperationType.values()[index];
    }

    public int[] getOperationsCount() {
        return operationsCount;
    }
}