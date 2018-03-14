package tdt4140.gr1808.server;

import java.io.*;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class ClientConnect extends Thread {
	private DBQuery dbquery;
	private ServerParser parser;
	private ClientConnection connection;

	public ClientConnect(DBQuery dbquery, ServerParser parser, ClientConnection connection) {
		this.dbquery = dbquery;
		this.parser = parser;
		this.connection = connection;
	}

	@Override
	public void run() {
		try {
			JSONObject jsonData = connection.read();
			action(jsonData);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (!connection.isClosed()) {
				try {
					connection.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void action(JSONObject jsonData) {
		HashMap<String, String> parsedData = parser.decode(jsonData);
		try {
			connect(parsedData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void connect(HashMap<String, String> parsedData) throws Exception {
		HashMap<String, String> hm = new HashMap<String, String>();

		switch (parsedData.get("mode")) {
		
		case "add_user":
			hm = dbquery.addUser(parsedData.get("user_type"), parsedData.get("name"));
			break;
		
		case "delete_user":
			hm = dbquery.deleteUser(parsedData.get("user_id"));
			break;
		
		case "add_data":
			List<Touple> arrayList = parser.deStringify(parsedData.get("data"));

			for (int i = 0; i < arrayList.size(); i++) {
				hm = dbquery.addPulseData(parsedData.get("user_id"), parsedData.get("data_type"),
						arrayList.get(i).getPuls(), arrayList.get(i).getDateTime());
			}
			break;
		
		case "delete_data":
			hm = dbquery.deletePulseData(parsedData.get("user_id"), parsedData.get("data_type"),
					parsedData.get("start_datetime"), parsedData.get("end_datetime"));
			break;
		
		case "get_data":
			hm = dbquery.getPulseData(parsedData.get("user_id"), parsedData.get("data_type"),
					parsedData.get("start_datetime"), parsedData.get("end_datetime"));
			break;
		
		case "response":
			JSONObject data = parser.encode(parsedData);
			try {
				connection.write(data);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		default:
			throw new Exception(
					"This should never happen. Mode was: " + parsedData.get("mode") + parsedData.get("data"));
		}
		try {
			if (!hm.isEmpty()) {
				connection.write(parser.encode(hm));
			} 
			else {
				throw new Exception("The hashmap that was not supposed to be empty was empty...");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}