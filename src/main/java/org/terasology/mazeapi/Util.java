package org.terasology.mazeapi;

import java.util.Random;

public class Util {

    private Random random = new Random();

    public Util(Random random) {
        this.random = random;
    }

    public Util() {
    }

    public int randomInt() { // TODO: 8/18/18 Replace with TS FastRandom, then inline
        return Math.abs(random.nextInt());
    }
}
