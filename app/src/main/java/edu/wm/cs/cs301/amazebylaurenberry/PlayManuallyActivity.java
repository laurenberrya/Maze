package edu.wm.cs.cs301.amazebylaurenberry;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Canvas;
import android.media.Image;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Random;

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


        Intent intent = getIntent();
        selectedAlgorithm = intent.getStringExtra("selectedAlgorithm");
        selectedDriver = intent.getStringExtra("selectedDriver");
        selectedLevel = intent.getStringExtra("selectedLevel");



        remainingBattery = findViewById(R.id.remainingBattery);

        remainingBattery.setText(getString(R.string.remainingBattery)+"3000");

        //for now
        bestPath = 50;



        int randWin = new Random().nextInt(2);
        Handler handler = new Handler();
        if(randWin ==0){

            handler.postDelayed(new Runnable() {
                public void run() {
                    go2Losing();
                }
            }, 5000);   //5 seconds

        }
        if(randWin==1){
            handler.postDelayed(new Runnable() {
                public void run() {
                    go2winning();
                }
            }, 5000);   //5 seconds

        }

    }

    /**
     * Responds to user click, uses switch statement to determine which item was selected and executes
     * desired functionality of that item
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.toggleMap:


                if(toggleMap.isChecked())
                {
                    toggleMap.setChecked(true);
                    Log.v(TAG,"Showing Local Map" );

                }
                else
                {
                    toggleMap.setChecked(false);
                    Log.v(TAG,"Hiding Local Map" );
                }



                break;
            case R.id.toggleSolution:

                if(toggleSolution.isChecked())
                {
                    toggleSolution.setChecked(true);
                    Log.v(TAG,"Showing Solution" );

                }
                else
                {
                    toggleSolution.setChecked(false);
                    Log.v(TAG,"Hiding Solution" );
                }

                break;
            case R.id.toggleWalls:

                if(toggleWalls.isChecked())
                {
                    toggleWalls.setChecked(true);
                    Log.v(TAG,"Showing full map" );

                }
                else
                {
                    toggleWalls.setChecked(false);
                    Log.v(TAG,"Hiding full map" );
                }

                break;

            case R.id.incrementButton:

                Log.v(TAG,"Increment size" );
                break;

            case R.id.decrementButton:

                Log.v(TAG,"Decrement size" );
                break;

            case R.id.leftKey:
                Log.v(TAG,"Rotate Left" );
                break;

            case R.id.rightKey:
                Log.v(TAG,"Rotate Right" );
                break;

            case R.id.upKey:
                Log.v(TAG,"Move Forward" );
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

        final Intent intent = new Intent(this, LosingActivity.class);
        intent.putExtra("selectedAlgorithm",selectedAlgorithm);
        intent.putExtra("selectedDriver", selectedDriver);
        intent.putExtra("selectedLevel", selectedLevel);

        intent.putExtra("battery",Float.toString(battery));
        intent.putExtra("bestPath", Integer.toString(bestPath));
        intent.putExtra("pathTaken", Integer.toString(pathTaken));

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                startActivity(intent);
            }
        }, 5000);   //5 seconds

      //  startActivity(intent);
    }

    /**
     * Helper method that takes you to winning screen
     */
    private void go2winning(){

        //since we haven't incorporated the robot yet
        battery = 3000;
        pathTaken = 100;


        final Intent intent = new Intent(this, WinningActivity.class);
        intent.putExtra("battery",Float.toString(battery));
        intent.putExtra("bestPath", Integer.toString(bestPath));
        intent.putExtra("pathTaken", Integer.toString(pathTaken));

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                startActivity(intent);
            }
        }, 5000);   //5 seconds


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
