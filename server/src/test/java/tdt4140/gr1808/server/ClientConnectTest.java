package tdt4140.gr1808.server;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.json.JSONException;
import org.json.JSONObject;

public class ClientConnectTest {
	@InjectMocks
	ClientConnect c1,c2;
	
	@Mock
	DBQuery q;
	ServerParser p;
	BufferedReader b1,b2;
	Socket s;
	OutputStream d;

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

}
