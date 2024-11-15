package travelAgencyOwner;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import travelAgencyModels.travelAgencyOwnerController;

public class TravelAgencyUpdatesCarView {
	@FXML
	private Pane sidePanel;
	@FXML
	private Pane mainPanel;
	Parent root;
	travelAgencyOwnerController taoController;
	public TravelAgencyUpdatesCarView() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/travelAgencyOwner/travelAgencyOwnerUpdateCar.fxml"));
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		taoController = new travelAgencyOwnerController();
		displayOwnerDetails();
	}
	
	public Parent getRoot() {
		return root;
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
