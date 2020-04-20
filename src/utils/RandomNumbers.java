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

    /* I think it is a bad way to do this. */
    public static int getRandomOperationIndex() {
        int randomNumber = RandomNumbers.RANDOM.nextInt(1001);
        if (randomNumber > 900) {
            // 15% of listSize.
            return 3;
        } else if (randomNumber > 800) {
            // 15% of contains.
            return 2;
        } else if (randomNumber > 350) {
            // 35% of remove.
            return 1;
        } else {
            // 35% of add.
            return 0;
        }
    }
}