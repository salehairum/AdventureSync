package travelAgencyModels;

import accountAndPersonModels.BusDriver;
import busDriver.BusDriverManageBusView;
import busDriver.BusDriverMenuView;
import busDriver.BusDriverMgrAccountView;
import dbHandlers.ReturnObjectUtility;

public class busDriverController {
	
	BusDriver busDriver;
	Bus bus;
	public busDriverController() {
	}

	public ReturnObjectUtility<Boolean> addBus(Bus newBus){
		bus=newBus;
		return bus.addBus(newBus);
	}
}
