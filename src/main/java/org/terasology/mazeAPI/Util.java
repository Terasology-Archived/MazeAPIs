package org.terasology.mazeAPI;

import java.util.Random;

public class Util {

    public static final Random RANDOM = new Random();

    public static int randomInt() { // TODO: 8/18/18 Replace with TS FastRandom, then inline
        return Math.abs(RANDOM.nextInt());
    }
}
