package tdt4140.gr1808.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;

import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;



public class ClientConnectTest {
	
    @Before
    public void setUp() {
        DBQuery dbQuery = mock(DBQuery.class);
        ServerParser parser = new ServerParser();
        DataOutputStream outputStream;
        ClientConnect tester = new ClientConnect(dbQuery, parser, BufferedReader inputStream, outputStream, Socket connectionSocket);

        HashMap<String, String> hashMap = new HashMap<String, String>();




    }
    @Test
    public void TestConnectAddUser() {
      public testConnectAddUser(){

      }
      


      when(parsedData.get("mode").thenReturn("get_data");
      when(dbQuery.getPulseData(parsedData.get("user_id"), parsedData.get("data_type"), parsedData.get("start_datetime"), parsedData.get("end_datetime")).thenReturn("pulseData");
      when(parser.encode(stringData).thenReturn(new JSONObject));

      tester.connect(HashMap<String,String> parsedData);

      AssertTrue(.equals(tester.connect(HashMap<String,String> parsedData)))
      tester.connect(Has)
      AssertTrue(1.equals(tester.connect



      when(dbQuery.addUser(parsedData.get("user_type"), parsedData.get("name")).thenReturn("success");
      AssertTrue("success!".equals(addUser(parsedData.get("user_type"), parsedData.get("name")));

    }
    @Test
    public void TestConnectGetData(){
      dbQuery = mock(DBQuery.class);
      when(dbQuery.getData("per").thenReturn("Flotters!"));
      assertTrue("Flotters!".equals(tester.getData("per")));
    }
    sa;
}
