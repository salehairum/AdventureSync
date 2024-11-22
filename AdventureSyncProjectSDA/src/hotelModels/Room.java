package hotelModels;

import java.sql.Date;
import java.util.ArrayList;

import application.Feedback;
import dbHandlers.HotelDBHandler;
import dbHandlers.ReturnListUtility;
import dbHandlers.ReturnObjectUtility;

public class Room {
	//variables
	private int roomID;
	private String description;
	private float pricePerNight;
	private boolean isBooked;
	private ArrayList<Feedback> ratings;
	private int hotelID;
	private HotelDBHandler hotelDbHandler;
	private int overallRating;
	//constructor
	public Room() {
		roomID = 0;
		description = "";
		isBooked = false;
		hotelDbHandler=new HotelDBHandler();
	}
	public Room(int roomID, int overallRating, float pricePerNight, String description) 
	{
		this.roomID = roomID;
		this.description = description;
		this.pricePerNight = pricePerNight;
		this.overallRating = overallRating;
	}
	public Room(int roomID, String description, float pricePerNight, boolean isBooked, 
            ArrayList<Feedback> ratings, int hotelID) {
	    this.roomID = roomID;
	    this.description = description;
	    this.pricePerNight = pricePerNight;
	    this.isBooked = isBooked;
	    this.ratings = ratings != null ? ratings : new ArrayList<>(); // Initialize with empty list if null
	    this.hotelID = hotelID;
		hotelDbHandler=new HotelDBHandler();
	}
	
	public Room(int roomID, String description, float pricePerNight, boolean isBooked, int hotelID) {
	    this.roomID = roomID;
	    this.description = description;
	    this.pricePerNight = pricePerNight;
	    this.isBooked = isBooked;
	    this.ratings =new ArrayList<>(); 
	    this.hotelID = hotelID;
		hotelDbHandler=new HotelDBHandler();
	}
	
	//getter and setter
	public int getRoomID() {
		return roomID;
	}
	public void setRoomID(int roomID) {
		this.roomID = roomID;
	}
	
	public int getOverallRating() {
		return overallRating;
	}
	public void setOverallRating(int overallRating) {
		this.overallRating = overallRating;
	}
	public int getHotelID() {
		return hotelID;
	}
	public void setHotelID(int hotelID) {
		this.hotelID = hotelID;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isBooked() {
		return isBooked;
	}
	public void setBooked(boolean isBooked) {
		this.isBooked = isBooked;
	}
	public float getPricePerNight() {
		return pricePerNight;
	}
	public void setPricePerNight(float pricePerNight) {
		this.pricePerNight = pricePerNight;
	}
	public ArrayList<Feedback> getRatings() {
		return ratings;
	}
	public void setRatings(ArrayList<Feedback> ratings) {
		this.ratings = ratings;
	}
	public ReturnObjectUtility<Room> updateRoomBookingStatus(int roomID, boolean bookingStatus) {
		return hotelDbHandler.updateRoomBookingStatus(roomID, bookingStatus);
	}
	public ReturnListUtility<Room> getRoomDetails(int hotelID) {
		return hotelDbHandler.retrieveRoomList(hotelID);
	}
//	public ReturnObjectUtility<Room> addRoomToBookedRooms(int touristId,int roomID){
//		return hotelDbHandler.add
//	}
	public ReturnObjectUtility<Float> getBill(int roomID, int touristID){
		return hotelDbHandler.getBill(roomID,touristID);
	}
	public ReturnObjectUtility<Boolean> addRoom(Room room) {
		return hotelDbHandler.addRoom(room);
	}
}
