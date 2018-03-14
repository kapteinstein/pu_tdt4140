package tdt4140.gr1808.server;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import java.io.*;
import java.net.Socket;
import java.util.HashMap;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.junit.Before;
import org.json.JSONObject;

public class ClientConnectTest {
		
		private DBQuery dbQuery;
		private ServerParser serverParser;
        private ClientConnect clientConnect;
        private JSONObject jsonObject;
		private ClientConnection clientConnection;
		private HashMap<String, String> hashMap;
        
	    @Before
	    public void setUp() {
	        dbQuery = mock(DBQuery.class);
	        serverParser = new ServerParser();
	        jsonObject = new JSONObject();
	        clientConnection = mock(ClientConnection.class);
	        hashMap = new HashMap<String, String>();
	    }
	    
	    @Test
	    public void connectionAddUserTest() throws IOException {
	    	jsonObject.put("mode", "add_user");
	    	jsonObject.put("user_type", "datagiver");
	    	jsonObject.put("name", "Peter Pukk");
	    	
	    	hashMap.put("message", "boner");

	    	
	    	when(clientConnection.read()).thenReturn(jsonObject);
	    	doNothing().when(clientConnection).write(any());
			when(dbQuery.addUser(isA(String.class), isA(String.class))).thenReturn(hashMap);
	        
	    	clientConnect = new ClientConnect(
	        		dbQuery, 
	        		serverParser, 
				    clientConnection);
	    	
	    	clientConnect.run();
	        
	        verify(dbQuery, times(1)).addUser("datagiver", "Peter Pukk");
	    }
	    
	    @Test
	    public void connectionDeleteUserTest() throws IOException {
	    	jsonObject.put("mode", "delete_user");
	    	jsonObject.put("user_id", "1");
	    	jsonObject.put("target_user_id", "1");
	    	
	    	hashMap.put("message", "boner");
	    	
	    	when(clientConnection.read()).thenReturn(jsonObject);
	    	doNothing().when(clientConnection).write(any());
	    	when(dbQuery.deleteUser(isA(String.class))).thenReturn(hashMap);
	        
	    	clientConnect = new ClientConnect(
	        		dbQuery, 
	        		serverParser,
	        		clientConnection);
	    	
	    	clientConnect.run();
	        
	        verify(dbQuery, times(1)).deleteUser("1");
	    }
	    
	    @Test
	    public void connectionDeleteDataTest() throws IOException {
	    	jsonObject.put("mode", "delete_data");
	    	jsonObject.put("user_id", "1");
	    	jsonObject.put("target_user_id", "1");
	    	jsonObject.put("data_type", "puls");
	    	jsonObject.put("start_datetime", "nu");
	    	jsonObject.put("end_datetime", "senere");
	    	
	    	hashMap.put("message", "boner");
	    	
	    	when(clientConnection.read()).thenReturn(jsonObject);
	    	doNothing().when(clientConnection).write(any());
	    	when(dbQuery.deletePulseData(isA(String.class), isA(String.class), isA(String.class), isA(String.class))).thenReturn(hashMap);
	        
	    	clientConnect = new ClientConnect(
	        		dbQuery, 
	        		serverParser, 
	        		clientConnection);
	    	
	    	clientConnect.run();

	    	
	    	
	        verify(dbQuery, times(1)).deletePulseData("1", "puls", "nu", "senere");
	    }
	    
	    @Test
	    public void connectionAddPulseDataTest() throws IOException {
	    	jsonObject.put("mode", "add_data");
	    	jsonObject.put("user_id", "1");
	    	jsonObject.put("target_user_id", "1");
	    	jsonObject.put("data_type", "puls");
	    	jsonObject.put("data", "88_1997-11-27 13:25:33");
	    	
	    	hashMap.put("message", "boner");
	    		
	    	when(clientConnection.read()).thenReturn(jsonObject);
	    	doNothing().when(clientConnection).write(any());
	    	when(dbQuery.addPulseData(isA(String.class), isA(String.class), isA(String.class), isA(String.class))).thenReturn(hashMap);
	        
	    	clientConnect = new ClientConnect(
	        		dbQuery, 
	        		serverParser, 
	        		clientConnection);
	    	
	    		clientConnect.run();

	        verify(dbQuery, times(1)).addPulseData("1", "puls", "88", "1997-11-27 13:25:33");
	    }
	    
	    @Test
	    public void connectionGetPulseDataTest() throws IOException {
	    	jsonObject.put("mode", "get_data");
	    	jsonObject.put("user_id", "1");
	    	jsonObject.put("data_type", "puls");
	    	jsonObject.put("target_user_id", "1");
	    	jsonObject.put("start_datetime", "nu");
	    	jsonObject.put("end_datetime", "senere");
	    	
	    	hashMap.put("message", "boner");

	    	when(clientConnection.read()).thenReturn(jsonObject);
	    	doNothing().when(clientConnection).write(any());
			when(dbQuery.getPulseData("1", "puls", "nu", "senere")).thenReturn(hashMap);
	        
	    	clientConnect = new ClientConnect(
	        		dbQuery, 
	        		serverParser, 
	        		clientConnection);
	    	
	    	clientConnect.run();

	    	verify(dbQuery, times(1)).getPulseData("1", "puls", "nu", "senere");
	    }
	    /*
	    @Test
	    public void connectionResponseTest() throws IOException {
	    	jsonObject.put("mode", "response");
	    	jsonObject.put("type", "error");
	    	jsonObject.put("data", "Du er en baijs");
	    		
			byteArray = new ByteArrayInputStream(jsonObject.toString().getBytes());
			inputStream = new BufferedReader(new InputStreamReader(byteArray));
	    	
	    	doNothing().when(outputStream).writeUTF(jsonObject.toString());
	    	
	    	clientConnect = new ClientConnect(
	        		dbQuery, 
	        		serverParser, 
	        		inputStream,
				    outputStream,
				    connectionSocket);
	    	
	    	clientConnect.run();

	    	verify(outputStream, times(1)).writeUTF(jsonObject.toString());
	    }*/
}
