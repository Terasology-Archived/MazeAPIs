package org.terasology.mazeAPI.mazeItems;

import org.terasology.mazeAPI.config.MazeConfig;

import java.io.IOException;

public interface Level extends Iterable<Tile> {
    Tile getTile(int x, int y);
    void render(String filename) throws IOException;

    void doorify(MazeConfig config);

    void removeDeadEnds();

    void fillEmptySpaceWithMazes(MazeConfig config);

    /**
     * Attempts to place a room of given dimensions on position, reports success.
     *
     * @param x X position of the top-left corner of the room
     * @param y Y position of the top-left corner of the room
     * @param width Width of the room
     * @param height Height of the room
     * @param region Region to assign room's tiles to.
     * @return True if room was placed successfully, false if space was already occupied.
     */
    boolean attemptPlaceRoom(int x, int y, int width, int height, MazeRegion region);
}
