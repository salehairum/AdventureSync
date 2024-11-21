package travelAgencyModels;

import java.time.LocalDate;

import accountAndPersonModels.BusDriver;
import accountAndPersonModels.Tourist;
import application.Feedback;
import dbHandlers.ReturnObjectUtility;
import hotelModels.Hotel;
import hotelModels.Room;

public class TouristController {
	Tourist tourist;
	Hotel hotel;
	Bus bus;
	public TouristController() {
		tourist=new Tourist();
		hotel=new Hotel();
		bus=new Bus();
	}
	public ReturnObjectUtility<Tourist> addTourist(Tourist newTourist){
		return tourist.addTourist(newTourist);
	}
	
	public ReturnObjectUtility<Integer> addCarToRentedCars(int touristId,int carID){
		return tourist.addCarToRentedCars(touristId, carID);
	}
	
	public ReturnObjectUtility<Tourist> removeCarFromRentedCars(int touristId,int carID){
		return tourist.removeCarFromRentedCars(touristId, carID);
	}
	
	public ReturnObjectUtility<Integer> addSeatToBookedSeats(int touristId,int seatID){
		return tourist.addSeatToBookedSeats(touristId, seatID);
	}
	public String[] getTouristProfileDetail(int touristId)
    {
    	ReturnObjectUtility<Tourist> returnData = tourist.getDetail(touristId);
    	String touristName = returnData.getObject().getName();
        int touristId_ = returnData.getObject().getTouristID();
        String touristIdStr = String.valueOf(touristId_);
        String touristCnic = returnData.getObject().getCnic();
        LocalDate dob = returnData.getObject().getDob();
        String touristDob = dob.toString();
        String[] profileDetails = {touristName, touristIdStr, touristCnic, touristDob};
        return profileDetails;
    }
	
	public ReturnObjectUtility<Room> addRoomToBookedRooms(int touristId,int roomID){
		return tourist.addRoomToBookedRooms(touristId, roomID);
	}
	
	public ReturnObjectUtility<Room> removeRoomFromBookedRooms(int touristId,int roomID){
		return tourist.removeRoomFromBookedRooms(touristId, roomID);
	}
	
	public ReturnObjectUtility<Hotel> retrieveHotelObject(int hotelID) {
		return hotel.retrieveHotelObject(hotelID);
	}

	public ReturnObjectUtility<Bus> retrieveBusObject(int busId) {
		return bus.retrieveBusObject(busId);
	}
	public ReturnObjectUtility<Feedback> giveFeedbackToBus(Feedback feedback) {
		return tourist.giveFeedbackToBus(feedback);
	}
	public ReturnObjectUtility<Feedback> giveFeedbackToRoom(Feedback feedback) {
		return tourist.giveFeedbackToRoom(feedback);
	}
	
	public ReturnObjectUtility<Tourist> deductMoney(int touristID, float bill, int transactionID,boolean deduct){
		return tourist.deductMoney(touristID, bill, transactionID, deduct);
	}
	public ReturnObjectUtility<Boolean> checkBalance(int touristID, float bill){
		return tourist.checkBalance(touristID, bill);
	}
}
