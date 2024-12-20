package controllers;

import java.time.LocalDate;

import accountAndPersonModels.HotelOwner;
import application.Feedback;
import dataUtilityClasses.FeedbackWithRoomID;
import dataUtilityClasses.ReturnListUtility;
import dataUtilityClasses.ReturnObjectUtility;
import hotelModels.FoodItem;
import hotelModels.Hotel;
import hotelModels.Kitchen;
import hotelModels.Room;
import hotelOwnerView.HotelOwnerMenuView;
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
	public ReturnObjectUtility<Float> getBill(int roomID, int nNights){
		return hotelOwner.getBill(roomID, nNights);
	}
	public ReturnObjectUtility<Float> addMoneyRoom(int roomID, float bill){
		return hotelOwner.addMoneyRoom(roomID, bill);
	}
	public ReturnObjectUtility<Float> addMoneyFood(int foodID, float bill){
		return hotelOwner.addMoneyFood(foodID, bill);
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
	public ReturnObjectUtility<Integer> getHotelOwnerID(int foodID) {
		return kitchen.getHotelOwnerID(foodID);
	}
	public ReturnObjectUtility<Boolean> addHotel(Hotel hotel, int hotelOwnerID) {
		return hotelOwner.addHotel(hotel, hotelOwnerID);
	}
	public ReturnObjectUtility<Boolean> deleteRoom(int roomID) {
		return hotelOwner.deleteRoom(roomID);
	}
	public ReturnObjectUtility<Integer> checkPassword(String enteredPassword, String username) {
		return hotelOwner.checkPassword(enteredPassword, username);
	}
	public ReturnObjectUtility<Boolean> deleteFoodItem(int foodID) {
		return kitchen.deleteFoodItem(foodID);
	}
	public ReturnObjectUtility<Boolean> updateHotel(Hotel hotel) {
		return hotelOwner.updateHotel(hotel);
	}
	public ReturnObjectUtility<Hotel> retrieveHotelObject(int hotelID) {
		return hotelOwner.retrieveHotelObject(hotelID);
	}
	public ReturnObjectUtility<Boolean> updateRoom(Room room) {
		return hotelOwner.updateRoom(room);
	}
	public ReturnObjectUtility<Room> retrieveRoomObject(int roomID) {
		return hotelOwner.retrieveRoomObject(roomID);
	}
	public ReturnObjectUtility<FoodItem> retrieveFoodItemObject(int foodID) {
		return kitchen.retrieveFoodItemObject(foodID);
	}

	public ReturnObjectUtility<Boolean> updateFoodItem(FoodItem foodItem) {
		return kitchen.updateFoodItem(foodItem);
	}
	public ReturnObjectUtility<HotelOwner> updateHotelOwner(HotelOwner hotelOwner) {
		return hotelOwner.updateHotelOwner(hotelOwner);
	}
	public ReturnObjectUtility<HotelOwner> retrieveAllHotelOwnerData(int HotelOwnerID) {
		return hotelOwner.retrieveAllHotelOwnerData(HotelOwnerID);
	}
	public ReturnObjectUtility<Integer> getNumberOfNights(int roomID, int touristID){
		return hotelOwner.getNumberOfNights(roomID, touristID);
	}
	public ReturnObjectUtility<HotelOwner> deleteHotelOwner(int hotelOwnerID) {
		return hotelOwner.deleteHotelOwner(hotelOwnerID);
	}
	public ReturnObjectUtility<Hotel> retrieveHotelDetails(int hotelID){
		return hotel.retrieveHotelDetails(hotelID);
	}
	public ReturnObjectUtility<Integer> compareOldPassword(String enteredPassword, int hotelOwnerID) {
		return hotelOwner.compareOldPassword(enteredPassword, hotelOwnerID);
	}
	public ReturnObjectUtility<HotelOwner> updatePassword(String password, int accountID) {
		return hotelOwner.updatePassword(password, accountID);
	}
}
