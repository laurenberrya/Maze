package edu.wm.cs.cs301.amazebylaurenberry;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

public class WinningActivity extends AppCompatActivity {


    private static final String TAG = "Logs";

    String battery;
    String bestPath;
    String pathTaken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning);

        Intent intent = getIntent();
        battery = intent.getStringExtra("battery");
        bestPath = intent.getStringExtra("bestPath");
        pathTaken = intent.getStringExtra("pathTaken");

        Log.v(TAG, "battery: " +battery);
        Log.v(TAG, "bestPath: " +bestPath);
        Log.v(TAG, "pathTaken: " +pathTaken);

        TextView energyConsumption = findViewById(R.id.energyConsumption);
        TextView shortestPath = findViewById(R.id.shortestPath);
        TextView actualPath = findViewById(R.id.actualPath);

        energyConsumption.setText(getString(R.string.energyConsumed)+" "+battery);
        shortestPath.setText(getString(R.string.shortestPossiblePathLength) + bestPath);
        actualPath.setText(getString(R.string.lengthOfYourPath)+ pathTaken);


    }
    /**
     * whenever the back button is pressed we want the game to go back to the main screen
     * so that the user can restart from scratch.
     */
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this,AMazeActivity.class);
        startActivity(intent);
    }
}
