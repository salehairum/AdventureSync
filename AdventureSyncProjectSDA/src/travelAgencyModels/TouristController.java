package travelAgencyModels;

import accountAndPersonModels.Tourist;
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
	
	public ReturnObjectUtility<Tourist> addCarToRentedCars(int touristId,int carID){
		return tourist.addCarToRentedCars(touristId, carID);
	}
	
	public ReturnObjectUtility<Tourist> removeCarFromRentedCars(int touristId,int carID){
		return tourist.removeCarFromRentedCars(touristId, carID);
	}
	
	public ReturnObjectUtility<Seat> addSeatToBookedSeats(int touristId,int seatID){
		return tourist.addSeatToBookedSeats(touristId, seatID);
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
	
}
