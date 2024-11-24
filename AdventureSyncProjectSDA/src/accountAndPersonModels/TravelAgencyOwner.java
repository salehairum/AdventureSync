package accountAndPersonModels;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import dataUtilityClasses.FeedbackWithBusID;
import dataUtilityClasses.ReturnListUtility;
import dataUtilityClasses.ReturnObjectUtility;
import dbHandlers.TravelAgencyDBHandler;
import travelAgencyModels.Bus;
import travelAgencyModels.Car;

public class TravelAgencyOwner extends Person {
	private int agencyOwnerID;
	//private HashMap<Integer,Car> cars;
	private TravelAgencyDBHandler travelAgencyDBHandler;
	Car car;
	Bus bus;
	BusDriver busDriver;
	FeedbackWithBusID fBusID;
	//constructors
	public TravelAgencyOwner()
	{
		super();
		travelAgencyDBHandler=new TravelAgencyDBHandler();
		car=new Car();
		bus=new Bus();
		busDriver = new BusDriver();
		fBusID = new FeedbackWithBusID();
	}
	public TravelAgencyOwner(int agencyOwnerID, String name, LocalDate dob, String cnic)
	{
		super(name, dob, cnic);
		this.agencyOwnerID = agencyOwnerID;
		travelAgencyDBHandler=new TravelAgencyDBHandler();
		car=new Car();
		bus=new Bus();
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
	public ReturnListUtility<Car> getAllCars() {
	    // Fetch car data from the database handler
	    return car.retrieveCarList();
	}
	public ReturnListUtility<Car> getAllNotRentedCars() {
	    // Fetch car data from the database handler
	    return car.retrieveNotRentedCarList();
	}
	public ReturnListUtility<Car> getAllTouristRentedCars(int touristID) {
	    // Fetch car data from the database handler
	    return car.retrieveTouristRentedCarList(touristID);
	}
	public ReturnListUtility<Bus> getAllBusWithBusDriverID() {
	    // Fetch car data from the database handler
	    return bus.retrieveBusListWithBusDriverID();
	}
	public ReturnListUtility<BusDriver> getBusDriverDetails() {
	    // Fetch car data from the database handler
	    return busDriver.getBusDriverDetails();
	}
	public ReturnListUtility<FeedbackWithBusID> getFeedbackDetailsWithBusID() {
	    // Fetch car data from the database handler
	    return fBusID.retrieveBusListWithBusDriverID();
	}
	public ReturnObjectUtility<Car> updateCarRentalStatus(int carID, boolean rentalStatus) {
		return car.updateCarRentalStatus(carID, rentalStatus);
	}

	public ReturnObjectUtility<Float> getBill(int carID){
		return car.getBill(carID);
	}
	public ReturnObjectUtility<Float> getOverallRating(){
		return fBusID.getOverallRating();
	}
	public ReturnObjectUtility<Float> addMoney(float bill){
		return car.addMoney(bill);
	}
	
	public ReturnObjectUtility<Float> getKmsTravelledBill(int carID, int nKms){
		return car.getKmsTravelledBill(carID, nKms);
	}
	
	public ReturnObjectUtility<Integer> checkPassword(String enteredPassword, String username) {
		return travelAgencyDBHandler.checkPassword(enteredPassword, username);
	}
	
	public ReturnObjectUtility<Boolean> deleteCar(int carID) {
		return car.deleteCar(carID);
	}
	public ReturnObjectUtility<TravelAgencyOwner> updateAgencyOwner(TravelAgencyOwner owner) {
		return travelAgencyDBHandler.updateAgencyOwner(owner);
	}
	public ReturnObjectUtility<TravelAgencyOwner> retrieveAllTravelAgencyOwnerData(int TravelAgencyOwnerID){
		return travelAgencyDBHandler.retrieveAllTravelAgencyOwnerData(TravelAgencyOwnerID);
	}

}
