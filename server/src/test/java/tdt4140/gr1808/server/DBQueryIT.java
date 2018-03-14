package tdt4140.gr1808.server;

import org.junit.BeforeClass;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

import java.sql.DriverManager;
import java.util.HashMap; 

public class DBQueryIT {
	static DBQuery db = null;
	HashMap<String, String> hm;
	
	@BeforeClass
	public static void setUpConnection() {
		db = new DBQuery();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			db.con = DriverManager.getConnection("jdbc:mysql://mysql.stud.ntnu.no:3306/hkmardal_testing_database?useSSL=false", "hkmardal_admin", "sommer");
		} catch (Exception e) {
			fail();
		}
	}

	@After
	public void setUp() {
		hm = null;
	}
	
	private void assertHmFailure(String message) {
		assertTrue(hm.get("type").equals("error"));
		assertTrue(hm.get("data").equals(message));
	}
	
	private void assertHmMessage(String message) {
		assertTrue(hm.get("type").equals("message"));
		assertTrue(hm.get("data").equals(message));
	}
	
	private void assertHmData(String data) {
		assertTrue(hm.get("type").equals("data"));
		assertTrue(hm.get("data").equals(data));
	}
	
	@Test
	public void connectionTest() {
		assertTrue(db.con != null);
	}
	
	@Test
	public void getUserTypeTest() {
		hm = db.getUserType("1");
		assertHmData("datagiver");
		
		hm = db.getUserType("1234344");
		assertHmFailure("Data is null");
		
		hm = db.getUserType("sju");
		assertHmFailure("Unknown column 'sju' in 'where clause'");
	}
	
	@Test
	public void getUsernameTest() {
		hm = db.getUsername("1");
		assertHmData("Håkon Mardal");
		
		hm = db.getUsername("1234344");
		assertHmFailure("Data is null");
		
		hm = db.getUsername("sju");
		assertHmFailure("Unknown column 'sju' in 'where clause'");
	}
	
	@Test 
	public void getPulseDataTest() {
		hm = db.getPulseData("1", "pulse", "2018-03-01 01:00:00", "2018-03-01 23:59:59");
		assertHmData("73_2018-03-01 16:40:12");
		
		hm = db.getPulseData("1234344", "pulse", "2018-03-01 01:00:00", "2018-03-01 23:59:59");
		assertHmFailure("Data is null");
		
		hm = db.getPulseData("sju", "pulse", "2018-03-01 01:00:00", "2018-03-01 23:59:59");
		assertHmFailure("Unknown column 'sju' in 'where clause'");
	}
	
	@Test
	public void addAndDeletePulseDataTest() {
		hm = db.addPulseData("1", "pulse", "44", "2018-03-13 13:56:23");
		assertHmMessage("Pulse-data added successfully");
		
		hm = db.addPulseData("sju", "pulse", "44", "2018-03-13 13:56:23");
		assertHmFailure("Incorrect integer value: 'sju' for column 'user_id' at row 1");
		
		hm = db.deletePulseData("1", "pulse", "2018-03-13 13:56:22", "2018-03-13 13:56:24");
		assertHmMessage("Pulse-data deleted successfully");
		
		hm = db.deletePulseData("sju", "pulse", "2018-03-13 00:50:00", "2018-03-01 15:00:00");
		assertHmFailure("Unknown column 'sju' in 'where clause'");
	}
	
	@Test
	public void addAndDeleteUserTest() {
		String returnedId;
		String returnedMessage = "User was added successfully with userID ";
		hm = db.addUser("datagiver", "Testmann IT");
		returnedId = hm.get("data").substring(returnedMessage.length());
		assertHmMessage(returnedMessage + returnedId);
		
		hm = db.deleteUser(returnedId);
		assertHmMessage("User was deleted successfully");
		
		hm = db.deleteUser("sju");
		assertHmFailure("Unknown column 'sju' in 'where clause'");
	}
}
