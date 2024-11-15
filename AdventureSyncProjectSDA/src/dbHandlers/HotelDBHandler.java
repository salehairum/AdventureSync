package dbHandlers;

import java.sql.Connection;
import java.sql.Date;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.time.LocalDate;

import accountAndPersonModels.HotelOwner;
import hotelModels.Room;
import travelAgencyModels.Car;

public class HotelDBHandler {
	private static Connection conn;
	public HotelDBHandler(Connection c) {
		conn = c;
	}
	//room related functions
	public String addRoom(Room room) {
		PreparedStatement pstmt;
		try {
			 String sql = "INSERT INTO Room (hotelID, rDescription, pricePerNight) VALUES (?, ?, ?);";
			 pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			    
			 // Set parameters
			 pstmt.setInt(1, room.getHotelID());
			 pstmt.setString(2, room.getDescription());
			 pstmt.setFloat(3, room.getPricePerNight());

			 // Execute the insert
			 int rowsAffected = pstmt.executeUpdate();
			    
			 if (rowsAffected > 0) {
				 try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
		                if (generatedKeys.next()) {
		                    int newRoomId = generatedKeys.getInt(1);
		                    return "Car added successfully with ID: " + newRoomId;
		                } else {
		                    return "Car added, but ID retrieval failed.";
		                }
		            }
			 } else {
				 return "Failed to add car.";
			 }
			 
		} catch (SQLException e) {
			String errorMessage = e.getMessage().toLowerCase();
			
			if (errorMessage != null) {
		        if (errorMessage.contains("foreign key constraint")) {
		            return "Error: Invalid reference. Check if the related data exists.";
		        } else {
		            return "Issue in adding room to db: " + errorMessage;
		        }
		    } else {
		        return "An unknown error occurred.";
		    }
		}
	}
	
	//hotel owner related functions
	public static ReturnObjectUtility<HotelOwner> retrieveHotelOwnerData(int hotelOwnerID) {
		ReturnObjectUtility<HotelOwner> returnData = new ReturnObjectUtility();
		
		try {
			Statement stmt = conn.createStatement();
	        ResultSet rSet=stmt.executeQuery("select * from HotelOwner where hotelOwnerId="+hotelOwnerID);
	        
	        if (rSet.next()) { // Check if a result was found
	            // Create and populate a hotel owner object
	            int hotelOwnerIDRetrieved = rSet.getInt("hotelOwnerId");
	            String name = rSet.getString("aName");
	            Date date = rSet.getDate("dob");
	            LocalDate dob = date.toLocalDate();
	            String cnic = rSet.getString("cnic");

	            HotelOwner hotelOwner = new HotelOwner(hotelOwnerIDRetrieved, name, dob, cnic);
	            
	            // Set the hotel owner object and success message
	            returnData.setObject(hotelOwner);
	            returnData.setMessage("Hotel Owner data retrieved successfully.");
	        } else {
	            // If no result is found, set an error message
	            returnData.setMessage("Error: Hotel Owner does not exist.");
	        }
            
		}
		catch(SQLException e){
			String errorMessage = e.getMessage().toLowerCase();
		    
		    if (errorMessage.contains("no such hotel owner") || errorMessage.contains("does not exist") ||errorMessage.contains("no current")) {
		       returnData.setMessage("Error: Hotel Owner does not exist.");
		    } else {
		        // General case for other SQL exceptions
		    	returnData.setMessage("Issue in retrieving hotel owner from database: " + e.getMessage());
		    }
		}
        return returnData;
	}
}