package tdt4140.gr1808.server;

import java.sql.*;
import java.util.HashMap;

public class DBQuery {

private Connection con;
//private HashMap<String, String> result;
	
	public DBQuery() {
		this.con = connect();
	}
	
	//Establishes a connection to the database
	public Connection connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql.stud.ntnu.no:3306/hkmardal_databasen?useSSL=false", "hkmardal_admin", "sommer");
			return con;
		} catch(Exception e) {
			System.out.println("Could not connect to database.");
			e.printStackTrace();
			return null;
		}
	}
	
	//Adds a user to the database
	public HashMap<String, String> addUser(String user_type, String name) {
		try {
			Statement stmnt = con.createStatement();
			stmnt.executeUpdate("insert into user(user_type, name) values('" + user_type + "', '" + name + "')");
			stmnt.close();
		} catch(Exception e) {
			return fillHashMap("failure", null, e.getMessage());
		}
		return fillHashMap("success", null, "User was added successfully");
	}
	
	//Deletes a user from the database
	public HashMap<String, String> deleteUser(String user_id) {
		try {
			Statement stmnt = con.createStatement();
			stmnt.executeUpdate("delete from user where user_id = " + user_id);
			stmnt.close();
		} catch(Exception e) {
			return fillHashMap("failure", null, e.getMessage());
		}
		return fillHashMap("success", null, "User was deleted successfully");
	}
	
	//Deletes pulse data for a given user for a given time-period from the database
	public HashMap<String, String> deletePulseData(String user_id, String data_type, String start_datetime, String end_datetime) {
		try {
			Statement stmnt = con.createStatement();
			stmnt.executeUpdate("delete from data_super where user_id = " + user_id + " and time_stamp between \"" + 
					start_datetime + "\" and \"" + end_datetime + "\"");
			stmnt.close();
		} catch(Exception e) {
			return fillHashMap("failure", null, e.getMessage());
		}
		return fillHashMap("success", null, "Pulse-data deleted successfully");
	}
	
	//Adds pulse data for a given user for a given time-period to the database
	public HashMap<String, String> addPulseData(String user_id, String data_type, String pulse, String timeStamp) {
		try {
			Statement stmnt = con.createStatement();
			stmnt.executeUpdate("insert into data_super(user_id, time_stamp) values('" + user_id + "', '" + timeStamp + "')");
			stmnt.executeUpdate("insert into pulse_data(pulse) values('" + pulse + "')");
			stmnt.close();
		} catch(Exception e) {
			return fillHashMap("failure", null, e.getMessage());
		}
		return fillHashMap("success", null, "Pulse-data added successfully");
	}
	
	//Returns pulse data on the format "pulse_timestamp,pulse_timestamp, ..."
	public HashMap<String, String> getPulseData(String user_id, String data_type, String start_datetime, String end_datetime) {
		String str = "";
		try {
			Statement stmnt = con.createStatement();
			ResultSet rs = stmnt.executeQuery("select pulse, time_stamp from data_super natural join pulse_data where user_id = " + 
					user_id + " and time_stamp between \"" + start_datetime + "\" and \"" + end_datetime + "\"");
			while(rs.next()) {
				str += Integer.toString(rs.getInt(1)) + "_" + rs.getString(2) + ",";
			}
			str = str.replaceAll(",$", "");
			stmnt.close();
		} catch(Exception e) {
			return fillHashMap("failure", null, e.getMessage());
		}
		if (str == null || str.equals("")) {
			return fillHashMap("failure", null, "Data is null");
		}
		return fillHashMap("success", str, "Pulse-data retireved successfully");
	}
	
	//Returns the username of a user with a given user_id
	public HashMap<String, String> getUsername(String user_id) {
		String username = null;
		HashMap<String, String> hm = new HashMap<>();
		try {
			Statement stmnt = con.createStatement();
			ResultSet rs = stmnt.executeQuery("select name from user where user_id = " + user_id);
			if (rs.next()) {
				username = rs.getString(1);
			}
			stmnt.close();
		} catch (Exception e) {
			return fillHashMap("failure", null, e.getMessage());
		}
		if (username == null) {
			return fillHashMap("failure", null, "Data is null");
		}
		return fillHashMap("success", username, "Username retrieved successfully");
	}

	//Returns the user type of a user with a given user_id
	public HashMap<String, String> getUserType(String user_id) {
		String userType = null;
		try {
			Statement stmnt = con.createStatement();
			ResultSet rs = stmnt.executeQuery("select user_type from user where user_id = " + user_id);
			if (rs.next()) {
				userType = rs.getString(1);
			}
			stmnt.close();
		} catch(Exception e) {
			return fillHashMap("failure", null, e.getMessage());
		}
		if (userType == null) {
			return fillHashMap("failure", null, "Data is null");
		}
		return fillHashMap("success", userType, "User type retrieved successfully");
	}
	
	private HashMap<String, String> fillHashMap(String status, String data, String message) {
		HashMap<String, String> hm = new HashMap<>();
		hm.put("status", status);
		hm.put("data", data);
		hm.put("message", message);
		return hm;
	}
	
	public static void main(String[] args) {
		DBQuery db = new DBQuery();
		HashMap<String, String> hm = db.getPulseData("100", "pulse", "2017-10-10 11:11:11", "2019-10-10 11:11:11");
		String str;
		str = hm.get("message") + "\n" + hm.get("data");
		System.out.println(str);
	}
}
