package accountAndPersonModels;

import java.time.LocalDate;

import dbHandlers.BusDBHandler;
import dbHandlers.ReturnObjectUtility;
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
	
	public ReturnObjectUtility<BusDriver> addBusDriver(BusDriver bDriver){
		return busDBHandler.addBusDriver(bDriver);
	}
	
}
