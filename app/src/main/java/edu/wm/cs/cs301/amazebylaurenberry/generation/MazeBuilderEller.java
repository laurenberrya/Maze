package edu.wm.cs.cs301.amazebylaurenberry.generation;

import java.util.Random;


public class MazeBuilderEller extends MazeBuilder implements Runnable {

	public MazeBuilderEller(String skill) {
		super(skill);
		System.out.println("MazeBuilderEller uses Eller's algorithm to generate maze.");
	}
	public MazeBuilderEller(boolean det, String skill) {
		super(det, skill);
		System.out.println("MazeBuilderEller uses Eller's algorithm to generate maze.");
	}

	private int[][] maze;

	/**
	 * This method generate pathways within the maze. It modifies the already initialized  
	 * 3d array called maze. The variable r is used to access the current row and the 
	 * variable c points to the current column. The variable setNumber is used to give each
	 * cell in the maze a unique set identifier.
	 * 
	 * The method calls deleteEastWalls and deleteBottomWalls to delete walls according to
	 * Eller's algorithm.
	 */		
	@Override
	protected void generatePathways() {
		maze = new int[width][height];
		int r = 0;
		int c = 0;
		int setNumber = 0;
		Wallboard wall = null;


		for (r =0; r<height; r++) {
			// for the first row in the maze
			if(r == 0) {
				for(setNumber = 0; setNumber < width ; setNumber++) {
					maze[setNumber][0] = setNumber;
				}
				setNumber++;
					
			}
			//for all other rows in the maze
			else {		
				for(int i = 0 ; i < width ; i++) {
					//if there's a north wall, the cell has to be part of a new set
					if(floorplan.hasWall(i, r, CardinalDirection.North)) {
						maze[i][r] = setNumber;
						setNumber++;
					}
					// no north wall means cell is part of the set above it
					else {
						maze[i][r] = maze[i][r - 1];
					}
						
				}
			}
			deleteEastWalls( c, r, wall);
			deleteBottomWalls( c, r, wall);

			c = 0;
		}	
	
	}

	/**
	 * This method is called from generate pathways. It goes through the maze row by row, 
	 * and deletes east-facing walls using a random number generator. On the last row, it
	 * deletes any east walls within cells of the same set.
	 * 
	 * @param c points to the current column 
	 * @param r points to the current row 
	 * @param wall sets the location of a wallboard that will be deleted
	 */	
	public void deleteEastWalls( int c, int r,  Wallboard wall) {

		while(c != width) {
			
			// if we are on the last row and the cells are not a part of the
			// same set, then delete the wallboard between them
			if(r == (height - 1)) {
				for(int i = 0 ; i < (width - 1) ; i++) {
					if(maze[i][r] != maze[i+1][r]) {
						wall = new Wallboard(i, r, CardinalDirection.East);
						floorplan.deleteWallboard(wall);
					}
						
				}
				break;
			}		
			
			// if we are not in the last cell of the row, then randomly delete
			// east facing wallboards
			if(c != (width - 1)) {
				if(maze[c][r] != maze[c + 1][r]) {
					Random rand = new Random();
					int rando = rand.nextInt(2);
					
					// if rando = 0, keep wall. if rando = 1, remove wall
					if (rando == 1) {
						wall = new Wallboard(c, r, CardinalDirection.East);
						floorplan.deleteWallboard(wall);
						maze[c+1][r] = maze[c][r];
					}
					
				}
	
			}
			c++;

		}
	}

	/**
	 * This method is called from generate pathways. It goes through the columns in the 
	 * current row and randomly deletes a bottom wall from each set.
	 * 
	 * @param c points to the current column 
	 * @param r points to the current row 
	 * @param wall sets the location of a wallboard that will be deleted
	 */	
	protected void deleteBottomWalls(int c, int r, Wallboard wall) {
		c = 0;
		int deleteWallCount =0; //counts the number of walls deleted from the row
		
		// want to delete bottom walls from all rows except for the last one
		while(c != width && r <(height-1))  {
			
			int setstart = c;	//marks the start of the set
			int rando = 0;
			
			// if we are on the last cell in the row and no walls have been deleted from 
			// the row then we want to delete the current bottom wall to ensure there's
			// always a path from row to row
			if(c == (width - 1) && deleteWallCount>0) { 
				wall = new Wallboard(c, r, CardinalDirection.South);
				floorplan.deleteWallboard(wall);
				break;
			}
			
			// if the current cell is in the same set as the cell next to it,
			// continue moving right without any deletion of walls
			if(maze[c][r] == maze[c+1][r]) {
				c++;
			}
			
			// if the current cell is the only cell in the set, we need to delete
			// the bottom wall
			if (setstart == c) {
				wall = new Wallboard(c, r, CardinalDirection.South);
				floorplan.deleteWallboard(wall);
				
			}
			
			// deletes bottom walls randomly from an interval that contains the boundaries
			// of the current set as long as the set contains more than 1 cell
			if (setstart != c) {
				rando = random.nextIntWithinInterval(setstart, c);
				wall = new Wallboard(rando, r, CardinalDirection.South);
				floorplan.deleteWallboard(wall);
				deleteWallCount++;
			}
			
			c++;		

		}
	
	}
	
}







