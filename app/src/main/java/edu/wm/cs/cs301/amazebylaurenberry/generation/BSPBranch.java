/**
 * 
 */
package edu.wm.cs.cs301.amazebylaurenberry.generation;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import gui.MazeFileWriter;

/**
 * BSPNodes are used to build a binary tree, where internal nodes keep track 
 * of lower and upper bounds of (x,y) coordinates.
 * Leaf nodes carry a list of walls. 
 * Branch nodes are internal nodes of the tree.
 * A BSP tree is a data structure to search for a set of walls 
 * to put on display in the FirstPersonView and Map.
 * 
 * This code is refactored code from Maze.java by Paul Falstad, www.falstad.com, Copyright (C) 1998, all rights reserved
 * Paul Falstad granted permission to modify and use code for teaching purposes.
 * Refactored by Peter Kemper
 */
public class BSPBranch extends BSPNode {
	// left and right branches of the binary tree
	private BSPNode lbranch, rbranch; 
	// (x,y) coordinates and (dx,dy) direction
	private int x;
	private int y;
	private int dx;
	private int dy;

	/**
	 * Constructor with values for all internal fields
	 * Upper and lower bound values are obtained from the 
	 * given left and right branches, so those trees must
	 * be carry valid values.
	 * @param px x coordinate
	 * @param py y coordinate
	 * @param pdx x direction
	 * @param pdy y direction
	 * @param left child, assumes bounds are valid
	 * @param right child, assumes bounds are valid
	 */
	public BSPBranch(int px, int py, int pdx, int pdy, BSPNode left, BSPNode right) {
		lbranch = left;
		rbranch = right;
		x = px; 
		y = py;
		dx = pdx; 
		dy = pdy;
		// update bounds in super class with values from left and right branches
		// note: own values of this node do not matter
		// obviously tree is built from valid subtrees in a bottom up manner
		// or there must be some update later 
		setLowerBoundX(Math.min(left.getLowerBoundX(), right.getLowerBoundX()));
		setUpperBoundX(Math.max(left.getUpperBoundX(), right.getUpperBoundX()));
		setLowerBoundY(Math.min(left.getLowerBoundY(), right.getLowerBoundY()));
		setUpperBoundY(Math.max(left.getUpperBoundY(), right.getUpperBoundY()));
	}
	/**
	 * @return tells if object is a leaf node
	 */
	@Override
	public boolean isIsleaf() {
		return false ;
	}

	public BSPNode getLeftBranch(){
		return lbranch;
	}

	public BSPNode getRightBranch(){
		return rbranch;
	}

	/**
	 * Store the content of a branch node, in particular its left and right children
	 * 
	 * The method recursively stores BSP nodes for left and right children.
	 * Note that the numbering schemes needs to match with the MazeFileReader class.
	 * 
	 * @param n is the node considered
	 * @param doc document to add data to
	 * @param mazeXML element to add data to
	 * @param number is an index number for this node in the XML format
	 * @return the highest used index number
	 */
	public int store(Document doc, Element mazeXML, int number) {
		super.store(doc, mazeXML, number) ; //leaves number unchanged
		if (isIsleaf() == true)
			System.out.println("WARNING: isleaf flag and class are inconsistent!");
		// store: x, y, dx, dy
		MazeFileWriter.appendChild(doc, mazeXML, "xBSPNode_" + number, getX()) ;
		MazeFileWriter.appendChild(doc, mazeXML, "yBSPNode_" + number, getY()) ;
		MazeFileWriter.appendChild(doc, mazeXML, "dxBSPNode_" + number, getDx()) ;
		MazeFileWriter.appendChild(doc, mazeXML, "dyBSPNode_" + number, getDy()) ;
		// recursively store left and right branches
		if (lbranch == null)
		{
			// this is likely to be dead code as BSPBranches seem to have always 2 children
			number++ ;
			MazeFileWriter.appendChild(doc, mazeXML, "xlBSPNode_" + number, Integer.MIN_VALUE) ;
		}
		else
		{
			// recursion
			number++ ;
			//number = MazeFileWriter.storeBSPNode(lbranch, doc, mazeXML, number) ;
			number = lbranch.store(doc, mazeXML, number) ;
		}
		// it is important that the recursion on the left branch updates the number value
		// such that for the nodes on the right branch we use new unique numbers
		if (rbranch == null)
		{
			// this is likely to be dead code as BSPBranches seem to have always 2 children
			number++ ;
			MazeFileWriter.appendChild(doc, mazeXML, "xlBSPNode_" + number, Integer.MAX_VALUE) ;
		}
		else
		{
			// recursion
			number++ ;
			//number = MazeFileWriter.storeBSPNode(rbranch, doc, mazeXML, number) ;
			number = rbranch.store(doc, mazeXML, number) ;
		}
		return number ; // return the last number that was used
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @return the dx
	 */
	public int getDx() {
		return dx;
	}

	/**
	 * @return the dy
	 */
	public int getDy() {
		return dy;
	}



}
