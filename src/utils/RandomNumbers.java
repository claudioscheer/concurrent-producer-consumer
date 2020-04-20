package utils;

import java.util.SplittableRandom;

public final class RandomNumbers {

    // This value will be the same as the capacity of the list.
    public static int MAX;
    private static SplittableRandom RANDOM = new SplittableRandom();

    /*
     * This method is necessary, because if you choose a random number to remove and
     * add to the list, you may never remove an element from the list. This will
     * make the list remain full most of the time.
     */
    public static int getRandomInt() {
        int randomNum = RandomNumbers.RANDOM.nextInt(RandomNumbers.MAX);
        return randomNum;
    }
}