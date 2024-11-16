package travelAgencyModels;

import java.sql.Date;

import dbHandlers.ReturnObjectUtility;
import dbHandlers.TravelAgencyDBHandler;

public class Tour {
	protected int tourID;
	protected String origin;
	protected String destination;
	protected Date date;
	private int busID;	//id of the bus to which this tour is assigned
	private TravelAgencyDBHandler travelAgencyDBHandler;
	
	public Tour(int tourID, String origin, String destination, Date date, int busID) {
        this.tourID = tourID;
        this.origin = origin;
        this.destination = destination;
        this.date = date;
        this.busID = busID;
		travelAgencyDBHandler=new TravelAgencyDBHandler();
    }
	//getters and setters
	public int getBusID() {
		return busID;
	}
	public void setBusID(int busID) {
		this.busID = busID;
	}
	public int getTourID() {
		return tourID;
	}
	public void setTourID(int tourID) {
		this.tourID = tourID;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	//assign tour to bus
	public ReturnObjectUtility<Tour> assignTour(Tour tour){
		return travelAgencyDBHandler.assignTour(tour);
	}
}
