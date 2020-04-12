package main;

import enums.ProducerConsumerType;

public class Producer extends ProducerConsumer {

    public Producer(Monitor<Double> monitor) {
        super(monitor);
    }

    @Override
    public void operate() throws InterruptedException {
        this.monitor.enq(Math.random());
    }

    @Override
    public ProducerConsumerType getType() {
        return ProducerConsumerType.producer;
    }
}