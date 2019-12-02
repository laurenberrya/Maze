package edu.wm.cs.cs301.amazebylaurenberry.gui;
import edu.wm.cs.cs301.amazebylaurenberry.generation.CardinalDirection;
import edu.wm.cs.cs301.amazebylaurenberry.generation.Floorplan;
import edu.wm.cs.cs301.amazebylaurenberry.generation.Maze;
import edu.wm.cs.cs301.amazebylaurenberry.generation.StoreMaze;
import edu.wm.cs.cs301.amazebylaurenberry.gui.Constants.UserInput;
import edu.wm.cs.cs301.amazebylaurenberry.gui.Robot.Direction;
import edu.wm.cs.cs301.amazebylaurenberry.gui.Robot.Turn;


import android.os.Message;

import static edu.wm.cs.cs301.amazebylaurenberry.PlayAnimationActivity.aHandler;

/**
 * 
 * @author Lauren Berry and Shrikant Mishra
 *
 */

public class BasicRobot implements Robot{

	//private Controller controller;
	private Maze configuration;
	private Floorplan floorplan;
	
	private float batteryLevel; //display on exit screen
	private int pathLength; //display on exit screen
	//private CardinalDirection currentDir;
	
	private boolean roomSensor;
	private boolean isMoving;
	private boolean forwardSensor;
	private boolean backwardSensor;
	private boolean rightSensor;
	private boolean leftSensor;

	private StatePlaying state;
	
	

	/**
	 * Constructor without any parameters. Initializes all sensors
	 * and battery level. Creates a controller to be used by the robot
	 * and calls setMaze() with that controller.
	 */
	public BasicRobot() {
		setBatteryLevel(3000);
		pathLength = 0;
		
		roomSensor = true;
		isMoving = true;
		forwardSensor = true;
		backwardSensor = true;
		rightSensor = true;
		leftSensor = true;

	//	controller = new Controller();
	//	setMaze(controller);

		configuration = StoreMaze.getWholeMaze();
		state = getStatePlaying();

	}
	
	
	/**
	 * Constructor with a controller parameter. Initializes all sensors
	 * and battery level. Calls setMaze() with the controller that was
	 * passed in.
	 */
	/*public BasicRobot(Controller controller) {
		this.controller = controller;
		
		setBatteryLevel(3000);
		pathLength = 0;
		
		roomSensor = true;
		isMoving = true;
		forwardSensor = true;
		backwardSensor = true;
		rightSensor = true;
		leftSensor = true;
		
		//currentDir = CardinalDirection.East;
		//currentDir = controller.getCurrentDirection();
		
		setMaze(controller);
		
	}*/
	
	
	/**
	 * Provides the current position as (x,y) coordinates for the maze cell as an array of length 2 with [x,y].
	 * @postcondition 0 <= x < width, 0 <= y < height of the maze. 
	 * @return array of length 2, x = array[0], y=array[1]
	 * @throws Exception if position is outside of the maze
	 */
	public int[] getCurrentPosition() throws Exception{
		if (getStatePlaying().getCurrentPosition()[0] <0 || getStatePlaying().getCurrentPosition()[0]>= configuration.getWidth() || getStatePlaying().getCurrentPosition()[1] < 0 || getStatePlaying().getCurrentPosition()[1]>= configuration.getHeight()) {
			throw new Exception("Current robot position not within maze boundaries");
		}
		else{
			return getStatePlaying().getCurrentPosition();
		}
		
	}
	
	
	/**
	 * Provides the current cardinal direction.
	 * @return cardinal direction is robot's current direction in absolute terms
	 */	
	public CardinalDirection getCurrentDirection() {
		//return currentDir;
		return getStatePlaying().getCurrentDirection();
	}
	
	
	/**
	 * Provides the robot with a reference to the controller to cooperate with.
	 * The robot memorizes the controller such that this method is most likely called only once
	 * and for initialization purposes. The controller serves as the main source of information
	 * for the robot about the current position, the presence of walls, the reaching of an exit.
	 * The controller is assumed to be in the playing state.
	 * @param controller is the communication partner for robot
	 * @precondition controller != null, controller is in playing state and has a maze
	 */
	//public void setMaze(Controller controller) {
	public void setMaze(Maze maze){
		assert(maze != null);
		
		if (maze != null) {
			configuration = maze;
		}
	}
	
	
	/**
	 * Returns the current battery level.
	 * The robot has a given battery level (energy level) 
	 * that it draws energy from during operations. 
	 * The particular energy consumption is device dependent such that a call 
	 * for distance2Obstacle may use less energy than a move forward operation.
	 * If battery level <= 0 then robot stops to function and hasStopped() is true.
	 * @return current battery level, level is > 0 if operational. 
	 */
	public float getBatteryLevel() {
		return batteryLevel;
	}
	
	
	/**
	 * Sets the current battery level.
	 * The robot has a given battery level (energy level) 
	 * that it draws energy from during operations. 
	 * The particular energy consumption is device dependent such that a call 
	 * for distance2Obstacle may use less energy than a move forward operation.
	 * If battery level <= 0 then robot stops to function and hasStopped() is true.
	 * @param level is the current battery level
	 * @precondition level >= 0 
	 */
	public void setBatteryLevel(float level) {
		if (level >=0) {
			batteryLevel = level;
		}
	}
	
	
	/** 
	 * Gets the distance traveled by the robot.
	 * The robot has an odometer that calculates the distance the robot has moved.
	 * Whenever the robot moves forward, the distance 
	 * that it moves is added to the odometer counter.
	 * The odometer reading gives the path length if its setting is 0 at the start of the game.
	 * The counter can be reset to 0 with resetOdomoter().
	 * @return the distance traveled measured in single-cell steps forward
	 */
	public int getOdometerReading() {
		return pathLength;
	}
	
	
	/** 
     * Resets the odomoter counter to zero.
     * The robot has an odometer that calculates the distance the robot has moved.
     * Whenever the robot moves forward, the distance 
     * that it moves is added to the odometer counter.
     * The odometer reading gives the path length if its setting is 0 at the start of the game.
     */
	public void resetOdometer() {
		pathLength = 0;
	}
	
	
	/**
	 * Gives the energy consumption for a full 360 degree rotation.
	 * Scaling by other degrees approximates the corresponding consumption. 
	 * @return energy for a full rotation
	 */
	public float getEnergyForFullRotation() {
		float fullRot = 12;
		return fullRot;
	}
	
	
	/**
	 * Gives the energy consumption for moving forward for a distance of 1 step.
	 * For simplicity, we assume that this equals the energy necessary 
	 * to move 1 step backwards and that scaling by a larger number of moves is 
	 * approximately the corresponding multiple.
	 * @return energy for a single step forward
	 */
	public float getEnergyForStepForward() {
		float moveForward = 5;
		return moveForward;
	}
	
	
	
	
	
