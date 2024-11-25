package hotelModels;

import java.util.ArrayList;

import dataUtilityClasses.ReturnListUtility;
import dataUtilityClasses.ReturnObjectUtility;
import dbHandlers.HotelDBHandler;

public class Hotel {
	//variables
	private int hotelID;
	private String location;
	private ArrayList<Room> rooms;
	private Kitchen kitchen;
	private String hotelName;
	private HotelDBHandler hotelDbHandler;
	private Room room;
	//constructor
	public Hotel()
	{
		hotelID = 0;
		location = "";
		rooms = null;
		hotelName="";
		kitchen=new Kitchen();
        hotelDbHandler=new HotelDBHandler();
        room=new Room();
	}
	public Hotel(int hotelID, String location, String hotelName)
	{
		this.hotelID = hotelID;
		this.hotelName = hotelName;
		this.location = location;
        hotelDbHandler=new HotelDBHandler();
        room=new Room();
	}
	public Hotel(int hotelID, String hotelName, String location, Kitchen kitchen, ArrayList<Room> rooms) {
	        this.hotelID = hotelID;
	        this.hotelName = hotelName;
	        this.location = location;
	        this.kitchen = kitchen;
	        this.rooms = rooms != null ? rooms : new ArrayList<>(); 
	        hotelDbHandler=new HotelDBHandler();
	        room=new Room();
	 }
	//getter and setter
	public int getHotelID() {
		return hotelID;
	}
	public void setHotelID(int hotelID) {
		this.hotelID = hotelID;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public ArrayList<Room> getRooms() {
		return rooms;
	}
	public void setRooms(ArrayList<Room> rooms) {
		this.rooms = rooms;
	}
	public Kitchen getKitchen() {
		return kitchen;
	}
	public void setKitchen(Kitchen kitchen) {
		this.kitchen = kitchen;
	}
	//other functions
	public String getHotelName() {
		return hotelName;
	}
	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}
	
	public ReturnObjectUtility<Hotel> retrieveHotelObject(int hotelID) {
		return room.retrieveHotelObject(hotelID);
	}
	public ReturnObjectUtility<Room> updateRoomBookingStatus(int roomID, boolean bookingStatus) {
		Room room=new Room();
		return room.updateRoomBookingStatus(roomID, bookingStatus);
	}
	public ReturnObjectUtility<FoodItem> updateFoodQuantity(int foodID, int quantity, boolean add) {
		return kitchen.updateFoodQuantity(foodID, quantity, add);
	}
	public ReturnListUtility<Hotel> getHotelDetails() {
		return hotelDbHandler.retrieveHotelList();
	}
	public ReturnObjectUtility<Boolean> addHotel(Hotel hotel, int hotelOwnerID) {
		return hotelDbHandler.addHotel(hotel, hotelOwnerID);
	}
	public ReturnObjectUtility<Boolean> updateHotel(Hotel hotel) {
		return hotelDbHandler.updateHotel(hotel);
	}
	public ReturnObjectUtility<Integer> getNumberOfNights(int roomID, int touristID){
		return hotelDbHandler.getNumberOfNights(roomID, touristID);
	}
	public ReturnObjectUtility<Hotel> retrieveHotelDetails(int hotelID){
		return hotelDbHandler.retrieveHotelDetails(hotelID);
	}
}
