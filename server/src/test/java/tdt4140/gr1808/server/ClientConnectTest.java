package tdt4140.gr1808.server;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import java.io.*;
import java.net.Socket;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.junit.Before;
import org.json.JSONObject;

public class ClientConnectTest {
	@InjectMocks
	ClientConnect c1,c2;
	
	@Mock
	DBQuery q;
	ServerParser p;
	BufferedReader b1,b2;
	Socket s;
	DataOutputStream d;

		@Test
		public void inputToJsonTest() throws IOException{
			Exception error;
			String s1, s2;
			String test1, test2;
			String testString1,testString2;
			InputStream inputStream1, inputStream2;
			
			
			//first			
			test1 = new JSONObject().put("mode", "delete_user").toString();
			inputStream1 = new ByteArrayInputStream(test1.getBytes());
			b1 = new BufferedReader(new InputStreamReader(inputStream1));
			c1 = new ClientConnect(q,p,b1,d,s);

			testString1 = new JSONObject().put("mode", "delete_user").toString();
			s1 = c1.inputToJson(b1).toString();
			
			assertEquals(testString1,s1);
			
			
			//second
			test2 = "illegal argument";
			inputStream2 = new ByteArrayInputStream(test2.getBytes());
			b2 = new BufferedReader(new InputStreamReader(inputStream2));
			c2 = new ClientConnect(q,p,b2,d,s);

			testString2 = "org.json.JSONException: A JSONObject text must begin with '{' at 1 [character 2 line 1]";
			try{
				s2 = c1.inputToJson(b2).toString();		
			}catch (Exception e) {
				error = e;	
				s2 = error.toString();
			}
			assertEquals(testString2,s2);
	}
		
		private DBQuery dbQuery;
		private ServerParser serverParser;
		private BufferedReader inputStream;
		private DataOutputStream outputStream;
		private Socket connectionSocket;
        ClientConnect clientConnect;
        JSONObject jsonObject;
        ByteArrayInputStream byteArray;
		
	    @Before
	    public void setUp() {
	        dbQuery = mock(DBQuery.class);
	        serverParser = new ServerParser();
	        outputStream = mock(DataOutputStream.class);
	        connectionSocket = mock(Socket.class);
	        jsonObject = new JSONObject();
	    }
	    
	    @Test
	    public void connectionAddUserTest() throws IOException {
	    	jsonObject.put("mode", "add_user");
	    	jsonObject.put("user_type", "datagiver");
	    	jsonObject.put("name", "Peter Pukk");
	    	
			byteArray = new ByteArrayInputStream(jsonObject.toString().getBytes());
			inputStream = new BufferedReader(new InputStreamReader(byteArray));
	    	
	    	doNothing().when(dbQuery).addUser(isA(String.class), isA(String.class));
	        
	    	clientConnect = new ClientConnect(
	        		dbQuery, 
	        		serverParser, 
	        		inputStream,
				    outputStream,
				    connectionSocket);
	    	
	    	clientConnect.run();
	        
	        verify(dbQuery, times(1)).addUser("datagiver", "Peter Pukk");
	    }
	    
	    @Test
	    public void connectionDeleteUserTest() throws IOException {
	    	jsonObject.put("mode", "delete_user");
	    	jsonObject.put("user_id", "1");
	    	jsonObject.put("target_user_id", "1");

	    	
			byteArray = new ByteArrayInputStream(jsonObject.toString().getBytes());
			inputStream = new BufferedReader(new InputStreamReader(byteArray));
	    	
	    	doNothing().when(dbQuery).deleteUser(isA(String.class));
	        
	    	clientConnect = new ClientConnect(
	        		dbQuery, 
	        		serverParser, 
	        		inputStream,
				    outputStream,
				    connectionSocket);
	    	
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
	    	

	    	
			byteArray = new ByteArrayInputStream(jsonObject.toString().getBytes());
			inputStream = new BufferedReader(new InputStreamReader(byteArray));
	    	
	    	doNothing().when(dbQuery).deletePulseData(isA(String.class), isA(String.class), isA(String.class), isA(String.class));
	        
	    	clientConnect = new ClientConnect(
	        		dbQuery, 
	        		serverParser, 
	        		inputStream,
				    outputStream,
				    connectionSocket);
	    	
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
	    		
			byteArray = new ByteArrayInputStream(jsonObject.toString().getBytes());
			inputStream = new BufferedReader(new InputStreamReader(byteArray));
	    	
	    	doNothing().when(dbQuery).addPulseData(isA(String.class), isA(String.class), isA(String.class), isA(String.class));
	        
	    	clientConnect = new ClientConnect(
	        		dbQuery, 
	        		serverParser, 
	        		inputStream,
				    outputStream,
				    connectionSocket);
	    	
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
	    		
			byteArray = new ByteArrayInputStream(jsonObject.toString().getBytes());
			inputStream = new BufferedReader(new InputStreamReader(byteArray));
	    	
	    	when(dbQuery.getPulseData("1", "puls", "nu", "senere")).thenReturn("ok");
	        
	    	clientConnect = new ClientConnect(
	        		dbQuery, 
	        		serverParser, 
	        		inputStream,
				    outputStream,
				    connectionSocket);
	    	
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
