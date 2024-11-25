package controllers;

import java.time.LocalDate;
import java.util.List;

import accountAndPersonModels.BusDriver;
import accountAndPersonModels.TravelAgencyOwner;
import dataUtilityClasses.FeedbackWithBusID;
import dataUtilityClasses.ReturnListUtility;
import dataUtilityClasses.ReturnObjectUtility;
import travelAgencyModels.Bus;
import travelAgencyModels.Car;
import travelAgencyModels.Tour;

public class travelAgencyOwnerController {
	TravelAgencyOwner travelAgencyOwner;
	
	public travelAgencyOwnerController()
	{
		travelAgencyOwner = new TravelAgencyOwner();
	}
	
	//retrieve profile
	public String[] getTravelAgencyOwnerProfileDetail(int travelAgecnyOwnerId)
    {
    	ReturnObjectUtility<TravelAgencyOwner> returnData = travelAgencyOwner.getDetail(travelAgecnyOwnerId);
    	String travelAgencyOwnerName = returnData.getObject().getName();
        int travelAgencyOwnerId_ = returnData.getObject().getAgencyOwnerID();
        String travelAgencyOwnerIdStr = String.valueOf(travelAgencyOwnerId_);
        String travelAgencyOwnerCnic = returnData.getObject().getCnic();
        LocalDate dob = returnData.getObject().getDob();
        String travelAgencyOwnerDob = dob.toString();
        String[] profileDetails = {travelAgencyOwnerName, travelAgencyOwnerIdStr, travelAgencyOwnerCnic, travelAgencyOwnerDob};
        return profileDetails;
    }
	
	//car related functions
	public ReturnObjectUtility<Boolean> addCar(Car car) {
		return travelAgencyOwner.addCar(car);
	}
	
	public ReturnObjectUtility<Car> retrieveCarObject(int carID) {
		return travelAgencyOwner.retrieveCarObject(carID);
	}
	
	public ReturnObjectUtility<Boolean> updateCar(Car car){
		return travelAgencyOwner.updateCar(car);
	}
	
	public ReturnObjectUtility<Car> updateCarRentalStatus(int carID, boolean rentalStatus) {
		return travelAgencyOwner.updateCarRentalStatus(carID, rentalStatus);
	}
	
	//assigning tour to bus
	public ReturnObjectUtility<Tour> assignTour(Tour tour){
		return tour.assignTour(tour);
	}
	
	//travel agency related functions
	public ReturnObjectUtility<TravelAgencyOwner> addAgencyOwner(TravelAgencyOwner owner) {
		return travelAgencyOwner.addAgencyOwner(owner);
	}
	
	public ReturnListUtility<Car> getCarDetails() {
	    // Call the TravelAgencyOwner to fetch car details
		return travelAgencyOwner.getAllCars();
	}
	public ReturnListUtility<Car> getNotRentedCarDetails() {
	    // Call the TravelAgencyOwner to fetch car details
		return travelAgencyOwner.getAllNotRentedCars();
	}
	public ReturnListUtility<Car> getTouristRentedCarDetails(int touristID) {
	    // Call the TravelAgencyOwner to fetch car details
		return travelAgencyOwner.getAllTouristRentedCars(touristID);
	}
	public ReturnListUtility<Bus> getBusDetailsWithBusDriverID() {
	    // Call the TravelAgencyOwner to fetch car details
		return travelAgencyOwner.getAllBusWithBusDriverID();
	}
	public ReturnListUtility<BusDriver> getBusDriverDetails() {
	    // Call the TravelAgencyOwner to fetch car details
		return travelAgencyOwner.getBusDriverDetails();
	}
	public ReturnListUtility<FeedbackWithBusID> getFeedbackDetailsWithBusID() {
	    // Call the TravelAgencyOwner to fetch car details
		return travelAgencyOwner.getFeedbackDetailsWithBusID();
	}
	public ReturnObjectUtility<Float> getKmsTravelledBill(int carID, int nKms){
		return travelAgencyOwner.getKmsTravelledBill(carID, nKms);
	}

	public ReturnObjectUtility<Float> getBill(int carID){
		return travelAgencyOwner.getBill(carID);
	}
	public ReturnObjectUtility<Float> getOverallRating(){
		return travelAgencyOwner.getOverallRating();
	}
	public ReturnObjectUtility<Float> addMoney(float bill){
		return travelAgencyOwner.addMoney(bill);
	}
	public ReturnObjectUtility<Integer> checkPassword(String enteredPassword, String username) {
		return travelAgencyOwner.checkPassword(enteredPassword, username);
	}
	public ReturnObjectUtility<Boolean> deleteCar(int carID) {
		return travelAgencyOwner.deleteCar(carID);
	}
	public ReturnObjectUtility<TravelAgencyOwner> updateAgencyOwner(TravelAgencyOwner owner) {
		return travelAgencyOwner.updateAgencyOwner(owner);
	}
	public ReturnObjectUtility<TravelAgencyOwner> retrieveAllTravelAgencyOwnerData(int TravelAgencyOwnerID){
		return travelAgencyOwner.retrieveAllTravelAgencyOwnerData(TravelAgencyOwnerID);
	}
}
