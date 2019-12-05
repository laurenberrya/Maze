package edu.wm.cs.cs301.amazebylaurenberry.gui;

import edu.wm.cs.cs301.amazebylaurenberry.generation.CardinalDirection;
import edu.wm.cs.cs301.amazebylaurenberry.generation.Distance;
import edu.wm.cs.cs301.amazebylaurenberry.generation.Maze;
import edu.wm.cs.cs301.amazebylaurenberry.gui.Robot.Direction;
import edu.wm.cs.cs301.amazebylaurenberry.gui.Robot.Turn;

/**
 * This class implements the Wizard algorithm that operates a robot to escape from a given maze.
 * The wizard uses information from a Distance object (handed to it via setDistance method) to 
 * find the exit by looking for a neighbor that is closer to the exit. The wizard can also take
 * advantage of the jump feature of the robot, as long as there is enough battery left and the
 * exit is located in the forward direction from the robot. 
 * 
 * Collaborators: implements the RobotDriver interface, uses a Distance object
 * 
 * @author Lauren Berry and Shrikant Mishra
 */
public class Wizard implements RobotDriver {
	
	private Robot robot;
	private int width, height;
	private float initialBattery;
	private Maze configuration;
	private Distance distance;
	
	public Wizard() {
		robot = null;
		height = 0;
		width = 0;
		initialBattery = 0;
		distance = null;
	}
	
	
	/**
	 * Assigns a robot platform to the driver. 
	 * The driver uses a robot to perform, this method provides it with this necessary information.
	 * @param r robot to operate
	 */
	public void setRobot(Robot r) {
		robot = r;
		initialBattery = robot.getBatteryLevel();
	}
	
	/**
	 * Provides the robot driver with information on the dimensions of the 2D maze
	 * measured in the number of cells in each direction.
	 * @param width of the maze
	 * @param height of the maze
	 * @precondition 0 <= width, 0 <= height of the maze.
	 */
	public void setDimensions(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Provides the robot driver with information on the distance to the exit.
	 * Only some drivers such as the wizard rely on this information to find the exit.
	 * @param distance gives the length of path from current position to the exit.
	 * @precondition null != distance, a full functional distance object for the current maze.
	 */
	public void setDistance(Distance distance) {
		if (distance != null) {
			this.distance = distance;
		}	
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
	
	/**
	 * Drives the robot towards the exit given it exists and 
	 * given the robot's energy supply lasts long enough. 
	 * @return true if driver successfully reaches the exit, false otherwise
	 * @throws exception if robot stopped due to some problem, e.g. lack of energy
	 */
	public boolean drive2Exit() throws Exception {
		configuration = ((BasicRobot) robot).getMaze();
		
		while (!robot.isAtExit()) {
			
			if (robot.hasStopped()) {
				return false;
			}
		
			CardinalDirection cd = robot.getCurrentDirection();
			
			float batteryCopy = robot.getBatteryLevel();
			boolean possibleToJumpToExit = true;
			int[] currentPos = robot.getCurrentPosition();
			
			//checks to see if wizard can jump walls until the exit without running out of battery
			while(possibleToJumpToExit) {
				if (robot.hasOperationalSensor(Direction.FORWARD) && robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD)) {
					while(!robot.isAtExit()) {
						if (!configuration.hasWall(currentPos[0], currentPos[1], cd)) {
							batteryCopy = batteryCopy - 5;
							if(cd == CardinalDirection.North) { //north is down
								currentPos[1]--;
							}
							if(cd == CardinalDirection.South) { //south is up
								currentPos[1]++;
							}
							if(cd == CardinalDirection.East) { //east is right
								currentPos[0]++;
							}
							if(cd == CardinalDirection.West) { //west is left
								currentPos[0]--;
							}
							
							if (batteryCopy <=0) {
								possibleToJumpToExit = false;
							}
						}
						
						if (configuration.hasWall(currentPos[0], currentPos[1], cd)) {
							batteryCopy = batteryCopy - 50;
							if(cd == CardinalDirection.North) { //north is down
								currentPos[1]--;
							}
							if(cd == CardinalDirection.South) { //south is up
								currentPos[1]++;
							}
							if(cd == CardinalDirection.East) { //east is right
								currentPos[0]++;
							}
							if(cd == CardinalDirection.West) { //west is left
								currentPos[0]--;
							}
							
							if (batteryCopy <=0) {
								possibleToJumpToExit = false;
							}
						}
					}
	
				}
				break;
			}
			
			if(possibleToJumpToExit) {
				if (!configuration.hasWall(currentPos[0], currentPos[1], cd)) {
					robot.move(1, false);
					
				}
				
				if (configuration.hasWall(currentPos[0], currentPos[1], cd)) {
					robot.jump();
				}
				
			}
			
			else {
				//figure out the coordinates of the next move
				int[] nextSchmove = configuration.getNeighborCloserToExit(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1]);
				
				//figures out which direction to rotate the robot based on the spot it 
				//wants to move to
				if (nextSchmove[0] == robot.getCurrentPosition()[0]++) {

					if (cd == CardinalDirection.West) {
						robot.rotate(Turn.AROUND);
					}
					if (cd == CardinalDirection.South) {
						robot.rotate(Turn.RIGHT);
					}
					if (cd == CardinalDirection.North ) {
						robot.rotate(Turn.LEFT);
					}
				}
				else if (nextSchmove[0] == robot.getCurrentPosition()[0]--) {
					
					if (cd == CardinalDirection.East) {
						robot.rotate(Turn.AROUND);
					}
					if (cd == CardinalDirection.South) {
						robot.rotate(Turn.LEFT);
					}
					if (cd == CardinalDirection.North) {
						robot.rotate(Turn.RIGHT);
					}
					
				}
				else if (nextSchmove[1] == robot.getCurrentPosition()[1]--) {

					if (cd == CardinalDirection.East) {
						robot.rotate(Turn.RIGHT);
					}
					if (cd == CardinalDirection.West) {
						robot.rotate(Turn.LEFT);
					}
					if (cd == CardinalDirection.South) {
						robot.rotate(Turn.AROUND);
					}
					
				}
				else if (nextSchmove[1] == robot.getCurrentPosition()[1]++) {
					
					if (cd == CardinalDirection.East) {
						robot.rotate(Turn.LEFT);
					}
					if (cd == CardinalDirection.West) {
						robot.rotate(Turn.RIGHT);
					}
					if (cd == CardinalDirection.North ) {
						robot.rotate(Turn.AROUND);
					}
					
				}
				
				robot.move(1, false);
				
			}
		}
			
		
		//robot is now at exit
		//need to make sure the robot is facing the right way to go through the exit
		
		return ((BasicRobot) robot).robotDriverAtExit((BasicRobot) robot);
	

	}
	
	
	/**
	 * Returns the total energy consumption of the journey, i.e.,
	 * the difference between the robot's initial energy level at
	 * the starting position and its energy level at the exit position. 
	 * This is used as a measure of efficiency for a robot driver.
	 */
	public float getEnergyConsumption() {
		float ec = initialBattery - robot.getBatteryLevel() ;
		return ec;
	}
	
	/**
	 * Returns the total length of the journey in number of cells traversed. 
	 * Being at the initial position counts as 0. 
	 * This is used as a measure of efficiency for a robot driver.
	 */
	public int getPathLength() {
		return robot.getOdometerReading();
	}
	
}