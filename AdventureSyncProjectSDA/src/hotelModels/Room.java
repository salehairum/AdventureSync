package hotelModels;

import java.sql.Date;
import java.util.ArrayList;

import application.Feedback;

public class Room {
	//variables
	private int roomID;
	private String description;
	private float pricePerNight;
	private boolean isBooked;
	private ArrayList<Feedback> ratings;
	private int hotelID;
	private Date bookDate;
	private Date checkoutDate;
	//constructor
	public Room() {
		roomID = 0;
		description = "";
		isBooked = false;
	}
	//getter and setter
	public int getRoomID() {
		return roomID;
	}
	public void setRoomID(int roomID) {
		this.roomID = roomID;
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
	
}