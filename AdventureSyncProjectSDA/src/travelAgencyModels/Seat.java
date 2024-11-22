package travelAgencyModels;

import dbHandlers.BusDBHandler;
import dbHandlers.ReturnListUtility;
import dbHandlers.ReturnObjectUtility;

public class Seat {
	private int seatID;
	private int busID;
	boolean isBooked;
	int rowNo;
	private BusDBHandler busHandler;

	public Seat(int seatID, int busID, boolean isBooked, int rowNo) {
        this.seatID = seatID;
        this.busID = busID;
        this.isBooked = isBooked;
        this.rowNo = rowNo;
        busHandler=new BusDBHandler();
    }
	//default booked=false
	public Seat(int seatID, int busID, int rowNo) {
        this.seatID = seatID;
        this.busID = busID;
        this.isBooked = false;
        this.rowNo = rowNo;
        busHandler=new BusDBHandler();
    }
	
	public Seat() {
        busHandler=new BusDBHandler();
	}
	
	//getters and setters
	public int getSeatID() {
		return seatID;
	}
	public void setSeatID(int seatID) {
		this.seatID = seatID;
	}
	public int getBusID() {
		return busID;
	}
	public void setBusID(int busID) {
		this.busID = busID;
	}
	public boolean isBooked() {
		return isBooked;
	}
	public void setBooked(boolean isBooked) {
		this.isBooked = isBooked;
	}
	public int getRowNo() {
		return rowNo;
	}
	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}
	
	public ReturnObjectUtility<Seat> updateSeatBookingStatus(int seatID, boolean bookingStatus) {
		return busHandler.updateSeatBookingStatus(seatID, bookingStatus);
	}
	
	public ReturnObjectUtility<Seat> retrieveSeatObject(int seatId, int busId) {
		return busHandler.retrieveSeatObject(seatId, busId);
	}
	public ReturnListUtility<Seat> getSeatDetails(int busId) {
		return busHandler.getSeatDetails(busId);
	}
}
