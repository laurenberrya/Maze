package edu.wm.cs.cs301.amazebylaurenberry;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.os.Message;

import edu.wm.cs.cs301.amazebylaurenberry.generation.Maze;
import edu.wm.cs.cs301.amazebylaurenberry.gui.BasicRobot;
import edu.wm.cs.cs301.amazebylaurenberry.gui.Constants;
import edu.wm.cs.cs301.amazebylaurenberry.gui.MazePanel;
import edu.wm.cs.cs301.amazebylaurenberry.gui.Robot;
import edu.wm.cs.cs301.amazebylaurenberry.gui.RobotDriver;
import edu.wm.cs.cs301.amazebylaurenberry.gui.StatePlaying;
import edu.wm.cs.cs301.amazebylaurenberry.gui.WallFollower;
import edu.wm.cs.cs301.amazebylaurenberry.gui.Wizard;
import edu.wm.cs.cs301.amazebylaurenberry.generation.StoreMaze;


/**
 * Class: PlayAnimationActivity
 *
 * Responsibility: Will eventually display the animation of the robot driver moving through the maze, but for
 * proj6, it displays the remaining energy, and provides features to toggle visibility of the map/solution.
 * Also has a start/pause button to start the exploration and to pause the animation.
 *
 * Collaborators: activity_play_animation.xml is the layout for this screen, and strings used on the layout
 * are stored in strings.xml. This class implements an OnClickListener, and calls WinningActivity or LosingActivity
 */

