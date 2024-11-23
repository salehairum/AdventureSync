package accountAndPersonModels;

import java.time.LocalDate;

import dbHandlers.HotelDBHandler;
import dbHandlers.ReturnObjectUtility;
import hotelModels.Hotel;
import hotelModels.Room;

public class HotelOwner extends Person{
	private int hotelOwnerID;
	private HotelDBHandler hotelDBHandler;
	private Room room;

	public HotelOwner()
	{
		super();
		hotelDBHandler=new HotelDBHandler();
		room=new Room();
	}
	public HotelOwner(int hotelOwnerID, String name, LocalDate dob, String cnic)
	{
		super(name, dob, cnic);
		this.hotelOwnerID = hotelOwnerID;
		hotelDBHandler=new HotelDBHandler();
		room=new Room();
	}
	//getters and setters
	public int getHotelOwnerID() {
		return hotelOwnerID;
	}

	public void setHotelOwnerID(int hotelOwnerID) {
		this.hotelOwnerID = hotelOwnerID;
	}
	
	public ReturnObjectUtility<HotelOwner> getDetail(int hotelOwnerID)
	{
		ReturnObjectUtility<HotelOwner> returnData = hotelDBHandler.retrieveHotelOwnerData(hotelOwnerID);
		return returnData;
	}
	
	public ReturnObjectUtility<HotelOwner> addHotelOwner(HotelOwner hotelOwner) {
		return hotelDBHandler.addHotelOwner(hotelOwner);
	}
	
	public ReturnObjectUtility<Float> getBill(int roomID, int touristID){
		return room.getBill(roomID,touristID);
	}
	public ReturnObjectUtility<Float> addMoney(int roomID, float bill){
		return hotelDBHandler.addMoney(roomID, bill);
	}
	
	public ReturnObjectUtility<Integer> getHotelID(int hotelOwnerID){
		return hotelDBHandler.getHotelID(hotelOwnerID);
	}
	
	public ReturnObjectUtility<Boolean> addRoom(Room room) {
		return room.addRoom(room);
	}
	public ReturnObjectUtility<Boolean> addHotel(Hotel hotel, int hotelOwnerID) {
		return hotel.addHotel(hotel, hotelOwnerID);
	}
	public ReturnObjectUtility<Boolean> deleteRoom(int roomID) {
		return room.deleteRoom(roomID);
	}
	public ReturnObjectUtility<Integer> checkPassword(String enteredPassword, String username) {
		return hotelDBHandler.checkPassword(enteredPassword, username);
	}
}
