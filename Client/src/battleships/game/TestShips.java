/*
 * TestShips,java.java
 * Version 1.0 (2013-05-30)
 */

package battleships.game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestShips {
	
	/**
	 * Instances of class to test.
	 */
	Coordinate c = null;
	Coordinate c1 = null;
	Coordinate c2 = null;
	Coordinate c3 = null;
	Coordinate c4 = null;
	Coordinate c5 = null;
	Coordinate c6 = null;
	Coordinate c7 = null;
	
	Submarine sub;
	Destroyer dest;
	Aircraft_carrier aircc;

	@Before
	public void setUp() throws Exception {
		c = new Coordinate(1,3);
		c1 = new Coordinate(4,8);
		c2 = new Coordinate(1,3);
		c3 = new Coordinate(2,3);
		c4 = new Coordinate(2,4);
		c5 = new Coordinate(2,5);
		c6 = new Coordinate(2,6);
		c7 = new Coordinate(2,7);
		
		sub = new Submarine(c);
		dest = new Destroyer(c5, c3, c4);
		aircc = new Aircraft_carrier(c7, c3, c5, c6, c4);
	}

	/**
	 * Testing all methods in class Submarine.
	 */
	@Test
	public void testSubmarine() {
	//	assertEquals(1, sub.getInstances(), 0.1);
		assertEquals("Submarine", sub.getName());
		assertEquals(c2, sub.getFirstCoord());
		assertEquals(1, sub.getLength(), 0.1);
		assertEquals(false, sub.containsCoord(c1));
		assertEquals(false, sub.hitByShot(c1));
		assertEquals(false, sub.isSunk());
		
		assertEquals(true, sub.hitByShot(c2));
		assertEquals(true, sub.isSunk());
		
	}
	
	/**
	 * Testing all methods in class Destroyer.
	 */
	@Test
	public void testDestroyer() {
	//	assertEquals(1, aircc.getInstances(), 0.1);
		assertEquals("Destroyer", dest.getName());
		assertEquals(c3, dest.getFirstCoord());
		assertEquals(3, dest.getLength(), 0.1);
		assertEquals(true, dest.containsCoord(c4));
		assertEquals(true, dest.hitByShot(c5));
		assertEquals(false, dest.isSunk());
		
	}
	
	/**
	 * Testing all methods in class Aircraft_carrier.
	 */
	@Test
	public void testAircraft_carrier() {
	//	assertEquals(2, aircc.getInstances(), 0.1);
		assertEquals("Aircraft carrier", aircc.getName());
		assertEquals(c3, aircc.getFirstCoord());
		assertEquals(5, aircc.getLength(), 0.1);
		assertEquals(true, aircc.containsCoord(c7));
		assertEquals(true, aircc.hitByShot(c5));
		assertEquals(false, sub.isSunk());		
	}

}
