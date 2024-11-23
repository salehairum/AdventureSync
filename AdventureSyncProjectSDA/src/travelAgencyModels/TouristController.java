package travelAgencyModels;

import java.time.LocalDate;

import accountAndPersonModels.BusDriver;
import accountAndPersonModels.Tourist;
import application.Feedback;
import dbHandlers.ReturnListUtility;
import dbHandlers.ReturnObjectUtility;
import hotelModels.FoodItem;
import hotelModels.Hotel;
import hotelModels.Room;
import hotelModels.RoomWithHotel;

public class TouristController {
	Tourist tourist;
	Hotel hotel;
	Bus bus;
	Seat seat;
	Room room;
	RoomWithHotel roomWithHotel;
	FoodItem food;
	public TouristController() {
		tourist=new Tourist();
		hotel=new Hotel();
		bus=new Bus();
		seat=new Seat();
		room = new Room();
		roomWithHotel = new RoomWithHotel();
		food = new FoodItem();
	}
	public ReturnObjectUtility<Tourist> addTourist(Tourist newTourist){
		return tourist.addTourist(newTourist);
	}
	
	public ReturnObjectUtility<Integer> addCarToRentedCars(int touristId,int carID){
		return tourist.addCarToRentedCars(touristId, carID);
	}
	
	public ReturnObjectUtility<Integer> removeCarFromRentedCars(int touristId,int carID){
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
	
	public ReturnObjectUtility<Integer> addRoomToBookedRooms(int touristId,int roomID){
		return tourist.addRoomToBookedRooms(touristId, roomID);
	}
	
	public ReturnObjectUtility<Integer> removeRoomFromBookedRooms(int touristId,int roomID){
		return tourist.removeRoomFromBookedRooms(touristId, roomID);
	}
	
	public ReturnObjectUtility<Hotel> retrieveHotelObject(int hotelID) {
		return hotel.retrieveHotelObject(hotelID);
	}

	public ReturnObjectUtility<Bus> retrieveBusObject(int busId) {
		return bus.retrieveBusObject(busId);
	}
	public ReturnListUtility<Bus> getBusDetailsWithBusDriverID() {
		return bus.retrieveBusListWithBusDriverID();
	}
	public ReturnListUtility<Seat> getSeatDetails(int busID) {
		return seat.getSeatDetails(busID);
	}
	public ReturnListUtility<Hotel> getHotelDetails() {
		return hotel.getHotelDetails();
	}
	public ReturnListUtility<FoodItem> getFoodDetails(int hotelID) {
		return food.getFoodDetails(hotelID);
	}
	public ReturnListUtility<Room> getRoomDetails(int hotelID) {
		return room.getRoomDetails(hotelID);
	}
	public ReturnListUtility<RoomWithHotel> getBookedRoomDetails(int touristID) {
		return roomWithHotel.getBookedRoomDetails(touristID);
	}
	public ReturnObjectUtility<Feedback> giveFeedbackToBus(Feedback feedback) {
		return tourist.giveFeedbackToBus(feedback);
	}
	public ReturnObjectUtility<Feedback> giveFeedbackToRoom(Feedback feedback) {
		return tourist.giveFeedbackToRoom(feedback);
	}

	public ReturnObjectUtility<Integer> orderFood(int touristId,int foodID){
		return tourist.orderFood(touristId, foodID);
	}	
	
	public ReturnObjectUtility<Tourist> deductMoney(int touristID, float bill, int transactionID,boolean deduct){
		return tourist.deductMoney(touristID, bill, transactionID, deduct);
	}
	public ReturnObjectUtility<Boolean> checkBalance(int touristID, float bill){
		return tourist.checkBalance(touristID, bill);
	}
	public ReturnObjectUtility<Integer> checkPassword(String enteredPassword, String username) {
		return tourist.checkPassword(enteredPassword, username);
	}
}
