package travelAgencyModels;

import java.time.LocalDate;

import accountAndPersonModels.BusDriver;
import accountAndPersonModels.Tourist;
import dbHandlers.ReturnObjectUtility;

public class TouristController {
	Tourist tourist;
	public TouristController() {
		tourist=new Tourist();
		tourist=new Tourist();
	}
	public ReturnObjectUtility<Tourist> addTourist(Tourist newTourist){
		return tourist.addTourist(newTourist);
	}
	
	public ReturnObjectUtility<Tourist> addCarToRentedCars(int touristId,int carID){
		return tourist.addCarToRentedCars(touristId, carID);
	}
	
	public ReturnObjectUtility<Tourist> removeCarFromRentedCars(int touristId,int carID){
		return tourist.removeCarFromRentedCars(touristId, carID);
	}
	
	public ReturnObjectUtility<Seat> addSeatToBookedSeats(int touristId,int seatID){
		return tourist.addSeatToBookedSeats(touristId, seatID);
	}
	public String[] getTouristProfileDetail(int touristId)
    {
    	ReturnObjectUtility<Tourist> returnData = tourist.getDetail(touristId);
    	String touristName = returnData.getObject().getName();
        int touristId_ = returnData.getObject().getTouristID();
        String touristIdStr = String.valueOf(touristId_);
        String touristCnic = returnData.getObject().getCnic();
        LocalDate dob = returnData.getObject().getDob();
        String touristDob = dob.toString();
        String[] profileDetails = {touristName, touristIdStr, touristCnic, touristDob};
        return profileDetails;
    }
}
