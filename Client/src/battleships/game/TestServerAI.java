/*
 * TestServer.java	
 * Version 1.0 (2013-05-31)
 */
package battleships.game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Test for using the class ServerAI in the game Battleships.
 * 
 * @author Åsa Waldhe
 *
 */
public class TestServerAI {
	
	/**
	 * Instances of classes to test.
	 */	
	Coordinate c1;
	Coordinate c2;

	ServerAI ai;

	@Before
	public void setUp() throws Exception {		
		
		c1 = new Coordinate(4,8);
		c2 = new Coordinate(0,3);
		
		ai = new ServerAI(5, 3, 1);		
	}
	
	/**
	 * Testing methods in ServerAI.
	 */
	@Test
	public void test() {
		
		Coordinate c = ai.shoot();
		System.out.println(c.toString());
		Navy n = ai.getNavy();
		assertEquals(true, n.allSet());
		Ship s = n.shot(c2);
		assertEquals("Submarine", s.getName());
		ai.setResult(c2, s);
	}
}
