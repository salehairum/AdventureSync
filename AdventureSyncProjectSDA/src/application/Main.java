package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import travelAgencyOwner.TravelAgencyManageAccountView;
import travelAgencyOwner.TravelAgencyManageBusView;
import travelAgencyOwner.TravelAgencyManageCarsView;
import travelAgencyOwner.TravelAgencyOwnerMenuView;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		String connectionString="jdbc:sqlserver://DESKTOP-E85OBQM\\SQLEXPRESS;databaseName=db_crms;integratedSecurity=true;encrypt=false";
		DatabaseManager dbManager=new DatabaseManager(connectionString);
		
		try {
//			TravelAgencyOwnerMenuView travelAgencyMenu=new TravelAgencyOwnerMenuView();
//			Parent root=travelAgencyMenu.getRoot();
			
			TravelAgencyManageAccountView travelAgencyAccount=new TravelAgencyManageAccountView();
			Parent root=travelAgencyAccount.getRoot();
			
			// Create the scene and set it
			Scene scene = new Scene(root, 750, 500);
	        // Set the scene to the primary stage
	        primaryStage.setScene(scene);
	        primaryStage.setTitle("Travel Agency Owner Menu");
	        primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
