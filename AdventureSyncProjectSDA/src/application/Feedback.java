package application;

public class Feedback {
	//variables
	private int feedbackID;
	private int serviceID;
	private int rating;
	private String comment;
	private String typeOfFeedback;
	private int touristID;
	public Feedback(int feedbackID, int serviceID, int rating, String comment, String typeOfFeedback, int touristID) {
	        this.feedbackID = feedbackID;
	        this.serviceID = serviceID;
	        this.rating = rating;
	        this.comment = comment;
	        this.typeOfFeedback = typeOfFeedback;
	        this.touristID = touristID;
	    }
	//constructor
	public Feedback() {
		feedbackID = 0;
		rating = 0;
		comment = "";
		touristID = 0;
	}
	public Feedback(int fID, String comment) {
		feedbackID = fID;
		this.comment = comment;
	}
	//getter and setter
	public int getFeedbackID() {
		return feedbackID;
	}
	public String getTypeOfFeedback() {
		return typeOfFeedback;
	}
	public void setTypeOfFeedback(String typeOfFeedback) {
		this.typeOfFeedback = typeOfFeedback;
	}
	public void setFeedbackID(int feedbackID) {
		this.feedbackID = feedbackID;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getTouristID() {
		return touristID;
	}
	public void setTouristID(int touristID) {
		this.touristID = touristID;
	}

	public int getServiceID() {
		return serviceID;
	}
	public void setServiceID(int serviceID) {
		this.serviceID = serviceID;
	}

}
