package travelAgencyOwner;

public class Car {
	//attributes
	private int carID;
	private String brand;
	private String model;
	private int year;
	private boolean rentalStatus;  //0: not rented, 1: rented
	private float rentalFee; 
	private float costPerKm; 
	private String plateNumber;
	
	//getters and setters
	public int getCarID() {
		return carID;
	}
	public void setCarID(int carID) {
		this.carID = carID;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
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
	public float getcostPerKm() {
		return costPerKm;
	}
	public void setcostPerKm(float costPerKm) {
		this.costPerKm = costPerKm;
	}
	public String getPlateNumber() {
		return plateNumber;
	}
	//no setter for plate number, it cannot be modified.
}
