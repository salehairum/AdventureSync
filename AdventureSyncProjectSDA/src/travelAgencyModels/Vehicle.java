package travelAgencyModels;

public class Vehicle {
	private int id;
	private String brand;
	private String model;
	private int year; 
	private String plateNumber;
	
	//getters and setters
	public int getCarID() {
		return id;
	}
	public void setCarID(int id) {
		this.id = id;
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
	public String getPlateNumber() {
		return plateNumber;
	}
	//no setter for plate no, it can not be modified
}
