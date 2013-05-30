//----------------------------------------------------------------
// Namn: Fredrik Str�mbergsson
// Datum: 2013-05-24
// 
// ShipPlacer.java:
// Anv�nds vid utplacering av skepp f�r att h�lla reda p� vilka skepp/delar av skepp som blivit utplacerade.
//----------------------------------------------------------------

package battleships.client;
import java.util.Vector;
import battleships.game.*;

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
	
	// L�gg till en ub�t
	public void addNumSubmarines() {
		Submarines++; 
		myNavy.addShip(new Submarine(currentCoords.elementAt(0)));
		currentCoords.clear();
	}
	
	// L�gg till en j�gare
	public void addNumDestroyers() {
		Destroyers++;
		myNavy.addShip(new Destroyer(currentCoords.elementAt(0), currentCoords.elementAt(1), currentCoords.elementAt(2)));
		currentCoords.clear();
	}
	
	// L�gg till hangarfartyg
	public void addNumAircraftcarriers() {
		Aircraftc++;
		myNavy.addShip(new Aircraft_carrier(currentCoords.elementAt(0), currentCoords.elementAt(1), currentCoords.elementAt(2), currentCoords.elementAt(3), currentCoords.elementAt(4)));
		currentCoords.clear();
	}
	
	// Vad som l�ggs ut just nu
	public void addingShip(String what) {currentlyPlacing = what;}
	
	// R�kna ned antalet klick/utplaceringar (koordinater)
	public void Count() {Counter--;}

	// Nollst�ll
	public void Reset() {
		Submarines = 0;
		Destroyers = 0;
		Aircraftc = 0;
		Counter = 0;
		myNavy = new Navy(5, 3, 1);
		currentCoords.clear();
	}
	
}


