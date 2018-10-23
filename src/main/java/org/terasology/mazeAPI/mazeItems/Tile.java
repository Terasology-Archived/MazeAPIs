package org.terasology.mazeAPI.mazeItems;

import org.terasology.math.geom.Vector2i;

public class Tile {
    public final Vector2i position;

    public Tile(Vector2i position) {
        this.position = position;
    }

    public MazeRegion region = MazeRegion.WALL;
    public TileType tileType = TileType.WALL;
    public boolean isPassable = false;
}
