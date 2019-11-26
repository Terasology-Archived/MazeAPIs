package org.terasology.mazeapi.mazeItems;

import org.terasology.math.geom.Vector2i;
import org.terasology.mazeapi.config.MazeConfig;

import java.io.IOException;

public interface Level extends Iterable<Tile> {
  default Tile getTile(Vector2i v) {
    return getTile(v.x(), v.y());
  }

  Tile getTile(int x, int y);

  void render(String filename) throws IOException;

  void interConnect(MazeConfig config);

  /**
   * Removes all dead ends on corridors in this maze.
   */
  void removeDeadEnds();

  /**
   * Fills all unoccupied space with corridors.
   * <p>
   * Should not overwrite any rooms, and subsequent calls to this without
   * modifying the maze in meantime should do nothing.
   *
   * @param config The config of this maze.
   */
  void fillEmptySpaceWithMazes(MazeConfig config);

  /**
   * Attempts to place a room of given dimensions on position, reports success.
   *
   * @param x      X position of the top-left corner of the room
   * @param y      Y position of the top-left corner of the room
   * @param width  Width of the room
   * @param height Height of the room
   * @param region Region to assign room's tiles to.
   * @return True if room was placed successfully, false if space was already occupied.
   */
  boolean attemptPlaceRoom(int x, int y, int width, int height, MazeRegion region);
}
