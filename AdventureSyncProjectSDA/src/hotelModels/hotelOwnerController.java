package hotelModels;

import java.time.LocalDate;

import accountAndPersonModels.HotelOwner;
import dbHandlers.ReturnObjectUtility;
import hotelOwner.HotelOwnerMenuView;
import javafx.scene.Parent;

public class hotelOwnerController
{
    private HotelOwner hotelOwner;
    private Hotel hotel;
    
    public hotelOwnerController()
    {
    	hotelOwner = new HotelOwner();
    	hotel = new Hotel();
    }
    public String[] getHotelOwnerProfileDetail(int hotelOwnerId)
    {
    	ReturnObjectUtility<HotelOwner> returnData = hotelOwner.getDetail(hotelOwnerId);
    	String hotelOwnerName = returnData.getObject().getName();
        int hotelOwnerId_ = returnData.getObject().getHotelOwnerID();
        String hotelOwnerIdStr = String.valueOf(hotelOwnerId_);
        String hotelOwnerCnic = returnData.getObject().getCnic();
        LocalDate dob = returnData.getObject().getDob();
        String hotelOwnerDob = dob.toString();
        String[] profileDetails = {hotelOwnerName, hotelOwnerIdStr, hotelOwnerCnic, hotelOwnerDob};
        return profileDetails;
    }
    public ReturnObjectUtility<HotelOwner> addHotelOwner(HotelOwner newHotelOwner) {
		return hotelOwner.addHotelOwner(newHotelOwner);
	}
    
    public ReturnObjectUtility<Room> updateRoomBookingStatus(int roomID, boolean bookingStatus) {
		return hotel.updateRoomBookingStatus(roomID, bookingStatus);
	}
}
