package tdt4140.gr1808.server;

import java.sql.*;

public class DBQuery {

Connection con;
	
	public DBQuery() {
		this.con = connect();
	}
	
	//Establishes a connection to the database
	public Connection connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://mysql.stud.ntnu.no:3306/hkmardal_databasen", "hkmardal_admin", "sommer");
			return con;
		} catch(Exception e) {
			System.out.println("Could not connect to database.");
			e.printStackTrace();
			return null;
		}
	}
	
	//Adds a user to the database
	public void addUser(String user_type, String name) {
		try {
			Statement stmnt = con.createStatement();
			stmnt.executeUpdate("insert into user(user_type, name) values('" + user_type + "', '" + name + "')");
			stmnt.close();
		} catch(Exception e) {
			System.out.println("Could not create user " + name);
			e.printStackTrace();
		}
	}
	
	//Deletes a user from the database
	public void deleteUser(String user_id) {
		try {
			Statement stmnt = con.createStatement();
			stmnt.executeUpdate("delete from user where user_id = " + user_id);
			stmnt.close();
		} catch(Exception e) {
			System.out.println("Could not delete user " + user_id);
			e.printStackTrace();
		}
	}
	
	//Deletes pulse data for a given user for a given time-period from the database
	public void deletePulseData(String user_id, String data_type, String start_datetime, String end_datetime) {
		try {
			Statement stmnt = con.createStatement();
			stmnt.executeUpdate("delete from data_super where user_id = " + user_id + " and time_stamp between \"" + start_datetime + "\" and \"" + end_datetime + "\"");
			stmnt.close();
		} catch(Exception e) {
			System.out.println("Could not delete data");
			e.printStackTrace();
		}
	}
	
	//Adds pulse data for a given user for a given time-period to the database
	public void addPulseData(String user_id, String data_type, String pulse, String timeStamp) {
		try {
			Statement stmnt = con.createStatement();
			stmnt.executeUpdate("insert into data_super(user_id, time_stamp) values('" + user_id + "', '" + timeStamp + "')");
			stmnt.executeUpdate("insert into pulse_data(pulse) values('" + pulse + "')");
			stmnt.close();
		} catch(Exception e) {
			System.out.println("Could not add data to database");
			e.printStackTrace();
		}
	}
	
	//Returns pulse data on the format "pulse_timestamp, pulse_timestamp, ..."
	public String getPulseData(String user_id, String data_type, String start_datetime, String end_datetime) {
		String str = "";
		try {
			Statement stmnt = con.createStatement();
			ResultSet rs = stmnt.executeQuery("select pulse, time_stamp from data_super natural join pulse_data where user_id = " + user_id + " and time_stamp between \"" + start_datetime + "\" and \"" + end_datetime + "\"");
			while(rs.next()) {
				str += Integer.toString(rs.getInt(1)) + "_" + rs.getString(2) + ", ";
			}
			str = str.replaceAll(", $", "");
			stmnt.close();
			return str;
		} catch(SQLException e) {
			System.out.println("Could not get data");
			e.printStackTrace();
		}
		return str;
	}
	
	//Returns user info on the format "user_id user_type name"
	public String getUser(String user_id) {
		String user = null;
		try {
			Statement stmnt = con.createStatement();
			ResultSet rs = stmnt.executeQuery("select * from user where user_id = " + user_id);
			if (rs.next()) {
				user = Integer.toString(rs.getInt(1)) + " " + rs.getString(2) + " " + rs.getString(3);
			}
			stmnt.close();
			return user;
		} catch(SQLException e) {
			System.out.println("Could not get user");
			e.printStackTrace();
		}
		return user;
	}

	//Returns the user type of a user with a given user_id
	public String getUserType(String user_id) {
		String userType = null;
		try {
			Statement stmnt = con.createStatement();
			ResultSet rs = stmnt.executeQuery("select user_type from user where user_id = " + user_id);
			if (rs.next()) {
				userType = rs.getString(1);
			}
			stmnt.close();
			return userType;
		} catch(SQLException e) {
			System.out.println("Could not get user-type");
			e.printStackTrace();
		}
		return userType;
	}
}
