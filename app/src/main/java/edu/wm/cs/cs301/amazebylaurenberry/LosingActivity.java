package edu.wm.cs.cs301.amazebylaurenberry;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;


public class LosingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_losing);

        TextView energyConsumption = findViewById(R.id.energyConsumption);
        TextView shortestPath = findViewById(R.id.shortestPath);
        TextView actualPath = findViewById(R.id.actualPath);


        energyConsumption.setText(getString(R.string.energyConsumed));


        shortestPath.setText(getString(R.string.shortestPossiblePathLength));
        actualPath.setText(getString(R.string.lengthOfYourPath));

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
