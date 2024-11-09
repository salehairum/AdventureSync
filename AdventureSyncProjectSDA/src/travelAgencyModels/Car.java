package travelAgencyModels;

public class Car extends Vehicle{
	//attributes
	private boolean rentalStatus;  //0: not rented, 1: rented
	private float rentalFee; 
	private float costPerKm; 
	
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
}
