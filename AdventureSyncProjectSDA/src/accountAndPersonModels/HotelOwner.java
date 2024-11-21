package accountAndPersonModels;

import java.time.LocalDate;

import dbHandlers.HotelDBHandler;
import dbHandlers.ReturnObjectUtility;
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
	
	public ReturnObjectUtility<Float> getBill(int roomID){
		return room.getBill(roomID);
	}
	public ReturnObjectUtility<Float> addMoney(int roomID, float bill){
		return hotelDBHandler.addMoney(roomID, bill);
	}
}
