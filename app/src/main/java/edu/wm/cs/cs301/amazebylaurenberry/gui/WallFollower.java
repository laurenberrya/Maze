package edu.wm.cs.cs301.amazebylaurenberry.gui;

import edu.wm.cs.cs301.amazebylaurenberry.generation.CardinalDirection;
import edu.wm.cs.cs301.amazebylaurenberry.generation.Distance;
import edu.wm.cs.cs301.amazebylaurenberry.generation.Maze;
import edu.wm.cs.cs301.amazebylaurenberry.gui.Robot.Direction;
import edu.wm.cs.cs301.amazebylaurenberry.gui.Robot.Turn;

/**
 * This class implements the WallFollower algorithm that operates a robot to escape from a given maze.
 * The objective of the wall follower is to get out of the maze as quickly as possible (objective 
 * function: minimize path length). Limitations are that the robot needs sensors to recognize if there 
 * is a wall in front of it and if there is one at one side (this implementation uses the left side) to 
 * perform. WallFollower does not have access to the Distance class.
 * 
 * Collaborators: implements the RobotDriver interface
 * 
 * @author Lauren Berry and Shrikant Mishra
 */
public class WallFollower implements RobotDriver {
	
	private Robot robot;
	private int width, height;
	private float initialBattery;
	private Maze configuration;
	boolean paused; //= true;
	
	public WallFollower() {
		robot = null;
		height = 0;
		width = 0;
		initialBattery = 0;
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
		//wallFollower can't access this
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
		CardinalDirection cd = robot.getCurrentDirection();
		
		
		while (!robot.isAtExit()) {
			
			if (robot.hasStopped()) {
				return false;
			}
	
			boolean frontWall = configuration.hasWall(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1], cd);
			boolean leftWall = configuration.hasWall(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1], cd.rotateClockwise().rotateClockwise().rotateClockwise());
			boolean rightWall = configuration.hasWall(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1], cd.rotateClockwise());	
			
			if (robot.hasOperationalSensor(Direction.LEFT) && !leftWall ) {
				robot.rotate(Turn.LEFT);
				robot.move(1, false);
			}
			
			else if (robot.hasOperationalSensor(Direction.FORWARD) && !frontWall) {
				robot.move(1, false);
			}
			
			else if (robot.hasOperationalSensor(Direction.RIGHT) && !rightWall) {
				robot.rotate(Turn.RIGHT);
				robot.move(1, false);
				
			}
			
			else {
				robot.rotate(Turn.RIGHT);
				robot.rotate(Turn.RIGHT);
			}
	
		}

		//robot now at exit position
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

	public void pause(){
		if (paused){
			paused = false;
		}
		else{
			paused = true;
		}

	}
	
}