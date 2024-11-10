package travelAgencyOwner;

public class TravelAgencyOwnerController {
	TravelAgencyOwnerMenuView menu;
	TravelAgencyManageCarsView mgrCars;
	TravelAgencyManageBusView mgrBus;
	TravelAgencyManageAccountView mgrAccount;
	public TravelAgencyOwnerController(TravelAgencyOwnerMenuView menuView, TravelAgencyManageCarsView carsView, TravelAgencyManageBusView busView, TravelAgencyManageAccountView accView) {
		menu=new TravelAgencyOwnerMenuView();
		menu=menuView;
		
		mgrCars=new TravelAgencyManageCarsView();
		mgrCars=carsView;
		
		mgrBus=new TravelAgencyManageBusView();
		mgrBus=busView;
		
		mgrAccount= new TravelAgencyManageAccountView();
		mgrAccount=accView;
	}
}
