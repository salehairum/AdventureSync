package application;

public class Feedback {
	//variables
	private int feedbackID;
	private int rating;
	private String comment;
	int touristID;
	//constructor
	public Feedback() {
		feedbackID = 0;
		rating = 0;
		comment = "";
		touristID = 0;
	}
	//getter and setter
	public int getFeedbackID() {
		return feedbackID;
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

}
