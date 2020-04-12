import main.Monitor;
import main.MonitorSynchronized;

public class TestMonitor {

    public static void main(String[] args) {
        int maxSize = 100000000;
        int operations = 100000000;

        System.out.println("Monitor...");
        Monitor<Double> monitor = new Monitor<Double>(maxSize);
        long startTimeEnq = System.nanoTime();
        for (int i = 0; i < operations; i++) {
            try {
                monitor.enq(Math.random());
            } catch (InterruptedException e) {
            }
        }
        long endTimeEnq = System.nanoTime();
        double durationEnq = (endTimeEnq - startTimeEnq) / 1000000d;
        System.out.println(String.format("Enqueue: %s", durationEnq));
        long startTimeDeq = System.nanoTime();
        for (int i = 0; i < operations; i++) {
            try {
                Double d = monitor.deq();
            } catch (InterruptedException e) {
            }
        }
        long endTimeDeq = System.nanoTime();
        double durationDeq = (endTimeDeq - startTimeDeq) / 1000000d;
        System.out.println(String.format("Dequeue: %s", durationDeq));

        System.out.println();
        System.out.println("MonitorSynchronized...");
        MonitorSynchronized<Double> monitorSynchronized = new MonitorSynchronized<Double>(maxSize);
        long startTimeEnq2 = System.nanoTime();
        for (int i = 0; i < operations; i++) {
            try {
                monitorSynchronized.enq(Math.random());
            } catch (InterruptedException e) {
            }
        }
        long endTimeEnq2 = System.nanoTime();
        double durationEnq2 = (endTimeEnq2 - startTimeEnq2) / 1000000d;
        System.out.println(String.format("Enqueue: %s", durationEnq2));
        long startTimeDeq2 = System.nanoTime();
        for (int i = 0; i < operations; i++) {
            try {
                Double d = monitorSynchronized.deq();
            } catch (InterruptedException e) {
            }
        }
        long endTimeDeq2 = System.nanoTime();
        double durationDeq2 = (endTimeDeq2 - startTimeDeq2) / 1000000d;
        System.out.println(String.format("Dequeue: %s", durationDeq2));
    }
}
