import main.Monitor;

public class TestMonitor {

    public static void main(String[] args) {
        int maxSize = 10000000;
        int operations = 10000000;

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
        System.out.println(durationEnq);

        long startTimeDeq = System.nanoTime();
        for (int i = 0; i < operations; i++) {
            try {
                Double d = monitor.deq();
            } catch (InterruptedException e) {
            }
        }
        long endTimeDeq = System.nanoTime();

        double durationDeq = (endTimeDeq - startTimeDeq) / 1000000d;
        System.out.println(durationDeq);

        System.out.println(monitor.getCurrentSize());
    }
}
