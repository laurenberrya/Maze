package edu.wm.cs.cs301.amazebylaurenberry.generation;

public class PlaceMaze {


    private static Maze tempMaze;
    public static Maze getMaze() {
        return tempMaze;
    }
    public static void setMaze(Maze tempMaze) {
        PlaceMaze.tempMaze = tempMaze;
    }
}
