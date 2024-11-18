package travelAgencyModels;

import java.time.LocalDate;

import accountAndPersonModels.BusDriver;
import accountAndPersonModels.HotelOwner;
import busDriver.BusDriverManageBusView;
import busDriver.BusDriverMenuView;
import busDriver.BusDriverMgrAccountView;
import dbHandlers.ReturnObjectUtility;

public class busDriverController {
	
	BusDriver busDriver;
	Bus bus;
	public busDriverController() {
		bus = new Bus();
		busDriver = new BusDriver();
	}

	public ReturnObjectUtility<Boolean> addBus(Bus newBus){
		bus=newBus;
		return bus.addBus(newBus);
	}
	
	public ReturnObjectUtility<Bus> retrieveBusObject(int busId) {
		return bus.retrieveBusObject(busId);
	}
	
	public ReturnObjectUtility<Boolean> updateBus(Bus bus){
		return bus.updateBus(bus);
	}
	public String[] getBusDriverProfileDetail(int busDriverId)
    {
    	ReturnObjectUtility<BusDriver> returnData = busDriver.getDetail(busDriverId);
    	String busDriverName = returnData.getObject().getName();
        int busDriverId_ = returnData.getObject().getBusDriverID();
        String busDriverIdStr = String.valueOf(busDriverId_);
        String busDriverCnic = returnData.getObject().getCnic();
        LocalDate dob = returnData.getObject().getDob();
        String busDriverDob = dob.toString();
        String[] profileDetails = {busDriverName, busDriverIdStr, busDriverCnic, busDriverDob};
        return profileDetails;
    }
}
