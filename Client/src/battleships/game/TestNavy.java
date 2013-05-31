/*
 * TestNavy.java	
 * Version 1.0 (2013-05-31)
 */

package battleships.game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * This class is used for testing the class Navy, a part of the game Battleshs.
 * 
 * @author Åsa Waldhe
 *
 */
public class TestNavy {
		
	/**
	 * Instances of classes to test.
	 */	
	Navy navy, navy1;
	Coordinate c = null;
	Coordinate c1 = null;
	Coordinate c2 = null;
	Coordinate c3 = null;
	Coordinate c4 = null;
	Coordinate c5 = null;
	Coordinate c6 = null;
	Coordinate c7 = null;
	
	Submarine sub, sub1;
	Destroyer dest, dest1, dest2;
	Aircraft_carrier aircc;
	
	Map map;
	
	/**
	 * Set-up method, before each test.
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		navy = new Navy(2, 3, 1);
		navy1 = new Navy(2, 0, 0);
		
		c = new Coordinate(1,3);
		c1 = new Coordinate(4,8);
		c2 = new Coordinate(1,3);
		c3 = new Coordinate(2,3);
		c4 = new Coordinate(2,4);
		c5 = new Coordinate(2,5);
		c6 = new Coordinate(2,6);
		c7 = new Coordinate(2,7);
		
		sub = new Submarine(c);
		sub1 = new Submarine(c1);
		dest = new Destroyer(c5, c3, c4);
		dest = new Destroyer(c7, c6, c5);
		dest = new Destroyer(c5, c3, c4);
		aircc = new Aircraft_carrier(c7, c3, c5, c6, c4);
		
		map = new Map();
	}
	
	/**
	 * Testing methods in Navy. 
	 */
	@Test
	public void test() {
		navy.addShip(sub);
		navy.addShip(dest);
		navy.addShip(aircc);
		
		assertEquals(false, navy.allGone());
		assertEquals(false, navy.allSet());
		
		navy.addShip(sub1);
		navy.addShip(dest2);
		navy.addShip(dest2);
		
		assertEquals(6, navy.numberShips(), 0.1);
		assertEquals(true, navy.allSet());
		
		Ship s = null;
		s = navy.shot(c);
		assertEquals("Submarine", s.getName());
		assertEquals(true, s.isSunk());
		
		navy1.addShip(sub);
		navy1.addShip(sub1);
		s = navy1.shot(c);
		s = navy1.shot(c1);
		assertEquals(true, navy1.allGone());
		// Getmap
		map = navy1.getMap();
		int[][] a = map.getOcean();
		for(int i=0; i<a.length; i++){
			for(int j=0; j<a.length; j++)
				System.out.println(a[i][j]);
		}		
	}

}
