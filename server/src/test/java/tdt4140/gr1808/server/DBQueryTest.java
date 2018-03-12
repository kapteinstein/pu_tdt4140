package tdt4140.gr1808.server;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import static org.mockito.Mockito.*;

public class DBQueryTest {
	ConnectionMockDBQuery dBQuery;
	static Statement statement;
	static ResultSet resultSet;
	Connection con;
	
	//Overrides the connection() method in DBQuery to mock the connection to the database
	private static class ConnectionMockDBQuery extends DBQuery {
	    @Override
		public Connection connect() {
	        try {

	        	con = mock(Connection.class);
	        	statement = mock(Statement.class);
	        	resultSet = mock(ResultSet.class);
				when(con.createStatement()).thenReturn(statement);
				when(statement.executeUpdate(anyString())).thenReturn(1);
				when(statement.executeQuery(anyString())).thenReturn(resultSet);
				when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
				when(resultSet.getInt(1)).thenReturn(69);
				when(resultSet.getString(1)).thenReturn("username/type");
				when(resultSet.getString(2)).thenReturn("timestamp");
			} catch (SQLException e) {
				System.out.println("??????????????????????");
			}
	        return con;
	    }
	}
	
	@Before
	public void setUp() {
		dBQuery = new ConnectionMockDBQuery();
	}

	@Test
	public void addUserTest() throws SQLException {
		dBQuery.addUser("datagiver", "Pål Pot");
		verify(statement, times(1)).executeUpdate("insert into user(user_type, name) values('datagiver', 'Pål Pot')");
	}
	
	@Test
	public void deleteUserTest() throws SQLException {
		dBQuery.deleteUser("11");
		verify(statement, times(1)).executeUpdate("delete from user where user_id = 11");
	}
	
	@Test
	public void deletePulseData() throws SQLException {
		dBQuery.deletePulseData("11", "puls", "nu", "senere");
		verify(statement, times(1)).executeUpdate("delete from data_super where user_id = 11 and time_stamp between \"nu\" and \"senere\"");
	}
	
	@Test
	public void addPulseDataTest() throws SQLException {
		dBQuery.addPulseData("13", "puls", "9000", "17.30.17 09:45:10");
		InOrder inOrder = inOrder(statement);
		inOrder.verify(statement).executeUpdate("insert into data_super(user_id, time_stamp) values('13', '17.30.17 09:45:10')");
		inOrder.verify(statement).executeUpdate("insert into pulse_data(pulse) values('9000')");
	}
	
	@Test
	public void getPulseDataTest() throws SQLException {
		String pulseDataString = dBQuery.getPulseData("99", "puls", "nu", "senere");
		assertTrue(pulseDataString.equals("69_timestamp,69_timestamp"));
	}
	
	@Test
	public void getUserNameStringTest() {
		String userTypeString = dBQuery.getUsername("1");
		assertTrue(userTypeString.equals("username/type"));
	}
	
	@Test
	public void getUserTypeTest() {
		String userTypeString = dBQuery.getUserType("12");
		System.out.println(userTypeString);
		assertTrue(userTypeString.equals("username/type"));
	}
}
