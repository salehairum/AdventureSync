package accountAndPersonModels;

import java.time.LocalDate;

import dataUtilityClasses.ReturnListUtility;
import dataUtilityClasses.ReturnObjectUtility;
import dbHandlers.BusDBHandler;
import dbHandlers.TravelAgencyDBHandler;
import travelAgencyModels.Bus;
import travelAgencyModels.Car;

public class BusDriver extends Person {

	private int busDriverID;
	private BusDBHandler busDBHandler;
	
	public BusDriver()
	{
		super();
		busDBHandler = new BusDBHandler();
	}
	public BusDriver(int busDriverID, String name, LocalDate dob, String cnic)
	{
		super(name, dob, cnic);
		this.busDriverID = busDriverID;
		busDBHandler=new BusDBHandler();
	}
	//getters and setters
	public int getBusDriverID() {
		return busDriverID;
	}

	public void setBusDriverID(int busDriverID) {
		this.busDriverID = busDriverID;
	}
	public ReturnObjectUtility<BusDriver> getDetail(int busDriverID)
	{
		ReturnObjectUtility<BusDriver> returnData = BusDBHandler.retrieveBusDriverData(busDriverID);
		return returnData;
	}
	public ReturnListUtility<BusDriver> getBusDriverDetails()
	{
		ReturnListUtility<BusDriver> returnData = BusDBHandler.retrieveBusDriverList();
		return returnData;
	}
	public ReturnObjectUtility<BusDriver> addBusDriver(BusDriver bDriver){
		return busDBHandler.addBusDriver(bDriver);
	}
	
	public ReturnObjectUtility<Integer> checkPassword(String enteredPassword, String username) {
		return busDBHandler.checkPassword(enteredPassword, username);
	}
	
	public ReturnObjectUtility<Float> addMoney(int busId, float bill){
		return busDBHandler.addMoney(busId, bill);
	}
	
	public ReturnObjectUtility<Boolean> completeTour(int tourID) {
		return busDBHandler.completeTour(tourID);
	}
	public ReturnObjectUtility<BusDriver> retrieveAllBusDriverData(int busDriverID) {
		return busDBHandler.retrieveAllBusDriverData(busDriverID);
	}
	public ReturnObjectUtility<BusDriver> updateBusDriver(BusDriver busDriver) {
		return busDBHandler.updateBusDriver(busDriver);
	}
	public ReturnObjectUtility<BusDriver> deleteBusDriver(int busDriverID) {
		return busDBHandler.deleteBusDriver(busDriverID);
	}
	public ReturnObjectUtility<Integer> compareOldPassword(String enteredPassword, int busDriverID) {
		return busDBHandler.compareOldPassword(enteredPassword, busDriverID);
	}
	public ReturnObjectUtility<BusDriver> updatePassword(String password, int accountID) {
		return busDBHandler.updatePassword(password, accountID);
	}
}
