/*
 * Copyright 2019 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import java.util.List;

public class ThinWallsLevel implements Level {

  private final int HEIGHT;
  private final int WIDTH;
  private Tile[][] tiles;

  public ThinWallsLevel(int width, int height) {
    this.HEIGHT = height;
    this.WIDTH = width;
    this.tiles = new Tile[WIDTH][HEIGHT];

    for (int x = 0; x < WIDTH; x++) {
      for (int y = 0; y < HEIGHT; y++) {
        tiles[x][y] = new Tile(new Vector2i(x, y));
      }
    }
  }

  @Override
  public Tile getTile(int x, int y) {
    return tiles[x][y];
  }

  @Override
  public void render(String filename) throws IOException {
    BufferedImage image = new BufferedImage(WIDTH * 5, HEIGHT * 5, BufferedImage.TYPE_INT_ARGB);
    for (int i = 0; i < WIDTH; i++) {
      for (int j = 0; j < HEIGHT; j++) {
        image.setRGB(5 * i, 5 * j, Color.BLACK.getRGB());
        image.setRGB(5 * i + 4, 5 * j, Color.BLACK.getRGB());
        image.setRGB(5 * i, 5 * j + 4, Color.BLACK.getRGB());
        image.setRGB(5 * i + 4, 5 * j + 4, Color.BLACK.getRGB());
        if (!tiles[i][j].isPassable) {
          switch (getTile(i, j).tileType) {
            case WALL:
              renderTile(image, i, j, Color.BLACK.getRGB());
              break;
            case POTENTIAL_DOOR:
              renderTile(image, i, j, Color.YELLOW.getRGB());
              break;
            default:
              renderTile(image, i, j, Color.RED.getRGB());
          }
        } else {
          switch (getTile(i, j).tileType) {
            case CORRIDOR:
              renderTile(image, i, j, Color.WHITE.getRGB());
              break;
            case DOOR:
              renderTile(image, i, j, Color.ORANGE.getRGB());
              break;
            case ROOM:
              renderTile(image, i, j, Color.LIGHT_GRAY.getRGB());
              break;
            default:
              renderTile(image, i, j, Color.RED.getRGB());
          }}
          int rgb = Color.BLACK.getRGB();
          if (getTile(i, j).isOpenUp) {
            rgb = Color.WHITE.getRGB();
          }
          image.setRGB(5 * i + 1, 5 * j, rgb);
          image.setRGB(5 * i + 2, 5 * j, rgb);
          image.setRGB(5 * i + 3, 5 * j, rgb);
          rgb = Color.BLACK.getRGB();
          if (getTile(i, j).isOpenLeft) {
            rgb = Color.WHITE.getRGB();
          }
          image.setRGB(5 * i, 5 * j + 1, rgb);
          image.setRGB(5 * i, 5 * j + 2, rgb);
          image.setRGB(5 * i, 5 * j + 3, rgb);
          rgb = Color.BLACK.getRGB();
          if (getTile(i, j).isOpenDown) {
            rgb = Color.WHITE.getRGB();
          }
          image.setRGB(5 * i + 1, 5 * j + 4, rgb);
          image.setRGB(5 * i + 2, 5 * j + 4, rgb);
          image.setRGB(5 * i + 3, 5 * j + 4, rgb);
          rgb = Color.BLACK.getRGB();
          if (getTile(i, j).isOpenRight) {
            rgb = Color.WHITE.getRGB();
          }
          image.setRGB(5 * i + 4, 5 * j + 1, rgb);
          image.setRGB(5 * i + 4, 5 * j + 2, rgb);
          image.setRGB(5 * i + 4, 5 * j + 3, rgb);
        }

    }
    File file = new File(filename);
    ImageIO.write(image, "png", file);
  }

  private void renderTile(BufferedImage image, int i, int j, int rgb) {
    image.setRGB(5 * i + 1, 5 * j + 1, rgb);
    image.setRGB(5 * i + 2, 5 * j + 1, rgb);
    image.setRGB(5 * i + 3, 5 * j + 1, rgb);
    image.setRGB(5 * i + 1, 5 * j + 2, rgb);
    image.setRGB(5 * i + 2, 5 * j + 2, rgb);
    image.setRGB(5 * i + 3, 5 * j + 2, rgb);
    image.setRGB(5 * i + 1, 5 * j + 3, rgb);
    image.setRGB(5 * i + 2, 5 * j + 3, rgb);
    image.setRGB(5 * i + 3, 5 * j + 3, rgb);
  }

  @Override //TODO slow due to randomness
  public void interConnect(MazeConfig config) {
    boolean khvhjvhjvghvjh = true; //TODO better name
    while (khvhjvhjvghvjh) {
      khvhjvhjvghvjh = false;
      for (int x = 0; x < WIDTH; x++) {
        for (int y = 0; y < HEIGHT; y++) {
          if (x != 0) {
            if (getTile(x, y).region != getTile(x - 1, y).region) {
              if ((!getTile(x, y).region.isConnectedToMain && getTile(x - 1, y).region.isConnectedToMain) || (getTile(x, y).region.isConnectedToMain && !getTile(x - 1, y).region.isConnectedToMain)) {
                khvhjvhjvghvjh = true;
                // small chance so it is not always top left corner
                if (config.util.randomInt() % 15 == 0) {// TODO configurable chance, finetune, MazeConfig
                  getTile(x, y).isOpenLeft = true;
                  getTile(x - 1, y).isOpenRight = true;
                  getTile(x, y).region.isConnectedToMain = true;
                  getTile(x - 1, y).region.isConnectedToMain = true;
                }
              } else if (getTile(x, y).region.isConnectedToMain) { //Open random connections. only when both are already main for simplifying
                if (config.util.randomInt() % 120 == 0) {// TODO configurable chance, finetune, MazeConfig
                  getTile(x, y).isOpenLeft = true;
                  getTile(x - 1, y).isOpenRight = true;
                }
              }
            }
          }
          if (y != 0) {
            if (getTile(x, y).region != getTile(x, y - 1).region) {
              if ((!getTile(x, y).region.isConnectedToMain && getTile(x, y - 1).region.isConnectedToMain) || (getTile(x, y).region.isConnectedToMain && !getTile(x, y - 1).region.isConnectedToMain)) {
                khvhjvhjvghvjh = true;
                // small chance so it is not always top left corner
                if (config.util.randomInt() % 15 == 0) {// TODO configurable chance, finetune, MazeConfig
                  getTile(x, y).isOpenUp = true;
                  getTile(x, y - 1).isOpenDown = true;
                  getTile(x, y).region.isConnectedToMain = true;
                  getTile(x, y - 1).region.isConnectedToMain = true;
                }
              } else if (getTile(x, y).region.isConnectedToMain) { //Open random connections. only when both are already main for simplifying
                if (config.util.randomInt() % 120 == 0) {// TODO configurable chance, finetune, MazeConfig
                  getTile(x, y).isOpenUp = true;
                  getTile(x, y - 1).isOpenDown = true;
                }
              }
            }
          }
        }
      }
    }
  }

  @Override// TODO optimise remember dead ends removed in last iteration and only check next to them
  public void removeDeadEnds() {
    boolean removed = true;
    while (removed) {
      removed = false;
      for (int x = 0; x < WIDTH; x++) {
        for (int y = 0; y < HEIGHT; y++) {
          final Tile tile = getTile(x, y);
          if (tile.isPassable) {
            int openSides = 0;

            if (tile.isOpenDown) {
              openSides++;
            }
            if (tile.isOpenRight) {
              openSides++;
            }
            if (tile.isOpenLeft) {
              openSides++;
            }
            if (tile.isOpenUp) {
              openSides++;
            }

            if (openSides == 1) {
              removed = true;
              if (tile.isOpenRight && x != WIDTH - 1) {
                getTile(x + 1, y).isOpenLeft = false;
              }
              if (tile.isOpenLeft && x != 0) {
                getTile(x -1, y).isOpenRight = false;
              }
              if (tile.isOpenUp && y != 0) {
                getTile(x, y-1).isOpenDown = false;
              }
              if (tile.isOpenDown && y != HEIGHT - 1) {
                getTile(x, y+1).isOpenUp = false;
              }
              markWall(tile);
            }
          }
        }
      }
    }
  }

  private void markWall(Tile tile) {
    tile.region = MazeRegion.WALL;
    tile.isPassable = false;
    tile.isOpenUp = false;
    tile.isOpenDown = false;
    tile.isOpenLeft = false;
    tile.isOpenRight = false;
    tile.tileType = TileType.WALL;
  }

  @Override
  public void fillEmptySpaceWithMazes(MazeConfig config) {
    for (int x = 0; x < WIDTH; x++) {
      for (int y = 0; y < HEIGHT; y++) {
        if (getTile(x, y).tileType == TileType.WALL) {
          final MazeRegion region = new MazeRegion();
          ArrayList<Vector2i> expandableTiles = new ArrayList<>();
          Vector2i currentTile = new Vector2i(x, y);
          setCorridor(region, currentTile);
          expandableTiles.add(currentTile);
          while (!expandableTiles.isEmpty()) {
            expandCorridor(expandableTiles, region, config);
          }
        }
      }
    }
  }

  private void setCorridor(MazeRegion region, Vector2i currentTile) {
    getTile(currentTile).tileType = TileType.CORRIDOR;
    getTile(currentTile).isPassable = true;
    getTile(currentTile).region = region;
  }

  private void expandCorridor(List<Vector2i> expandableTiles, MazeRegion region, MazeConfig config) {
    int tileIndex = config.util.randomInt() % expandableTiles.size();
    int wallNeighbors = 0;
    final Vector2i currentTile = new Vector2i(expandableTiles.get(tileIndex));
    if (expandableTiles.get(tileIndex).x() != 0) {
      if (getTile(currentTile.addX(-1)).tileType == TileType.WALL) {
        wallNeighbors++;
      }
      currentTile.addX(1);
    }
    if (expandableTiles.get(tileIndex).x() != WIDTH - 1) {
      if (getTile(currentTile.addX(1)).tileType == TileType.WALL) {
        wallNeighbors++;
      }
      currentTile.addX(-1);
    }
    if (expandableTiles.get(tileIndex).y() != 0) {
      if (getTile(currentTile.addY(-1)).tileType == TileType.WALL) {
        wallNeighbors++;
      }
      currentTile.addY(1);
    }
    if (expandableTiles.get(tileIndex).y() != HEIGHT - 1) {
      if (getTile(currentTile.addY(1)).tileType == TileType.WALL) {
        wallNeighbors++;
      }
      currentTile.addY(-1);
    }
    if (wallNeighbors == 0) {
      expandableTiles.remove(tileIndex);
      return;
    }
    wallNeighbors = config.util.randomInt() % wallNeighbors;
    if (expandableTiles.get(tileIndex).x() != 0) {
      if (getTile(currentTile.addX(-1)).tileType == TileType.WALL) {
        if (wallNeighbors-- == 0) {
          setCorridor(region, currentTile);
          expandableTiles.add(new Vector2i(currentTile));
          getTile(currentTile).isOpenRight = true;
          getTile(currentTile.x + 1, currentTile.y).isOpenLeft = true;
        }
      }
      currentTile.addX(1);
    }
    if (expandableTiles.get(tileIndex).x() != WIDTH - 1) {
      if (getTile(currentTile.addX(1)).tileType == TileType.WALL) {
        if (wallNeighbors-- == 0) {
          setCorridor(region, currentTile);
          expandableTiles.add(new Vector2i(currentTile));
          getTile(currentTile).isOpenLeft = true;
          getTile(currentTile.x - 1, currentTile.y).isOpenRight = true;
        }
      }
      currentTile.addX(-1);
    }
    if (expandableTiles.get(tileIndex).y() != 0) {
      if (getTile(currentTile.addY(-1)).tileType == TileType.WALL) {
        if (wallNeighbors-- == 0) {
          setCorridor(region, currentTile);
          expandableTiles.add(new Vector2i(currentTile));
          getTile(currentTile).isOpenDown = true;
          getTile(currentTile.x, currentTile.y + 1).isOpenUp = true;
        }
      }
      currentTile.addY(1);
    }
    if (expandableTiles.get(tileIndex).y() != HEIGHT - 1) {
      if (getTile(currentTile.addY(1)).tileType == TileType.WALL) {
        if (wallNeighbors == 0) {
          setCorridor(region, currentTile);
          expandableTiles.add(new Vector2i(currentTile));
          getTile(currentTile).isOpenUp = true;
          getTile(currentTile.x, currentTile.y - 1).isOpenDown = true;
        }
      }
    }
  }

  @Override
  public boolean attemptPlaceRoom(int x, int y, int width, int height, MazeRegion region) {
    if (x + width > WIDTH || y + height > HEIGHT) {
      return false;
    }
    for (int w = 0; w < width; w++) {
      for (int h = 0; h < height; h++) {
        if (getTile(x + w, y + h).region != MazeRegion.WALL) {
          return false;
        }
      }
    }
    for (int w = 0; w < width; w++) {
      for (int h = 0; h < height; h++) {
        getTile(x + w, y + h).isPassable = true;
        getTile(x + w, y + h).region = region;
        getTile(x + w, y + h).tileType = TileType.ROOM;
        getTile(x + w, y + h).isOpenUp = h != 0;
        getTile(x + w, y + h).isOpenDown = h != height - 1;
        getTile(x + w, y + h).isOpenLeft = w != 0;
        getTile(x + w, y + h).isOpenRight = w != width - 1;
      }
    }
    return true;
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
}