	///////////////////////////////////////////////////////////////////
	/////////////////// Sensors   /////////////////////////////////////
	///////////////////////////////////////////////////////////////////
	/**
	 * Tells if current position (x,y) is right at the exit but still inside the maze. 
	 * Used to recognize termination of a search.
	 * @return true if robot is at the exit, false otherwise
	 */
	public boolean isAtExit() {
		Floorplan fp = configuration.getFloorplan();
		try {
			return fp.isExitPosition(getCurrentPosition()[0], getCurrentPosition()[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * Tells if a sensor can identify the exit in the given direction relative to 
	 * the robot's current forward direction from the current position.
	 * @return true if the exit of the maze is visible in a straight line of sight
	 * @throws UnsupportedOperationException if robot has no sensor in this direction
	 */
	public boolean canSeeThroughTheExitIntoEternity(Direction direction) throws UnsupportedOperationException {

		assert(hasOperationalSensor(direction));
		
		if (!hasOperationalSensor(direction)) {
			throw new UnsupportedOperationException("Robot does not have operational sensor in given direction");
		}
		
		if(hasOperationalSensor(direction) && batteryLevel>=1) {
			energyConsumption(0,1,0,0,0);
			if (distanceToObstacle(direction) == Integer.MAX_VALUE) {
				return true;
			}
		}
		return false;	
	}
	
	
	/**
	 * Tells if current position is inside a room. 
	 * @return true if robot is inside a room, false otherwise
	 * @throws UnsupportedOperationException if not supported by robot
	 */	
	public boolean isInsideRoom() throws UnsupportedOperationException {
		assert(hasRoomSensor());
		if (!hasRoomSensor()) {
			throw new UnsupportedOperationException("Robot does not have room sensor");
		}
		
		Floorplan floorplan = configuration.getFloorplan();
		
		try {
			if (floorplan.isInRoom(getCurrentPosition()[0], getCurrentPosition()[1])) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * Tells if the robot has a room sensor.
	 */
	public boolean hasRoomSensor() {
		return roomSensor;
	}
	
	
	/**
	 * Tells if the robot has stopped for reasons like lack of energy, hitting an obstacle, etc.
	 * @return true if the robot has stopped, false otherwise
	 */
	public boolean hasStopped() {
		if(isMoving) {
			return false;
		}
		else{
			return true;
		}
	}
	
	
	/**
	 * Tells the distance to an obstacle (a wall) 
	 * in the given direction.
	 * The direction is relative to the robot's current forward direction.
	 * Distance is measured in the number of cells towards that obstacle, 
	 * e.g. 0 if the current cell has a wallboard in this direction, 
	 * 1 if it is one step forward before directly facing a wallboard,
	 * Integer.MaxValue if one looks through the exit into eternity.
	 * @param direction specifies the direction of the sensor
	 * @return number of steps towards obstacle if obstacle is visible 
	 * in a straight line of sight, Integer.MAX_VALUE otherwise
	 * @throws UnsupportedOperationException if the robot does not have
	 * an operational sensor for this direction
	 */
	public int distanceToObstacle(Direction direction) throws UnsupportedOperationException {
		assert(hasOperationalSensor(direction));
		
		if(!hasOperationalSensor(direction)) {
			throw new UnsupportedOperationException("Robot does not have operational sensor in given direction");
		}
		
		int posX = 0;
		int posY = 0;
		try {
			posX = getCurrentPosition()[0];
			posY = getCurrentPosition()[1];
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int dist =0;
		
		CardinalDirection cardDirect = convertDirectionToCardinalDirection(direction);
		
		//configuration = controller.getMazeConfiguration();
		
		while(configuration.hasWall(posX, posY, cardDirect) == false){
			if (batteryLevel<1) { //not enough battery to complete distance check
				isMoving = false;
				return 0;
			}
			
			dist++;
			energyConsumption(1,0,0,0,0);
			
			if (cardDirect == CardinalDirection.South) {
				posY++;
			}
			if (cardDirect == CardinalDirection.North) {
				posY--;
			}
			if (cardDirect == CardinalDirection.East) {
				posX++;
			}
			if (cardDirect == CardinalDirection.West) {
				posX--;
			}
			
			if(posX<0 || posX>= configuration.getWidth() || posY<0 || posY >= configuration.getHeight()) {
				return Integer.MAX_VALUE;
			}
		}
		return dist;
	}
		
	
	/**
	 * Tells if the robot has an operational distance sensor for the given direction.
	 * The interface is generic and may be implemented with robots 
	 * that are more or less equipped with sensor or have sensors that
	 * are subject to failures and repairs. 
	 * The purpose is to allow for a flexible robot driver to adapt
	 * its driving strategy according the features it
	 * finds supported by a robot.
	 * @param direction specifies the direction of the sensor
	 * @return true if robot has operational sensor, false otherwise
	 */
	public boolean hasOperationalSensor(Direction direction) {
		
		if(direction == Direction.FORWARD) {
			return forwardSensor;
		}
		else if(direction == Direction.BACKWARD) {
			return backwardSensor;
		}
		else if(direction == Direction.RIGHT) {
			return rightSensor;
		}
		else{ //direction is left
			return leftSensor;
		}
	}
	
	
	/**
	 * Makes the robot's distance sensor for the given direction fail.
	 * Subsequent calls to measure the distance to an obstacle in 
	 * this direction will return with an exception.
	 * If the robot does not have a sensor in this direction, 
	 * the method does not have any effect.
	 * Only distance sensors can fail, the room sensor and exit
	 * sensor if installed are always operational.
	 * @param direction specifies the direction of the sensor
	 */
	public void triggerSensorFailure(Direction direction) {
		
		if(direction == Direction.FORWARD) {
			forwardSensor = false;
		}
		if(direction == Direction.BACKWARD) {
			backwardSensor = false;
		}
		if(direction == Direction.RIGHT) {
			rightSensor = false;
		}
		if(direction == Direction.LEFT){ 
			leftSensor = false;
		}
	}
	
	
	/**
	 * Makes the robot's distance sensor for the given direction
	 * operational again. 
	 * A method call for an already operational sensor has no effect
	 * but returns true as the robot has an operational sensor
	 * for this direction.
	 * A method call for a sensor that the robot does not have
	 * has not effect and the method returns false.
	 * @param direction specifies the direction of the sensor
	 * @return true if robot has operational sensor, false otherwise
	 */
	public boolean repairFailedSensor(Direction direction) {
		if(direction == Direction.FORWARD && forwardSensor || direction == Direction.BACKWARD && backwardSensor || direction == Direction.LEFT && leftSensor || direction == Direction.RIGHT && rightSensor) {
			return true;
		}
		if(direction == Direction.FORWARD &&!forwardSensor) {
			forwardSensor = true;
		}
		if(direction == Direction.BACKWARD && !backwardSensor) {
			backwardSensor = true;
		}
		if(direction == Direction.LEFT && !leftSensor) {
			leftSensor = true;
		}
		if(direction == Direction.RIGHT && !rightSensor) {
			rightSensor = true;
		}
		return false;
	}
	
	
	
	
	
	
	
	///////////////////////////////////////////////////////////////////
	/////////////////// Actuators /////////////////////////////////////
	///////////////////////////////////////////////////////////////////
	/**
	 * Turn robot on the spot for amount of degrees. 
	 * If robot runs out of energy, it stops, 
	 * which can be checked by hasStopped() == true and by checking the battery level. 
	 * @param direction to turn and relative to current forward direction. 
	 */
	public void rotate(Turn turn) {
		
		if(!hasStopped()) {
			if (batteryLevel<3) { //not enough battery to rotate
				isMoving = false;
				youLose();
				return;
			}

			
			if(turn == Turn.LEFT) {
				state.keyDown(UserInput.Left, 0);
				if(getCurrentDirection() == CardinalDirection.North) {
					getStatePlaying().setCurrentDirection(1,0);
				}
				if(getCurrentDirection() == CardinalDirection.East) {
					getStatePlaying().setCurrentDirection(0,1);
				}
				if(getCurrentDirection() == CardinalDirection.South) {
					getStatePlaying().setCurrentDirection(-1,0);
				}
				if(getCurrentDirection() == CardinalDirection.West) {
					getStatePlaying().setCurrentDirection(0,-1);
				}
				energyConsumption(0,0,1,0,0);
			}
			
			if(turn == Turn.RIGHT) {
				state.keyDown(UserInput.Right, 0);
				if(getCurrentDirection() == CardinalDirection.North) {
					getStatePlaying().setCurrentDirection(-1,0);
				}
				if(getCurrentDirection() == CardinalDirection.East) {
					getStatePlaying().setCurrentDirection(0,-1);
				}
				if(getCurrentDirection() == CardinalDirection.South) {
					getStatePlaying().setCurrentDirection(1,0);
				}
				if(getCurrentDirection() == CardinalDirection.West) {
					getStatePlaying().setCurrentDirection(0,1);
				}
				energyConsumption(0,0,1,0,0);
			}
			
			if(turn == Turn.AROUND) {
				if (batteryLevel>=6 ) {
					state.keyDown(UserInput.Right, 0);
					state.keyDown(UserInput.Right, 0);
					
					if(getCurrentDirection() == CardinalDirection.North) {
						getStatePlaying().setCurrentDirection(0,1);
					}
					else if(getCurrentDirection() == CardinalDirection.East) {
						getStatePlaying().setCurrentDirection(-1,0);
					}
					else if(getCurrentDirection() == CardinalDirection.South) {
						getStatePlaying().setCurrentDirection(0,-1);
					}
					else if(getCurrentDirection() == CardinalDirection.West) {
						getStatePlaying().setCurrentDirection(1,0);
					}
					
					energyConsumption(0,0,1,0,0);
					energyConsumption(0,0,1,0,0);
				}
				else {
					isMoving = false;
					youLose();
					return;
				}
			}
			
			if(batteryLevel == 0) {
				isMoving = false;
				youLose();
				return;
			}
		}
	}
	
	
	
	/**
	 * Moves robot forward a given number of steps. A step matches a single cell.
	 * If the robot runs out of energy somewhere on its way, it stops, 
	 * which can be checked by hasStopped() == true and by checking the battery level. 
	 * If the robot hits an obstacle like a wall, it depends on the mode of operation
	 * what happens. If an algorithm drives the robot, it remains at the position in front 
	 * of the obstacle and also hasStopped() == true as this is not supposed to happen.
	 * This is also helpful to recognize if the robot implementation and the actual maze
	 * do not share a consistent view on where walls are and where not.
	 * If a user manually operates the robot, this behavior is inconvenient for a user,
	 * such that in case of a manual operation the robot remains at the position in front
	 * of the obstacle but hasStopped() == false and the game can continue.
	 * @param distance is the number of cells to move in the robot's current forward direction 
	 * @param manual is true if robot is operated manually by user, false otherwise
	 * @precondition distance >= 0
	 */
	public void move(int distance, boolean manual) {
		
		floorplan = configuration.getFloorplan();
		
		if(batteryLevel <5) {
			isMoving = false;
			youLose();
			return;
		}

		//getCurrentPosition might result in an exception, need to use a
		//try catch block
		try {
			if (!hasStopped()) {
				
				//moves are the number of forward moves the robot was able to perform,
				//compare it to the desired number of moves and if the two do not equal 
				//each other then the robot must've hit an obstacle aka a wall and 
				//hasStopped must then be true

				int moves=0;
				for (int i=0; i<distance; i++) {
					if(batteryLevel <5) {
						isMoving = false;
						youLose();
						return;
					}
					if ( !floorplan.hasWall(getCurrentPosition()[0], getCurrentPosition()[1], getCurrentDirection())) {
						state.keyDown(UserInput.Up,0);
						if(getCurrentDirection() == CardinalDirection.North) { //north is down
							getStatePlaying().setCurrentPosition(getCurrentPosition()[0], getCurrentPosition()[1]--);
						}
						if(getCurrentDirection() == CardinalDirection.South) { //south is up
							getStatePlaying().setCurrentPosition(getCurrentPosition()[0], getCurrentPosition()[1]++);
						}
						if(getCurrentDirection() == CardinalDirection.East) { //east is right
							getStatePlaying().setCurrentPosition(getCurrentPosition()[0]++, getCurrentPosition()[1]);
						}
						if(getCurrentDirection() == CardinalDirection.West) { //west is left
							getStatePlaying().setCurrentPosition(getCurrentPosition()[0]--, getCurrentPosition()[1]);
						}

						energyConsumption(0,0,0,1,0);
						pathLength++;
						moves++;
					}

				}

				if (moves!= distance &&!manual) {
					isMoving =false;
					youLose();
				}
			}
			if(batteryLevel == 0) {
				isMoving = false;
				youLose();
				return;
			}
		}

		catch(Exception x) {
				x.printStackTrace();
		}
	}


	
	/**
	 * Makes robot move in a forward direction even if there is a wall
	 * in front of it. In this sense, the robot jumps over the wall
	 * if necessary. The distance is always 1 step and the direction
	 * is always forward.
	 * @throws Exception is thrown if the chosen wall is an exterior wall 
	 * and the robot would land outside of the maze that way. 
	 * The current location remains set at the last position, 
	 * same for direction but the game is supposed
	 * to end with a failure.
	 */
	public void jump() throws Exception{
		if(getCurrentPosition()[0] == 0 && getCurrentDirection() == CardinalDirection.West || getCurrentPosition()[0] == configuration.getWidth()-1 && getCurrentDirection() == CardinalDirection.East
				|| getCurrentPosition()[1] == 0 && getCurrentDirection() == CardinalDirection.South || getCurrentPosition()[1] == configuration.getHeight()-1 && getCurrentDirection() == CardinalDirection.North) {
			throw new Exception();
		}
		
		if(batteryLevel <50) {
			batteryLevel = 0;
			isMoving = false;
		}
		
		if(getCurrentDirection() == CardinalDirection.North) {
			getStatePlaying().setCurrentPosition(getCurrentPosition()[0], getCurrentPosition()[1]--);
			energyConsumption(0,0,0,0,1);
			pathLength ++;
		}
		if(getCurrentDirection() == CardinalDirection.South) {
			getStatePlaying().setCurrentPosition(getCurrentPosition()[0], getCurrentPosition()[1]++);
			energyConsumption(0,0,0,0,1);
			pathLength ++;
		}
		if(getCurrentDirection() == CardinalDirection.East) {
			getStatePlaying().setCurrentPosition(getCurrentPosition()[0]++, getCurrentPosition()[1]);
			energyConsumption(0,0,0,0,1);
			pathLength ++;
		}
		if(getCurrentDirection() == CardinalDirection.West) {
			getStatePlaying().setCurrentPosition(getCurrentPosition()[0]--, getCurrentPosition()[1]);
			energyConsumption(0,0,0,0,1);
			pathLength ++;
		}
		
		if(batteryLevel == 0) {
			isMoving = false;
			youLose();
			return;
		}
	}
	
	
	
	/**
	 * Helper method used to convert a robot direction into a cardinal
	 * direction the controller can read. Called in the distanceToObstacle
	 * method.
	 * @param direction is the robot direction
	 * @return a cardinal direction analogous to the robot direction 
	 */
	private CardinalDirection convertDirectionToCardinalDirection(Direction direction) {
		CardinalDirection cd = getCurrentDirection();
		
		if (direction == Direction.LEFT) {
			cd.rotateClockwise();
			cd.rotateClockwise();
			cd.rotateClockwise();
		}
		
		else if (direction == Direction.RIGHT) {
			cd.rotateClockwise();
		}
		
		else if (direction == Direction.BACKWARD) {
			cd.oppositeDirection();
		}
		
		else { //forward
			cd = getCurrentDirection();
		}
		return cd;
	}
	
	
	
	/**
	 * Keeps track of energy consuming moves. If any of the parameters are 1,
	 * then the operation has been performed, and the method decreases the battery
	 * level as instructed.
	 * @param distanceCheck is 1 if a distance check has been performed, 0 if not
	 * @param checkExit is 1 if an exit check has been performed, 0 if not
	 * @param rotate is 1 if the robot has rotated, 0 if not
	 * @param moveForward is 1 if the robot has moved forward, 0 if not
	 * @param jumpWall is 1 if the robot has jumped a wall, 0 if not
	 */
	private void energyConsumption(int distanceCheck, int checkExit, int rotate, int moveForward, int jumpWall) {
		// distance sensing in one direction or checking if exit is visible in one direction == -1
		// rotating left or right (quarter turn or 90 degrees) == -3
		// moving forward one step (one cell) == -5
		// jumping over a wall == -50
		
		if(distanceCheck == 1) {
			batteryLevel--;
		}
		if(checkExit == 1) {
			batteryLevel--;
		}
		if(rotate == 1) {
			batteryLevel = batteryLevel - 3;
		}
		if(moveForward == 1) {
			batteryLevel = batteryLevel - 5;
		}
		if(jumpWall == 1) {
			batteryLevel = batteryLevel - 50;
		}
	}
	
	/**
	 * Helper method used to move the robot through the exit once the RobotDriver class
	 * has gotten it to the exit position. Called in Wizard and WallFollower.
	 * 
	 * @param robot the driver is operating on
	 * @return boolean value that tells whether or not the robot finished the maze
	 */
	public boolean robotDriverAtExit(BasicRobot robot) {
		if (robot.hasOperationalSensor(Direction.FORWARD)&&robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD)) {
			robot.move(1, false);
			if(!robot.hasStopped()) {
				return true;
			}
		}
		if (robot.hasOperationalSensor(Direction.LEFT)&&robot.canSeeThroughTheExitIntoEternity(Direction.LEFT)) {
			robot.rotate(Turn.LEFT);
			robot.move(1, false);
			if(!robot.hasStopped()) {
				return true;
			}
		}
		if (robot.hasOperationalSensor(Direction.RIGHT)&&robot.canSeeThroughTheExitIntoEternity(Direction.RIGHT)) {
			robot.rotate(Turn.RIGHT);
			robot.move(1, false);
			if(!robot.hasStopped()) {
				return true;
			}
		}
		if (robot.hasOperationalSensor(Direction.BACKWARD)&&robot.canSeeThroughTheExitIntoEternity(Direction.BACKWARD)) {
			robot.rotate(Turn.LEFT);
			robot.rotate(Turn.LEFT);
			robot.move(1, false);
			if(!robot.hasStopped()) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * The following methods are used in testing. 
	 * Allows access to the floorplan, current position, 
	 * current state, and maze the robot is using.
	 */
	public Floorplan returnFloorplan() {
		return configuration.getFloorplan();
	}
	
	public int[] returnCurrentPos() {
		return getStatePlaying().getCurrentPosition();
		
	}

	public void setStatePlaying(StatePlaying state){
		this.state = state;
	}

	public StatePlaying getStatePlaying(){
		return state;
	}
	
	
	public Maze getMaze() {
		return configuration;
	}

	private void youLose(){
		Message msg = new Message();
		msg.arg1 = -1;

		/* Sending the message */
		aHandler.sendMessage(msg);
	}
	

}