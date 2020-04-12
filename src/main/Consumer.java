package main;

import enums.ProducerConsumerType;

public class Consumer extends ProducerConsumer {

    public Consumer(Monitor<Double> monitor) {
        super(monitor);
    }

    @Override
    public void operate() throws InterruptedException {
        this.monitor.deq();
    }

    @Override
    public ProducerConsumerType getType() {
        return ProducerConsumerType.consumer;
    }
}