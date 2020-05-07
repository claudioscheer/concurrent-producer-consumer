package utils;

import java.util.SplittableRandom;

public final class RandomNumbers {

    private static SplittableRandom RANDOM = new SplittableRandom();
    public static int MAX_SIZE = Integer.MAX_VALUE;

    /*
     * This method is necessary, because if you choose a random number to remove and
     * add to the list, you may never remove an element from the list. This will
     * make the list remain full most of the time.
     */
    public static int getRandomInt() {
        int randomNum = RandomNumbers.RANDOM.nextInt(RandomNumbers.MAX_SIZE);
        return randomNum;
    }

    /* I think it is a bad way to do this. */
    public static int getRandomOperationIndex() {
        int randomNumber = RandomNumbers.RANDOM.nextInt(1001);
        if (randomNumber > 998) {
            // 0.02% of listSize.
            return 3;
        } else if (randomNumber > 800) {
            // 19.98% of contains.
            return 2;
        } else if (randomNumber > 400) {
            // 40% of remove.
            return 1;
        } else {
            // 40% of add.
            return 0;
        }
    }
}