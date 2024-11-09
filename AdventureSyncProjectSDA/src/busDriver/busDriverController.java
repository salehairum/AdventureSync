package busDriver;

public class busDriverController {
	BusDriverManageBusView mgrBus;
	BusDriverMenuView menu;
	BusDriverMgrAccountView mgrAcc;
	
	public busDriverController(BusDriverManageBusView busView, BusDriverMenuView menuView, BusDriverMgrAccountView accView) {
		mgrBus=new BusDriverManageBusView();
		mgrBus=busView;
		
		menu=new BusDriverMenuView();
		menu=menuView;
		
		mgrAcc= new BusDriverMgrAccountView();
		mgrAcc=accView;
	}
}
