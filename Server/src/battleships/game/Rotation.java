/*
 * Rotation.java	
 * Version 1.0 (2013-05-24)
 */

package battleships.game;

/**
 * When placing ships, a rotation must be set for the server to
 * know which coordinates determine a hit.
 * 
 * @author Christopher Nilsson
 */
public enum Rotation
{
	/**
	 * Left to right.
	 */
	HORIZONTAL,

	/**
	 * Top to bottom.
	 */
	VERTICAL,
	
	/**
	 * Rotation not specified.
	 */
	NONE
}