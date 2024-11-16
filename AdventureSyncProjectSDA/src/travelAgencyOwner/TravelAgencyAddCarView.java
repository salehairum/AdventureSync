package travelAgencyOwner;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import travelAgencyModels.travelAgencyOwnerController;

public class TravelAgencyAddCarView {
	
	@FXML
	private Button addButton;
	@FXML
	private TextField brandInput;
	@FXML
	private TextField modelInput;
	@FXML
	private TextField yearInput;
	@FXML
	private TextField rentalFeeInput;
	@FXML
	private TextField plateNoInput;
	@FXML
	private TextField costPerKmInput;
	Parent root;
	travelAgencyOwnerController taoController;
	public TravelAgencyAddCarView() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/travelAgencyOwner/travelAgencyOwnerAddCar.fxml"));
		loader.setController(this);
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	@FXML
	private void initialize() {
		buttonAssignment();
		taoController = new travelAgencyOwnerController();
		displayOwnerDetails();
	}
	
	public Parent getRoot() {
		return root;
	}
	
	public void buttonAssignment() {
		EventHandler<ActionEvent> addButtonHandler=(event)->{
			System.out.println(brandInput.getText());
		};
		addButton.setOnAction(addButtonHandler);
	}
	// Method to set the name and ID fields dynamically
    public void displayOwnerDetails() {
        Text nameText = (Text) root.lookup("#name");
        Text idText = (Text) root.lookup("#id");
        Text cnicText = (Text) root.lookup("#cnic");
        Text dobText = (Text) root.lookup("#dob");
        String profileDetail[] = taoController.getTravelAgencyOwnerProfileDetail(1);
        if (nameText != null) {
            nameText.setText(profileDetail[0]);
        }
        if (idText != null) {
            idText.setText(profileDetail[1]);
        }
        if (cnicText != null) {
        	cnicText.setText(profileDetail[2]);
        }
        if (dobText != null) {
        	dobText.setText(profileDetail[3]);
        }
    }
}
