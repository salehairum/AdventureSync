package hotelOwner;

import java.io.IOException;

import hotelModels.hotelOwnerController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

public class hotelOwnerLogin {
	@FXML
	private Pane sidePanel;
	@FXML
	private Pane mainPanel;
	
	Parent root;
	hotelOwnerController hoContoller;
	public hotelOwnerLogin() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/hotelOwner/hotelOwnerLogin.fxml"));
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		hoContoller = new hotelOwnerController();
	}
	
	public Parent getRoot() {
		return root;
	}

}
