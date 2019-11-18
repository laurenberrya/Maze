package edu.wm.cs.cs301.amazebylaurenberry;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Canvas;
import android.media.Image;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class PlayManuallyActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "Logs";
    private ToggleButton toggleSolution;
    private ToggleButton toggleMap;
    private ToggleButton toggleWalls;
    private ImageButton zoomOutButton;
    private ImageButton zoomInButton;
    private MazePanel playScreen;
    String selectedDriver;
    String selectedAlgorithm;
    String selectedLevel;
    StatePlaying state;
    Robot robot;
    ManualDriver driver;
    int bestPath;
    int pathTaken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_manually);
        zoomInButton = findViewById(R.id.zoomIn);
        zoomOutButton= findViewById(R.id.zoomOut);

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
        // Canvas canvas = new Canvas();
        //playScreen.draw(canvas);

        Intent intent = getIntent();
        selectedAlgorithm = intent.getStringExtra("selectedAlgorithm");
        selectedDriver = intent.getStringExtra("selectedDriver");
        selectedLevel = intent.getStringExtra("selectedLevel");

        //gets the generated maze
        MazeConfiguration maze  = MazeHolder.getData();
        //Toast.makeText(PlayManuallyActivity.this, maze.toString(), Toast.LENGTH_LONG).show();

        robot = new BasicRobot();
        driver = new ManualDriver();
        //set BatteryLevel must be set before setRobot so that the initial battery
        //level can be establised in ManualDriver
        robot.setBatteryLevel(3000);
        driver.setRobot(robot);

        //controller.setRobotAndDriver(robot, Driver);
        //allows the robot to access the controller
        //robot.setMaze(controller);

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
     * Responds to user click, uses switch statement to determine which item was selected and executes
     * desired functionality of that item
     * @param v
     */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.toggleMap:

                state.keyDown(Constants.UserInput.ToggleLocalMap,1);

                if(toggleMap.isChecked())
                {
                    toggleMap.setChecked(true);

                    //Toast.makeText(PlayManuallyActivity.this, "Showing Map", Toast.LENGTH_LONG).show();
                    Log.v(TAG,"Showing Local Map" );

                }
                else
                {
                    toggleMap.setChecked(false);
                    //Toast.makeText(PlayManuallyActivity.this, "Hiding Map", Toast.LENGTH_LONG).show();
                    Log.v(TAG,"Hiding Local Map" );
                }



                break;
            case R.id.toggleSolution:

                state.keyDown(Constants.UserInput.ToggleSolution,1);

                if(toggleSolution.isChecked())
                {
                    toggleSolution.setChecked(true);
                    //Toast.makeText(PlayManuallyActivity.this, "Showing Solution", Toast.LENGTH_LONG).show();
                    Log.v(TAG,"Showing Solution" );

                }
                else
                {
                    toggleSolution.setChecked(false);
                    //Toast.makeText(PlayManuallyActivity.this, "Hiding Solution", Toast.LENGTH_LONG).show();
                    Log.v(TAG,"Hiding Solution" );
                }

                break;
            case R.id.toggleWalls:

                state.keyDown(Constants.UserInput.ToggleFullMap,1);

                if(toggleWalls.isChecked())
                {
                    toggleWalls.setChecked(true);
                    //Toast.makeText(PlayManuallyActivity.this, "Showing Visited Walls", Toast.LENGTH_LONG).show();
                    Log.v(TAG,"Showing full map" );

                }
                else
                {
                    toggleWalls.setChecked(false);
                    //Toast.makeText(PlayManuallyActivity.this, "Hiding Visited Walls", Toast.LENGTH_LONG).show();
                    Log.v(TAG,"Hiding full map" );
                }

                break;

            case R.id.zoomIn:

                state.keyDown(Constants.UserInput.ZoomIn,1);

                //Toast.makeText(PlayManuallyActivity.this, "Zoom In", Toast.LENGTH_LONG).show();
                Log.v(TAG,"Zoom In" );
                break;

            case R.id.zoomOut:

                state.keyDown(Constants.UserInput.ZoomOut,1);

                //Toast.makeText(PlayManuallyActivity.this, "Zoom Out", Toast.LENGTH_LONG).show();
                Log.v(TAG,"Zoom Out" );
                break;

            case R.id.leftKey:
                //Toast.makeText(PlayManuallyActivity.this, "Rotate Left", Toast.LENGTH_LONG).show();
                Log.v(TAG,"Rotate Left" );
                driver.keyDown(Constants.UserInput.Left);
                break;

            case R.id.rightKey:
                // Toast.makeText(PlayManuallyActivity.this, "Rotate Right", Toast.LENGTH_LONG).show();
                Log.v(TAG,"Rotate Right" );
                driver.keyDown(Constants.UserInput.Right);
                break;

            case R.id.upKey:
                //Toast.makeText(PlayManuallyActivity.this, "Move Forward", Toast.LENGTH_LONG).show();

                Log.v(TAG,"Move Forward" );
                driver.keyDown(Constants.UserInput.Up);
                if (state.hasWon == true){
                    go2winning();
                }
                break;





            default:
                break;
        }

        // if the map or visited walls is being displayed then we want the zoom buttons to
        //be visible and usable, otherwise we don't want them there to confuse the user
        if (toggleMap.isChecked()||toggleWalls.isChecked()){
            zoomInButton.setVisibility(View.VISIBLE);
            zoomOutButton.setVisibility(View.VISIBLE);
        }
        else{
            zoomInButton.setVisibility(View.INVISIBLE);
            zoomOutButton.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * simply switches to the winning activity
     */
    public void go2winning(){

        pathTaken = robot.getOdometerReading();

        Intent intent = new Intent(this, WinningActivity.class);
        intent.putExtra("bestPath",Integer.toString(bestPath));
        intent.putExtra("selectedDriver", selectedDriver);
        intent.putExtra("pathTaken", Integer.toString(pathTaken));
        startActivity(intent);
    }
    /**
     * whenever the back button is pressed we want the game to go back to the main screen
     * so that the user can restart from scratch
     */
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this,AMazeActivity.class);
        startActivity(intent);
    }


}
