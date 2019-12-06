package edu.wm.cs.cs301.amazebylaurenberry;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;
import android.os.Vibrator;

/**
 * Class: WinningActivity
 *
 * Responsibility: Displays that the user has won, along with directions on how to restart,
 * overall energy consumption, the length of the path taken and the length of the shortest
 * possible path.
 *
 * Collaborators: activity_winning.xml is the layout for this screen, and strings used on the layout
 * are stored in strings.xml. The class is called from one of the playing screens, and can go back
 * to the title screen if the back button is pressed.
 */

public class WinningActivity extends AppCompatActivity {


    private static final String TAG = "Logs";

    String battery;
    String bestPath;
    String pathTaken;
    Vibrator vibrator;


    /**
     * Creates UI elements and gets intent info from the previous state
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning);

        Intent intent = getIntent();
        battery = intent.getStringExtra("battery");
        bestPath = intent.getStringExtra("bestPath");
        pathTaken = intent.getStringExtra("pathTaken");

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        vibrator.vibrate(200);

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
     * Takes the user back to the main screen when the back button is pressed.
     */
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this,AMazeActivity.class);
        startActivity(intent);
    }
}
