package hotelModels;

import java.sql.Date;
import java.util.ArrayList;

import application.Feedback;
import dataUtilityClasses.ReturnListUtility;
import dataUtilityClasses.ReturnObjectUtility;
import dbHandlers.HotelDBHandler;

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
	public Room(int roomID, int overallRating, float pricePerNight, String description, boolean isBooked) 
	{
		this.roomID = roomID;
		this.description = description;
		this.pricePerNight = pricePerNight;
		this.overallRating = overallRating;
		this.isBooked = isBooked;
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
	public boolean getIsBooked() {
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
	public ReturnListUtility<Room> getNonBookedRoomDetails(int hotelID) {
		return hotelDbHandler.retrieveNonBookedRoomList(hotelID);
	}
//	public ReturnObjectUtility<Room> addRoomToBookedRooms(int touristId,int roomID){
//		return hotelDbHandler.add
//	}
	public ReturnObjectUtility<Float> getBill(int roomID, int nNights){
		return hotelDbHandler.getBill(roomID,nNights);
	}
	public ReturnObjectUtility<Boolean> addRoom(Room room) {
		return hotelDbHandler.addRoom(room);
	}
	public ReturnObjectUtility<Boolean> deleteRoom(int roomID) {
		return hotelDbHandler.deleteRoom(roomID);
	}
	public ReturnObjectUtility<Boolean> updateRoom(Room room) {
		return hotelDbHandler.updateRoom(room);
	}
	public ReturnObjectUtility<Room> retrieveRoomObject(int roomID) {
		return hotelDbHandler.retrieveRoomObject(roomID);
	}
	public ReturnObjectUtility<Hotel> retrieveHotelObject(int hotelID) {
		return hotelDbHandler.retrieveHotelObject(hotelID);
	}
}
