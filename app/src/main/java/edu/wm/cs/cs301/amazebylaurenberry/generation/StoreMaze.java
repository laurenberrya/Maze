package edu.wm.cs.cs301.amazebylaurenberry.generation;

import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Random;

public class StoreMaze {

    private static final String TAG = "Logs";
    private static Random rand = new Random();
    private static int hashLoc;

    public static HashMap<String, Integer> mazeLevels = new HashMap<String,Integer>();
    public static HashMap<String, Integer> mazeDrivers = new HashMap<String,Integer>();
    public static HashMap<String, Integer> mazeAlgs = new HashMap<String,Integer>();

    private static Maze tempMaze;

    public static void setMaze(String level, String driver, String alg){


        Random rand = new Random();
        hashLoc = rand.nextInt(100);

        Log.v(TAG, "put level, driver, and alg in hash map at location: " + Integer.toString(hashLoc));
        mazeLevels.put(level, hashLoc);
        mazeDrivers.put(driver, hashLoc);
        mazeAlgs.put(alg, hashLoc);


    }

    public static int getMaze(String level){
        Log.v(TAG, "getting maze w level " + level);
        return mazeLevels.get(level);
    }

    public static Maze getWholeMaze() {

        return tempMaze;
    }
    public static void setWholeMaze(Maze tempMaze) {

        StoreMaze.tempMaze = tempMaze;
    }
}
