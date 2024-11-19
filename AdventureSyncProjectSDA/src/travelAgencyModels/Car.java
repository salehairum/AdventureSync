package travelAgencyModels;

import dbHandlers.ReturnObjectUtility;
import dbHandlers.TravelAgencyDBHandler;

public class Car extends Vehicle{
	//attributes
	private boolean rentalStatus;  //0: not rented, 1: rented
	private float rentalFee; 
	private float costPerKm; ;
	private TravelAgencyDBHandler travelAgencyDBHandler;
	
	//constructor
	public Car() {
		super();
		travelAgencyDBHandler=new TravelAgencyDBHandler();
	}
	//with default rental status false
	public Car(int id, String brand, String model, int year, String plateNumber, float rentalFee, float costPerKm) {
		super(id, brand, model, year, plateNumber);
		this.rentalStatus = false;
		this.rentalFee = rentalFee;
		this.costPerKm = costPerKm;
		travelAgencyDBHandler=new TravelAgencyDBHandler();
	}
	
	//with specific rental status
	public Car(int id, String brand, String model, int year, String plateNumber, boolean rentalStatus, float rentalFee, float costPerKm) {
		super(id, brand, model, year, plateNumber);
		this.rentalStatus = rentalStatus;
		this.rentalFee = rentalFee;
		this.costPerKm = costPerKm;
		travelAgencyDBHandler=new TravelAgencyDBHandler();
	}

	//getters and setters
	public boolean isRented() {
		return rentalStatus;
	}
	public void setRentalStatus(boolean rentalStatus) {
		this.rentalStatus = rentalStatus;
	}
	public float getRentalFee() {
		return rentalFee;
	}
	public void setRentalFee(float rentalFee) {
		this.rentalFee = rentalFee;
	}
	public float getCostPerKm() {
		return costPerKm;
	}
	public void setCostPerKm(float costPerKm) {
		this.costPerKm = costPerKm;
	}
	
	//communication with db handler
	public ReturnObjectUtility<Boolean> addCar(Car car) {
		return travelAgencyDBHandler.addCar(car);
	}
	
	public ReturnObjectUtility<Car> retrieveCarObject(int carID) {
		return travelAgencyDBHandler.retrieveCarObject(carID);
	}
	
	public ReturnObjectUtility<Boolean> updateCar(Car car){
		return travelAgencyDBHandler.updateCar(car);
	}
	
	//assigning tour to bus
	public ReturnObjectUtility<Tour> assignTour(Tour tour){
		return tour.assignTour(tour);
	}
}
