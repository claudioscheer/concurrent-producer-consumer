package main;

import enums.ProducerConsumerType;

public abstract class ProducerConsumer extends Thread {

    public abstract void operate() throws InterruptedException;

    public abstract ProducerConsumerType getType();

    public volatile static boolean countingOperations = true;
    protected final Monitor<Double> monitor;
    private int operationsCount = 0;

    public ProducerConsumer(Monitor<Double> monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            try {
                this.operate();
                if (ProducerConsumer.countingOperations) {
                    ++operationsCount;
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void startThread() {
        this.start();
    }

    public int getOperationsCount() {
        return operationsCount;
    }
}