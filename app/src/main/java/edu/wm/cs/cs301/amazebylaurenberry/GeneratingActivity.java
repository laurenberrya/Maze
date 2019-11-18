package edu.wm.cs.cs301.amazebylaurenberry;

import androidx.appcompat.app.AppCompatActivity;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Bundle;

public class GeneratingActivity extends AppCompatActivity {

    private static final String TAG = "Logs";
    private ProgressBar progressBar;
    TextView txt;
    Integer count =1;
    String selectedDriver;
    String selectedAlgorithm;
    String selectedLevel;
    Order.Builder builder;
    StubOrder state;

    MazeFactory factory;
    MazeConfiguration maze;
    String deterministic = "false";

    public static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generating);

        Intent intent = getIntent();
        selectedAlgorithm = intent.getStringExtra("selectedAlgorithm");
        selectedDriver = intent.getStringExtra("selectedDriver");
        selectedLevel = intent.getStringExtra("selectedLevel");
        deterministic = intent.getStringExtra("deterministic");
        //Log.v(TAG, deterministic);
        if (deterministic.equals("true")){
            factory= new MazeFactory(true, selectedLevel);
            Log.v(TAG, "deterministic factory");
        }
        else{
            factory = new MazeFactory(selectedLevel);
            Log.v(TAG, "NON deterministic factory");
        }

        //Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        TextView algorithm = findViewById(R.id.algorithm);
        algorithm.setText("Selected Maze generation algorithm: "+ selectedAlgorithm);

        TextView driver = findViewById(R.id.driver);
        driver.setText("Selected Driver: "+ selectedDriver);

        TextView level = findViewById(R.id.level);
        level.setText("Selected level: "+ selectedLevel);


        txt = txt = (TextView) findViewById(R.id.progress);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);

        count =1;
        progressBar.setVisibility(VISIBLE);
        progressBar.setProgress(0);

        state = new StubOrder();

        builder = determineBuilder();

        //state.setIsPerfect(isPerfect);
        state.setSkillLevel(Integer.parseInt(selectedLevel));
        state.setBuilder(builder);
        //factory.order(state);
        //factory.waitTillDelivered();


        // handler used to received messages from the background thread that
        //generates the maze. Updates progress bar
        handler = new Handler(){

            public void handleMessage(Message msg) {
                /* get the value from the Message */
                int progress = msg.arg1;
                txt.setText("Generating..."+ Integer.toString(progress) + "%");
                progressBar.setProgress(progress);
            }
        };


        new MyTask().execute(100);


        //maze = state.getMazeConfiguration();

    }

    /**
     * simple method that takes selected builder as a string and converts it to the
     * necessary enum builder
     * @return
     */
    private Order.Builder determineBuilder() {
        Order.Builder build;
        if (selectedAlgorithm.equals("Default")){
            build = Order.Builder.DFS;
        }
        else if (selectedAlgorithm.equals("Eller")){
            build = Order.Builder.Eller;
        }
        else {
            build = Order.Builder.Prim;
        }
        return build;
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
            //progressBar.setVisibility(View.GONE);
            maze = state.getMazeConfiguration();
            //Toast.makeText(GeneratingActivity.this, maze.toString(), Toast.LENGTH_LONG).show();
            MazeHolder.setData(maze);
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
            txt.setText("Generating..."+ values[0] + "%");
            progressBar.setProgress(values[0]);
        }





    }

    /**
     * switches to the correct playing screen, ie if the selected driver was manual then
     * it switches to PlayManuallyActivity, otherwise it switches ot the PlayAnimationActivity.
     */
    private void switchToPlaying(){
        Intent intent;

        if (selectedDriver.equals("Manual")){

            intent = new Intent(this, PlayManuallyActivity.class);

        }
        else{
            intent = new Intent(this, PlayAnimationActivity.class);
        }
        intent.putExtra("selectedAlgorithm",selectedAlgorithm);
        intent.putExtra("selectedDriver", selectedDriver);
        intent.putExtra("selectedLevel", selectedLevel);
        startActivity(intent);


    }




}
