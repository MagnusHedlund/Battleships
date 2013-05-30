/*
 * ShipPlacer.java
 * Version 1.0 (2013-05-30)
 */

package battleships.client;
import java.util.Vector;
import battleships.game.*;

/**
 * Used by Client to place ships and coordinates while creating a navy.
 * 
 * @author Fredrik Strömbergsson
 */
public class ShipPlacer {
	
	// Deklarationer
	private int Submarines = 0;
	private int Destroyers = 0;
	private int Aircraftc = 0;
	private int Counter = 0;
	private String currentlyPlacing = "";
	private Navy myNavy = new Navy(5, 3, 1);
	private Vector<Coordinate> currentCoords = new Vector<Coordinate>();
	
	// GET metoder
	public int getNumSubmarines() {return Submarines;}
	public int getNumDestroyers() {return Destroyers;}
	public int getNumAircraftcarriers() {return Aircraftc;}
	public boolean placementIsDone() {return Counter == 0;}
	public String whatShipWasPlaced() {return currentlyPlacing;}
	public Navy getNavy() {return myNavy;}
	
	// SET metoder
	public void setCounter(int C) {Counter = C;}
	public void addCurrentCoordinates(int x, int y) {currentCoords.add(new Coordinate(x, y));}
	
	/**
	 * Add a submarine to the Navy
	 */
	public void addNumSubmarines() {
		Submarines++; 
		myNavy.addShip(new Submarine(currentCoords.elementAt(0)));
		currentCoords.clear();
	}
	
	/**
	 * Add a destroyer to the Navy
	 */
	public void addNumDestroyers() {
		Destroyers++;
		myNavy.addShip(new Destroyer(currentCoords.elementAt(0), currentCoords.elementAt(1), currentCoords.elementAt(2)));
		currentCoords.clear();
	}
	
	/**
	 * Add a aircraft carrier to the Navy
	 */
	public void addNumAircraftcarriers() {
		Aircraftc++;
		myNavy.addShip(new Aircraft_carrier(currentCoords.elementAt(0), currentCoords.elementAt(1), currentCoords.elementAt(2), currentCoords.elementAt(3), currentCoords.elementAt(4)));
		currentCoords.clear();
	}
	
	/**
	 * Sets what is currently being placed
	 * @param	what	What is being placed? For example: "Submarine"
	 */
	public void addingShip(String what) {currentlyPlacing = what;}
	
	/**
	 * Decrements the counter that keeps track of how many coordinates we are going to place.
	 */
	public void Count() {Counter--;}

	/**
	 * Resets ShipPlacer to default
	 */
	public void Reset() {
		Submarines = 0;
		Destroyers = 0;
		Aircraftc = 0;
		Counter = 0;
		myNavy = new Navy(5, 3, 1);
		currentCoords.clear();
	}
	
}


