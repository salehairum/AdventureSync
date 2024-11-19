package travelAgencyModels;

import accountAndPersonModels.Tourist;
import dbHandlers.ReturnObjectUtility;

public class TouristController {
	Tourist tourist;
	public TouristController() {
		tourist=new Tourist();
		tourist=new Tourist();
	}
	public ReturnObjectUtility<Tourist> addTourist(Tourist newTourist){
		return tourist.addTourist(newTourist);
	}
}
