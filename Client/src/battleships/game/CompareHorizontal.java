/*
 * CompareHorizontal.java	
 * Version 1.0 (2013-05-31)
 */

package battleships.game;

import java.util.Comparator;

/**
 * This class compares two instances of the class Coordinate in respect of the x-coordinate.
 * @author Åsa Waldhe
 *
 */
public class CompareHorizontal implements Comparator<Coordinate> {

	/**
	 * Compares two coordinates's x-coordinate and tells whether they are equal, or one has a higher value than the other.
	 * 
	 * @param c1, c2				Coordinates to compare.
	 * @return integer				1 means that c1 has a lower value than c2, -1 means that c1 has a higher value than c2, 
	 * 								0 means that they are equal.
	 */
	@Override
	public int compare(Coordinate c1, Coordinate c2) {
		if (c1.getX() < c2.getX())
			return -1;
		else if (c1.getX() > c2.getX())
			return 1;
		else
			return 0;
	}
}
