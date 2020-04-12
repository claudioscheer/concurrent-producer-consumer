import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import main.Consumer;
import main.Monitor;
import main.Producer;
import main.ProducerConsumer;
import enums.ProducerConsumerType;

public class Test {

    private static List<ProducerConsumer> getAllProducersConsumers(int numberProducers, int numberConsumers,
            Monitor<Double> monitor) {
        List<ProducerConsumer> producersConsumers = new ArrayList<>();
        for (int i = 0; i < numberConsumers; i++) {
            producersConsumers.add(new Consumer(monitor));
        }
        for (int i = 0; i < numberProducers; i++) {
            producersConsumers.add(new Producer(monitor));
        }
        return producersConsumers;
    }

    private static int getOperationsCountSum(List<ProducerConsumer> producerConsumers, ProducerConsumerType type) {
        int sum = 0;
        for (ProducerConsumer producerConsumer : producerConsumers) {
            if (producerConsumer.getType() == type) {
                sum += producerConsumer.getOperationsCount();
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        int listInitialSize = Integer.parseInt(args[0]);
        int listMaximumSize = Integer.parseInt(args[1]);
        boolean warmUp = args[2].equals("1");
        int producers = Integer.parseInt(args[3]);
        int consumers = Integer.parseInt(args[4]);

        System.out.println();
        System.out.println(String.format("Initial list size: %s", listInitialSize));
        System.out.println(String.format("Maximum list size: %s", listMaximumSize));
        System.out.println(String.format("Use warm-up: %s", warmUp));
        System.out.println(String.format("Number of producers: %s", producers));
        System.out.println(String.format("Number of consumers: %s", consumers));
        System.out.println();
        System.out.println("Starting test...");

        // Start the monitor.
        Monitor<Double> monitor = new Monitor<Double>(listMaximumSize);
        for (int i = 0; i < listInitialSize; i++) {
            try {
                monitor.enq(Math.random());
            } catch (InterruptedException e) {
            }
        }

        ProducerConsumer.countingOperations = !warmUp;

        System.out.println("Starting producers and consumers...");
        List<ProducerConsumer> producersConsumers = getAllProducersConsumers(producers, consumers, monitor);
        for (ProducerConsumer producerConsumer : producersConsumers) {
            producerConsumer.startThread();
        }

        // Use a period of warm-up. During this period, operations are not counted.
        if (warmUp) {
            System.out.println("Warming-up...");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
            }
            ProducerConsumer.countingOperations = true;
        }

        try {
            System.out.println("Producing and consuming...");
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
        }

        System.out.println("Interrupting threads...");
        System.out.println();
        for (ProducerConsumer producerConsumer : producersConsumers) {
            try {
                producerConsumer.interrupt();
                producerConsumer.join();
            } catch (InterruptedException e) {
            }
        }

        int currentMonitorSize = monitor.getCurrentSize();
        int enqOperationsCount = getOperationsCountSum(producersConsumers, ProducerConsumerType.producer);
        int deqOperationsCount = getOperationsCountSum(producersConsumers, ProducerConsumerType.consumer);

        System.out.println(String.format("Current monitor size: %s", currentMonitorSize));
        System.out.println(String.format("Number of enqueues: %s", enqOperationsCount));
        System.out.println(String.format("Number of dequeues: %s", deqOperationsCount));

        System.out.println("Finished.");
    }
}
