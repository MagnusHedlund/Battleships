/*
 * CompareVertical.java	
 * Version 1.0 (2013-05-30)
 */

package battleships.game;

import java.util.Comparator;

/**
 * This class compares two instances of the class Coordinate in respect of the y-coordinate.
 * @author Åsa Waldhe
 *
 */
public class CompareVertical implements Comparator<Coordinate> {

	/**
	 * Compares two coordinates's y-coordinate and tells whether they are equal, or one has a higher value than the other.
	 * 
	 * @author Åsa Waldhe
	 * 
	 * @param c1, c2				Coordinates to compare.
	 * @return integer				1 means that c1 has a lower value than c2, -1 means that c1 has a higher value than c2, 
	 * 								0 means that they are equal.
	 */
	@Override
	public int compare(Coordinate c1, Coordinate c2) {
		if (c1.getY() < c2.getY())
			return -1;
		else if (c1.getY() > c2.getY())
			return 1;
		else
			return 0;
	}

}
