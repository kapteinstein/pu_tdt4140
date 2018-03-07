package tdt4140.gr1808.server;

import java.io.*;
import java.net.Socket;
import org.json.JSONObject;

import java.util.HashMap;

public class ClientConnect extends Thread {
	private DBQuery dbquery;
	private ServerParser parser;
	private BufferedReader inputStream;
	private DataOutputStream outputStream;
	private Socket connectionSocket;
	
	public ClientConnect(DBQuery dbquery,
		     ServerParser parser,
		     BufferedReader inputStream,
		     DataOutputStream outputStream,
		     Socket connectionSocket) {
	this.dbquery = dbquery;
	this.parser = parser;
	this.inputStream = inputStream;
	this.outputStream = outputStream;
	this.connectionSocket = connectionSocket;
}
	
	private void action(JSONObject jsonData) {
		int userId;
		String mode;
		String userType;

		HashMap<String, String> parsedData = parser.decode(jsonData); 
		/*
		//nesten alt dette tilhører senere issues og er derfor kommentert ut for oyeblikket siden det ikke skal testes enda.
		//skal sjekke om brukeren er av rett type til a fa lov a gjore denne operasjonen og om dataen er på rett format osv

		userId = parsedData.get("user_id");
		try {
			userType = dbquery.get_user_type(userId);
		}
		finally{
			//sende tilbakemelding om at brukeren ikke finnes
		}
		
		switch(parsedData.get("mode")) {
			case "add_user":
				//skal senere sjekke om dataene er på rett form
				connect(parsedData);	
				break;
			
			case "delete_user":
				if (userType == "datagiver"){
					//sjekke om brukeren ikke prover a slette noen andre enn seg selv
					connect(parsedData);
				} else {
					//send en melding tilbake om at denne brukeren ikke far lov a gjore dette?
					break;
				}
			
			case "add_data":
				if (userType == ""){
					connect(parsedData);
				} else {
					//send en melding tilbake om at denne brueren ikke far lov a gjore dette?
					break;
				}
			case "get_data":
				if (userType == "Tjenesteyter"){ 
					//her kan vi senere legge inn sjekker for a se om dataene er på rett form
					//Skal senere sjekke her om brukeren som vi prøver aa se dataene til har gitt tillatelse for dette!
					connect(parsedData);
				} else if (userType == "Datagiver"){ 
					//kan senere legge inn sjekker for å se om dataene er på rett form
					//Sjekke at datagiveren kun prøver å se sine egne data. Viss ikke skal de ikke ha tilgang.
					connect(parsedData);
				} break;
				
			case "response":
				if (userType == ""){
					connect(parsedData);
				} else {
					//send en melding tilbake om at denne brukeren ikke far lov a gjore dette?
					break;
				}
			default:
				//tilbakemelding om at det var ugylding mode
		}
		*/
		try {
			connect(parsedData); //denne vil bli tatt vekk nar det over blir ferdig
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void connect(HashMap<String, String> parsedData) throws Exception {
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
				dbquery.addPulseData(parsedData.get("user_id"), parsedData.get("data_type"), parsedData.get("data"), parsedData.get("time_stamp"));
				break;
			case "get_data":
				String stringData = dbquery.getPulseData(parsedData.get("user_id"), parsedData.get("data_type"),
					parsedData.get("start_datetime"), parsedData.get("end_datetime"));

				try {
					outputStream.writeUTF(stringData);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} //Legger dataen som skal til tjenesteyter ut på outputStreamen
				break;
			case "response":
				JSONObject data = parser.encode(parsedData);
				String melding = data.toString();
				try {
					outputStream.writeUTF(melding);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// legger HashMap ut på outputStream
			default:
				throw new Exception("This should never happen. Something is wrong in the ServerParser class");
		}
		//denne trenger ikke sjekke om brukeren får lov å gjøre denne operasjonen, har allerede sjekket

		//parsedData er HashMap

		//bruk parser til å stringifye dataen
		//får dataen tilbake på denne formen: array?

		//String mode = parsedData.getString("mode");
		//kall rett metode i dbquery med en for-løkke for å sende inn/hente data
	}
	
	public JSONObject inputToJson(BufferedReader input) throws IOException {
		JSONObject jsonData = new JSONObject();
		String line;
		StringBuilder sb = new StringBuilder();
		while ((line = input.readLine()) != null) {
			sb.append(line);
		}
		jsonData = new JSONObject(sb.toString());
		return jsonData;
	}

	@Override
	public void run() {	
		try{	
			JSONObject jsonData = inputToJson(inputStream);
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