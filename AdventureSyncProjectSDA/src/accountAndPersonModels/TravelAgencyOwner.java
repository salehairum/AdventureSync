package accountAndPersonModels;

import java.util.HashMap;

import travelAgencyModels.Car;

public class TravelAgencyOwner extends Person {
	private int agencyOwnerID;
	private HashMap<Integer,Car> cars;
	
	//getters and setters
	public int getAgencyOwnerID() {
		return agencyOwnerID;
	}
	public void setAgencyOwnerID(int agencyOwnerID) {
		this.agencyOwnerID = agencyOwnerID;
	}
	public HashMap<Integer, Car> getCars() {
		return cars;
	}
	public void setCars(HashMap<Integer, Car> cars) {
		this.cars = cars;
	}
	
}
