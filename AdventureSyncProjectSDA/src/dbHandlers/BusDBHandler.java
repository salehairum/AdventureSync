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
	
	//update bus
	public ReturnObjectUtility<Boolean> updateBus(Bus bus) {
	    ReturnObjectUtility<Boolean> returnData = new ReturnObjectUtility<Boolean>();
	    PreparedStatement pstmt;

	    try {
	        // SQL Query to update the Bus table
	        String sql = "UPDATE Bus SET brand = ?, model = ?, manufactureYear = ?, plateNumber = ?, noOfSeats = ?, noOfRows = ?, priceOfSeat = ?, hasTour = ? WHERE busId = ?";
	        pstmt = conn.prepareStatement(sql);

	        // Set parameters
	        pstmt.setString(1, bus.getBrand());              
	        pstmt.setString(2, bus.getModel());              
	        pstmt.setInt(3, bus.getYear());     
	        pstmt.setString(4, bus.getPlateNumber());       
	        pstmt.setInt(5, bus.getNoOfSeats());            
	        pstmt.setInt(6, bus.getNoOfRows());             
	        pstmt.setFloat(7, bus.getPriceOfSeats());       
	        pstmt.setBoolean(8, bus.hasTour());          
	        pstmt.setInt(9, bus.getID());                

	        // Execute the update
	        int rowsAffected = pstmt.executeUpdate();

	        // Check if the update was successful
	        if (rowsAffected > 0) {
	            returnData.setMessage("Bus with bus ID " + bus.getID() + " updated successfully.");
	            returnData.setSuccess(true);
	        } else {
	            returnData.setMessage("Failed to update bus.");
	            returnData.setSuccess(false);
	        }

	    } catch (SQLException e) {
	        String errorMessage = e.getMessage().toLowerCase();

	        // Handle SQL errors with specific messages
	        if (errorMessage != null) {
	            if (errorMessage.contains("unique constraint") || errorMessage.contains("duplicate")) {
	                returnData.setMessage("Please enter a unique plate number.");
	            } else if (errorMessage.contains("foreign key constraint")) {
	                returnData.setMessage("Error: Invalid reference. Check if the related data exists.");
	            } else {
	                returnData.setMessage("Issue in updating bus in db: " + errorMessage);
	            }
	        }  
	    }
		return returnData;
	  }

	public ReturnObjectUtility<Bus> retrieveBusObject(int busId) {
	    ReturnObjectUtility<Bus> returnData = new ReturnObjectUtility<>();
	    
	    try {
	        Statement stmt = conn.createStatement();
	        ResultSet rSet = stmt.executeQuery("SELECT * FROM Bus WHERE busId = " + busId);
	        
	        if (rSet.next()) { // Check if a result was found
	            // Create and populate a Bus object
	            int retrievedBusId = rSet.getInt("busId");
	            String brand = rSet.getString("brand");
	            String model = rSet.getString("model");
	            int manufactureYear = rSet.getInt("manufactureYear");
	            String plateNumber = rSet.getString("plateNumber");
	            int noOfSeats = rSet.getInt("noOfSeats");
	            int noOfRows = rSet.getInt("noOfRows");
	            float priceOfSeat = rSet.getFloat("priceOfSeat");
	            boolean hasTour = rSet.getBoolean("hasTour");

	            // Retrieve seats for this bus
	            HashMap<Integer, Seat> seats = new HashMap<>();
	            ResultSet seatSet = stmt.executeQuery("SELECT * FROM Seat WHERE busId = " + busId);
	            while (seatSet.next()) {
	                int seatId = seatSet.getInt("seatId");
	                int rowNo = seatSet.getInt("rowNo");
	                boolean isBooked = seatSet.getBoolean("isBooked");

	                // Create a new Seat object
	                Seat seat = new Seat(seatId, busId, isBooked, rowNo);
	                seats.put(seatId, seat); // Add seat to the HashMap
	            }
	            
	            Bus bus = new Bus(retrievedBusId, brand, model, manufactureYear, plateNumber, seats, noOfSeats, noOfRows, priceOfSeat);
	            
	            // Set the Bus object and success message
	            returnData.setObject(bus);
	            returnData.setMessage("Bus data retrieved successfully.");
	            returnData.setSuccess(true);
	        } else {
	            // If no result is found, set an error message
	            returnData.setMessage("Error: Bus does not exist.");
	            returnData.setSuccess(false);
	        }
	    } catch (SQLException e) {
	        String errorMessage = e.getMessage().toLowerCase();
	        
	        if (errorMessage.contains("no such bus") || errorMessage.contains("does not exist") || errorMessage.contains("no current")) {
	            returnData.setMessage("Error: Bus does not exist.");
	        } else {
	            // General case for other SQL exceptions
	            returnData.setMessage("Issue in retrieving bus from database: " + e.getMessage());
	        }

	        returnData.setSuccess(false);
	    }
	    
	    return returnData;
	}

}
