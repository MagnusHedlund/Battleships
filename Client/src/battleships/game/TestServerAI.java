package game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

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
