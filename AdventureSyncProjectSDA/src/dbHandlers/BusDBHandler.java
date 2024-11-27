package dbHandlers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import accountAndPersonModels.BusDriver;
import accountAndPersonModels.HotelOwner;
import accountAndPersonModels.Tourist;
import accountAndPersonModels.Account;
import accountAndPersonModels.BusDriver;
import accountAndPersonModels.TravelAgencyOwner;
import application.Feedback;
import dataUtilityClasses.FeedbackWithBusID;
import dataUtilityClasses.ReturnListUtility;
import dataUtilityClasses.ReturnObjectUtility;
import travelAgencyModels.Bus;
import travelAgencyModels.Car;
import travelAgencyModels.Seat;
import travelAgencyModels.Tour;


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
	public ReturnObjectUtility<Boolean> addBus(Bus bus,int busDriverID) {
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
			int newBusId=0;
			if(rowsAffected>0) {
				//generate id of bus just added
			    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
	                if (generatedKeys.next()) {
	                	newBusId= generatedKeys.getInt(1);
	                    returnData.setMessage("Bus added successfully with ID: " + newBusId);
	                } else {
	                	returnData.setMessage("Bus added successfully!");
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
                 return returnData;
			 }
			 sql = "INSERT INTO BusDriverDrivesBus (busDriverID, busID) VALUES (?, ?);";
			 pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			    
			 // Set parameters
			 pstmt.setInt(1,busDriverID);
			 pstmt.setInt(2, newBusId);;
			 rowsAffected = pstmt.executeUpdate();
			    
			 if (rowsAffected <= 0) {
				 returnData.setMessage("Failed to assign bus to bus driver.");
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

	public ReturnObjectUtility<Integer> checkPassword(String enteredPassword, String username) {
		ReturnObjectUtility<Integer> returnData=new ReturnObjectUtility<Integer>();
		PreparedStatement pstmt;
		try {
			 Statement stmt = conn.createStatement();
		        ResultSet rSet = stmt.executeQuery("select accPassword, accountID from account where username='" + username+"'");
		        
		        if (rSet.next()) { // Check if a result was found
		            String accPassword= rSet.getString("accPassword");
			        int accountID=rSet.getInt("accountID");
		            Statement stmtForAccount = conn.createStatement();
			        ResultSet rSetForAccount = stmt.executeQuery("select busDriverID from account inner join busdriver on busdriver.accountID=account.accountID where account.accountID=" + accountID);
			        if(!rSetForAccount.next()) {
			        	returnData.setMessage("This username does not belong to a bus driver account!");
			            returnData.setSuccess(false);
			            return returnData;
			        }
		            
			        int busDriverID=rSetForAccount.getInt("busDriverID");
			        if(accPassword.equals(enteredPassword)) {
			        	returnData.setObject(busDriverID);
			            returnData.setMessage("Logged in successfully");
			            returnData.setSuccess(true);
			            return returnData;
			        }
			        else {
			        	returnData.setObject(busDriverID);
			            returnData.setMessage("Incorrect Password");
			            returnData.setSuccess(false);
			            return returnData;
			        }

		        } else {
		            // If no result is found, set an error message
		            returnData.setMessage("Error: User does not exist.");
		            returnData.setSuccess(false);
		        }
		} catch (SQLException e) {
			String errorMessage = e.getMessage().toLowerCase();
			
			if (errorMessage != null) {
		        if (errorMessage.contains("foreign key constraint")) {
		        	returnData.setMessage("Error: Invalid reference. Check if the related data exists.");
		        } else {
		        	returnData.setMessage("Issue in logging in: " + errorMessage);
		        }
		    } else {
		    	returnData.setMessage("An unknown error occurred.");
		    }

            returnData.setSuccess(false);
		}
		return returnData;
	}

	public ReturnObjectUtility<Integer> compareOldPassword(String enteredPassword, int busDriverID) {
		ReturnObjectUtility<Integer> returnData=new ReturnObjectUtility<Integer>();
		PreparedStatement pstmt;
		try {
			 Statement stmt = conn.createStatement();
		        ResultSet rSet = stmt.executeQuery("select account.accountID, account.accPassword from account inner join busDriver on busDriver.accountID=account.accountID where busDriverID=" + busDriverID);
		        
		        int accountID=0;
		        if (rSet.next()) { // Check if a result was found
		            String accPassword= rSet.getString("accPassword");
			        accountID=rSet.getInt("accountID");
		            
			        Statement stmtForAccount = conn.createStatement();
			        
			        if(accPassword.equals(enteredPassword)) {
			        	returnData.setObject(accountID);
			            returnData.setMessage("Correct Old Password");
			            returnData.setSuccess(true);
			            return returnData;
			        }
			        else {
			        	returnData.setObject(accountID);
			            returnData.setMessage("Please correctly enter your old password.");
			            returnData.setSuccess(false);
			            return returnData;
			        }
		        } else {
		            // If no result is found, set an error message
		            returnData.setMessage("Error: User does not exist.");
		            returnData.setSuccess(false);
		        }
		} catch (SQLException e) {
			String errorMessage = e.getMessage().toLowerCase();
			
			if (errorMessage != null) {
		        if (errorMessage.contains("foreign key constraint")) {
		        	returnData.setMessage("Error: Invalid reference. Check if the related data exists.");
		        } else {
		        	returnData.setMessage("Issue in changing password: " + errorMessage);
		        }
		    } else {
		    	returnData.setMessage("An unknown error occurred.");
		    }

            returnData.setSuccess(false);
		}
		return returnData;
	}
	
	public ReturnObjectUtility<BusDriver> updatePassword(String password, int accountID) {
		ReturnObjectUtility<BusDriver> returnData=new ReturnObjectUtility();
		PreparedStatement pstmt;
		try {
			 //first update travel agency owner
			 String sql = "UPDATE account SET accPassword= ? where accountID= ?";
			 pstmt = conn.prepareStatement(sql);
			    
			 // Set parameters
			 pstmt.setString(1, password);
			 pstmt.setObject(2, accountID);
			 
			 // Execute the insert
			 int rowsAffected = pstmt.executeUpdate();
			    
			 if (rowsAffected > 0) {
				 returnData.setMessage("Password updated successfully.");
				 returnData.setSuccess(true);
			 } else {
				 returnData.setMessage("Failed to update password.");
				 returnData.setSuccess(false);
			 }
			 
		} catch (SQLException e) {
			String errorMessage = e.getMessage().toLowerCase();
			
			if (errorMessage != null) {
		        if (errorMessage.contains("foreign key constraint")) {
		        	returnData.setMessage("Error: Invalid reference. Check if the related data exists.");
		        } else {
		        	returnData.setMessage("Issue in updating password in db: " + errorMessage);
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
	
	public ReturnObjectUtility<Bus> retrieveBusObjectWithTours(int busId) {
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
	            
	            if(!hasTour)
	            {
		            returnData.setObject(bus);
		            returnData.setMessage("Bus is not assigned any tour, you cannot book seats..");
		            returnData.setSuccess(false);
	            }
	            else {
		            // Set the Bus object and success message
		            returnData.setObject(bus);
		            returnData.setMessage("Bus data retrieved successfully.");
		            returnData.setSuccess(true);
	            }
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
	
	
	public ReturnObjectUtility<BusDriver> deleteBusDriver(int busDriverID) {
	    ReturnObjectUtility<BusDriver> returnData = new ReturnObjectUtility<>();
	    PreparedStatement pstmt;
	    ResultSet rs;

	    try {
	    	//see if there are rented cars or not
	    	Statement stmt=conn.createStatement();
	        ResultSet rSet=stmt.executeQuery("select hasTour, bus.busid from bus inner join BusDriverDrivesBus on bus.busid=busdriverdrivesbus.busid where busdriverdrivesbus.busdriverid="+busDriverID);
	        int busID=0;
	        if(rSet.next()) {
	        	int hasTour= rSet.getInt("hasTour");
	        	busID= rSet.getInt("busid");
	        	if(hasTour==1) {
	                returnData.setMessage("Your bus is currently assigned a tour. Your account cannot be deleted");
	                returnData.setSuccess(false);
	                return returnData;
	        	}
	        }
	        
	        stmt=conn.createStatement();
	        rSet=stmt.executeQuery("select accountID from busdriver where busdriverid="+busDriverID);
	        int accID=0;
	        if(rSet.next()) {
	        	accID= rSet.getInt("accountID");
	        }
	        
	        String sql = "Delete from busDriverdrivesbus where busDriverID="+busDriverID;
	        pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	        
	        int rowsAffected = pstmt.executeUpdate();
	        if (rowsAffected <= 0) {
	        	returnData.setMessage("Your account could not be deleted. Try again later");
                returnData.setSuccess(false);
                return returnData;
            }
	        //delete from seats
	        sql = "Delete from seat where busID="+busID;
	        pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	        
	        rowsAffected = pstmt.executeUpdate();
	        
	        sql = "Delete from bushasfeedback where busID="+busID;
	        pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	        
	        rowsAffected = pstmt.executeUpdate();
	        
	        sql = "Delete from bus where busID="+busID;
	        pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	        
	        rowsAffected = pstmt.executeUpdate();
	        if (rowsAffected <= 0) {
	        	returnData.setMessage("Your bus could not be deleted. Try again later");
                returnData.setSuccess(false);
                return returnData;
            }
	        
	        
	        sql = "Delete from busDriver where busDriverID="+busDriverID;
	        pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	        
	        rowsAffected = pstmt.executeUpdate();
	        if (rowsAffected > 0) {
	            rs = pstmt.getGeneratedKeys();
	            if (rs.next()) {
	            	int retrievedAccountID = rs.getInt(1);
	                returnData.setMessage("Your account has been deleted successfully!");
	                returnData.setSuccess(true);
	            }
	        }  else {
                returnData.setMessage("Your account could not be deleted. Try again later");
                returnData.setSuccess(false);
                return returnData;
            }
	        

	        sql = "Delete from account where accountID="+accID;
	        pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	        
	        rowsAffected = pstmt.executeUpdate();
	        if (rowsAffected <= 0) {
	        	returnData.setMessage("Your account could not be deleted. Try again later");
                returnData.setSuccess(false);
                return returnData;
            }

	    } catch (SQLException e) {
	        String errorMessage = e.getMessage().toLowerCase();

	        if (errorMessage != null) {
	            if (errorMessage.contains("foreign key constraint")) {
	                returnData.setMessage("Error: Invalid reference. Check if the related data exists.");
	            }else {
	                returnData.setMessage("Issue in deleting bus driver in DB: " + errorMessage);
	            }
	        } else {
	            returnData.setMessage("An unknown error occurred.");
	        }
	        returnData.setSuccess(false);
	    }
	    return returnData;
	}
	
	public static ReturnListUtility<BusDriver> retrieveBusDriverList() {
	    ReturnListUtility<BusDriver> returnData = new ReturnListUtility<>();
	    HashMap<Integer, BusDriver> busDriverList = new HashMap<>();

	    try {
	        Statement stmt = conn.createStatement();
	        // Query to retrieve all bus drivers
	        ResultSet rSet = stmt.executeQuery("SELECT * FROM BusDriver");

	        if (!rSet.next()) { // Check if no bus drivers exist
	            returnData.setMessage("Error: No bus drivers found in the database.");
	            returnData.setSuccess(false);
	        } else {
	            do {
	                // Retrieve and populate bus driver attributes
	                int busDriverID = rSet.getInt("busDriverID");
	                String name = rSet.getString("bname");
	                LocalDate dob = rSet.getDate("dob").toLocalDate(); // Convert SQL Date to LocalDate
	                String cnic = rSet.getString("cnic");

	                // Create a new BusDriver object
	                BusDriver busDriver = new BusDriver(busDriverID, name, dob, cnic);

	                // Add the BusDriver object to the list
	                busDriverList.put(busDriverID, busDriver);
	            } while (rSet.next());

	            // Set the list and success message
	            returnData.setList(busDriverList);
	            returnData.setMessage("Bus driver list retrieved successfully.");
	            returnData.setSuccess(true);
	        }
	    } catch (SQLException e) {
	        String errorMessage = e.getMessage().toLowerCase();

	        if (errorMessage.contains("no such busdriver") || errorMessage.contains("does not exist") || errorMessage.contains("no current")) {
	            returnData.setMessage("Error: Bus driver table does not exist or is empty.");
	        } else {
	            // General case for other SQL exceptions
	            returnData.setMessage("Issue in retrieving bus driver list from database: " + e.getMessage());
	        }

	        returnData.setSuccess(false);
	    }

	    return returnData;
	}

	public ReturnObjectUtility<BusDriver> retrieveAllBusDriverData(int busDriverID) {
	    ReturnObjectUtility<BusDriver> returnData = new ReturnObjectUtility<>();

	    try {
	        Statement stmt = conn.createStatement();
	        String query = "SELECT t.busDriverId, t.bName, t.dob, t.cnic, a.accountID, a.username, a.accpassword, a.email, a.balance FROM busDriver t JOIN Account a ON t.accountID = a.accountID WHERE t.busDriverID = " + busDriverID;

	        ResultSet rSet = stmt.executeQuery(query);

	        if (rSet.next()) { // Check if a result was found
	            // Retrieve Tourist details
	            int busDriverIDRetrieved = rSet.getInt("busDriverId");
	            String name = rSet.getString("bName");
	            Date date = rSet.getDate("dob");
	            LocalDate dob = date.toLocalDate();
	            String cnic = rSet.getString("cnic");

	            // Create a Tourist object
	            BusDriver busDriver = new BusDriver(busDriverIDRetrieved, name, dob, cnic);

	            // Retrieve Account details
	            int accountID = rSet.getInt("accountID");
	            String username = rSet.getString("username");
	            String email = rSet.getString("email");
	            String password = rSet.getString("accPassword");
	            float balance = rSet.getFloat("balance");

	            // Create an Account object
	            Account account = new Account(accountID, username, password, email, balance);

	            busDriver.setAccount(account);
	            // Set the result and success message
	            returnData.setObject(busDriver);
	            returnData.setMessage("Bus Driver and account data retrieved successfully.");
	            returnData.setSuccess(true);
	        } else {
	            // If no result is found, set an error message
	            returnData.setMessage("Error: Bus Driver does not exist.");
	            returnData.setSuccess(false);
	        }

	    } catch (SQLException e) {
	        String errorMessage = e.getMessage().toLowerCase();

	        if (errorMessage.contains("no such BusDriver") || errorMessage.contains("does not exist") || errorMessage.contains("no current")) {
	            returnData.setMessage("Error: Bus Driver does not exist.");
	        } else {
	            // General case for other SQL exceptions
	            returnData.setMessage("Issue in retrieving Bus Driver from database: " + e.getMessage());
	        }
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
	public ReturnListUtility<Seat> getSeatDetails(int busId) {
	    ReturnListUtility<Seat> returnData = new ReturnListUtility<>();
	    
	    try {
	        // Create a Statement to execute the query
	        Statement stmt = conn.createStatement();
	        
	        // Query to get available seats (isBooked = 0) for the specified busId
	        String query = "SELECT * FROM Seat WHERE busId = " + busId + " AND isBooked = 0";
	        ResultSet rSet = stmt.executeQuery(query);
	        
	        // HashMap to store the available seats
	        HashMap<Integer, Seat> seatList = new HashMap<>();

	        if (!rSet.next()) {
	            // If no result is found, set an error message
	            returnData.setMessage("No available seats.");
	            returnData.setSuccess(false);
	        } else {
	            do { // Iterate through the result set to fetch all available seats
	                int seatId = rSet.getInt("seatId");
	                int rowNo = rSet.getInt("rowNo");
	                boolean isBooked = rSet.getBoolean("isBooked");

	                // Create a Seat object
	                Seat seat = new Seat(seatId, busId, isBooked, rowNo);

	                // Add seat to the seatList HashMap
	                seatList.put(seatId, seat);
	            } while (rSet.next());

	            // Set the list of available seats and success message
	            returnData.setList(seatList);
	            returnData.setMessage("Available seats retrieved successfully.");
	            returnData.setSuccess(true);
	        }

	    } catch (SQLException e) {
	        String errorMessage = e.getMessage().toLowerCase();
	        
	        if (errorMessage.contains("no such seat") || errorMessage.contains("does not exist") || errorMessage.contains("no current")) {
	            returnData.setMessage("Error: No seats found.");
	        } else {
	            // General case for other SQL exceptions
	            returnData.setMessage("Issue in retrieving seats from database: " + e.getMessage());
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
	
	public ReturnObjectUtility<Integer> retrieveBusByDriverID(int busDriverID) {
	    // Create a utility object to store the return data
		ReturnObjectUtility<Integer> returnData = new ReturnObjectUtility<>();
	    
	    try {
	        // Create a statement to execute SQL queries
	        Statement stmt = conn.createStatement();

	        // SQL query using JOIN to retrieve busID for the given busDriverID
	        String query = "SELECT Bus.busId " +
	                       "FROM BusDriverDrivesBus " +
	                       "JOIN Bus ON BusDriverDrivesBus.busID = Bus.busId " +
	                       "WHERE BusDriverDrivesBus.busDriverID = " + busDriverID;

	        ResultSet rSet = stmt.executeQuery(query);
	        
	        // Check if any results are returned
	        if (!rSet.next()) {
	            // If no result is found, set an error message
	            returnData.setMessage("Error: No buses found for the given driver.");
	            returnData.setSuccess(false);
	        } else {
	            // Create a list to store the retrieved busIDs
	            Integer busID = rSet.getInt("busId");

	            // Set the list and success message
	            returnData.setObject(busID);
	            returnData.setMessage("Bus IDs retrieved successfully.");
	            returnData.setSuccess(true);
	        }
	    } catch (SQLException e) {
	        // Handle SQL exceptions with appropriate messages
	        String errorMessage = e.getMessage().toLowerCase();

	        if (errorMessage.contains("no such bus") || errorMessage.contains("does not exist") || errorMessage.contains("no current")) {
	            returnData.setMessage("Error: No buses found for the given driver.");
	        } else {
	            // General case for other SQL exceptions
	            returnData.setMessage("Issue in retrieving buses from database: " + e.getMessage());
	        }
	        returnData.setSuccess(false);
	    }

	    return returnData;
	}
	
	public static ReturnListUtility<FeedbackWithBusID> getFeedbackDetailsWithBusID() {
	    ReturnListUtility<FeedbackWithBusID> returnData = new ReturnListUtility<>();
	    
	    try {
	        // Create statement and execute the feedback query
	        Statement stmt = conn.createStatement();
	        ResultSet rSet = stmt.executeQuery("SELECT bhf.BusID, f.feedbackID, f.comment FROM BusHasFeedback bhf " +
	                                           "JOIN Feedback f ON bhf.feedbackID = f.feedbackID " +
	                                           "WHERE f.typeOfFeedback = 'bus' order by bhf.busID");

	        // List to store feedback details
	        HashMap<Integer, FeedbackWithBusID> feedbackList=new HashMap<Integer, FeedbackWithBusID>();
	        // Process the result set
	        while (rSet.next()) {
	            int busId = rSet.getInt("BusID");
	        	int feedbackID = rSet.getInt("feedbackID");
	            String comment = rSet.getString("comment");

	            // Create FeedbackWithBusID object and add to the list
	            FeedbackWithBusID feedback = new FeedbackWithBusID(feedbackID, busId, comment);
	            feedbackList.put(feedback.getFeedbackID(), feedback);
	        }

	        // Set the result in the returnData object
	        if (feedbackList.isEmpty()) {
	            returnData.setMessage("No feedback found for buses.");
	            returnData.setSuccess(false);
	        } else {
	            returnData.setList(feedbackList);
	            returnData.setMessage("Feedback details retrieved successfully.");
	            returnData.setSuccess(true);
	        }
	        
	    } catch (SQLException e) {
	        // Handle SQL exceptions
	        returnData.setMessage("Error retrieving feedback details: " + e.getMessage());
	        returnData.setSuccess(false);
	    }

	    return returnData;
	}
	
	public static ReturnListUtility<Feedback> retrieveFeedbackList(int busID) {
	    ReturnListUtility<Feedback> returnData = new ReturnListUtility<>();
	    
	    try {
	        // Create statement and execute the feedback query
	        Statement stmt = conn.createStatement();
	        ResultSet rSet = stmt.executeQuery("SELECT f.feedbackID, f.comment FROM BusHasFeedback bhf " +
	                                           "JOIN Feedback f ON bhf.feedbackID = f.feedbackID " +
	                                           "WHERE f.typeOfFeedback = 'bus' and bhf.busID = " + busID);

	        // List to store feedback details
	        HashMap<Integer, Feedback> feedbackList=new HashMap<Integer, Feedback>();
	        // Process the result set
	        while (rSet.next()) {
	        	int feedbackID = rSet.getInt("feedbackID");
	            String comment = rSet.getString("comment");

	            // Create FeedbackWithBusID object and add to the list
	            Feedback feedback = new Feedback(feedbackID, comment);
	            feedbackList.put(feedback.getFeedbackID(), feedback);
	        }

	        // Set the result in the returnData object
	        if (feedbackList.isEmpty()) {
	            returnData.setMessage("No feedback has been given for this bus");
	            returnData.setSuccess(false);
	        } else {
	            returnData.setList(feedbackList);
	            returnData.setMessage("Feedback details retrieved successfully.");
	            returnData.setSuccess(true);
	        }
	        
	    } catch (SQLException e) {
	        // Handle SQL exceptions
	        returnData.setMessage("Error retrieving feedback details: " + e.getMessage());
	        returnData.setSuccess(false);
	    }

	    return returnData;
	}
	
	public ReturnListUtility<Bus> retrieveBusListWithBusDriverID() {
		ReturnListUtility<Bus> returnData=new ReturnListUtility();
		
		try {
		    Statement stmt = conn.createStatement();
		    ResultSet rSet = stmt.executeQuery(
		        "SELECT b.busId, b.brand, b.model, b.manufactureYear, b.plateNumber, b.noOfSeats, " +
		        "b.noOfRows, b.priceOfSeat, b.hasTour, bddb.busDriverID " +
		        "FROM Bus b " +
		        "INNER JOIN BusDriverDrivesBus bddb ON b.busId = bddb.busID"
		    );

		    HashMap<Integer, Bus> busList = new HashMap<>();

		    if (!rSet.next()) {
		        // If no result is found, set an error message
		        returnData.setMessage("Error: No buses found.");
		        returnData.setSuccess(false);
		    } else {
		        do {
		            // Fetch bus details
		            int retrievedBusId = rSet.getInt("busId");
		            String brand = rSet.getString("brand");
		            String model = rSet.getString("model");
		            int manufactureYear = rSet.getInt("manufactureYear");
		            String plateNumber = rSet.getString("plateNumber");
		            int noOfSeats = rSet.getInt("noOfSeats");
		            int noOfRows = rSet.getInt("noOfRows");
		            float priceOfSeat = rSet.getFloat("priceOfSeat");
		            boolean hasTour = rSet.getBoolean("hasTour");
		            int busDriverID = rSet.getInt("busDriverID"); // Fetch bus driver ID

		         // Create a new Bus object
		            Bus bus = new Bus(retrievedBusId, brand, model, manufactureYear, plateNumber, noOfSeats, noOfRows, priceOfSeat, busDriverID);

		            busList.put(bus.getID(), bus);
		        } while (rSet.next());
		        
		        // Set success data
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
	public ReturnListUtility<Bus> getBusDetailsWithTouristID(int touristID) {
		ReturnListUtility<Bus> returnData=new ReturnListUtility();
		
		try {
		    Statement stmt = conn.createStatement();
		    ResultSet rSet = stmt.executeQuery(
		        "select distinct b.busId, b.brand, b.model, b.manufactureYear, b.plateNumber from bus b join seat s on b.busId = s.busId " +
		        "join TouristHasBookedSeats thbs on thbs.seatID = s.seatId where thbs.touristID = " + touristID);
		    //select distinct b.busId, b.brand, b.model, b.manufactureYear, b.plateNumber from bus b join seat s on b.busId = s.busId 
		    //join TouristHasBookedSeats thbs on thbs.seatID = s.seatId where thbs.touristID = 1

		    HashMap<Integer, Bus> busList = new HashMap<>();

		    if (!rSet.next()) {
		        // If no result is found, set an error message
		        returnData.setMessage("Error: No buses found.");
		        returnData.setSuccess(false);
		    } else {
		        do {
		            // Fetch bus details
		            int retrievedBusId = rSet.getInt("busId");
		            String brand = rSet.getString("brand");
		            String model = rSet.getString("model");
		            int manufactureYear = rSet.getInt("manufactureYear");
		            String plateNumber = rSet.getString("plateNumber");

		         // Create a new Bus object
		            Bus bus = new Bus(retrievedBusId, brand, model, manufactureYear, plateNumber);

		            busList.put(bus.getID(), bus);
		        } while (rSet.next());
		        
		        // Set success data
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
		        if (errorMessage.contains("no such seat") || errorMessage.contains("does not exist") ||errorMessage.contains("no current")) {
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

	public ReturnObjectUtility<Float> getBill(int busId){
	    ReturnObjectUtility<Float> returnData = new ReturnObjectUtility<>();
		 try {
		        Statement stmt = conn.createStatement();
		        ResultSet rSet = stmt.executeQuery("SELECT priceOfSeat FROM Bus WHERE busId = " + busId);
		        
		        if (rSet.next()) { // Check if a result was found
		            float priceOfSeat = rSet.getFloat("priceOfSeat");

		            returnData.setObject(priceOfSeat);
		            returnData.setMessage("Bus bill retrieved successfully.");
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
	public ReturnObjectUtility<Float> getOverallRating() {
	    ReturnObjectUtility<Float> returnData = new ReturnObjectUtility<>();

	    try {
	        // Create the SQL query to retrieve the average rating for the bus
	        String query = "SELECT ROUND(AVG(CAST(f.rating AS FLOAT)), 2) AS overallAverageRating " +
	                       "FROM BusHasFeedback bhf " +
	                       "JOIN Feedback f ON bhf.feedbackID = f.feedbackID " +
	                       "WHERE f.typeOfFeedback = 'bus'";
	        // Execute the query
	        Statement stmt = conn.createStatement();
	        ResultSet rSet = stmt.executeQuery(query);

	        // Check if a result is found
	        if (rSet.next()) {
	            float overallAverageRating = rSet.getFloat("overallAverageRating");

	            // Set the retrieved rating in the return object
	            returnData.setObject(overallAverageRating);
	            returnData.setMessage("Overall rating retrieved successfully.");
	            returnData.setSuccess(true);
	        } else {
	            // If no result is found, set an error message
	            returnData.setMessage("Error: No feedback found for this bus.");
	            returnData.setSuccess(false);
	        }
	    } catch (SQLException e) {
	        String errorMessage = e.getMessage().toLowerCase();
	        
	        if (errorMessage.contains("no such bus") || errorMessage.contains("does not exist") || errorMessage.contains("no current")) {
	            returnData.setMessage("Error: Bus does not exist.");
	        } else {
	            // General case for other SQL exceptions
	            returnData.setMessage("Issue in retrieving bus rating from database: " + e.getMessage());
	        }

	        returnData.setSuccess(false);
	    }
	    
	    return returnData;
	}

	public ReturnObjectUtility<Float> addMoney(int busId, float bill){
		 ReturnObjectUtility<Float> returnData = new ReturnObjectUtility<>();
		 PreparedStatement pstmt;
		 try {
			 	String sql = "UPDATE account SET balance = balance + ? WHERE accountID = (SELECT accountId from busdriver inner join  BusDriverDrivesBus on busDriver.busDriverId=BusDriverDrivesBus.busDriverID  WHERE busID = ?)";
		        pstmt = conn.prepareStatement(sql);

		        // Set parameters
		        pstmt.setFloat(1, bill); // Deduction amount
		        pstmt.setInt(2, busId); // bus ID

		        // Execute the update
		        int rowsAffected = pstmt.executeUpdate();

		        if (rowsAffected > 0) {
		            returnData.setMessage("Balance updated successfully for bus driver");
		            returnData.setSuccess(true);
		        } else {
		            returnData.setMessage("Failed to update balance for bus driver");
		            returnData.setSuccess(false);
		        }
		        
		    } catch (SQLException e) {
		        String errorMessage = e.getMessage().toLowerCase();

		        if (errorMessage != null) {
		            if (errorMessage.contains("foreign key constraint")) {
		                returnData.setMessage("Error: Invalid reference. Check if the related data exists.");
		            } else {
		                returnData.setMessage("Issue in deducting money in DB: " + errorMessage);
		            }
		        } else {
		            returnData.setMessage("An unknown error occurred.");
		        }
		        returnData.setSuccess(false);
		    }
		    return returnData;
	}

	public ReturnObjectUtility<Integer> retrieveTourID(int busDriverID){
	    ReturnObjectUtility<Integer> returnData = new ReturnObjectUtility<>();
		 try {
		        Statement stmt = conn.createStatement();
		        ResultSet rSet = stmt.executeQuery("select tourID from tour inner join BusDriverDrivesBus on BusDriverDrivesBus.busID=tour.busID where busDriverID=" + busDriverID);
		        
		        if (rSet.next()) { // Check if a result was found
		            int tourID = rSet.getInt("tourID");

		            returnData.setObject(tourID);
		            returnData.setMessage("Tour retrieved successfully.");
		            returnData.setSuccess(true);
		        } else {
		            // If no result is found, set an error message
		            returnData.setMessage("Error: Tour does not exist.");
		            returnData.setSuccess(false);
		        }
		    } catch (SQLException e) {
		        String errorMessage = e.getMessage().toLowerCase();
		        
		        if (errorMessage.contains("no such bus") || errorMessage.contains("does not exist") || errorMessage.contains("no current")) {
		            returnData.setMessage("Error: Tour does not exist.");
		        } else {
		            // General case for other SQL exceptions
		            returnData.setMessage("Issue in retrieving tour from database: " + e.getMessage());
		        }

		        returnData.setSuccess(false);
		    }
		    
		    return returnData;
	}
	
	public ReturnObjectUtility<Boolean> completeTour(int tourID) {
	    ReturnObjectUtility<Boolean> returnData = new ReturnObjectUtility<>();
	    try {
	        String busIDQuery = "SELECT busID, date FROM tour WHERE tourID = ?";
	        PreparedStatement pstmt = conn.prepareStatement(busIDQuery);
	        pstmt.setInt(1, tourID);
	        ResultSet rSet = pstmt.executeQuery();
	        
	        if (!rSet.next()) {
	            returnData.setMessage("Error: The tour does not exist.");
	            returnData.setSuccess(false);
	            return returnData;
	        }
	        
	        int busID = rSet.getInt("busID");
	        LocalDate tourDate = rSet.getDate("date").toLocalDate();

	        if (tourDate.isAfter(LocalDate.now())) {
	            returnData.setMessage("Error: The tour date is in the future. Cannot complete the tour.");
	            returnData.setSuccess(false);
	            return returnData;
	        }

	        String updateBusQuery = "UPDATE Bus SET hasTour = 0 WHERE busID = ?";
	        pstmt = conn.prepareStatement(updateBusQuery);
	        pstmt.setInt(1, busID);
	        int rowsAffected = pstmt.executeUpdate();
	        
	        if (rowsAffected <= 0) {
	            returnData.setMessage("Error: Unable to update Bus information.");
	            returnData.setSuccess(false);
	            return returnData;
	        } 
	        
	        //all seats need to be unbooked
	        String updateSeatQuery = "UPDATE Seat SET isBooked= 0 WHERE busID = ?";
	        pstmt = conn.prepareStatement(updateBusQuery);
	        pstmt.setInt(1, busID);
	        rowsAffected = pstmt.executeUpdate();
	        
	        if (rowsAffected <= 0) {
	            returnData.setMessage("Error: Unable to update Seat information.");
	            returnData.setSuccess(false);
	            return returnData;
	        } 
	        
	        //remove association with tourist
	        String deleteQuery = "DELETE FROM TouristHasBookedSeats WHERE seatID IN (SELECT seatId FROM Seat WHERE busId = ? )";
	        pstmt = conn.prepareStatement(deleteQuery);
	        pstmt.setInt(1, busID);
	        rowsAffected = pstmt.executeUpdate();
	        
	        if (rowsAffected > 0) {
	            returnData.setMessage("Tour with ID " + tourID + " completed successfully.");
	            returnData.setSuccess(true);
	        } else {
	            returnData.setMessage("Tour could not be completed.");
	            returnData.setSuccess(false);
	        }
	        
	        deleteQuery = "DELETE FROM tour WHERE tourID = ?";
	        pstmt = conn.prepareStatement(deleteQuery);
	        pstmt.setInt(1, tourID);
	        rowsAffected = pstmt.executeUpdate();
	        
	        if (rowsAffected > 0) {
	            returnData.setMessage("Tour with ID " + tourID + " completed successfully.");
	            returnData.setSuccess(true);
	        } else {
	            returnData.setMessage("Tour could not be completed.");
	            returnData.setSuccess(false);
	        }
	    } catch (SQLException e) {
	        String errorMessage = e.getMessage().toLowerCase();
	        if (errorMessage.contains("foreign key constraint") || errorMessage.contains("integrity constraint")) {
	            returnData.setMessage("Error: Cannot delete tour as it is referenced in rental records. Remove related records first.");
	        } else {
	            returnData.setMessage("Issue in completing the tour: " + e.getMessage());
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
		        returnData.setMessage("Failed to create account.");
		        returnData.setSuccess(false);
		        return returnData;
		    }

		        // Step 2: Insert into TravelAgencyOwner table
		        sql = "INSERT INTO busDriver (accountID, bName, dob, cnic) VALUES ( ?, ?, ?, ?)";
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
		            	busDriver.setBusDriverID(retrievedOwnerID);
		            	returnData.setObject(busDriver);
		            	returnData.setMessage("Bus Driver with id "+retrievedOwnerID+" added successfuly!");
		                returnData.setSuccess(true);
		                return returnData;
		            } else {
		                returnData.setMessage("Bus Driver added successfully.");
		                returnData.setSuccess(false);
		                return returnData;
		            }
		        } else {
		            returnData.setMessage("Failed to add Bus Driver.");
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
			 String sql = "UPDATE busDriver SET bname = ?, dob = ?, cnic= ? where busDriverId= ?";
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
			 sql = "UPDATE Account SET username = ?, email= ?, accPassword= ?, balance=balance+? where AccountID= ?";
			 pstmt = conn.prepareStatement(sql);
			    
			 Account account=busDriver.getAccount();
			 
			 // Set parameters
			 pstmt.setString(1, account.getUsername());
			 pstmt.setObject(2, account.getEmail());
			 pstmt.setString(3, account.getPassword());
			 pstmt.setFloat(4, account.getBalance());
			 pstmt.setInt(5, account.getAccountID());
			 
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
	public ReturnObjectUtility<Float> getOverallRating(int busID) {
	    ReturnObjectUtility<Float> returnData = new ReturnObjectUtility<>();

	    try {
	        // Create the SQL query to retrieve the average rating for the bus
	        String query = "SELECT ROUND(AVG(CAST(f.rating AS FLOAT)), 2) AS overallAverageRating " +
	                       "FROM BusHasFeedback bhf " +
	                       "JOIN Feedback f ON bhf.feedbackID = f.feedbackID " +
	                       "WHERE f.typeOfFeedback = 'bus' and bhf.BusID = " + busID;
	        // Execute the query
	        Statement stmt = conn.createStatement();
	        ResultSet rSet = stmt.executeQuery(query);

	        // Check if a result is found
	        if (rSet.next()) {
	            float overallAverageRating = rSet.getFloat("overallAverageRating");

	            // Set the retrieved rating in the return object
	            returnData.setObject(overallAverageRating);
	            returnData.setMessage("Overall rating retrieved successfully.");
	            returnData.setSuccess(true);
	        } else {
	            // If no result is found, set an error message
	            returnData.setMessage("Error: No feedback found for this bus.");
	            returnData.setSuccess(false);
	        }
	    } catch (SQLException e) {
	        String errorMessage = e.getMessage().toLowerCase();
	        
	        if (errorMessage.contains("no such bus") || errorMessage.contains("does not exist") || errorMessage.contains("no current")) {
	            returnData.setMessage("Error: Bus does not exist.");
	        } else {
	            // General case for other SQL exceptions
	            returnData.setMessage("Issue in retrieving bus rating from database: " + e.getMessage());
	        }

	        returnData.setSuccess(false);
	    }
	    
	    return returnData;
	}
	
	public ReturnObjectUtility<Tour> getBusTourDetail(int busID) {
	    // Initialize the return object
	    ReturnObjectUtility<Tour> returnData = new ReturnObjectUtility<>();

	    try {
	        // Create an SQL statement
	        Statement stmt = conn.createStatement();
	        
	        // Execute the query
	        ResultSet rSet = stmt.executeQuery("select tourID, origin, destination, date from Tour where busID = " + busID);

	        // Check if a result is found
	        if (rSet.next()) {
	            // Retrieve values from the result set
	            int tourID = rSet.getInt("tourID");
	            String origin = rSet.getString("origin");
	            String destination = rSet.getString("destination");
	            Date date = rSet.getDate("date");
	            
	            // Create a new Tour object using the constructor
	            Tour tour = new Tour(tourID, origin, destination, date);
	            
	            // Set the Tour object and success message in the return object
	            returnData.setObject(tour);
	            returnData.setMessage("Tour details retrieved successfully.");
	            returnData.setSuccess(true);
	        } else {
	            // If no result is found, set an error message
	            returnData.setMessage("Error: No tour found for the given bus ID.");
	            returnData.setSuccess(false);
	        }
	    } catch (SQLException e) {
	        // Handle SQL exceptions
	        String errorMessage = e.getMessage().toLowerCase();
	        
	        if (errorMessage.contains("no such tour") || errorMessage.contains("does not exist") || errorMessage.contains("no current")) {
	            returnData.setMessage("Error: Tour does not exist for the given bus ID.");
	        } else {
	            // General case for other SQL exceptions
	            returnData.setMessage("Issue in retrieving tour details from database: " + e.getMessage());
	        }
	    }
	    // Return the data
	    return returnData;
	}

	public ReturnObjectUtility<Bus> getBusDetail(int busID) {
	    // Initialize the return object
	    ReturnObjectUtility<Bus> returnData = new ReturnObjectUtility<>();

	    try {
	        // Create an SQL statement
	        Statement stmt = conn.createStatement();
	        
	        // Execute the query
	        ResultSet rSet = stmt.executeQuery("select busID, brand, model, manufactureYear, plateNumber, noOfSeats, priceOfSeat, hasTour from bus where busID = " + busID);
	        //select busID, brand, model, manufactureYear, plateNumber, noOfSeats, priceOfSeat, hasTour from bus where busID = 1
	        // Check if a result is found
	        if (rSet.next()) {
	            // Retrieve values from the result set
	            int busIDD = rSet.getInt("busID");
	            String brand = rSet.getString("brand");
	            String model = rSet.getString("model");
	            int year = rSet.getInt("manufactureYear");
	            String plate = rSet.getString("plateNumber");
	            int noOfSeat = rSet.getInt("noOfSeats");
	            float price = rSet.getFloat("priceOfSeat");
	            boolean hasTour = rSet.getBoolean("hasTour");
	            // Create a new Tour object using the constructor
	            Bus bus = new Bus(busIDD, brand, model, year, plate, noOfSeat, price, hasTour);
	            // Set the Tour object and success message in the return object
	            returnData.setObject(bus);
	            returnData.setMessage("Bus details retrieved successfully.");
	        } else {
	            // If no result is found, set an error message
	            returnData.setMessage("Error: No Bus found for the given bus ID.");
	        }
	    } catch (SQLException e) {
	        // Handle SQL exceptions
	        String errorMessage = e.getMessage().toLowerCase();
	        
	        if (errorMessage.contains("no such bus") || errorMessage.contains("does not exist") || errorMessage.contains("no current")) {
	            returnData.setMessage("Error: Bus does not exist for the given bus ID.");
	        } else {
	            // General case for other SQL exceptions
	            returnData.setMessage("Issue in retrieving bus details from database: " + e.getMessage());
	        }
	    }

	    // Return the data
	    return returnData;
	}
}
