package org.terasology.mazeapi.mazeItems;

public class MazeRegion {
    /**
     * The main region used in maze generation. At the end all accessible tiles should be in this region.
     */
    public static final MazeRegion MAIN = new MazeRegion();
    /**
     * Region used for all inaccessible tiles.
     *
     * Exists mainly to avoid NPE if tile data was not handled correctly.
     */
    public static final MazeRegion WALL = new MazeRegion();

    public boolean isConnectedToMain = false;

    static {
        MAIN.isConnectedToMain = true;
    }
}
