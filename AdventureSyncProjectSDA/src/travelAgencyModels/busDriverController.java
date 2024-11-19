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
		bus=new Bus();
		busDriver=new BusDriver();
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
	
	//db interaction functions
	public ReturnObjectUtility<BusDriver> addBusDriver(BusDriver bDriver){
		return busDriver.addBusDriver(bDriver);
	}
}
