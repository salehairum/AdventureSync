package dbHandlers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import accountAndPersonModels.BusDriver;
import accountAndPersonModels.HotelOwner;
import accountAndPersonModels.Account;
import accountAndPersonModels.BusDriver;
import accountAndPersonModels.TravelAgencyOwner;
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
	
	public ReturnObjectUtility<Seat> retrieveSeatObject(int seatId, int busId) {
	    ReturnObjectUtility<Seat> returnData = new ReturnObjectUtility<>();
	    
	    try {
	        // Create a Statement to execute the query
	        Statement stmt = conn.createStatement();
	        
	        // Query to get a specific seat based on seatId and busId
	        String query = "SELECT * FROM Seat WHERE busId = " + busId + " AND seatId = " + seatId;
	        ResultSet rSet = stmt.executeQuery(query);

	        if (rSet.next()) { // Check if a result was found
	            // Retrieve the seat details from the ResultSet
	            int retrievedSeatId = rSet.getInt("seatId");
	            int rowNo = rSet.getInt("rowNo");
	            boolean isBooked = rSet.getBoolean("isBooked");

	            // Create a Seat object
	            Seat seat = new Seat(retrievedSeatId, busId, isBooked, rowNo);

	            // Set the Seat object and success message
	            returnData.setObject(seat);
	            returnData.setMessage("Seat data retrieved successfully.");
	            returnData.setSuccess(true);
	        } else {
	            // If no result is found, set an error message
	            returnData.setMessage("Error: Seat does not exist.");
	            returnData.setSuccess(false);
	        }
	    } catch (SQLException e) {
	        String errorMessage = e.getMessage().toLowerCase();
	        
	        if (errorMessage.contains("no such seat") || errorMessage.contains("does not exist") || errorMessage.contains("no current")) {
	            returnData.setMessage("Error: Seat does not exist.");
	        } else {
	            // General case for other SQL exceptions
	            returnData.setMessage("Issue in retrieving seat from database: " + e.getMessage());
	        }

	        returnData.setSuccess(false);
	    }
	    
	    return returnData;
	}

	
	public ReturnListUtility<Bus> retrieveBusList() {
		ReturnListUtility<Bus> returnData=new ReturnListUtility();
		
		try {
			Statement stmt=conn.createStatement();
	        ResultSet rSet=stmt.executeQuery("select * from bus");
	        
	        HashMap<Integer, Bus> busList=new HashMap<Integer, Bus>();
	        
	        if(!rSet.next()) {
	            // If no result is found, set an error message
	            returnData.setMessage("Error: Bus does not exist.");
	            returnData.setSuccess(false);
	        }
	        else {
	        	 do { // Check if a result was found
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
	  	            ResultSet seatSet = stmt.executeQuery("SELECT * FROM Seat WHERE busId = " + retrievedBusId);
	  	            while (seatSet.next()) {
	  	                int seatId = seatSet.getInt("seatId");
	  	                int rowNo = seatSet.getInt("rowNo");
	  	                boolean isBooked = seatSet.getBoolean("isBooked");

	  	                // Create a new Seat object
	  	                Seat seat = new Seat(seatId, retrievedBusId, isBooked, rowNo);
	  	                seats.put(seatId, seat); // Add seat to the HashMap
	  	            }
	  	            
	  	            Bus bus = new Bus(retrievedBusId, brand, model, manufactureYear, plateNumber, seats, noOfSeats, noOfRows, priceOfSeat);
	  	            busList.put(bus.getID(), bus);

	 	        } while(rSet.next());
	        	 
	        	returnData.setList(busList);
	        	returnData.setMessage("Buses retrieved successfully.");
	            returnData.setSuccess(true);
	        }
            
		}
		catch(SQLException e){
			String errorMessage = e.getMessage().toLowerCase();
		    
		    if (errorMessage.contains("no such bus") || errorMessage.contains("does not exist") ||errorMessage.contains("no current")) {
		       returnData.setMessage("Error: Bus does not exist.");
		    } else {
		        // General case for other SQL exceptions
		    	returnData.setMessage("Issue in retrieving buses from database: " + e.getMessage());
		    }
            returnData.setSuccess(false);
		}
        return returnData;
	}
	
	public ReturnObjectUtility<Seat> updateSeatBookingStatus(int seatID, boolean bookingStatus) {
		ReturnObjectUtility<Seat> returnData=new ReturnObjectUtility();
		PreparedStatement pstmt;
		try {
			 String sql = "UPDATE Seat SET isBooked = ? WHERE seatID = ?";
			 pstmt = conn.prepareStatement(sql);
			    
			 // Set parameters
			 pstmt.setBoolean(1,bookingStatus);
			 pstmt.setInt(2, seatID);

			 // Execute the insert
			 int rowsAffected = pstmt.executeUpdate();
			    
			 if (rowsAffected > 0) {
				 if(bookingStatus) {
					 returnData.setMessage("Seat is now booked.");
				 }
				 else {
					 returnData.setMessage("Seat is no longer booked.");
				 }

		         returnData.setSuccess(true);
			 } else {
				 returnData.setMessage("Failed to update seat booking status.");
		         returnData.setSuccess(false);
			 }
			 
		} catch (SQLException e) {
			String errorMessage = e.getMessage().toLowerCase();
			
			if (errorMessage != null) {
		        if (errorMessage.contains("no such car") || errorMessage.contains("does not exist") ||errorMessage.contains("no current")) {
		        	returnData.setMessage("Error: Seat does not exist.");
		        }else {
		        	returnData.setMessage("Issue in updating seat in db: " + errorMessage);
		        }
		    } else {
		    	returnData.setMessage("An unknown error occurred.");
		    }

	        returnData.setSuccess(false);
		}
		return returnData;
	}

	
	//bus driver related functions
	public static ReturnObjectUtility<BusDriver> retrieveBusDriverData(int busDriverID) {
			ReturnObjectUtility<BusDriver> returnData = new ReturnObjectUtility();
			
			try {
				Statement stmt = conn.createStatement();
		        ResultSet rSet=stmt.executeQuery("select * from BusDriver where busDriverId=" + busDriverID);
		        
		        if (rSet.next()) { // Check if a result was found
		            // Create and populate a hotel owner object
		            int busDriverIDRetrieved = rSet.getInt("busDriverId");
		            String name = rSet.getString("bName");
		            Date date = rSet.getDate("dob");
		            LocalDate dob = date.toLocalDate();
		            String cnic = rSet.getString("cnic");

		            BusDriver busDriver = new BusDriver(busDriverIDRetrieved, name, dob, cnic);
		            
		            // Set the hotel owner object and success message
		            returnData.setObject(busDriver);
		            returnData.setMessage("Bus Driver data retrieved successfully.");
		        } else {
		            // If no result is found, set an error message
		            returnData.setMessage("Error: Bus Driver does not exist.");
		        }
	            
			}
			catch(SQLException e){
				String errorMessage = e.getMessage().toLowerCase();
			    
			    if (errorMessage.contains("no such bus driver") || errorMessage.contains("does not exist") ||errorMessage.contains("no current")) {
			       returnData.setMessage("Error: Bus driver does not exist.");
			    } else {
			        // General case for other SQL exceptions
			    	returnData.setMessage("Issue in retrieving bus driver from database: " + e.getMessage());
			    }
			}
	        return returnData;
		}

	//bus driver agency owner functions
	public ReturnObjectUtility<BusDriver> addBusDriver(BusDriver busDriver) {
		ReturnObjectUtility<BusDriver> returnData = new ReturnObjectUtility<>();
		PreparedStatement pstmt;
		ResultSet rs;

		try {
			// First Insert into Account table
		    String sql = "INSERT INTO Account (username, email, accPassword, balance) VALUES (?, ?, ?, ?)";
		    pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		    // Set parameters
		    Account account = busDriver.getAccount();
		    pstmt.setString(1, account.getUsername());
		    pstmt.setString(2, account.getEmail());
		    pstmt.setString(3, account.getPassword());
		    pstmt.setFloat(4, account.getBalance());

		    // Execute the insert and fetch generated accountID
		    int retrievedAccountID=0;
		    int rowsAffected = pstmt.executeUpdate();
		    if (rowsAffected > 0) {
		    	rs = pstmt.getGeneratedKeys();
		        if (rs.next()) {
		        	retrievedAccountID = rs.getInt(1);
		        	account.setAccountID(retrievedAccountID);
		            busDriver.getAccount().setAccountID(retrievedAccountID);
		        } else {
		            returnData.setMessage("Failed to retrieve generated Account ID.");
		            returnData.setSuccess(false);
		            return returnData;
		        }
		    } else {
		        returnData.setMessage("Failed to insert into Account table.");
		        returnData.setSuccess(false);
		        return returnData;
		    }

		        // Step 2: Insert into TravelAgencyOwner table
		        sql = "INSERT INTO busDriver (accountID, aName, dob, cnic) VALUES ( ?, ?, ?, ?)";
		        pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		        // Set parameters
		        pstmt.setInt(1, busDriver.getAccount().getAccountID());
		        pstmt.setString(2, busDriver.getName());
		        pstmt.setObject(3, busDriver.getDob());
		        pstmt.setString(4, busDriver.getCnic());

		        // Execute the insert
		        int retrievedOwnerID=0;
		        rowsAffected = pstmt.executeUpdate();
		        if (rowsAffected > 0) {
		            rs = pstmt.getGeneratedKeys();
		            if (rs.next()) {
		            	retrievedOwnerID = rs.getInt(1);
		            	returnData.setMessage("Bus Driver with id "+retrievedOwnerID+" added successfuly!");
		                returnData.setSuccess(true);
		                return returnData;
		            } else {
		                returnData.setMessage("Failed to retrieve generated Account ID.");
		                returnData.setSuccess(false);
		                return returnData;
		            }
		        } else {
		            returnData.setMessage("Failed to insert into Bus Driver table.");
		            returnData.setSuccess(false);
		            return returnData;
		        }

		    } catch (SQLException e) {
		        String errorMessage = e.getMessage().toLowerCase();

		        if (errorMessage != null) {
		            if (errorMessage.contains("foreign key constraint")) {
		                returnData.setMessage("Error: Invalid reference. Check if the related data exists.");
		            } else if (errorMessage.contains("unique key constraint")) {
		                returnData.setMessage("Error: Duplicate username, email, or CNIC detected.");
		            } else {
		                returnData.setMessage("Issue in adding bus driver in DB: " + errorMessage);
		            }
		        } else {
		            returnData.setMessage("An unknown error occurred.");
		        }
		        returnData.setSuccess(false);
		    }
		    return returnData;
		}

	public ReturnObjectUtility<BusDriver> updateBusDriver(BusDriver busDriver) {
		ReturnObjectUtility<BusDriver> returnData=new ReturnObjectUtility();
		PreparedStatement pstmt;
		try {
			 //first update travel agency owner
			 String sql = "UPDATE busDriver SET bname = ?, dob = ?, cnic= ? where TravelAgencyOwnerId= ?";
			 pstmt = conn.prepareStatement(sql);
			    
			 // Set parameters
			 pstmt.setString(1, busDriver.getName());
			 pstmt.setObject(2, busDriver.getDob());
			 pstmt.setString(3, busDriver.getCnic());
			 pstmt.setInt(4, busDriver.getBusDriverID());

			 // Execute the insert
			 int rowsAffected = pstmt.executeUpdate();
			    
			 if (rowsAffected <=0 ) {
				 returnData.setMessage("Failed to update busDriver.");
				 returnData.setSuccess(false);
				 return returnData;
			 }
			 
			 //now update account
			 sql = "UPDATE Account SET username = ?, email= ?, accPassword= ? where AccountID= ?";
			 pstmt = conn.prepareStatement(sql);
			    
			 Account account=busDriver.getAccount();
			 
			 // Set parameters
			 pstmt.setString(1, account.getUsername());
			 pstmt.setObject(2, account.getEmail());
			 pstmt.setString(3, account.getPassword());
			 pstmt.setInt(4, account.getAccountID());
			 
			 // Execute the insert
			 rowsAffected = pstmt.executeUpdate();
			    
			 if (rowsAffected > 0) {
				 returnData.setMessage("Bus Driver with id "+busDriver.getBusDriverID()+" updated successfully.");
				 returnData.setSuccess(true);
			 } else {
				 returnData.setMessage("Failed to update Bus Driver.");
				 returnData.setSuccess(false);
			 }
			 
		} catch (SQLException e) {
			String errorMessage = e.getMessage().toLowerCase();
			
			if (errorMessage != null) {
		        if (errorMessage.contains("foreign key constraint")) {
		        	returnData.setMessage("Error: Invalid reference. Check if the related data exists.");
		        } else {
		        	returnData.setMessage("Issue in updating Bus Driver in db: " + errorMessage);
		        }
		    } else {
		    	returnData.setMessage("An unknown error occurred.");
		    }
			returnData.setSuccess(false);
		}
		return returnData;
	}
	

}
