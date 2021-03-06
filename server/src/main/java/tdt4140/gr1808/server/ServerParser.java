package tdt4140.gr1808.server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/*
 * This parser decodes JsonObjects into HashMaps with decode()
 * Does the opposite with encode()
 * deStringifies data from a String "puls_date time,puls_date time" to an ArrayList [Touple(String puls, String dateTime) with deStringify()
 */

public class ServerParser {

	public ServerParser() {
	}

	public HashMap<String, String> decode(JSONObject jsonObject) {
		try {
			String mode = jsonObject.getString("mode");
			switch (mode) {
				case "add_user":
					return decodeAddUser(jsonObject);
				case "delete_user":
					return decodeDeleteUser(jsonObject);
				case "delete_data":
					return decodeDeleteData(jsonObject);
				case "add_data":
					return decodeAddData(jsonObject);
				case "get_data":
					return decodeGetData(jsonObject);
				default:
					return noSuchMode(mode);
			}
		} catch (JSONException e) {
			return missingKey(e);
		}
	}

	public JSONObject encode(HashMap<String, String> hashMap) {
		String mode = hashMap.get("mode");
		String type = hashMap.get("type");

		if (hashMap.size() == 0) {
			throw new IllegalArgumentException("The HashMap given as an argument is empty.");
		}

		if (!mode.equals("response")) {
			throw new IllegalArgumentException("The server should only encode HashMaps with mode='response'.");
		}
		try {
			switch (type) {
				case "message":
					return encodeMessage(hashMap);
				case "error":
					return encodeError(hashMap);
				case "data":
					return encodeData(hashMap);
				default:
					throw new IllegalArgumentException("The server should only encode HashMaps with type='message'/'error'/'data'.");
			}
		}
		catch (JSONException e) {
			throw new IllegalArgumentException("Make sure you followed protocol, '" + e + "' was raised.");
		}


	}

	public List<Touple> deStringify(String dataString) {
		List<Touple> dataList = new ArrayList<Touple>();
		
		//Checks if the string is empty or if it does not contain "_" (that is the format is severely wrong) 
		if (dataString.length() == 0 || !dataString.contains("_")) {
			return dataList;
		}
		
		//Splits the string into datapoints on the form "puls_date time,puls_date time"
		String[] dataArray = dataString.split(",");
		
		
		
		for (int i = 0; i < dataArray.length; i++) {
			//Splits the datapoints into ["puls", "datetime"]
			String[] toupleArray = dataArray[i].split("_"); 
			
			
			Touple dataTouple = new Touple(toupleArray[0], toupleArray[1]);
			dataList.add(dataTouple);
		}
		
		return dataList;
	}

	// Helper methods for encode------------------------------------------------------------------------------------------------------------------

	private JSONObject encodeMessage(HashMap<String, String> hashMap) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		String data = hashMap.get("data");

		jsonObject.put("mode", "response");
		jsonObject.put("type", "message");
		jsonObject.put("data", data);

		return jsonObject;
	}

	private JSONObject encodeError(HashMap<String, String> hashMap) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		String data = hashMap.get("data");

		jsonObject.put("mode", "response");
		jsonObject.put("type", "error");
		jsonObject.put("data", data);

		return jsonObject;
	}

	private JSONObject encodeData(HashMap<String, String> hashMap) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		String data = hashMap.get("data");

		jsonObject.put("mode", "response");
		jsonObject.put("type", "data");
		jsonObject.put("data", data);

		return jsonObject;
	}

	// Helper methods for decode------------------------------------------------------------------------------------------------------------------

	private HashMap<String, String> decodeAddUser(JSONObject jsonObject) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		String user_type;
		String name;

		try {
			user_type = jsonObject.getString("user_type");
			name = jsonObject.getString("name");
		} catch (JSONException e) {
			return (missingKey(e));
		}
		hashMap.put("mode", "add_user");
		hashMap.put("name", name);
		hashMap.put("user_type", user_type);

		return hashMap;
	}

	private HashMap<String, String> decodeDeleteUser(JSONObject jsonObject) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		String user_id;
		String target_user_id;

		try {
			user_id = jsonObject.getString("user_id");
			target_user_id = jsonObject.getString("target_user_id");
		} catch (JSONException e) {
			return (missingKey(e));
		}
		hashMap.put("mode", "delete_user");
		hashMap.put("user_id", user_id);
		hashMap.put("target_user_id", target_user_id);
		return hashMap;
	}

	private HashMap<String, String> decodeDeleteData(JSONObject jsonObject) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		String user_id;
		String target_user_id;
		String data_type;
		String start_datetime;
		String end_datetime;

		try {
			user_id = jsonObject.getString("user_id");
			target_user_id = jsonObject.getString("target_user_id");
			data_type = jsonObject.getString("data_type");
			start_datetime = jsonObject.getString("start_datetime");
			end_datetime = jsonObject.getString("end_datetime");
		} catch (JSONException e) {
			return (missingKey(e));
		}
		hashMap.put("mode", "delete_data");
		hashMap.put("user_id", user_id);
		hashMap.put("target_user_id", target_user_id);
		hashMap.put("data_type", data_type);
		hashMap.put("start_datetime", start_datetime);
		hashMap.put("end_datetime", end_datetime);

		return hashMap;
	}

	private HashMap<String, String> decodeAddData(JSONObject jsonObject) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		String user_id;
		String target_user_id;
		String data_type;
		String data;

		try {
			user_id = jsonObject.getString("user_id");
			target_user_id = jsonObject.getString("target_user_id");
			data_type = jsonObject.getString("data_type");
			data = jsonObject.getString("data");
		} catch (JSONException e) {
			return (missingKey(e));
		}
		hashMap.put("mode", "add_data");
		hashMap.put("user_id", user_id);
		hashMap.put("target_user_id", target_user_id);
		hashMap.put("data_type", data_type);
		hashMap.put("data", data);

		return hashMap;
	}

	private HashMap<String, String> decodeGetData(JSONObject jsonObject) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		String user_id;
		String target_user_id;
		String data_type;
		String start_datetime;
		String end_datetime;

		try {
			user_id = jsonObject.getString("user_id");
			target_user_id = jsonObject.getString("target_user_id");
			data_type = jsonObject.getString("data_type");
			start_datetime = jsonObject.getString("start_datetime");
			end_datetime = jsonObject.getString("end_datetime");
		} catch (JSONException e) {
			return (missingKey(e));
		}
		hashMap.put("mode", "get_data");
		hashMap.put("user_id", user_id);
		hashMap.put("target_user_id", target_user_id);
		hashMap.put("data_type", data_type);
		hashMap.put("start_datetime", start_datetime);
		hashMap.put("end_datetime", end_datetime);

		return hashMap;

	}

	private HashMap<String, String> noSuchMode(String mode) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("mode", "response");
		hashMap.put("type", "error");
		hashMap.put("data", "There is no such mode as: " + mode);

		return hashMap;
	}

	private HashMap<String, String> missingKey(JSONException e) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("mode", "response");
		hashMap.put("type", "error");
		hashMap.put("data", e.toString());

		return hashMap;
	}
}
