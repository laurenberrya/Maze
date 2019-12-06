package edu.wm.cs.cs301.amazebylaurenberry;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.Bundle;
import android.util.Log;
import android.os.AsyncTask;
import android.os.Message;


import edu.wm.cs.cs301.amazebylaurenberry.generation.Maze;
import edu.wm.cs.cs301.amazebylaurenberry.generation.MazeFactory;
import edu.wm.cs.cs301.amazebylaurenberry.generation.Order;
import edu.wm.cs.cs301.amazebylaurenberry.generation.StoreMaze;
import edu.wm.cs.cs301.amazebylaurenberry.generation.StubOrder;

/**
 * Class: GeneratingActivity
 *
 * Responsibilities: Intermediate page in between the main and playing screen. Displays
 * user selections for their maze generation and also shows a progress bar that is updated via a
 * Handler. Once the progress bar is full, the screen switches to a playing screen, either
 * PlayAnimationActivity or PlayManuallyActivity.
 *
 * Collaborators: activity_generating.xml is the layout for this screen, and strings used on the layout
 * are stored in strings.xml. The class transitions to PlayManuallyActivity if a manual driver was
 * selected, otherwise it goes to PlayAnimationActivity.
 */

public class GeneratingActivity extends AppCompatActivity {

    private static final String TAG = "Logs";
    private ProgressBar progressBar;
    TextView txt;
    String selectedDriver;
    String selectedAlgorithm;
    String selectedLevel;

    Order.Builder builder;
    StubOrder state;

    MazeFactory factory;
    Maze maze;
    String deterministic = "false";

    public static Handler handler;

    /**
     * Creates UI elements and gets intent info from the previous state
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generating);

        Intent intent = getIntent();
        selectedAlgorithm = intent.getStringExtra("selectedAlgorithm");
        selectedDriver = intent.getStringExtra("selectedDriver");
        selectedLevel = intent.getStringExtra("selectedLevel");
        deterministic = intent.getStringExtra("deterministic");

        TextView algorithm = findViewById(R.id.algorithm);
        algorithm.setText("Selected Maze generation algorithm: "+ selectedAlgorithm);

        TextView driver = findViewById(R.id.driver);
        driver.setText("Selected Driver: "+ selectedDriver);

        TextView level = findViewById(R.id.level);
        level.setText("Selected level: "+ selectedLevel);

        if (deterministic == "true"){
            factory= new MazeFactory(true, selectedLevel);
            Log.v(TAG, "det factory");
        }
        else{
            factory = new MazeFactory(selectedLevel);
            Log.v(TAG, "not det factory");
        }


        txt  = (TextView) findViewById(R.id.progress);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);
        progressBar.setVisibility(View.VISIBLE);


        state = new StubOrder();

        if (selectedAlgorithm.equals("Default")){
            builder = Order.Builder.DFS;
        }
        else if (selectedAlgorithm.equals("Prim")){
            builder = Order.Builder.Prim;
        }
        else {
            builder = Order.Builder.Eller;
        }


        state.setSkill(Integer.parseInt(selectedLevel));
        state.setBuilder(builder);


        ///// START HERE ///////////

       /* Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                //for now, set incrementally bc maze code not added in this proj
                progressBar.setProgress(0);
                txt.setText("Generating: 0%");
            }
        }, 2000);   //2 seconds
        handler.postDelayed(new Runnable() {
            public void run() {
                progressBar.setProgress(50);
                txt.setText("Generating: 50%");
            }
        }, 2000);   //2 seconds
        handler.postDelayed(new Runnable() {
            public void run() {
                progressBar.setProgress(100);
                txt.setText("Generating: 100%");
            }
        }, 2000);   //2 seconds

        handler.postDelayed(new Runnable() {
            public void run() {
                switchToPlaying();
            }
        }, 2000); */  //2 seconds

        // handler used to received messages from the background thread that
        //generates the maze. Updates progress bar
        handler = new Handler(){

            public void handleMessage(Message msg) {
                /* get the value from the Message */
                int progress = msg.arg1;
                txt.setText( Integer.toString(progress) + "% done");
                progressBar.setProgress(progress);
            }
        };


        new MyTask().execute(100);

    }



    /**
     * Class that runs the maze generation from a background thread so that the UI doesn't freeze up
     * extends the AsyncTask class to do so
     */
    public class  MyTask extends AsyncTask<Integer, Integer, String> {
        /**
         * what the thread is doing, for now just simulates generation
         * @param params
         * @return
         */




        @Override
        protected String doInBackground(Integer... params) {
            //for (; count <= params[0]; count++) {
            //Thread.sleep(1);
            factory.order(state);
            factory.waitTillDelivered();
            //publishProgress(count);
            //}
            return "Task Completed.";
        }

        /**
         * once the thread/generation is finished, switch to playing screen
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            maze = state.getConfiguration();
            StoreMaze.setWholeMaze(maze);
            txt.setText(result);
            switchToPlaying();
            Log.v(TAG, "Finished Generating");

        }

        /**
         * nothing
         */
        @Override
        protected void onPreExecute() {

            txt.setText("Task Starting...");
        }

        /**
         * updates graphics of the progress bar in relation to the generation progress
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            txt.setText(values[0] + "% done");
            progressBar.setProgress(values[0]);
        }





    }



    /**
     * Switches to the corresponding playing screen based on user selection from the title screen.
     * If a manual driver was selected, it goes to PlayManuallyActivity, otherwise it goes to
     * PlayAnimationActivity.
     */
    private void switchToPlaying(){
        Intent intent;

        if (selectedDriver.equals("Manual")){
            intent = new Intent(this, PlayManuallyActivity.class);
        }
        else{
            intent = new Intent(this, PlayAnimationActivity.class);
            Log.v(TAG, selectedDriver);
        }
        intent.putExtra("selectedAlgorithm",selectedAlgorithm);
        intent.putExtra("selectedDriver", selectedDriver);
        intent.putExtra("selectedLevel", selectedLevel);
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
