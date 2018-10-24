package org.terasology.mazeAPI.config;

import org.terasology.mazeAPI.Util;

public class MazeConfig {
    public int width = 301;
    public int height = 301;
    public LevelTypes levelTypes = LevelTypes.WIDE_WALLS;
    public int roomPlaceAttempts = 1000;

    // following are inclusive
    public int minRoomWidth = 3;
    public int minRoomHeight = 3;
    public int maxRoomWidth = 15;
    public int maxRoomHeight = 15;

    public Util util = new Util();
}
