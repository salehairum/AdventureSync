package accountAndPersonModels;

import java.time.LocalDate;
import java.util.HashMap;

import dbHandlers.ReturnObjectUtility;
import dbHandlers.TravelAgencyDBHandler;
import travelAgencyModels.Car;

public class TravelAgencyOwner extends Person {
	private int agencyOwnerID;
	private HashMap<Integer,Car> cars;
	private TravelAgencyDBHandler travelAgencyDBHandler;
	
	public TravelAgencyOwner()
	{
		super();
	}
	public TravelAgencyOwner(int agencyOwnerID, String name, LocalDate dob, String cnic)
	{
		super(name, dob, cnic);
		this.agencyOwnerID = agencyOwnerID;
	}
	//getters and setters
	public int getAgencyOwnerID() {
		return agencyOwnerID;
	}
	public void setAgencyOwnerID(int agencyOwnerID) {
		this.agencyOwnerID = agencyOwnerID;
	}
	public HashMap<Integer, Car> getCars() {
		return cars;
	}
	public void setCars(HashMap<Integer, Car> cars) {
		this.cars = cars;
	}
	
	public ReturnObjectUtility<TravelAgencyOwner> getDetail(int travelAgencyOwnerID)
	{
		ReturnObjectUtility<TravelAgencyOwner> returnData = travelAgencyDBHandler.retrieveTravelAgencyOwnerData(travelAgencyOwnerID);
		System.out.println(returnData.getMessage());
		System.out.println("Agency owner");
		return returnData;
	}
	
}
