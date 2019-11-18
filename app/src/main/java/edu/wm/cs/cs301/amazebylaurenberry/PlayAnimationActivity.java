package edu.wm.cs.cs301.amazebylaurenberry;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.util.Random;

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
    public static Handler aHandler;
    float battery;
    int bestPath;
    int pathTaken;


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

        remainingBattery.setText(getString(R.string.remainingBattery)+"3000");

        //for now
        bestPath = 50;


        int randWin = new Random().nextInt(2);
        if(randWin ==0){
            go2Losing();
        }
        if(randWin==1){
            go2winning();
        }

    }


    /**
     * listens and responds to user clicks. The cases are easily understood without comments
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toggleMap:


                if (toggleMap.isChecked()) {
                    toggleMap.setChecked(true);

                    Toast.makeText(PlayAnimationActivity.this, "Showing Map", Toast.LENGTH_LONG).show();
                    Log.v(TAG,"Showing Map" );

                } else {
                    toggleMap.setChecked(false);
                    Toast.makeText(PlayAnimationActivity.this, "Hiding Map", Toast.LENGTH_LONG).show();
                    Log.v(TAG,"Hiding Map" );
                }


                break;
            case R.id.toggleSolution:

                if (toggleSolution.isChecked()) {
                    toggleSolution.setChecked(true);
                    Toast.makeText(PlayAnimationActivity.this, "Showing Solution", Toast.LENGTH_LONG).show();
                    Log.v(TAG,"Showing Solution" );

                } else {
                    toggleSolution.setChecked(false);
                    Toast.makeText(PlayAnimationActivity.this, "Hiding Solution", Toast.LENGTH_LONG).show();
                    Log.v(TAG,"Hiding Solution" );
                }

                break;
            case R.id.toggleWalls:


                if (toggleWalls.isChecked()) {
                    toggleWalls.setChecked(true);
                    Toast.makeText(PlayAnimationActivity.this, "Showing Visited Walls", Toast.LENGTH_LONG).show();
                    Log.v(TAG,"Showing Visited Walls" );

                } else {
                    toggleWalls.setChecked(false);
                    Toast.makeText(PlayAnimationActivity.this, "Hiding Visited Walls", Toast.LENGTH_LONG).show();
                    Log.v(TAG,"Hiding Visited Walls" );
                }

                break;

            case R.id.incrementButton:

               // state.keyDown(Constants.UserInput.ZoomIn,1);

                Toast.makeText(PlayAnimationActivity.this, "Increment Size", Toast.LENGTH_LONG).show();
                Log.v(TAG,"Increment Size" );
                break;

            case R.id.decrementButton:

               // state.keyDown(Constants.UserInput.ZoomOut,1);

                Toast.makeText(PlayAnimationActivity.this, "Decrement Size", Toast.LENGTH_LONG).show();
                Log.v(TAG,"Decrement Size" );
                break;




            case R.id.pauseButton:
                Toast.makeText(PlayAnimationActivity.this, "Pausing Animation", Toast.LENGTH_LONG).show();
                Log.v(TAG,"Pausing Animation" );
                pauseButton.setVisibility(View.INVISIBLE);
               // driver.pause();

                startButton.setVisibility(View.VISIBLE);
                break;

            case R.id.startButton:
                Toast.makeText(PlayAnimationActivity.this, "Resuming Animation", Toast.LENGTH_LONG).show();
                Log.v(TAG,"Resuming Animation" );
                //driver.pause();
                startButton.setVisibility(View.INVISIBLE);
                pauseButton.setVisibility(View.VISIBLE);


                break;

            default:
                break;
        }

        // if the map or visited walls is being displayed then we want the zoom buttons to
        //be visible and usable, otherwise we don't want them there to confuse the user
        if (toggleMap.isChecked()||toggleWalls.isChecked()){
            incrementButton.setVisibility(View.VISIBLE);
            decrementButton.setVisibility(View.VISIBLE);
        }
        else{
            incrementButton.setVisibility(View.INVISIBLE);
            decrementButton.setVisibility(View.INVISIBLE);
        }
    }




    /**
     * Helper method that takes you to losing screen
     */
    private void go2Losing() {
        //since we haven't incorporated the robot yet
        battery = 3000;
        pathTaken = 100;

        Intent intent = new Intent(this, LosingActivity.class);
        intent.putExtra("selectedAlgorithm",selectedAlgorithm);
        intent.putExtra("selectedDriver", selectedDriver);
        intent.putExtra("selectedLevel", selectedLevel);

        intent.putExtra("battery",Float.toString(battery));
        intent.putExtra("bestPath", Integer.toString(bestPath));
        intent.putExtra("pathTaken", Integer.toString(pathTaken));

        startActivity(intent);
    }


    /**
     * Helper method that takes you to winning screen
     */
    private void go2winning(){

        //since we haven't incorporated the robot yet
        battery = 3000;
        pathTaken = 100;


        Intent intent = new Intent(this, WinningActivity.class);
        intent.putExtra("battery",Float.toString(battery));
        intent.putExtra("bestPath", Integer.toString(bestPath));
        intent.putExtra("pathTaken", Integer.toString(pathTaken));
        startActivity(intent);
    }

    /**
     * whenever the back button is pressed we want the game to go back to the main screen
     * so that the user can restart from scratch
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, AMazeActivity.class);
        startActivity(intent);
    }

}
