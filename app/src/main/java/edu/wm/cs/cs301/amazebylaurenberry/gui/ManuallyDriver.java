package edu.wm.cs.cs301.amazebylaurenberry.gui;

import edu.wm.cs.cs301.amazebylaurenberry.generation.Distance;
import edu.wm.cs.cs301.amazebylaurenberry.generation.Maze;
import edu.wm.cs.cs301.amazebylaurenberry.gui.Constants.UserInput;
import edu.wm.cs.cs301.amazebylaurenberry.gui.Robot.Turn;
import edu.wm.cs.cs301.amazebylaurenberry.generation.CardinalDirection;
import edu.wm.cs.cs301.amazebylaurenberry.gui.Robot.Direction;

/**
 *
 * Responsibilities: This class interacts with a BasicRobot object and
 * uses manual key input to drive a robot. Most of the functionality of the robot is
 * encapuslated in teh BasicRobot class, but within this class is where the manual
 * actions are registered and passed on. If a MazeApplication has a ManualDriver, then SimpleKeylistener
 * will execute the KeyDown method within this class if one of 3 keys is selected (up, left, right)
 * Any other selected key will execute the normal defualt functionality of a maze without a ManualDriver
 *
 * Collaborators: SimpleKeyListener, Controller (by interacting with object of BasicRobot)
 *
 * Implementing classes: MazeApplication
 *
 * @author Gavin Burkholder
 *
 */

public class ManuallyDriver implements RobotDriver {

    private Robot robot;
    private int width;
    private int height;
    private float initialBattery;
    private Distance distance;
    private StatePlaying state;


    /**
     * Allows this class to be able to access the functionality of a BasicRobot
     * Called in the MazeApplication constructor. Also sets the inital battery to
     * the robot's battery before any actions are made .
     */
    @Override
    public void setRobot(Robot r) {
        // TODO Auto-generated method stub
        robot = r;
        initialBattery = robot.getBatteryLevel();


    }
    /**
     * Provides the robot driver with information on the dimensions
     *  of the 2D maze measured in the number of cells in each direction.
     */
    @Override
    public void setDimensions(int w, int h) {
        // TODO Auto-generated method stub
        width = w;
        height=h;
    }

    /**
     * Provides the robot driver with information on the distance to the exit.
     * Only some drivers such as the wizard rely on this information to find the exit.
     */
    @Override
    public void setDistance(Distance dist) {
        // TODO Auto-generated method stub
        distance = dist;
    }
    /**
     * not used in this Class
     */
    @Override
    public boolean drive2Exit() throws Exception {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * returns how much energy the robot has thus far consumed
     */
    @Override
    public float getEnergyConsumption() {
        // TODO Auto-generated method stub
        return initialBattery-robot.getBatteryLevel();
    }
    /**
     * returns how far the robot has thus far travelled
     */
    @Override
    public int getPathLength() {
        // TODO Auto-generated method stub
        return robot.getOdometerReading();
    }

    /**
     * Tells the driver to check its robot for operational sensor.
     * If one or more of the robot's distance sensor become
     * operational again after a repair operation, this method
     * allows to make the robot driver aware of this change
     * and to bring its understanding of which sensors are operational
     * up to date.
     */
    @SuppressWarnings("static-access")
    public void triggerUpdateSensorInformation(boolean left, boolean right, boolean forward, boolean backward) {
        int delta_t = 3000;

        Thread leftT = new Thread();
        leftT.start();
        Thread rightT = new Thread();
        rightT.start();
        Thread forwardT = new Thread();
        forwardT.start();
        Thread backwardT = new Thread();
        backwardT.start();

        while (left) {
            robot.triggerSensorFailure(Direction.LEFT);
            try {
                leftT.sleep(delta_t);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            robot.repairFailedSensor(Direction.LEFT);
            try {
                leftT.sleep(delta_t);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while (right) {
            robot.triggerSensorFailure(Direction.RIGHT);
            try {
                rightT.sleep(delta_t);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            robot.repairFailedSensor(Direction.RIGHT);
            try {
                rightT.sleep(delta_t);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while (forward) {
            robot.triggerSensorFailure(Direction.FORWARD);
            try {
                forwardT.sleep(delta_t);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            robot.repairFailedSensor(Direction.FORWARD);
            try {
                forwardT.sleep(delta_t);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while (backward) {
            robot.triggerSensorFailure(Direction.BACKWARD);
            try {
                backwardT.sleep(delta_t);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            robot.repairFailedSensor(Direction.BACKWARD);
            try {
                backwardT.sleep(delta_t);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



    }

    public void pause() {

    }

    /**
     * If the maze application has a robot (set in the constructor of that class) and
     * the user presses the up, left, or right key, then this keyDown method will be used instead
     * of the controller/state playing one. If the user clicks any other key besides those 3 then
     * the controller/stater playing's keydown method will be used.
     *
     * Responsibilty: simply takes in the UserInput and sends it to the BasicRobot to operate
     * the selected functionality.
     *
     * Collaborators: registers the UserInput from the SimpleKeyListener class and then sends it to the BasicRobot
     * @param key = the key that the user clicks
     */
    public void keyDown(UserInput key) {
        switch (key) {
            case Up:
                robot.move(1, true);
                break;
            case Left:
                robot.rotate(Turn.LEFT);
                break;
            case Right:
                robot.rotate(Turn.RIGHT);
                break;
            default:
                break;
        }
    }

    public void setState(StatePlaying s){
        state = s;
    }

    public StatePlaying getState(){
        return state;
    }
}