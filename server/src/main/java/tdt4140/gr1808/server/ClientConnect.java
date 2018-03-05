package tdt4140.gr1808.server;

import java.io.*;
import java.net.Socket;
import org.json.JSONObject;

import java.util.HashMap;

public class ClientConnect implements Runnable {

	private DBQuery dbquery;
	private ServerParser parser;
	private BufferedReader inputStream;
	private DataOutputStream outputStream;
	private Socket connectionSocket;


	public ClientConnect(DBQuery dbquery,
			     ServerParser parser,
			     InputStream inputStream,
			     OutputStream outputStream,
			     Socket connectionSocket) {
		this.dbquery = dbquery;
		this.parser = parser;
		this.inputStream = new BufferedReader(new InputStreamReader(inputStream));
		this.outputStream = new DataOutputStream(outputStream);
		this.connectionSocket = connectionSocket;
	}


	private void action(JSONObject jsonData) {
		int userId;
		String mode;

		HashMap<String, String> parsedData = parser.decode(jsonData); //skal være HashMap<String, String>

		//skal sjekke om brukeren er av rett type til å få lov å gjøre denne operasjonen
		//userId = parsedData.get("user_id");
		//String userType = dbquery.get_user_type(userId)

		//String mode = parsedData.get("mode");

		//sender dataen til metoden som skal kalle rette metoder i DBQuery
		connect(parsedData);
	}

	private void connect(HashMap<String, String> parsedData) {
		//FORMÅL: få modus, user etc fra en bruker og utføre kommandoen.
		//Dersom man får returnert data, skal denne sendes på outputStream (kan evt gjøres i egen metode)
		// DBQuery query = new DBQuery();  // ikke ha med


		switch(parsedData.get("mode")) {
			case "add_user":
				dbquery.addUser(parsedData.get("user_type"), parsedData.get("name"));
				break;
			case "delete_user":
				dbquery.deleteUser(parsedData.get("user_id"));
				break;
			case "add_data":
				dbquery.addPulseData(parsedData.get("user_id"), parsedData.get("data_type"), parsedData.get("data"), /*TODO parsedData.get("time_stamp") is this needed?*/)
				break;
			case "get_data":
				String data = dbquery.getPulseData(parsedData.get("user_id"), parsedData.get("data_type"), parsedData.get("start_datetime"), parsedData.get("end_datetime"));
				outputStream.writeUTF(data); //Håkon sin oppgave
				break;
			case "response":
				// maa encodes forst. outputStream.writeUTF(parsedData.get("data"));
			default: //ERROR CRAZY????
		}
		//denne trenger ikke sjekke om brukeren får lov å gjøre denne operasjonen, har allerede sjekket

		//parsedData er HashMap

		//bruk parser til å stringifye dataen
		//får dataen tilbake på denne formen: array?

		//String mode = parsedData.getString("mode");
		//kall rett metode i dbquery med en for-løkke for å sende inn/hente data
	}

	@Override
	public void run() {
		try {
			JSONObject jsonData;
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = inputStream.readLine()) != null) {
				sb.append(line);
			}
			jsonData = new JSONObject(sb.toString());
			action(jsonData);

		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			if (!connectionSocket.isClosed()) {
				try {
					connectionSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
