package utils;

import java.util.Random;

public final class RandomNumbers {
    public static int getRandomInt() {
        int max = Integer.MAX_VALUE;
        Random rand = new Random();
        int randomNum = rand.nextInt(max);
        return randomNum;
    }
}