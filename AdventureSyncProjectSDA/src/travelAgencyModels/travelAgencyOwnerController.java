package travelAgencyModels;

import java.time.LocalDate;

import accountAndPersonModels.HotelOwner;
import accountAndPersonModels.TravelAgencyOwner;
import dbHandlers.ReturnObjectUtility;

public class travelAgencyOwnerController {
	TravelAgencyOwner travelAgencyOwner;
	
	public travelAgencyOwnerController()
	{
		travelAgencyOwner = new TravelAgencyOwner();
	}
	public String[] getTravelAgencyOwnerProfileDetail(int travelAgecnyOwnerId)
    {
    	ReturnObjectUtility<TravelAgencyOwner> returnData = travelAgencyOwner.getDetail(travelAgecnyOwnerId);
    	String travelAgencyOwnerName = returnData.getObject().getName();
        int travelAgencyOwnerId_ = returnData.getObject().getAgencyOwnerID();
        String travelAgencyOwnerIdStr = String.valueOf(travelAgencyOwnerId_);
        String travelAgencyOwnerCnic = returnData.getObject().getCnic();
        LocalDate dob = returnData.getObject().getDob();
        String travelAgencyOwnerDob = dob.toString();
        String[] profileDetails = {travelAgencyOwnerName, travelAgencyOwnerIdStr, travelAgencyOwnerCnic, travelAgencyOwnerDob};
        System.out.println("Controller");
        return profileDetails;
    }
}