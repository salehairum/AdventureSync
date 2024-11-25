package travelAgencyModels;

import java.util.HashMap;
import java.util.Map;

import application.Feedback;
import dataUtilityClasses.ReturnListUtility;
import dataUtilityClasses.ReturnObjectUtility;
import dbHandlers.BusDBHandler;

public class Bus extends Vehicle {
	//attributes
	private HashMap<Integer, Seat> seats;
	private int noOfSeats;
	private int noOfRows;
	private float priceOfSeats;
	private boolean hasTour;
	private Tour tour;
	private BusDBHandler busHandler;
	private int busDriverID;
	

	//with all parameters
	public Bus() {
		super();
		seats=new HashMap<Integer, Seat>();
		busHandler=new BusDBHandler();
		tour=new Tour();
	}
	
	public Bus(int id, String brand, String model, int year, String plateNumber,HashMap<Integer, Seat> seats, int noOfSeats, int noOfRows, float priceOfSeats, boolean hasTour, Tour tour) {
		super(id, brand, model, year, plateNumber);
		
		seats=new HashMap<Integer, Seat>();
		this.seats = seats;
		
		this.noOfSeats = noOfSeats;
		this.priceOfSeats = priceOfSeats;
		this.hasTour = hasTour;
		this.tour = tour;
		this.noOfRows = noOfRows;
		
		busHandler=new BusDBHandler();
	}
	public Bus(int id, String brand, String model, int year, String plateNumber, int noOfSeats, float priceOfSeats, boolean hasTour) {
		super(id, brand, model, year, plateNumber);
		
		this.noOfSeats = noOfSeats;
		this.priceOfSeats = priceOfSeats;
		this.hasTour = hasTour;
		
	}
	//without tour 
	public Bus(int id, String brand, String model, int year, String plateNumber, HashMap<Integer, Seat> seats, int noOfSeats,int noOfRows, float priceOfSeats) {
		super(id, brand, model, year, plateNumber);
		seats=new HashMap<Integer, Seat>();
		this.noOfSeats = noOfSeats;
		this.priceOfSeats = priceOfSeats;
		this.hasTour = false;
		this.noOfRows = noOfRows;
		this.seats = seats;
		tour=new Tour();
		
		busHandler=new BusDBHandler();
	}
	//without tour and seat with bus driver id 
	public Bus(int id, String brand, String model, int year, String plateNumber, int noOfSeats,int noOfRows, float priceOfSeats, int bdID) {
		super(id, brand, model, year, plateNumber);
		this.noOfSeats = noOfSeats;
		this.priceOfSeats = priceOfSeats;
		this.hasTour = false;
		this.noOfRows = noOfRows;
		busDriverID = bdID;
		busHandler=new BusDBHandler();
	}
	public Bus(int id, String brand, String model, int year, String plateNumber) {
		super(id, brand, model, year, plateNumber);
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
	public int getBusDriverID() {
		return busDriverID;
	}
	public void setBusDriverID(int busDriverID) {
		this.busDriverID = busDriverID;
	}
	public boolean isHasTour() {
		return hasTour;
	}

	public ReturnObjectUtility<Boolean> addBus(Bus bus,int busDriverID){
		return busHandler.addBus(bus, busDriverID);
	}
	
	public ReturnObjectUtility<Seat> updateSeatBookingStatus(int seatID, boolean bookingStatus) {
		Seat seat=new Seat();
		return seat.updateSeatBookingStatus(seatID, bookingStatus);
	}
	
	public ReturnObjectUtility<Bus> retrieveBusObject(int newBusId) {
		return busHandler.retrieveBusObject(newBusId);
	}
	
	public ReturnObjectUtility<Boolean> updateBus(Bus bus){
		return busHandler.updateBus(bus);
	}
	
	public ReturnObjectUtility<Seat> retrieveSeatObject(int seatId, int busId) {
		Seat seat=new Seat();
		return seat.retrieveSeatObject(seatId, busId);
	}
	
	public ReturnListUtility<Bus> retrieveBusList() {
		return busHandler.retrieveBusList();
	}
	public ReturnListUtility<Feedback> retrieveFeedbackList(int busID) {
		return busHandler.retrieveFeedbackList(busID);
	}
	public ReturnListUtility<Bus> retrieveBusListWithBusDriverID() {
		return busHandler.retrieveBusListWithBusDriverID();
	}
	public ReturnListUtility<Bus> getBusDetailsWithTouristID(int touristID) {
		return busHandler.getBusDetailsWithTouristID(touristID);
	}
	public ReturnObjectUtility<Float> getBill(int busId){
		return busHandler.getBill(busId);
	}
	
	public ReturnObjectUtility<Integer> retrieveTourID(int busDriverID){
		return tour.retrieveTourID(busDriverID);
	}
	public ReturnObjectUtility<Float> getOverallRating(int busID){
		return busHandler.getOverallRating(busID);
	}
	public ReturnObjectUtility<Integer> retrieveBusByDriverID(int busDriverID)
	{
		return busHandler.retrieveBusByDriverID(busDriverID);
	}
	public ReturnObjectUtility<Bus> getBusDetail(int busID)
	{
		//return busHandler.getBusDetail(busID);
		ReturnObjectUtility<Bus> bus = busHandler.getBusDetail(busID);
		return busHandler.getBusDetail(busID);
	}
}

