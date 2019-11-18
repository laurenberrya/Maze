package edu.wm.cs.cs301.amazebylaurenberry;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.Bundle;

public class GeneratingActivity extends AppCompatActivity {

    private static final String TAG = "Logs";
    private ProgressBar progressBar;
    TextView txt;
    String selectedDriver;
    String selectedAlgorithm;
    String selectedLevel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generating);

        Intent intent = getIntent();
        selectedAlgorithm = intent.getStringExtra("selectedAlgorithm");
        selectedDriver = intent.getStringExtra("selectedDriver");
        selectedLevel = intent.getStringExtra("selectedLevel");


        TextView algorithm = findViewById(R.id.algorithm);
        algorithm.setText("Selected Maze generation algorithm: "+ selectedAlgorithm);

        TextView driver = findViewById(R.id.driver);
        driver.setText("Selected Driver: "+ selectedDriver);

        TextView level = findViewById(R.id.level);
        level.setText("Selected level: "+ selectedLevel);


        txt = txt = (TextView) findViewById(R.id.progress);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);
        progressBar.setVisibility(View.VISIBLE);

        //for now, set incrementally bc maze code not added in this proj
        progressBar.setProgress(0);
        txt.setText("Generating: 0%");
        progressBar.setProgress(50);
        txt.setText("Generating: 50%");
        progressBar.setProgress(100);
        txt.setText("Generating: 100%");

        switchToPlaying();

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
