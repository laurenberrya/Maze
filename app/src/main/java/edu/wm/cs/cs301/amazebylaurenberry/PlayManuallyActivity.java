package edu.wm.cs.cs301.amazebylaurenberry;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import edu.wm.cs.cs301.amazebylaurenberry.generation.Maze;
import edu.wm.cs.cs301.amazebylaurenberry.generation.StoreMaze;
import edu.wm.cs.cs301.amazebylaurenberry.gui.BasicRobot;
import edu.wm.cs.cs301.amazebylaurenberry.gui.Constants;
import edu.wm.cs.cs301.amazebylaurenberry.gui.ManuallyDriver;
import edu.wm.cs.cs301.amazebylaurenberry.gui.MazePanel;
import edu.wm.cs.cs301.amazebylaurenberry.gui.Robot;
import edu.wm.cs.cs301.amazebylaurenberry.gui.StatePlaying;

/**
 * Class: PlayManuallyActivity
 *
 * Responsibility: Allows the user to manually navigate the robot through the maze (in theory). It
 * displays the remaining energy, and provides features to toggle visibility of the map/solution.
 * Also has left/right/up arrows to aide in navigating the robot.
 *
 * Collaborators: activity_play_manually.xml is the layout for this screen, and strings used on the layout
 * are stored in strings.xml. This class implements an OnClickListener, and calls WinningActivity or LosingActivity
 */
public class PlayManuallyActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "Logs";
    private ToggleButton toggleSolution;
    private ToggleButton toggleMap;
    private ToggleButton toggleWalls;
    private ImageButton decrementButton;
    private ImageButton incrementButton;
    private TextView remainingBattery;
    String selectedDriver;
    String selectedAlgorithm;
    String selectedLevel;
    float battery;
    int bestPath;
    int pathTaken;

    private MazePanel playScreen;
    StatePlaying state;
    Robot robot;
    ManuallyDriver driver;

    /**
     * Creates UI elements and gets intent info from the previous state
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_manually);

        incrementButton = (ImageButton) findViewById(R.id.incrementButton);
        decrementButton = (ImageButton) findViewById(R.id.decrementButton);

        toggleMap= (ToggleButton) findViewById(R.id.toggleMap);
        toggleSolution = (ToggleButton) findViewById(R.id.toggleSolution);
        toggleWalls = (ToggleButton) findViewById(R.id.toggleWalls);

        toggleMap.setOnClickListener(this);
        toggleSolution.setOnClickListener(this);
        toggleWalls.setOnClickListener(this);

        toggleMap.setChecked(true);
        toggleSolution.setChecked(true);
        toggleWalls.setChecked(true);

        playScreen = findViewById(R.id.playScreen);

        Intent intent = getIntent();
        selectedAlgorithm = intent.getStringExtra("selectedAlgorithm");
        selectedDriver = intent.getStringExtra("selectedDriver");
        selectedLevel = intent.getStringExtra("selectedLevel");

        remainingBattery = findViewById(R.id.remainingBattery);
        remainingBattery.setText(getString(R.string.remainingBattery)+"3000");



        //gets the generated maze
        Maze maze  = StoreMaze.getWholeMaze();
        //Toast.makeText(PlayManuallyActivity.this, maze.toString(), Toast.LENGTH_LONG).show();

        robot = new BasicRobot();
        driver = new ManuallyDriver();
        //set BatteryLevel must be set before setRobot so that the initial battery
        //level can be establised in ManualDriver
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
        bestPath = maze.getDistanceToExit(pos[0],pos[1]);

    }

    /**
     * Responds to user clicks on buttons
     * @param v
     */
    @Override
    public void onClick(View v) {

        if (v.getId() ==  R.id.toggleMap) {
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

        if (v.getId() ==  R.id.toggleSolution) {
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

        if (v.getId() ==  R.id.toggleWalls) {
            state.keyDown(Constants.UserInput.ToggleFullMap,1);
            if (toggleWalls.isChecked()) {
                toggleWalls.setChecked(true);
                Log.v(TAG, "Showing walls");
            }
            else {
                toggleWalls.setChecked(false);
                Log.v(TAG, "Hiding walls");
            }
        }

        if (v.getId() ==  R.id.incrementButton) {
            state.keyDown(Constants.UserInput.ZoomIn,1);
            Log.v(TAG, "Increment size");
        }

        if (v.getId() ==  R.id.decrementButton) {
            state.keyDown(Constants.UserInput.ZoomOut,1);
            Log.v(TAG, "Decrement size");
        }

        if (v.getId() ==  R.id.leftKey) {
            driver.keyDown(Constants.UserInput.Left);
            Log.v(TAG, "Rotate Left");
        }

        if (v.getId() ==  R.id.rightKey) {
            driver.keyDown(Constants.UserInput.Right);
            Log.v(TAG, "Rotate Right");
        }

        if (v.getId() ==  R.id.upKey) {
            driver.keyDown(Constants.UserInput.Up);
            Log.v(TAG, "Move Forward");
            if (state.getWin() == true){
                go2winning();
            }
        }


        // if the map or walls are being displayed then increment/decrement buttons should
        //be visible, otherwise we want them invisible as to not confuse the user
        if (toggleMap.isChecked()||toggleWalls.isChecked()){
            incrementButton.setVisibility(View.VISIBLE);
            decrementButton.setVisibility(View.VISIBLE);
        }
        if (!toggleMap.isChecked() && !toggleWalls.isChecked()){
            incrementButton.setVisibility(View.INVISIBLE);
            decrementButton.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Method that takes you to losing screen if go2losing button was pressed
     */
    public void go2losing() {

        pathTaken = robot.getOdometerReading();

        final Intent intent = new Intent(this, LosingActivity.class);
        intent.putExtra("selectedAlgorithm",selectedAlgorithm);
        intent.putExtra("selectedDriver", selectedDriver);
        intent.putExtra("selectedLevel", selectedLevel);

        intent.putExtra("battery",Float.toString(battery));
        intent.putExtra("bestPath", Integer.toString(bestPath));
        intent.putExtra("pathTaken", Integer.toString(pathTaken));

        startActivity(intent);
    }

    /**
     *  Method that takes you to winning screen if go2winning button was pressed
     */
    public void go2winning(){


        pathTaken = robot.getOdometerReading();

        final Intent intent = new Intent(this, WinningActivity.class);
        intent.putExtra("selectedAlgorithm",selectedAlgorithm);
        intent.putExtra("selectedDriver", selectedDriver);
        intent.putExtra("selectedLevel", selectedLevel);

        intent.putExtra("battery",Float.toString(battery));
        intent.putExtra("bestPath", Integer.toString(bestPath));
        intent.putExtra("pathTaken", Integer.toString(pathTaken));

        startActivity(intent);
    }


    /**
     * Takes the user back to the main screen when the back button is pressed.
     */
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this,AMazeActivity.class);
        startActivity(intent);
    }


}
