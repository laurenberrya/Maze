package edu.wm.cs.cs301.amazebylaurenberry;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import edu.wm.cs.cs301.amazebylaurenberry.generation.Maze;
import edu.wm.cs.cs301.amazebylaurenberry.gui.BasicRobot;
import edu.wm.cs.cs301.amazebylaurenberry.gui.Constants;
import edu.wm.cs.cs301.amazebylaurenberry.gui.Explorer;
import edu.wm.cs.cs301.amazebylaurenberry.gui.ManuallyDriver;
import edu.wm.cs.cs301.amazebylaurenberry.gui.MazePanel;
import edu.wm.cs.cs301.amazebylaurenberry.gui.Robot;
import edu.wm.cs.cs301.amazebylaurenberry.gui.RobotDriver;
import edu.wm.cs.cs301.amazebylaurenberry.gui.StatePlaying;
import edu.wm.cs.cs301.amazebylaurenberry.gui.WallFollower;
import edu.wm.cs.cs301.amazebylaurenberry.gui.Wizard;


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

    private ToggleButton toggleSolution;
    private ToggleButton toggleMap;
    private ToggleButton toggleWalls;
    private ImageButton decrementButton;
    private ImageButton incrementButton;
    private ImageButton pauseButton;
    private ImageButton startButton;
    private TextView remainingBattery;
    String selectedDriver;
    String selectedAlgorithm;
    String selectedLevel;
    float battery;
    int bestPath;
    int pathTaken;

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

        remainingBattery = findViewById(R.id.remainingBattery);

        remainingBattery.setText(getString(R.string.remainingBattery) + "3000");

        //for now
        bestPath = 50;

    }


    /**
     * Responds to user clicks on buttons
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.toggleMap) {
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
            Log.v(TAG, "Increment Size");
        }

        if (v.getId() == R.id.decrementButton) {
            Log.v(TAG, "Decrement Size");
        }

        if (v.getId() == R.id.pauseButton) {
            Log.v(TAG, "Pausing Animation");
            pauseButton.setVisibility(View.INVISIBLE);
            startButton.setVisibility(View.VISIBLE);
        }

        if (v.getId() == R.id.startButton) {
            Log.v(TAG, "Resuming Animation");
            startButton.setVisibility(View.INVISIBLE);
            pauseButton.setVisibility(View.VISIBLE);
        }


        // if the map or walls are being displayed then increment/decrement buttons should
        //be visible, otherwise we want them invisible as to not confuse the user
        if (toggleMap.isChecked() || toggleWalls.isChecked()) {
            incrementButton.setVisibility(View.VISIBLE);
            decrementButton.setVisibility(View.VISIBLE);
        }
        if (!toggleMap.isChecked() && !toggleWalls.isChecked()){
            incrementButton.setVisibility(View.INVISIBLE);
            decrementButton.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * Method that takes you to losing screen
     */
    public void go2losing(View view) {
        //since we haven't incorporated the robot yet
        battery = 3000;
        pathTaken = 100;

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
    public void go2winning(View view) {

        //since we haven't incorporated the robot yet
        battery = 3000;
        pathTaken = 100;

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
