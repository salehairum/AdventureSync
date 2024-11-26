package accountAndPersonModels;

import java.time.LocalDate;

import dataUtilityClasses.ReturnObjectUtility;
import dbHandlers.HotelDBHandler;
import hotelModels.Hotel;
import hotelModels.Room;

public class HotelOwner extends Person{
	private int hotelOwnerID;
	private HotelDBHandler hotelDBHandler;
	private Room room;
	private Hotel hotel;

	public HotelOwner()
	{
		super();
		hotelDBHandler=new HotelDBHandler();
		room=new Room();
		hotel=new Hotel();
	}
	public HotelOwner(int hotelOwnerID, String name, LocalDate dob, String cnic)
	{
		super(name, dob, cnic);
		this.hotelOwnerID = hotelOwnerID;
		hotelDBHandler=new HotelDBHandler();
		room=new Room();
		hotel=new Hotel();
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
	
	public ReturnObjectUtility<Float> getBill(int roomID, int nNights){
		return room.getBill(roomID,nNights);
	}
	public ReturnObjectUtility<Float> addMoneyRoom(int roomID, float bill){
		return hotelDBHandler.addMoneyRoom(roomID, bill);
	}
	public ReturnObjectUtility<Float> addMoneyFood(int foodID, float bill){
		return hotelDBHandler.addMoneyFood(foodID, bill);
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
	public ReturnObjectUtility<Boolean> updateHotel(Hotel hotel) {
		return hotel.updateHotel(hotel);
	}
	public ReturnObjectUtility<Hotel> retrieveHotelObject(int hotelID) {
		return hotel.retrieveHotelObject(hotelID);
	}
	public ReturnObjectUtility<Boolean> updateRoom(Room room) {
		return this.room.updateRoom(room);
	}
	public ReturnObjectUtility<Room> retrieveRoomObject(int roomID) {
		return room.retrieveRoomObject(roomID);
	}
	public ReturnObjectUtility<HotelOwner> updateHotelOwner(HotelOwner hotelOwner) {
		return hotelDBHandler.updateHotelOwner(hotelOwner);
	}
	public ReturnObjectUtility<HotelOwner> retrieveAllHotelOwnerData(int HotelOwnerID) {
		return hotelDBHandler.retrieveAllHotelOwnerData(HotelOwnerID);
	}
	public ReturnObjectUtility<Integer> getNumberOfNights(int roomID, int touristID){
		return hotel.getNumberOfNights(roomID, touristID);
	}
	public ReturnObjectUtility<HotelOwner> deleteHotelOwner(int hotelOwnerID) {
		return hotelDBHandler.deleteHotelOwner(hotelOwnerID);
	}
	public ReturnObjectUtility<Integer> compareOldPassword(String enteredPassword, int hotelOwnerID) {
		return hotelDBHandler.compareOldPassword(enteredPassword, hotelOwnerID);
	}
	public ReturnObjectUtility<HotelOwner> updatePassword(String password, int accountID) {
		return hotelDBHandler.updatePassword(password, accountID);
	}
}
