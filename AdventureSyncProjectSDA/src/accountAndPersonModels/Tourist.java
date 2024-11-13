package accountAndPersonModels;


import java.util.HashMap;

import travelAgencyModels.Seat;
import travelAgencyModels.Car;
import application.Room;

public class Tourist extends Person {
	private int touristID;
	private HashMap<Integer, Seat> bookedSeats;
	private HashMap<Integer, Car> rentedCars;
	private HashMap<Integer, Room> bookedRooms;
	
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
}
