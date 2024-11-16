package travelAgencyModels;

import java.util.HashMap;

import dbHandlers.BusDBHandler;
import dbHandlers.ReturnObjectUtility;

public class Bus extends Vehicle {
	//attributes
	private HashMap<Integer, Seat> seats;
	private int noOfSeats;
	private int noOfRows;
	private float priceOfSeats;
	private boolean hasTour;
	private Tour tour;
	private BusDBHandler busHandler;
	
	//with all parameters
	public Bus(int id, String brand, String model, int year, String plateNumber,HashMap<Integer, Seat> seats, int noOfSeats, int noOfRows, float priceOfSeats, boolean hasTour, Tour tour) {
		super(id, brand, model, year, plateNumber);
		this.seats = seats;
		this.noOfSeats = noOfSeats;
		this.priceOfSeats = priceOfSeats;
		this.hasTour = hasTour;
		this.tour = tour;
		this.noOfRows = noOfRows;
		
		busHandler=new BusDBHandler();
	}
	
	//without tour 
	public Bus(int id, String brand, String model, int year, String plateNumber, int noOfSeats,int noOfRows, float priceOfSeats, HashMap<Integer, Seat> seats) {
		super(id, brand, model, year, plateNumber);
		this.noOfSeats = noOfSeats;
		this.priceOfSeats = priceOfSeats;
		this.hasTour = false;
		this.noOfRows = noOfRows;
		this.seats = seats;
		
		busHandler=new BusDBHandler();
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
	public int getNoOfRows() {
		return noOfRows;
	}
	public void setNoOfRows(int noOfRows) {
		this.noOfRows = noOfRows;
	}

	public ReturnObjectUtility<Boolean> addBus(Bus bus){
		
		return busHandler.addBus(bus);
	}
}
