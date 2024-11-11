package hotelModels;

import java.util.ArrayList;

public class Hotel {
	//variables
	private int hotelID;
	private String location;
	private ArrayList<Room> rooms;
	private Kitchen kitchen;
	//constructor
	public Hotel()
	{
		hotelID = 0;
		location = "";
		rooms = null;
		kitchen = null;
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
	
}
