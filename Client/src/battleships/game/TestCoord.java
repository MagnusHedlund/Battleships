/*
 * TestCoord.java	
 * Version 1.0 (2013-05-31)
 */

package battleships.game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Testing the class coordinate, representing a coordinate in the game Battleships.
 * 
 * @author Åsa Waldhe
 *
 */
public class TestCoord {
	
	/**
	 * Instances of class to test.
	 */
	Coordinate c = null;
	Coordinate c1 = null;
	Coordinate c2 = null;

	@Before
	public void setUp() throws Exception {
		
		c = new Coordinate(1,3);
		c1 = new Coordinate(4,8);
		c2 = new Coordinate(1,3);
	}

	/**
	 * Testing getters for x- and y-coordinates.
	 */
	@Test
	public void testGetters() {
		assertEquals(1, c.getX(), 1);
		assertEquals(1, c.getX(), 0.1);
		assertEquals(3, c.getY(), 1);
	}
	
	/**
	 * Testing method toString.
	 */
	@Test
	public void testToString() {
		assertEquals("(1,3)", c.toString());
		
	}
	
	/**
	 * Testing method equals.
	 */
	@Test
	public void testEquals() {
		assertEquals(true, c.equals(c2));
		assertEquals(false, c.equals(c1));
		
	}

}
