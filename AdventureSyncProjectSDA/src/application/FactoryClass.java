package application;

import accountAndPersonModels.TravelAgencyOwner;
import dbHandlers.BusDBHandler;
import dbHandlers.DatabaseManager;
import dbHandlers.HotelDBHandler;
import dbHandlers.TouristDBHandler;
import dbHandlers.TravelAgencyDBHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import signupForms.AccountMenuView;

public class FactoryClass {
	DatabaseManager dbManager;
	HotelDBHandler hotelDB;
	TravelAgencyDBHandler travelDB;
	BusDBHandler busDB;
	TouristDBHandler touristDB;
	
	public FactoryClass(Stage primaryStage) {
		assignConnections();
		launchMainMenu(primaryStage);
	}
	
	public void launchMainMenu(Stage primaryStage) {
		try {
			AccountMenuView mainMenu = new AccountMenuView();
      
			Parent root = mainMenu.getRoot();
			Scene scene = new Scene(root, 750, 500);

			primaryStage.setScene(scene);
		    primaryStage.setTitle("Account Menu");
		    primaryStage.show();
			} catch(Exception e) {
				e.printStackTrace();
			}
	}
	
	public void assignConnections() {
		//saleha: E85OBQM
		//afsah: MHOGR9K
		String connectionString="jdbc:sqlserver://DESKTOP-MHOGR9K\\SQLEXPRESS;databaseName=sdaProjectDB;integratedSecurity=true;encrypt=false";
		dbManager=new DatabaseManager(connectionString);
		
		hotelDB= new HotelDBHandler(dbManager.getConnection());
		travelDB = new TravelAgencyDBHandler(dbManager.getConnection());
		busDB = new BusDBHandler(dbManager.getConnection());
		touristDB = new TouristDBHandler(dbManager.getConnection());
	}
//	
//	public void createSingletonAgencyOwner() {
//		TravelAgencyOwner agencyOwner=TravelAgencyOwner.getInstance();
//		if(agencyOwner==null)
//			agencyOwnerExists=false;
//		else agencyOwnerExists=true;
//	}
}
