package org.terasology.mazeapi.mazeItems;

import org.terasology.math.geom.Vector2i;
import org.terasology.mazeapi.config.MazeConfig;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Level where all walls are wide same as corridors.
 */
public class WideWallsLevel implements Level {

    private final int HEIGHT;
    private final int WIDTH;
    private Tile[][] tiles;

    public WideWallsLevel(int width, int height) {
        HEIGHT = height;
        WIDTH = width;
        checkEvenPosition(WIDTH, HEIGHT);
        tiles = new Tile[width][height];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                tiles[x][y] = new Tile(new Vector2i(x, y));
            }
        }
    }

    @Override
    public void fillEmptySpaceWithMazes(MazeConfig config) {
        for (int x = 1; x < WIDTH; x += 2) {
            for (int y = 1; y < HEIGHT; y += 2) {
                if (!getTile(x, y).isPassable) {
                    final MazeRegion region = new MazeRegion();
                    markCorridorTile(region, getTile(x, y));
                    ArrayList<Vector2i> expandableTiles = new ArrayList<>();
                    Vector2i currentTile = new Vector2i(x, y);
                    int freeDirections = getImpassableTilesAround(currentTile);
                    if (freeDirections > 0) {
                        expandableTiles.add(currentTile);
                    }
                    while (!expandableTiles.isEmpty()) {
                        expandTile(expandableTiles, region, config);
                    }
                }
            }
        }
    }

    private void expandTile(ArrayList<Vector2i> expandableTiles, MazeRegion region, MazeConfig config) {
        int tileIndex = config.util.randomInt() % expandableTiles.size();
        int freeTiles = getImpassableTilesAround(expandableTiles.get(tileIndex));
        if (freeTiles == 0) {
            expandableTiles.remove(tileIndex);
            return;
        }
        Vector2i currentTile;
        if (freeTiles == 1) {
            currentTile = expandableTiles.remove(tileIndex);
        } else {
            currentTile = expandableTiles.get(tileIndex);
        }
        if (currentTile.x != 1 && !getTile(currentTile.x - 2, currentTile.y).isPassable && --freeTiles == 0) {
            markCorridorTile(region, getTile(currentTile.x - 1, currentTile.y));
            markCorridorTile(region, getTile(currentTile.x - 2, currentTile.y));
            if (getImpassableTilesAround(new Vector2i(currentTile.x - 2, currentTile.y)) > 0) {
                expandableTiles.add(new Vector2i(currentTile.x - 2, currentTile.y));
            }
        }
        else if (currentTile.y != 1 && !getTile(currentTile.x, currentTile.y - 2).isPassable && --freeTiles == 0) {
            markCorridorTile(region, getTile(currentTile.x, currentTile.y - 1));
            markCorridorTile(region, getTile(currentTile.x, currentTile.y - 2));
            if (getImpassableTilesAround(new Vector2i(currentTile.x, currentTile.y - 2)) > 0) {
                expandableTiles.add(new Vector2i(currentTile.x, currentTile.y - 2));
            }
        }
        else if (currentTile.x != WIDTH - 2 && !getTile(currentTile.x + 2, currentTile.y).isPassable && --freeTiles == 0) {
            markCorridorTile(region, getTile(currentTile.x + 1, currentTile.y));
            markCorridorTile(region, getTile(currentTile.x + 2, currentTile.y));
            if (getImpassableTilesAround(new Vector2i(currentTile.x + 2, currentTile.y)) > 0) {
                expandableTiles.add(new Vector2i(currentTile.x + 2, currentTile.y));
            }
        }
        else {
            markCorridorTile(region, getTile(currentTile.x, currentTile.y + 1));
            markCorridorTile(region, getTile(currentTile.x, currentTile.y + 2));
            if (getImpassableTilesAround(new Vector2i(currentTile.x, currentTile.y + 2)) > 0) {
                expandableTiles.add(new Vector2i(currentTile.x, currentTile.y + 2));
            }
        }
    }

    private void markCorridorTile(MazeRegion region, Tile tile) {
        tile.isPassable = true;
        tile.region = region;
        tile.tileType = TileType.CORRIDOR;
    }

    private int getImpassableTilesAround(Vector2i tile) {
        int freeDirections = 0;
        if (tile.x != 1 && !getTile(tile.x - 2, tile.y).isPassable) {
            freeDirections++;
        }
        if (tile.y != 1 && !getTile(tile.x, tile.y - 2).isPassable) {
            freeDirections++;
        }
        if (tile.x != WIDTH - 2 && !getTile(tile.x + 2, tile.y).isPassable) {
            freeDirections++;
        }
        if (tile.y != HEIGHT - 2 && !getTile(tile.x, tile.y + 2).isPassable) {
            freeDirections++;
        }
        return freeDirections;
    }

    @Override
    public boolean attemptPlaceRoom(int x, int y, int width, int height, MazeRegion region) {
        checkEvenPosition(x, y);
        checkRoomDimensions(x, y, width, height);
        for (int i = 0; i < width; i += 2) {
            for (int j = 0; j < height; j += 2) {
                if (tiles[x + i][y + j].isPassable) {
                    return false;
                }
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                tiles[x + i][y + j].isPassable = true;
                tiles[x + i][y + j].region = region;
                tiles[x + i][y + j].tileType = TileType.ROOM;
            }
        }
        return true;
    }

    @Override
    public void render(String filename) throws IOException {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (!tiles[i][j].isPassable) {
                    switch (getTile(i, j).tileType) {
                        case WALL:
                            image.setRGB(i, j, Color.BLACK.getRGB());
                            break;
                        case POTENTIAL_DOOR:
                            image.setRGB(i, j, Color.YELLOW.getRGB());
                            break;
                        default:
                            image.setRGB(i, j, Color.RED.getRGB());
                    }
                } else {
                    switch (getTile(i, j).tileType) {
                        case CORRIDOR:
                            image.setRGB(i, j, Color.WHITE.getRGB());
                            break;
                        case DOOR:
                            image.setRGB(i, j, Color.ORANGE.getRGB());
                            break;
                        case ROOM:
                            image.setRGB(i, j, Color.LIGHT_GRAY.getRGB());
                            break;
                        default:
                            image.setRGB(i, j, Color.RED.getRGB());
                    }
                }
            }
        }
        File file = new File(filename);
        ImageIO.write(image, "png", file);
    }

    @Override
    public void interConnect(MazeConfig config) {
        ArrayList<Vector2i> markedPositions;
        while (true) {
            markedPositions = new ArrayList<>();
            markPossibleDoorPositions(markedPositions, config);
            if (markedPositions.isEmpty()) {
                return;
            }
            Vector2i chosenPosition = markedPositions.get(config.util.randomInt() % markedPositions.size());
            openDoor(chosenPosition.x, chosenPosition.y);
            if (chosenPosition.x % 2 == 0 && chosenPosition.y % 2 == 1) {
                getTile(chosenPosition.x - 1, chosenPosition.y).region.isConnectedToMain = true;
                getTile(chosenPosition.x + 1, chosenPosition.y).region.isConnectedToMain = true;
            } else if (chosenPosition.x % 2 == 1 && chosenPosition.y % 2 == 0) {
                getTile(chosenPosition.x, chosenPosition.y - 1).region.isConnectedToMain = true;
                getTile(chosenPosition.x, chosenPosition.y + 1).region.isConnectedToMain = true;
            }
        }
    }

    private void markPossibleDoorPositions(ArrayList<Vector2i> markedPositions, MazeConfig config) {
        for (int x = 1; x < WIDTH - 1; x++) {
            for (int y = 1; y < HEIGHT - 1; y++) {
                if (!getTile(x, y).isPassable) {
                    if (x % 2 == 0 && y % 2 == 1 && getTile(x - 1, y).region != getTile(x + 1, y).region) {
                        if (getTile(x - 1, y).region.isConnectedToMain != getTile(x + 1, y).region.isConnectedToMain) {
                            getTile(x, y).tileType = TileType.POTENTIAL_DOOR;
                            markedPositions.add(new Vector2i(x, y));
                        } else if (getTile(x, y).tileType == TileType.POTENTIAL_DOOR) {
                            randomlyOpenDoor(x, y, config);
                        }
                    }
                    if (x % 2 == 1 && y % 2 == 0 && getTile(x, y - 1).region != getTile(x, y + 1).region) {
                        if (getTile(x, y - 1).region.isConnectedToMain != getTile(x, y + 1).region.isConnectedToMain) {
                            getTile(x, y).tileType = TileType.POTENTIAL_DOOR;
                            markedPositions.add(new Vector2i(x, y));
                        } else if (getTile(x, y).tileType == TileType.POTENTIAL_DOOR) {
                            randomlyOpenDoor(x, y, config);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void removeDeadEnds() {
        boolean removed = true;
        while (removed) {
            removed = false;
            for (int x = 1; x < WIDTH - 1; x++) {
                for (int y = 1; y < HEIGHT - 1; y++) {
                    if (getTile(x, y).isPassable) {
                        int passableNeighbors = 0;
                        if (getTile(x + 1, y).isPassable) {
                            passableNeighbors++;
                        }
                        if (getTile(x - 1, y).isPassable) {
                            passableNeighbors++;
                        }
                        if (getTile(x, y + 1).isPassable) {
                            passableNeighbors++;
                        }
                        if (getTile(x, y - 1).isPassable) {
                            passableNeighbors++;
                        }
                        if (passableNeighbors < 2) {
                            getTile(x, y).isPassable = false;
                            getTile(x, y).tileType = TileType.WALL;
                            getTile(x, y).region = MazeRegion.WALL;
                            removed = true;
                        }
                    }
                }
            }
        }
    }

    private void randomlyOpenDoor(int x, int y, MazeConfig config) {
        if (config.util.randomInt() % 50 == 0) {
            openDoor(x, y);
        } else {
            getTile(x, y).tileType = TileType.WALL;
        }
    }

    private void openDoor(int x, int y) {
        getTile(x, y).isPassable = true;
        getTile(x, y).tileType = TileType.DOOR;
        getTile(x, y).region = MazeRegion.MAIN;
    }

    private void checkRoomDimensions(int x, int y, int width, int height) {
        if (width % 2 == 0 || height % 2 == 0 || width < 3 || height < 3 || x + width > WIDTH || y + height > HEIGHT) {
            throw new IllegalArgumentException("Room dimensions in WideWallsLevel must be even and greater than 3.");
        }
    }

    private void checkEvenPosition(int x, int y) {
        if (x % 2 == 0 || y % 2 == 0 || x > WIDTH || y > HEIGHT) {
            throw new IllegalArgumentException("Positions in WideWallsLevel must be even numbers.");
        }
    }

    @Override
    public Tile getTile(int x, int y) {
        return tiles[x][y];
    }

    @Override
    public Iterator<Tile> iterator() {
        return new Iterator<Tile>() {
            private int index = 0;
            @Override
            public boolean hasNext() {
                return index < WIDTH * HEIGHT;
            }

            @Override
            public Tile next() {
                return tiles[index / WIDTH][index++ % WIDTH];
            }
        };
    }

    public void computeTileData() {
        for (Tile tile : this) {
            tile.isOpenUp = tile.position.y != 0 && getTile(tile.position.x, tile.position.y - 1).isPassable;
            tile.isOpenDown = tile.position.y != HEIGHT - 1 && getTile(tile.position.x, tile.position.y + 1).isPassable;
            tile.isOpenLeft = tile.position.x != 0 && getTile(tile.position.x - 1, tile.position.y).isPassable;
            tile.isOpenRight = tile.position.x != WIDTH - 1 && getTile(tile.position.x + 1, tile.position.y).isPassable;
        }
    }
}
