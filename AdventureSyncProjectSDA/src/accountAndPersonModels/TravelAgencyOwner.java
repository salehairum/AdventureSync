package accountAndPersonModels;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import dataUtilityClasses.FeedbackWithBusID;
import dataUtilityClasses.ReturnListUtility;
import dataUtilityClasses.ReturnObjectUtility;
import dataUtilityClasses.SingletonReturnData;
import dbHandlers.TravelAgencyDBHandler;
import travelAgencyModels.Bus;
import travelAgencyModels.Car;

public class TravelAgencyOwner extends Person {
	private static TravelAgencyOwner instance;
	private int agencyOwnerID;
	//private HashMap<Integer,Car> cars;
	private static TravelAgencyDBHandler travelAgencyDBHandler;
	Car car;
	Bus bus;
	BusDriver busDriver;
	FeedbackWithBusID fBusID;
	boolean existsInDb;
	//constructors
	private TravelAgencyOwner()
	{
		super();
		existsInDb=false;
		travelAgencyDBHandler=new TravelAgencyDBHandler();
		car=new Car();
		bus=new Bus();
		busDriver = new BusDriver();
		fBusID = new FeedbackWithBusID();
	}
	private TravelAgencyOwner(int agencyOwnerID, String name, LocalDate dob, String cnic)
	{
		super(name, dob, cnic);
		this.agencyOwnerID = agencyOwnerID;
		travelAgencyDBHandler=new TravelAgencyDBHandler();
		car=new Car();
		bus=new Bus();
		existsInDb=false;
	}
	
	public static TravelAgencyOwner getInstance() {
		travelAgencyDBHandler=new TravelAgencyDBHandler();
		if(instance==null)
		{
			ReturnObjectUtility<SingletonReturnData> returnData=travelAgencyDBHandler.retrieveTravelAgencyOwnerData();
			if(returnData.isSuccess())
			{
				SingletonReturnData owner=returnData.getObject();
				initInstance(owner);
			}
			else return null;
		}

		return instance;
	}
	
	public static void initInstance(SingletonReturnData owner) {
		instance=new TravelAgencyOwner();
		instance.setName(owner.getName());
		instance.setCnic(owner.getCnic());
		instance.setDob(owner.getDob());
		instance.setAgencyOwnerID(owner.getId());
		instance.setAccount(owner.getAcc());
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
	public ReturnObjectUtility<SingletonReturnData> getDetail()
	{
		ReturnObjectUtility<SingletonReturnData> returnData = travelAgencyDBHandler.retrieveTravelAgencyOwnerData();
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
	public ReturnObjectUtility<SingletonReturnData> retrieveAllTravelAgencyOwnerData(){
		return travelAgencyDBHandler.retrieveAllTravelAgencyOwnerData();
	}

	public ReturnObjectUtility<TravelAgencyOwner> deleteAgencyOwner(int owner) {
		return travelAgencyDBHandler.deleteAgencyOwner(owner);
	}
	
	public ReturnObjectUtility<Integer> compareOldPassword(String enteredPassword, int travelAgencyOwnerID) {
		return travelAgencyDBHandler.compareOldPassword(enteredPassword, travelAgencyOwnerID);
	}

	public ReturnObjectUtility<TravelAgencyOwner> updatePassword(String password, int accountID) {
		return travelAgencyDBHandler.updatePassword(password, accountID);
	}
}
