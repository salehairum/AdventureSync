package accountAndPersonModels;

import java.time.LocalDate;

import dbHandlers.BusDBHandler;
import dbHandlers.ReturnObjectUtility;
import travelAgencyModels.Bus;

public class BusDriver extends Person {

	private int busDriverID;
	BusDBHandler busDBhandler;
	
	public BusDriver(int busDriverID,String name, LocalDate dob, String cnic) {
		super(name, dob, cnic);
		this.busDriverID = busDriverID;
		// TODO Auto-generated constructor stub
	}
	public BusDriver() {
		super();
		// TODO Auto-generated constructor stub
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
		ReturnObjectUtility<BusDriver> returnData = busDBhandler.retrieveBusDriverData(busDriverID);
		return returnData;
	}
	
}
