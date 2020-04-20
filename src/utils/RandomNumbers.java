package utils;

import java.util.Random;

public final class RandomNumbers {
    /*
     * This method is necessary, because if you choose a random number to remove and
     * add to the list, you may never remove an element from the list. This will
     * make the list remain full most of the time.
     */
    public static int getRandomInt() {
        int max = 50000;
        Random rand = new Random();
        int randomNum = rand.nextInt(max);
        return randomNum;
    }
}