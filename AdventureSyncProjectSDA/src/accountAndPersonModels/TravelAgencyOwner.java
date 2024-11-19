package accountAndPersonModels;

import java.time.LocalDate;
import java.util.HashMap;

import dbHandlers.ReturnObjectUtility;
import dbHandlers.TravelAgencyDBHandler;
import travelAgencyModels.Car;

public class TravelAgencyOwner extends Person {
	private int agencyOwnerID;
	//private HashMap<Integer,Car> cars;
	private TravelAgencyDBHandler travelAgencyDBHandler;
	Car car;
	
	//constructors
	public TravelAgencyOwner()
	{
		super();
		travelAgencyDBHandler=new TravelAgencyDBHandler();
		car=new Car();
	}
	public TravelAgencyOwner(int agencyOwnerID, String name, LocalDate dob, String cnic)
	{
		super(name, dob, cnic);
		this.agencyOwnerID = agencyOwnerID;
		travelAgencyDBHandler=new TravelAgencyDBHandler();
		car=new Car();
	}
	
	//getters and setters
	public int getAgencyOwnerID() {
		return agencyOwnerID;
	}
	public void setAgencyOwnerID(int agencyOwnerID) {
		this.agencyOwnerID = agencyOwnerID;
	}
//	public HashMap<Integer, Car> getCars() {
//		return cars;
//	}
//	public void setCars(HashMap<Integer, Car> cars) {
//		this.cars = cars;
//	}
//	
	//communication with db handler
	public ReturnObjectUtility<TravelAgencyOwner> getDetail(int travelAgencyOwnerID)
	{
		ReturnObjectUtility<TravelAgencyOwner> returnData = travelAgencyDBHandler.retrieveTravelAgencyOwnerData(travelAgencyOwnerID);
		return returnData;
	}
	
	public ReturnObjectUtility<Boolean>  addCar(Car car) {
		return car.addCar(car);
	}
	
	public ReturnObjectUtility<Car> retrieveCarObject(int carID) {
		return car.retrieveCarObject(carID);
	}
	
	public ReturnObjectUtility<Boolean> updateCar(Car car){
		return car.updateCar(car);
	}
	public ReturnObjectUtility<TravelAgencyOwner> addAgencyOwner(TravelAgencyOwner owner) {
		return travelAgencyDBHandler.addAgencyOwner(owner);
	}
	public ReturnObjectUtility<Car> updateCarRentalStatus(int carID, boolean rentalStatus) {
		return car.updateCarRentalStatus(carID, rentalStatus);
	}
}
