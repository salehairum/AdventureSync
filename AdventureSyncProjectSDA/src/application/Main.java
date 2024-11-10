package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

//import travel agency owner
import travelAgencyOwner.TravelAgencyManageAccountView;
import travelAgencyOwner.TravelAgencyManageBusView;
import travelAgencyOwner.TravelAgencyManageCarsView;
import travelAgencyOwner.TravelAgencyOwnerMenuView;
import travelAgencyOwner.TravelAgencyAddCarView;
import travelAgencyOwner.TravelAgencyDeleteCarsView;
import travelAgencyOwner.TravelAgencyUpdatesCarView;
import travelAgencyOwner.TravelAgencyViewBusesView;
//import bus driver
import busDriver.BusDriverMenuView;
import busDriver.BusDriverManageBusView;
import busDriver.BusDriverMgrAccountView;
import busDriver.BusDriverUpdateBusView;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		//String connectionString="jdbc:sqlserver://DESKTOP-E85OBQM\\SQLEXPRESS;databaseName=db_crms;integratedSecurity=true;encrypt=false";
		//DatabaseManager dbManager=new DatabaseManager(connectionString);
		
		try {
			
			TravelAgencyViewBusesView busDriverMenu=new TravelAgencyViewBusesView();
			Parent root=busDriverMenu.getRoot();
			
			// Create the scene and set it
			Scene scene = new Scene(root, 750, 500);
	       
		        // Set the scene to the primary stage
		        primaryStage.setScene(scene);
		        primaryStage.setTitle("Hotel Owner Menu");
		        primaryStage.show();
			} catch(Exception e) {
				e.printStackTrace();
			}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
