package hotelOwner;

import java.io.IOException;

import hotelModels.hotelOwnerController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HotelOwnerMenuView {
	@FXML
	private Text name;
	@FXML
	private Text id;
	@FXML
	private Text cnic;
	@FXML
	private Text dob;
	@FXML
	private ImageView roomLogo;
	
	Parent root;
	hotelOwnerController hoContoller;
	public HotelOwnerMenuView() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/hotelOwner/HotelOwnerMenu.fxml"));
		loader.setController(this);
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void initialize() {
		hoContoller = new hotelOwnerController();
		eventHandlersAssignment();
		displayOwnerDetails();
	}
	
	public Parent getRoot() {
		return root;
	}
	
	public void eventHandlersAssignment() {
		 EventHandler<MouseEvent> mngRoomButtonHandler = (event) -> {
			 try {
		        	String fxmlFile;
		            String stageTitle;
		            
		            fxmlFile = "/hotelOwner/HOMManageRoom.fxml";
		            stageTitle = "Manage room";
		            
		            // Load the new FXML file
		            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
		            Parent newFormRoot = loader.load();

		            // Create a new scene and stage for the new form
		            Scene newFormScene = new Scene(newFormRoot);
		            Stage newFormStage = new Stage();
		            newFormStage.setScene(newFormScene);
		            newFormStage.setTitle(stageTitle);

		            // Show the new form
		            newFormStage.show();

		            // Close the current form
		            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
		            currentStage.close();
		            
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		 };
		 
		roomLogo.setOnMouseClicked(mngRoomButtonHandler);
		 
	}
	
	// Method to display profile
    public void displayOwnerDetails() {
        String profileDetail[] = hoContoller.getHotelOwnerProfileDetail(1);
        name.setText(profileDetail[0]);
        id.setText(profileDetail[1]);
        cnic.setText(profileDetail[2]);
        dob.setText(profileDetail[3]);
    }

}
