package dbHandlers;

import java.sql.Connection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import travelAgencyModels.Car;

public class TravelAgencyDBHandler {
	private Connection conn;
	public TravelAgencyDBHandler(Connection c) {
		conn=c;
	}
	
	//car related functions
	public String addCar(Car car) {
		PreparedStatement pstmt;
		try {
			 String sql = "INSERT INTO Car (brand, model, manufactureYear, plateNumber, rentalStatus, rentalFee, costPerKm) VALUES (?, ?, ?, ?, 0, ?, ?);";
			 pstmt = conn.prepareStatement(sql);
			    
			 // Set parameters
			 pstmt.setString(1, car.getBrand());
			 pstmt.setString(2, car.getModel());
			 pstmt.setInt(3, car.getYear());
			 pstmt.setString(4, car.getPlateNumber());
			 pstmt.setFloat(5, car.getRentalFee());
			 pstmt.setFloat(6, car.getcostPerKm()); 

			 // Execute the insert
			 int rowsAffected = pstmt.executeUpdate();
			    
			 if (rowsAffected > 0) {
			        return "Car added successfully.";
			 } else {
			        return "Failed to add car.";
			 }
			 
		} catch (SQLException e) {
			    return "Issue in adding car to db: " + e.getMessage();
		}
	}
	
	public String deleteCar(int carID) {
		try {
			Statement stmt=conn.createStatement();
	        ResultSet rSet=stmt.executeQuery("select rentalStatus from car where carID="+carID);
	        
	        while(!rSet.next()) {
	        	//car does not exist
	        	return "Car does not exist";
	        }
	        
	        //car is already rented
	        while(rSet.getBoolean(1)) {
	        	return "Car is already rented";
	        }

	        //now delete car
			String deleteQuery = "DELETE FROM car WHERE carId = ?";
            PreparedStatement pstmt = conn.prepareStatement(deleteQuery);
            pstmt.setInt(1, carID);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                return "Car deleted successfully";
            } else {
            	return "Car could not be deleted";
            }
		}
		catch(SQLException se){
			return "Issue in deleting car..."+se.getMessage();
		}
        
	}

	public String retreiveCarData(int carID) {
		try {
			Statement stmt=conn.createStatement();
	        ResultSet rSet=stmt.executeQuery("select * from car where carID="+carID);
	        
	        while(!rSet.next()) {
	        	//car does not exist
	        	return "Car does not exist";
	        }

	        //now delete car
			String deleteQuery = "DELETE FROM car WHERE carId = ?";
            PreparedStatement pstmt = conn.prepareStatement(deleteQuery);
            pstmt.setInt(1, carID);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                return "Car deleted successfully";
            } else {
            	return "Car could not be deleted";
            }
		}
		catch(SQLException se){
			return "Issue in deleting car..."+se.getMessage();
		}
        
	}
}
