package org.terasology.mazeapi.mazeItems;

import org.terasology.math.geom.Vector2i;

public class Tile {
    public final Vector2i position;
    public boolean isOpenUp, isOpenDown, isOpenLeft, isOpenRight;

    public Tile(Vector2i position) {
        this.position = position;
    }

    public MazeRegion region = MazeRegion.WALL;
    public TileType tileType = TileType.WALL;
    public boolean isPassable = false;
}
