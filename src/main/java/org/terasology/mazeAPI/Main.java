package org.terasology.mazeAPI;

import org.terasology.mazeAPI.config.MazeConfig;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Maze maze = new Maze(new MazeConfig());
        try {
            maze.level.render("render.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
