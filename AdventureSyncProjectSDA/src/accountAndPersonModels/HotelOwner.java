package accountAndPersonModels;

import java.time.LocalDate;

import dbHandlers.HotelDBHandler;
import dbHandlers.ReturnObjectUtility;

public class HotelOwner extends Person{
	private int hotelOwnerID;
	private HotelDBHandler hotelDBHandler;

	public HotelOwner()
	{
		super();
		hotelDBHandler=new HotelDBHandler();
	}
	public HotelOwner(int hotelOwnerID, String name, LocalDate dob, String cnic)
	{
		super(name, dob, cnic);
		this.hotelOwnerID = hotelOwnerID;
		hotelDBHandler=new HotelDBHandler();
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
}
