package tdt4140.gr1808.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import org.json.JSONException;
import org.json.JSONObject;

public class clientConnect { 
	
	private static DBQuery dbquery;
	private static ServerParser parser;
	private static BufferedReader inputStream;
	private static DataOutputStream outputStream;
	private static Socket connectionSocket;
	
	
	public clientConnect(DBQuery dbq, Parser parser, BufferedReader inputStream, DataOutputStream outputstream, Socket connectionSocket) {
		this.dbquery = dbq;
		this.parser = parser;
		this.inputStream = inputStream;
		this.outputStream = outputstream;
		this.connectionSocket = connectionSocket;
	}
	
	
	private static void action(JSONObject jsonData) {
		int userId;
		String mode;
		
		String parsedData = parser.decode(jsonData);
		
		//skal sjekke om brukeren er av rett type til å få lov å gjøre denne operasjonen
		//userId = parsedData.getString("user_id");
		//String userType = DBQuery.get_user_type(userId)
				
		//String mode = parsedData.getString("mode");
		
		//sender dataen til metoden som skal kalle rette metoder i DBQuery
		connect(parsedData);
		
	}
	
	private static void connect(String parsedData) {
		//FORMÅL: få modus, user etc fra en bruker og utføre kommandoen. 
		//Dersom man får returnert data, skal denne sendes på outputStream (kan evt gjøres i egen metode)
		
		//denne trenger ikke sjekke om brukeren får lov å gjøre denne operasjonen, har allerede sjekket
		
		//parsedData er HashMap

		//bruk parser til å stringifye dataen
		//får dataen tilbake på denne formen: array?
		
		//String mode = parsedData.getString("mode");
		//kall rett metode i dbquery med en for-løkke for å sende inn/hente data
		
	}
	
	public static void main(String[] args) {
		
		while(true){	
			try {
				JSONObject jsonData;
				String line;
				StringBuilder sb = new StringBuilder();
				while ((line = inputStream.readLine()) != null) {
					sb.append(line);
				}
				jsonData = new JSONObject(sb.toString());
				action(jsonData);
				
			} catch(SocketException e){
				e.printStackTrace();;
			} finally{
				inputStream.close();
				connectionSocket.close();
			}
		}
	}
}