package accountAndPersonModels;


import java.time.LocalDate;
import java.util.HashMap;

import application.Feedback;
import dbHandlers.BusDBHandler;
import dbHandlers.ReturnObjectUtility;
import dbHandlers.TouristDBHandler;
import travelAgencyModels.Seat;
import travelAgencyModels.Bus;
import travelAgencyModels.Car;
import hotelModels.Room;

public class Tourist extends Person {
	private int touristID;
	private HashMap<Integer, Seat> bookedSeats;
	private HashMap<Integer, Car> rentedCars;
	private HashMap<Integer, Room> bookedRooms;
	private TouristDBHandler touristDBHandler;
	
	public Tourist()
	{
		super();
		touristDBHandler=new TouristDBHandler();
	}
	public Tourist(int touristID, String name, LocalDate dob, String cnic)
	{
		super(name, dob, cnic);
		this.touristID = touristID;
		touristDBHandler=new TouristDBHandler();
	}
	
	public Tourist(int touristID)
	{
		super();
		this.touristID = touristID;
		touristDBHandler=new TouristDBHandler();
	}
	
	
	//getters and setters
	public int getTouristID() {
		return touristID;
	}
	public void setTouristID(int touristID) {
		this.touristID = touristID;
	}
	public HashMap<Integer, Seat> getBookedSeats() {
		return bookedSeats;
	}
	public void setBookedSeats(HashMap<Integer, Seat> bookedSeats) {
		this.bookedSeats = bookedSeats;
	}
	public HashMap<Integer, Car> getRentedCars() {
		return rentedCars;
	}
	public void setRentedCars(HashMap<Integer, Car> rentedCars) {
		this.rentedCars = rentedCars;
	}
	public HashMap<Integer, Room> getBookedRooms() {
		return bookedRooms;
	}
	public void setBookedRooms(HashMap<Integer, Room> bookedRooms) {
		this.bookedRooms = bookedRooms;
	}
	
	//db interactions
	public ReturnObjectUtility<Tourist> addTourist(Tourist tourist){
		return touristDBHandler.addTourist(tourist);
	}
	
	public ReturnObjectUtility<Integer> addCarToRentedCars(int touristId,int carID){
		return touristDBHandler.addCarToRentedCars(touristId, carID);
	}
	
	public ReturnObjectUtility<Integer> removeCarFromRentedCars(int touristId,int carID){
		return touristDBHandler.removeCarFromRentedCars(touristId, carID);
	}
	
	public ReturnObjectUtility<Integer> addSeatToBookedSeats(int touristId,int seatID){
		return touristDBHandler.addSeatToBookedSeats(touristId, seatID);
	}

	public ReturnObjectUtility<Tourist> getDetail(int touristID)
	{
		ReturnObjectUtility<Tourist> returnData = touristDBHandler.retrieveTouristData(touristID);
		return returnData;
	}
	public ReturnObjectUtility<Integer> orderFood(int touristId,int foodID){
		return touristDBHandler.orderFood(touristId, foodID);
	}	

	public ReturnObjectUtility<Integer> addRoomToBookedRooms(int touristId,int roomID){
		return touristDBHandler.addRoomToBookedRooms(touristId, roomID);
	}
	
	public ReturnObjectUtility<Room> removeRoomFromBookedRooms(int touristId,int roomID){
		return touristDBHandler.removeRoomFromBookedRooms(touristId, roomID);
	}
	public ReturnObjectUtility<Feedback> giveFeedbackToBus(Feedback feedback) {
		return touristDBHandler.giveFeedbackToBus(feedback);
	}
	public ReturnObjectUtility<Feedback> giveFeedbackToRoom(Feedback feedback) {
		return touristDBHandler.giveFeedbackToRoom(feedback);
	}
	
	public ReturnObjectUtility<Tourist> deductMoney(int touristID, float bill, int transactionID,boolean deduct){
		return touristDBHandler.deductMoney(touristID, bill, transactionID,deduct);
	}
	public ReturnObjectUtility<Boolean> checkBalance(int touristID, float bill){
		return touristDBHandler.checkBalance(touristID, bill);
	}
	public ReturnObjectUtility<Integer> checkPassword(String enteredPassword, String username) {
		return touristDBHandler.checkPassword(enteredPassword, username);
	}
}
