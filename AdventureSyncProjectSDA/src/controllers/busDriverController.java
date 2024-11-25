package controllers;

import java.time.LocalDate;
import java.util.Date;

import accountAndPersonModels.BusDriver;
import accountAndPersonModels.HotelOwner;
import application.Feedback;
import busDriverView.BusDriverManageBusView;
import busDriverView.BusDriverMenuView;
import busDriverView.BusDriverMgrAccountView;
import dataUtilityClasses.ReturnListUtility;
import dataUtilityClasses.ReturnObjectUtility;
import travelAgencyModels.Bus;
import travelAgencyModels.Seat;
import travelAgencyModels.Tour;

//change this one
public class busDriverController {
	
	BusDriver busDriver;
	Bus bus;
	Tour tour;
	public busDriverController() {
		bus=new Bus();
		busDriver=new BusDriver();
		tour = new Tour();
	}

	public ReturnObjectUtility<Boolean> addBus(Bus newBus,int busDriverID){
		bus=newBus;
		return bus.addBus(newBus, busDriverID);
	}
	
	public ReturnObjectUtility<Boolean> updateBus(Bus bus){
		return bus.updateBus(bus);
	}
	public ReturnObjectUtility<Seat> updateSeatBookingStatus(int seatID, boolean bookingStatus) {
		return bus.updateSeatBookingStatus(seatID, bookingStatus);
	}
	
	public ReturnObjectUtility<Bus> retrieveBusObject(int busId) {
		return bus.retrieveBusObject(busId);
	}
	
	public String[] getBusDriverProfileDetail(int busDriverId)
    {
    	ReturnObjectUtility<BusDriver> returnData = busDriver.getDetail(busDriverId);
    	String busDriverName = returnData.getObject().getName();
        int busDriverId_ = returnData.getObject().getBusDriverID();
        String busDriverIdStr = String.valueOf(busDriverId_);
        String busDriverCnic = returnData.getObject().getCnic();
        LocalDate dob = returnData.getObject().getDob();
        String busDriverDob = dob.toString();
        String[] profileDetails = {busDriverName, busDriverIdStr, busDriverCnic, busDriverDob};
        return profileDetails;
    }
	public String[] getBusDetail(int busId)
    {
    	ReturnObjectUtility<Bus> returnData = bus.getBusDetail(busId);
    	String busID = String.valueOf(returnData.getObject().getID());
        String brand = returnData.getObject().getBrand();
        String model = returnData.getObject().getModel();
        String year = String.valueOf(returnData.getObject().getYear());
        String noOfSeat = String.valueOf(returnData.getObject().getNoOfSeats());
        String seatFee = String.valueOf(returnData.getObject().getPriceOfSeats());
        String plateNo = String.valueOf(returnData.getObject().getPlateNumber());
        String isAssigned = String.valueOf(returnData.getObject().isHasTour());
        String[] profileDetails = {busID, brand, model, year, noOfSeat, seatFee, plateNo, isAssigned};
        return profileDetails;
    }
	//db interaction functions
	public ReturnObjectUtility<BusDriver> addBusDriver(BusDriver bDriver){
		return busDriver.addBusDriver(bDriver);
	}
	public String[] getBusTourDetail(int busID){
		ReturnObjectUtility<Tour> returnData = tour.getBusTourDetail(busID);
		if(returnData != null)
		{
			String tourID = String.valueOf(returnData.getObject().getTourID());
	        String origin = returnData.getObject().getOrigin();
	        String destination = returnData.getObject().getDestination();
	        Date date = returnData.getObject().getDate();
	        String dateStr = date.toString();
	        String[] tourDetails = {tourID, origin, destination, dateStr};
	        return tourDetails;
		}
		else
			return null;
		
	}
	public ReturnListUtility<Bus> retrieveBusList() {
		return bus.retrieveBusList();
	}
	public ReturnListUtility<Feedback> retrieveFeedbackList(int busID) {
		return bus.retrieveFeedbackList(busID);
	}
	public ReturnObjectUtility<Seat> retrieveSeatObject(int seatId, int busId) {
		return bus.retrieveSeatObject(seatId, busId);
	}
	
	public ReturnObjectUtility<Float> getBill(int busId){
		return bus.getBill(busId);
	}
	
	public ReturnObjectUtility<Float> addMoney(int busId, float bill){
		return busDriver.addMoney(busId, bill);
	}
	
	public ReturnObjectUtility<Integer> retrieveTourID(int busDriverID){
		return bus.retrieveTourID(busDriverID);
	}
	public ReturnObjectUtility<Boolean> completeTour(int tourID) {
		return busDriver.completeTour(tourID);
	}
	public ReturnObjectUtility<Float> getOverallRating(int busID){
		return bus.getOverallRating(busID);
  }
	public ReturnObjectUtility<Integer> checkPassword(String enteredPassword, String username) {
		return busDriver.checkPassword(enteredPassword, username);
	}
	public ReturnObjectUtility<BusDriver> retrieveAllBusDriverData(int busDriverID) {
		return busDriver.retrieveAllBusDriverData(busDriverID);
	}
	public ReturnObjectUtility<BusDriver> updateBusDriver(BusDriver busDriver) {
		return busDriver.updateBusDriver(busDriver);
	}
	public ReturnObjectUtility<BusDriver> deleteBusDriver(int busDriverID) {
		return busDriver.deleteBusDriver(busDriverID);
	}
	public ReturnObjectUtility<Integer> retrieveBusByDriverID(int busDriverID)
	{
		return bus.retrieveBusByDriverID(busDriverID);
	}
}
