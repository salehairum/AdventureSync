package travelAgencyModels;

public class Car extends Vehicle{
	//attributes
	private boolean rentalStatus;  //0: not rented, 1: rented
	private float rentalFee; 
	private float costPerKm; 
	
	//constructor
	//with default rental status false
	public Car(int id, String brand, String model, int year, String plateNumber, float rentalFee, float costPerKm) {
		super(id, brand, model, year, plateNumber);
		this.rentalStatus = false;
		this.rentalFee = rentalFee;
		this.costPerKm = costPerKm;
	}
	
	//with specific rental status
	public Car(int id, String brand, String model, int year, String plateNumber, boolean rentalStatus, float rentalFee, float costPerKm) {
		super(id, brand, model, year, plateNumber);
		this.rentalStatus = rentalStatus;
		this.rentalFee = rentalFee;
		this.costPerKm = costPerKm;
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
}