public class PlayAnimationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Logs";
    private MazePanel playScreen;
    private ToggleButton toggleSolution;
    private ToggleButton toggleMap;
    private ToggleButton toggleWalls;
    private ImageButton decrementButton;
    private ImageButton incrementButton;
    private ImageButton pauseButton;
    private ImageButton startButton;
    private TextView remainingBattery;
    private Button go2losing;
    private Button go2winning;
    String selectedDriver;
    String selectedAlgorithm;
    String selectedLevel;
    float battery;
    int bestPath;
    int pathTaken;
    Robot robot;
    RobotDriver driver;
    StatePlaying state;
    public static Handler aHandler;

    boolean hasStarted = false;


    /**
     * Creates UI elements and gets intent info from the previous state
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_animation);

        incrementButton = (ImageButton) findViewById(R.id.incrementButton);
        decrementButton = (ImageButton) findViewById(R.id.decrementButton);

        toggleMap = (ToggleButton) findViewById(R.id.toggleMap);
        toggleSolution = (ToggleButton) findViewById(R.id.toggleSolution);
        toggleWalls = (ToggleButton) findViewById(R.id.toggleWalls);

        toggleMap.setOnClickListener(this);
        toggleSolution.setOnClickListener(this);
        toggleWalls.setOnClickListener(this);

        toggleMap.setChecked(true);
        toggleSolution.setChecked(true);
        toggleWalls.setChecked(true);

        Intent intent = getIntent();
        selectedAlgorithm = intent.getStringExtra("selectedAlgorithm");
        selectedDriver = intent.getStringExtra("selectedDriver");
        selectedLevel = intent.getStringExtra("selectedLevel");

        pauseButton = findViewById(R.id.pauseButton);
        startButton = findViewById(R.id.startButton);

        go2losing = findViewById(R.id.go2losing);
        go2winning = findViewById(R.id.go2winning);

        go2losing.setVisibility(View.INVISIBLE);
        go2winning.setVisibility(View.INVISIBLE);

        remainingBattery = findViewById(R.id.remainingBattery);

        remainingBattery.setText(getString(R.string.remainingBattery) + "3000");

        Maze maze  = StoreMaze.getWholeMaze();

        playScreen = findViewById(R.id.playScreen);

        robot = new BasicRobot();


        if (selectedDriver.equals("Wizard")){
            driver = new Wizard();
        }
        if (selectedDriver.equals("WallFollower")){
            driver = new Wizard();
        }


        robot.setBatteryLevel(3000);
        driver.setRobot(robot);



        state = new StatePlaying();
        robot.setStatePlaying(state);
        robot.setMaze(maze);
        state.setMazeConfiguration(maze);
        state.start(playScreen);

        int[]pos = new int[0];
        try {
            pos = robot.getCurrentPosition();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int posX = pos[0];
        int posY = pos[1];

        bestPath = maze.getDistanceToExit(posX,posY);


        //if the driver wins, the message sent will equal 1

        aHandler = new Handler(){

            public void handleMessage(Message msg) {
                int m1 = msg.arg1;
                int m2 = msg.arg2;


                if (m1==1){
                    go2winning();
                }
                else if (m1==-1){
                    go2losing();
                }
                else{
                    remainingBattery.setText(getString(R.string.remainingBattery)+" "+Integer.toString(m2));
                }



            }
        };


    }


    /**
     * Responds to user clicks on buttons
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.toggleMap) {
            state.keyDown(Constants.UserInput.ToggleLocalMap,1);

            if (toggleMap.isChecked()) {
                toggleMap.setChecked(true);
                Log.v(TAG, "Showing Map");

            }
            else {
                toggleMap.setChecked(false);
                Log.v(TAG, "Hiding Map");
            }
        }

        if (v.getId() == R.id.toggleSolution) {
            state.keyDown(Constants.UserInput.ToggleSolution,1);

            if (toggleSolution.isChecked()) {
                toggleSolution.setChecked(true);
                Log.v(TAG, "Showing Solution");

            }
            else {
                toggleSolution.setChecked(false);
                Log.v(TAG, "Hiding Solution");
            }
        }


        if (v.getId() == R.id.toggleWalls) {
            state.keyDown(Constants.UserInput.ToggleFullMap,1);

            if (toggleWalls.isChecked()) {
                toggleWalls.setChecked(true);
                Log.v(TAG, "Showing Walls");

            }
            else {
                toggleWalls.setChecked(false);
                Log.v(TAG, "Hiding Walls");
            }
        }

        if (v.getId() == R.id.incrementButton) {
            state.keyDown(Constants.UserInput.ZoomIn,1);

            Log.v(TAG, "Increment Size");
        }

        if (v.getId() == R.id.decrementButton) {
            state.keyDown(Constants.UserInput.ZoomOut,1);

            Log.v(TAG, "Decrement Size");
        }

        if (v.getId() == R.id.pauseButton) {

            Log.v(TAG, "Pausing Animation");

            pauseButton.setVisibility(View.INVISIBLE);
            driver.pause();
            startButton.setVisibility(View.VISIBLE);
        }

        if (v.getId() == R.id.startButton) {
            Log.v(TAG, "Resuming Animation");
            driver.pause();
            startButton.setVisibility(View.INVISIBLE);

            pauseButton.setVisibility(View.VISIBLE);

            try {
                if (!hasStarted){
                    driver.drive2Exit();
                    hasStarted =true;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }



    }


    /**
     * Method that takes you to losing screen
     */
    public void go2losing(/*View view*/) {


        battery = 3000- robot.getBatteryLevel();
        pathTaken = robot.getOdometerReading();

        final Intent intent = new Intent(this, LosingActivity.class);
        intent.putExtra("selectedAlgorithm", selectedAlgorithm);
        intent.putExtra("selectedDriver", selectedDriver);
        intent.putExtra("selectedLevel", selectedLevel);

        intent.putExtra("battery", Float.toString(battery));
        intent.putExtra("bestPath", Integer.toString(bestPath));
        intent.putExtra("pathTaken", Integer.toString(pathTaken));
        startActivity(intent);

    }


    /**
     * Method that takes you to winning screen
     */
    public void go2winning(/*View view*/) {

        battery = 3000- robot.getBatteryLevel();
        pathTaken = robot.getOdometerReading();

        final Intent intent = new Intent(this, WinningActivity.class);
        intent.putExtra("selectedAlgorithm",selectedAlgorithm);
        intent.putExtra("selectedDriver", selectedDriver);
        intent.putExtra("selectedLevel", selectedLevel);

        intent.putExtra("battery", Float.toString(battery));
        intent.putExtra("bestPath", Integer.toString(bestPath));
        intent.putExtra("pathTaken", Integer.toString(pathTaken));
        startActivity(intent);
    }

    /**
     * Takes the user back to the main screen when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, AMazeActivity.class);
        startActivity(intent);
    }

}
