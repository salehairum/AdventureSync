package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		//String connectionString="jdbc:sqlserver://DESKTOP-E85OBQM\\SQLEXPRESS;databaseName=db_crms;integratedSecurity=true;encrypt=false";
		//DatabaseManager dbManager=new DatabaseManager(connectionString);
		
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("HOMViewFood.fxml"));
			Parent root = loader.load();  // This loads the FXML and sets the controller automatically

			// Create the scene and set it
			Scene scene = new Scene(root);
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