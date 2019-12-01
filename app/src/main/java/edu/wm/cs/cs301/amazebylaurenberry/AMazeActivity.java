package edu.wm.cs.cs301.amazebylaurenberry;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Random;


/**
 * Class: AMazeActivity
 *
 * Responsibilities: Main (intro) screen for the app that is displayed upon startup. Allows
 * the user to select a maze generation algorithm, driver, and skill level for the maze. It
 * also allows the user to generate a new maze with the explore button or load an old maze with
 * the revist button.
 *
 * Collaborators: activity_main.xml is the layout for this screen, and strings used on the layout
 * are stored in strings.xml.
 */

public class AMazeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "Logs";

    private String selectedAlgorithm = "DFS";
    private String selectedDriver = "Manual";
    private String selectedLevel = "0";
    Spinner driversSpinner;

    int hashLoc;

    public static HashMap<String, Integer> mazeLevels = new HashMap<String,Integer>();
    public static HashMap<String, Integer> mazeDrivers = new HashMap<String,Integer>();
    public static HashMap<String, Integer> mazeAlgs = new HashMap<String,Integer>();


    /**
     * Creates UI elements and gets intent info from the previous state
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //makes new seekBar and sends to skillLevelSeekBar which handles user input
        SeekBar skillLevelSeekBar = findViewById(R.id.seekBar);
        final TextView changingText = findViewById(R.id.changingText);
        changingText.setText("Select Skill Level: ");
        skillLevelSeekBar(skillLevelSeekBar, changingText);

        //creates a spinner w an adapter for the user to choose a maze gen algorithm from
        Spinner algorithmsSpinner = findViewById(R.id.algorithmSpinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.algorithms_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        algorithmsSpinner.setAdapter(adapter1);
        algorithmsSpinner.setOnItemSelectedListener(this);

        //creates a spinner w an adapter for the user to choose a driver from the maze
        driversSpinner = findViewById(R.id.driversSpinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.drivers_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        driversSpinner.setAdapter(adapter2);
        driversSpinner.setOnItemSelectedListener(this);
    }


    /**
     * Method that loads an old maze when the revisit button is pressed. Relies on hashmaps that store
     * old level, driver, and algorithm location. Calls the helper method storeMaze if there hasn't been
     * a maze already played at that level. Idea for project 7 is to further reload using the seed,
     * but that info isn't available without importing the whole java maze proj.
     *
     * @param view
     */
    public void revisitOldMaze(View view) {
        Intent intent = new Intent(this, GeneratingActivity.class);

        if (mazeLevels.containsKey(selectedLevel) && mazeDrivers.containsValue(hashLoc) && mazeAlgs.containsValue(hashLoc)) {
            Toast.makeText(this, "Revisit old maze", Toast.LENGTH_SHORT).show();
            Log.v(TAG, "Revisit old maze");
            intent.putExtra("deterministic", "true");
        }
        else {

            Toast.makeText(this, "No saved maze at that level, generating new one", Toast.LENGTH_SHORT).show();
            Log.v(TAG, "No saved maze at that level, generating new one");
            storeMaze(selectedLevel, selectedDriver, selectedAlgorithm);
            intent.putExtra("deterministic", "false");
        }

        intent.putExtra("selectedAlgorithm", selectedAlgorithm);
        intent.putExtra("selectedDriver", selectedDriver);
        intent.putExtra("selectedLevel", selectedLevel);
        startActivity(intent);
    }


    /**
     * Helper method called in revistOldMaze. Stores the current maze details in hashmaps at the same
     * value to be later accessed if needed.
     *
     * @param level
     * @param driver
     * @param alg
     */
    public void storeMaze(String level, String driver, String alg) {
        Random rand = new Random();
        hashLoc = rand.nextInt(100);

        Log.v(TAG, "put level, driver, and alg in hash map at location: " + Integer.toString(hashLoc));
        mazeLevels.put(level, hashLoc);
        mazeDrivers.put(driver, hashLoc);
        mazeAlgs.put(alg, hashLoc);
    }


    /**
     * This method is called when the explore button is pressed. It displays a toast that the maze is
     * generating and then calls GeneratingActivity.
     *
     * @param view
     */
    public void generateNewMazeSelected(View view) {
        Toast.makeText(this, "Generating new maze", Toast.LENGTH_SHORT).show();

        storeMaze(selectedLevel, selectedDriver, selectedAlgorithm);
        Log.v(TAG, "Generating new maze");

        Intent intent = new Intent(this, GeneratingActivity.class);
        intent.putExtra("selectedAlgorithm", selectedAlgorithm);
        intent.putExtra("selectedDriver", selectedDriver);
        intent.putExtra("selectedLevel", selectedLevel);
        intent.putExtra("deterministic", "false");

        startActivity(intent);
    }

    /**
     * This method changes the textView display of the selected skill level based on the user's
     * movement of the little circle on the seekBar. A toast is displayed giving the selected
     * level when the user lets go of the bar.
     *
     * @param seekBar      seek bar object initiated in onCreate method
     * @param changingText text shown above the seek bar displaying the selected level
     */
    private void skillLevelSeekBar(SeekBar seekBar, final TextView changingText) {
        if (seekBar != null) {
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    changingText.setText("Skill Level: " + Integer.toString(seekBar.getProgress()));
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    //nothing here
                }
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    selectedLevel = Integer.toString(seekBar.getProgress());
                    Toast.makeText(AMazeActivity.this, "Current skill is " + seekBar.getProgress(), Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "Current Skill is: " + Integer.toString(seekBar.getProgress()));
                }
            });
        }
    }


    /**
     * Takes user input for both algorithm and driver spinners and displays the options the user chose
     * with a toast.
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String itemSelected = parent.getItemAtPosition(position).toString();
        if (itemSelected == "Manual" || itemSelected == "WallFollower" || itemSelected == "Wizard") {
            selectedDriver = itemSelected;
        }
        else {
            selectedAlgorithm = itemSelected;
        }
        Toast.makeText(AMazeActivity.this, "Selected " + itemSelected, Toast.LENGTH_SHORT).show();
        Log.v(TAG, "Selected " + itemSelected);
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //nothing here
    }

}
