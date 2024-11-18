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
	
	//constructors
	public TravelAgencyOwner()
	{
		super();
		travelAgencyDBHandler=new TravelAgencyDBHandler();
	}
	public TravelAgencyOwner(int agencyOwnerID, String name, LocalDate dob, String cnic)
	{
		super(name, dob, cnic);
		this.agencyOwnerID = agencyOwnerID;
		travelAgencyDBHandler=new TravelAgencyDBHandler();
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
	
	//communication with db handler
	public ReturnObjectUtility<TravelAgencyOwner> getDetail(int travelAgencyOwnerID)
	{
		ReturnObjectUtility<TravelAgencyOwner> returnData = travelAgencyDBHandler.retrieveTravelAgencyOwnerData(travelAgencyOwnerID);
		System.out.println(returnData.getMessage());
		System.out.println("Agency owner");
		return returnData;
	}
	
	public ReturnObjectUtility<Boolean>  addCar(Car car) {
		return travelAgencyDBHandler.addCar(car);
	}
	
	public ReturnObjectUtility<Car> retrieveCarObject(int carID) {
		return travelAgencyDBHandler.retrieveCarObject(carID);
	}
	
	public ReturnObjectUtility<Boolean> updateCar(Car car){
		return travelAgencyDBHandler.updateCar(car);
	}
}
