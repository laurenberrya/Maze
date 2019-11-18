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
 * <p>
 * Responsibilities: Main title screen of the MazeApplication, displayed on startup. Allows
 * the user to select a maze generation algorithm, a driver, and a skill level. Also allows the user
 * to generate a new maze or load an old one
 * <p>
 * Collaborators: activity_amaze.xml describes the layout of this screen, strings used for the text is
 * stored in strings.xml. Will interact with StateTitle along with main class from project 6
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
     * Instantiates UI elements and retrieves intent information from previous state
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //SeedHolder.initSeeds();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //makes new seekBar and sends to skillLevelSeekBar which handles user input
        SeekBar skillLevelSeekBar = findViewById(R.id.seekBar);
        final TextView changingText = findViewById(R.id.changingText);
        changingText.setText("Select Skill Level: ");
        skillLevelSeekBar(skillLevelSeekBar, changingText);


        /*
        creates a new spinner (dropdown menu) for the maze generation algorithm to
        be selected. Had to change class declaration to implement OnItemSelectedListener
        the method onItemSelected listens to user input for these
         */
        Spinner algorithmsSpinner = findViewById(R.id.algorithmSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.algorithms_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        algorithmsSpinner.setAdapter(adapter1);
        algorithmsSpinner.setOnItemSelectedListener(this);


        /*
        creates a spinner for the user to select a driver for the maze
         */
        driversSpinner = findViewById(R.id.driversSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.drivers_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        driversSpinner.setAdapter(adapter2);
        driversSpinner.setOnItemSelectedListener(this);

    }

    /**
     * when the revisit button is selected, load old maze from file
     *
     * @param view
     */
    public void revisitOldMaze(View view) {
        Intent intent = new Intent(this, GeneratingActivity.class);

        if (mazeLevels.containsKey(selectedLevel) && mazeDrivers.containsValue(hashLoc) && mazeAlgs.containsValue(hashLoc)) {
            Toast.makeText(this, "Revisit old maze", Toast.LENGTH_SHORT).show();
            Log.v(TAG, "Revisit old maze");
        }
        else {

            Toast.makeText(this, "No saved maze at that level, generating new one", Toast.LENGTH_SHORT).show();
            Log.v(TAG, "No saved maze at that level, generating new one");
            storeMaze(selectedLevel, selectedDriver, selectedAlgorithm);
        }


        intent.putExtra("selectedAlgorithm", selectedAlgorithm);
        intent.putExtra("selectedDriver", selectedDriver);
        intent.putExtra("selectedLevel", selectedLevel);
        startActivity(intent);
    }


    public void storeMaze(String level, String driver, String alg) {

        Random rand = new Random();

        hashLoc = rand.nextInt(100);

        Log.v(TAG, "put level, driver, and alg in hash map at location: " + Integer.toString(hashLoc));
        mazeLevels.put(level, hashLoc);
        mazeDrivers.put(driver, hashLoc);
        mazeAlgs.put(alg, hashLoc);
    }


    /**
     * when the generate new maze button is clicked, this method is called
     * displays toast message that a new maze will be generated. Sets parameters of user selections
     * to be sent to the next state
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

        startActivity(intent);

    }

    /**
     * This method listens to user interaction with the skill level seek bar
     * everytime the user moves the bar, the selected skill level text above the bar
     * updates. and whenver the user lets go of the bar a toast message displays
     * the selected level
     *
     * @param seekBar      seek bar object created in on creation method.
     * @param changingText text that show above the seek bar (selected level), changes whenever bar is moved
     */
    private void skillLevelSeekBar(SeekBar seekBar, final TextView changingText) {
        if (seekBar != null) {
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    // Write code to perform some action when progress is changed.
                    changingText.setText("Skill Level: " + Integer.toString(seekBar.getProgress()));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // Write code to perform some action when touch is started.
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // Write code to perform some action when touch is stopped.
                    selectedLevel = Integer.toString(seekBar.getProgress());
                    Toast.makeText(AMazeActivity.this, "Current skill is " + seekBar.getProgress(), Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "Current Skill is: " + Integer.toString(seekBar.getProgress()));
                }
            });
        }
    }

    /**
     * listens to user interaction for the spinners (for both the algorithm generation and
     * driver spinners). When an option is selected a toast message displays selected option
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
