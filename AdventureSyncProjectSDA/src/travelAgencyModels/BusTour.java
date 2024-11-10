package travelAgencyModels;

public class BusTour extends Tour {
	private int busID;	//id of the bus to which this tour is assigned

	//getters and setters
	public int getBusID() {
		return busID;
	}

	public void setBusID(int busID) {
		this.busID = busID;
	}
}
