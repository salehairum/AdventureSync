package travelAgencyModels;

import java.util.HashMap;

public class Bus extends Vehicle {
	//attributes
	private int busDriverID;
	private HashMap<Integer, Seat> seats;
	private int noOfSeats;
	private float priceOfSeats;
	private boolean hasTour;
	//private Tour tour;
	
	//getters and setters
	public int getBusDriverID() {
		return busDriverID;
	}
	public void setBusDriverID(int busDriverID) {
		this.busDriverID = busDriverID;
	}
	public int getNoOfSeats() {
		return noOfSeats;
	}
	public void setNoOfSeats(int noOfSeats) {
		this.noOfSeats = noOfSeats;
	}
	public boolean hasTour() {
		return hasTour;
	}
	public void setHasTour(boolean hasTour) {
		this.hasTour = hasTour;
	}
	public float getPriceOfSeats() {
		return priceOfSeats;
	}
	public void setPriceOfSeats(float priceOfSeats) {
		this.priceOfSeats = priceOfSeats;
	}
}