package travelAgencyModels;

import java.util.HashMap;

public class Bus extends Vehicle {
	//attributes
	private HashMap<Integer, Seat> seats;
	private int noOfSeats;
	private float priceOfSeats;
	private boolean hasTour;
	private Tour tour;
	
	public Bus(int id, String brand, String model, int year, String plateNumber,HashMap<Integer, Seat> seats, int noOfSeats, float priceOfSeats, boolean hasTour, Tour tour) {
		super(id, brand, model, year, plateNumber);
		this.seats = seats;
		this.noOfSeats = noOfSeats;
		this.priceOfSeats = priceOfSeats;
		this.hasTour = hasTour;
		this.tour = tour;
	}
	
	//getters and setters
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
	public HashMap<Integer, Seat> getSeats() {
		return seats;
	}
	public void setSeats(HashMap<Integer, Seat> seats) {
		this.seats = seats;
	}
	public Tour getTour() {
		return tour;
	}
	public void setTour(Tour tour) {
		this.tour = tour;
	}
}
