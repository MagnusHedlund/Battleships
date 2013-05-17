//----------------------------------------------------------------
// Namn: Fredrik Str�mbergsson
// Datum: 2013-05-17
// 
// ShipPlacer.java:
// Anv�nds vid utplacering av skepp f�r att h�lla reda p� vilka skepp/delar av skepp som blivit utplacerade.
//----------------------------------------------------------------

public class ShipPlacer {
	
	// Deklarationer
	private int Submarines = 0;
	private int Destroyers = 0;
	private int Aircraftc = 0;
	private int Counter = 0;
	private String currentlyPlacing = "";
	
	// GET metoder
	public int getNumSubmarines() {return Submarines;}
	public int getNumDestroyers() {return Destroyers;}
	public int getNumAircraftcarriers() {return Aircraftc;}
	public boolean placementIsDone() {return Counter == 0;}
	public String whatShipWasPlaced() {return currentlyPlacing;}
	
	// SET metoder
	public void setCounter(int C) {Counter = C;}
	public void addNumSubmarines() {Submarines++;}
	public void addNumDestroyers() {Destroyers++;}
	public void addNumAircraftcarriers() {Aircraftc++;}
	public void addingShip(String what) {currentlyPlacing = what;}
	
	// R�kna ned antalet klick/utplaceringar
	public void Count() {Counter--;}
	
	// Nollst�ll
	public void Reset() 
	{
		Submarines = 0;
		Destroyers = 0;
		Aircraftc = 0;
		Counter = 0;		
	}
	
}


