package dataUtilityClasses;

import dbHandlers.BusDBHandler;

public class FeedbackWithBusID {
	private int feedbackID;
	private int busID;
	private String comments;
	BusDBHandler busHandler;
	
	public FeedbackWithBusID(int feedbackID, int busID, String comments) {
		super();
		this.feedbackID = feedbackID;
		this.busID = busID;
		this.comments = comments;
		busHandler = new BusDBHandler();
	}
	public FeedbackWithBusID(int busID, String comments) {
		this.busID = busID;
		this.comments = comments;
		busHandler = new BusDBHandler();
	}
	public FeedbackWithBusID() {
		this.busID = 0;
		this.comments = "";
		busHandler = new BusDBHandler();
	}
	public int getBusID() {
		return busID;
	}
	public void setBusID(int busID) {
		this.busID = busID;
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
	public ReturnListUtility<FeedbackWithBusID> retrieveBusListWithBusDriverID() {
		return BusDBHandler.getFeedbackDetailsWithBusID();
	}
	public ReturnObjectUtility<Float> getOverallRating(){
		return busHandler.getOverallRating();
	}
}
