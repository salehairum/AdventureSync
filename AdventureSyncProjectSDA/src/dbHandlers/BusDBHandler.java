package dbHandlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import travelAgencyModels.Bus;
import travelAgencyModels.Car;
import travelAgencyModels.Seat;

public class BusDBHandler {
	static private Connection conn;
	
	//constructors
	public BusDBHandler() {}
	
	public BusDBHandler(Connection c) {
		conn = c;
	}
	
	public static Connection getConnection() {
		return conn;
	}

	public static void setConnection(Connection conn) {
		BusDBHandler.conn = conn;
	}

	//bus handling functions	
	public ReturnObjectUtility<Boolean> addBus(Bus bus) {
		ReturnObjectUtility<Boolean> returnData=new ReturnObjectUtility<Boolean>();
		PreparedStatement pstmt;
		try {
			String sql = "INSERT INTO Bus (brand, model, manufactureYear, plateNumber, noOfSeats, noOfrows, priceOfSeat, hasTour) VALUES (?, ?, ?, ?, ?,?, ?, 0);";
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			// Set parameters
			pstmt.setString(1, bus.getBrand());
			pstmt.setString(2, bus.getModel());
			pstmt.setInt(3, bus.getYear());
			pstmt.setString(4, bus.getPlateNumber());
			pstmt.setInt(5, bus.getNoOfSeats());
			pstmt.setInt(6, bus.getNoOfRows());
			pstmt.setFloat(7, bus.getPriceOfSeats()); 
			
			// Execute the insert
			int rowsAffected = pstmt.executeUpdate();
			if(rowsAffected>0) {
				//generate id of bus just added
				int newBusId=0;
			    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
	                if (generatedKeys.next()) {
	                	newBusId= generatedKeys.getInt(1);
	                    returnData.setMessage("Bus added successfully with ID: " + newBusId);
	                } else {
	                	returnData.setMessage("Bus added, but ID retrieval failed.");
	                }
	            }
			    returnData.setSuccess(true);
			    
				//add seats
				String seatSql = "INSERT INTO Seat (busId, rowNo) VALUES (?, ?);";
			    PreparedStatement seatPstmt = conn.prepareStatement(seatSql,Statement.RETURN_GENERATED_KEYS);
			
				HashMap<Integer, Seat> seats=bus.getSeats();
			    for (Map.Entry<Integer, Seat> entry : seats.entrySet()) {
			    	
			        seatPstmt.setInt(1, newBusId);
			        seatPstmt.setInt(2, entry.getValue().getRowNo());
			        rowsAffected =seatPstmt.executeUpdate();
			        if(rowsAffected<=0) {
						 returnData.setMessage("Failed to add seat.");
		                 returnData.setSuccess(false);
			        }
			    }
			}
			else {
				 returnData.setMessage("Failed to add bus.");
                 returnData.setSuccess(false);
			 }
			 
		} catch (SQLException e) {
			String errorMessage = e.getMessage().toLowerCase();
			
			if (errorMessage != null) {
		        if (errorMessage.contains("foreign key constraint")) {
		        	returnData.setMessage("Error: Invalid reference. Check if the related data exists.");
		        } else {
		        	returnData.setMessage("Issue in adding bus to db: " + errorMessage);
		        }
		    } else {
		    	returnData.setMessage("An unknown error occurred.");
		    }

            returnData.setSuccess(false);
		}
		return returnData;
	}
	
}
