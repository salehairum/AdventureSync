package hotelModels;

import dbHandlers.BusDBHandler;
import dbHandlers.HotelDBHandler;
import dbHandlers.ReturnListUtility;
import dbHandlers.ReturnObjectUtility;
import travelAgencyModels.FeedbackWithBusID;

public class FeedbackWithRoomID {
	private int feedbackID;
	private int roomID;
	private String comments;
	HotelDBHandler hoHandler;
	
	public FeedbackWithRoomID(int feedbackID, int roomID, String comments) {
		this.feedbackID = feedbackID;
		this.roomID = roomID;
		this.comments = comments;
		hoHandler = new HotelDBHandler();
	}
	public FeedbackWithRoomID(int roomID, String comments) {
		this.roomID = roomID;
		this.comments = comments;
		hoHandler = new HotelDBHandler();
	}
	public FeedbackWithRoomID() {
		this.roomID = 0;
		this.comments = "";
		hoHandler = new HotelDBHandler();
	}
	public int getRoomID() {
		return roomID;
	}
	public void setRoomID(int roomID) {
		this.roomID = roomID;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public int getFeedbackID() {
		return feedbackID;
	}
	public void setFeedbackID(int feedbackID) {
		this.feedbackID = feedbackID;
	}
	public ReturnListUtility<FeedbackWithRoomID> retrieveFeedbackList(int hotelID) {
		return hoHandler.retrieveFeedbackList(hotelID);
	}
	public ReturnObjectUtility<Float> getOverallRating(int hotelID){
		return hoHandler.getOverallRating(hotelID);
	}

}
