package accountAndPersonModels;

import java.time.LocalDate;

import dbHandlers.BusDBHandler;
import dbHandlers.ReturnObjectUtility;
import travelAgencyModels.Bus;

public class BusDriver extends Person {

	private int busDriverID;
	
	public BusDriver(String name, LocalDate dob, String cnic) {
		super(name, dob, cnic);
		// TODO Auto-generated constructor stub
	}
	
	//getters and setters
	public int getBusDriverID() {
		return busDriverID;
	}

	public void setBusDriverID(int busDriverID) {
		this.busDriverID = busDriverID;
	}
	
}
