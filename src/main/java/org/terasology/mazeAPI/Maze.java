package org.terasology.mazeAPI;

import org.terasology.mazeAPI.config.MazeConfig;
import org.terasology.mazeAPI.mazeItems.Level;
import org.terasology.mazeAPI.mazeItems.MazeRegion;
import org.terasology.mazeAPI.mazeItems.WideWallsLevel;

public class Maze {

    public final Level level;

    public Maze(MazeConfig config) {
        switch (config.levelTypes) {
            case WIDE_WALLS:
                level = generateWideWallsLevel(config);
                break;
            case THIN_WALLS:
                throw new UnsupportedOperationException("Thin walls levels not yet supported."); // TODO: 8/18/18 Add thin walls levels
            default:
                throw new UnsupportedOperationException("Requested unknown level type.");
        }
    }

    private WideWallsLevel generateWideWallsLevel(MazeConfig config) {
        checkWWLConfig(config);
        final WideWallsLevel level = new WideWallsLevel(config.width, config.height);
        level.attemptPlaceRoom(1, 1, 5, 5, MazeRegion.MAIN);
        int roomsPlaced = 0;
        for (int i = 0; i < config.roomPlaceAttempts; i++) {
            int roomWidth = Util.randomInt() % ((config.maxRoomWidth - config.minRoomWidth) / 2 + 1) * 2 + config.minRoomWidth;
            int roomHeight = Util.randomInt() % ((config.maxRoomHeight - config.minRoomHeight) / 2 + 1) * 2 + config.minRoomHeight;
            int roomX = Util.randomInt() % ((config.width - roomWidth) / 2) * 2 + 1;
            int roomY = Util.randomInt() % ((config.height - roomHeight) / 2) * 2 + 1;
            if (level.attemptPlaceRoom(roomX, roomY, roomWidth, roomHeight, new MazeRegion())) {
                roomsPlaced++;
            }
        }
        level.fillEmptySpaceWithMazes();
        level.doorify();
        level.removeDeadEnds();
        return level;
    }

    private void checkWWLConfig(MazeConfig config) {
        checkWWLParameterEven(config.width);
        checkWWLParameterEven(config.height);
        checkWWLParameterEven(config.minRoomHeight);
        checkWWLParameterEven(config.maxRoomHeight);
        checkWWLParameterEven(config.minRoomWidth);
        checkWWLParameterEven(config.maxRoomWidth);
    }

    private void checkWWLParameterEven(int parameter) {
        if (parameter % 2 == 0) {
            throw new IllegalArgumentException("WideWallsLevel must have all dimensions and positions even");
        }
    }
}
