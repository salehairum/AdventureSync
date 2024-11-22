package hotelModels;

import java.time.LocalDate;

import accountAndPersonModels.HotelOwner;
import application.Feedback;
import dbHandlers.ReturnListUtility;
import dbHandlers.ReturnObjectUtility;
import hotelOwner.HotelOwnerMenuView;
import javafx.scene.Parent;

public class hotelOwnerController
{
    private HotelOwner hotelOwner;
    private Hotel hotel;
    private Kitchen kitchen;
    private Room room;
    private FoodItem food;
    private FeedbackWithRoomID fRoom;
    public hotelOwnerController()
    {
    	hotelOwner = new HotelOwner();
    	hotel = new Hotel();
    	kitchen=new Kitchen();
    	room=new Room();
    	food=new FoodItem();
    	fRoom= new FeedbackWithRoomID();
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
    
    public ReturnObjectUtility<FoodItem> updateFoodQuantity(int foodID, int quantity, boolean add) {
		return hotel.updateFoodQuantity(foodID, quantity, add);
	}
	public ReturnObjectUtility<Float> getBill(int roomID, int touristID){
		return hotelOwner.getBill(roomID, touristID);
	}
	public ReturnObjectUtility<Float> addMoney(int roomID, float bill){
		return hotelOwner.addMoney(roomID, bill);
	}
	public ReturnObjectUtility<Integer> getHotelID(int hotelOwnerID){
		return hotelOwner.getHotelID(hotelOwnerID);
	}
	public ReturnObjectUtility<Boolean> addRoom(Room room) {
		return hotelOwner.addRoom(room);
	}
	public ReturnObjectUtility<Boolean> addFoodItem(FoodItem foodItem, int hotelID) {
		return kitchen.addFoodItem(foodItem, hotelID);
	}
	public ReturnObjectUtility<Float> getFoodBill(int foodID, int quantity){
		return kitchen.getFoodBill(foodID, quantity);
	}
	public ReturnListUtility<Room> getRoomDetails(int hotelID){
		return room.getRoomDetails(hotelID);
	}
	public ReturnListUtility<FoodItem> getFoodDetails(int hotelID) {
		return food.getFoodDetails(hotelID);
	}
	public ReturnObjectUtility<Float> getOverallRating(int hotelID){
		return fRoom.getOverallRating(hotelID);
	}
	public ReturnListUtility<FeedbackWithRoomID> retrieveFeedbackList(int hotelID) {
		return fRoom.retrieveFeedbackList(hotelID);
	}
}
