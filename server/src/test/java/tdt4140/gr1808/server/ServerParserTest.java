package tdt4140.gr1808.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.json.JSONException;
import org.json.JSONObject;

public class ServerParserTest {

	private JSONObject jsonObject;
	private ServerParser parser;
	private HashMap<String, String> hashMap;

	@Before
	public void initialize() {
		jsonObject = new JSONObject();
		parser = new ServerParser();
		hashMap = new HashMap<String, String>();
	}

	@Test
	public void tests_decode_add_user() throws JSONException {
		jsonObject.put("mode", "add_user");
		jsonObject.put("user_type", "datagiver");
		jsonObject.put("target_user_id", "1");
		jsonObject.put("name", "Peter Pukk");

		hashMap.put("mode", "add_user");
		hashMap.put("user_type", "datagiver");
		hashMap.put("target_user_id", "1");
		hashMap.put("name", "Peter Pukk");

		assertTrue(hashMap.equals(parser.decode(jsonObject)));
	}

	@Test
	public void tests_decode_delete_user() throws JSONException {
		jsonObject.put("mode", "delete_user");
		jsonObject.put("user_id", "11");
		jsonObject.put("target_user_id", "1");

		hashMap.put("mode", "delete_user");
		hashMap.put("user_id", "11");
		hashMap.put("target_user_id", "1");

		assertTrue(hashMap.equals(parser.decode(jsonObject)));
	}

	@Test
	public void tests_decode_delete_data() throws JSONException {
		jsonObject.put("mode", "delete_data");
		jsonObject.put("user_id", "11");
		jsonObject.put("target_user_id", "1");
		jsonObject.put("data_type", "puls");
		jsonObject.put("start_datetime", "1997-11-27 13:25:33");
		jsonObject.put("end_datetime", "1997-11-28 13:25:33");

		hashMap.put("mode", "delete_data");
		hashMap.put("user_id", "11");
		hashMap.put("target_user_id", "1");
		hashMap.put("data_type", "puls");
		hashMap.put("start_datetime", "1997-11-27 13:25:33");
		hashMap.put("end_datetime", "1997-11-28 13:25:33");

		assertTrue(hashMap.equals(parser.decode(jsonObject)));
	}

	@Test
	public void tests_decode_add_data() throws JSONException {
		jsonObject.put("mode", "add_data");
		jsonObject.put("user_id", "11");
		jsonObject.put("target_user_id", "1");
		jsonObject.put("data_type", "puls");
		jsonObject.put("data", "88_1997-11-27 13:25:33,88_1997-11-27 13:26:33");

		hashMap.put("mode", "add_data");
		hashMap.put("user_id", "11");
		hashMap.put("target_user_id", "1");
		hashMap.put("data_type", "puls");
		hashMap.put("data", "88_1997-11-27 13:25:33,88_1997-11-27 13:26:33");

		assertTrue(hashMap.equals(parser.decode(jsonObject)));
	}

	@Test
	public void tests_decode_get_data() throws JSONException {
		jsonObject.put("mode", "get_data");
		jsonObject.put("user_id", "11");
		jsonObject.put("target_user_id", "1");
		jsonObject.put("data_type", "puls");
		jsonObject.put("start_datetime", "1997-11-27 13:25:33");
		jsonObject.put("end_datetime", "1997-11-28 13:25:33");

		hashMap.put("mode", "get_data");
		hashMap.put("user_id", "11");
		hashMap.put("target_user_id", "1");
		hashMap.put("data_type", "puls");
		hashMap.put("start_datetime", "1997-11-27 13:25:33");
		hashMap.put("end_datetime", "1997-11-28 13:25:33");

		assertTrue(hashMap.equals(parser.decode(jsonObject)));
	}

	@Test
	public void tests_decode_empty() {
		hashMap = parser.decode(jsonObject);

		assertEquals(hashMap.get("mode"), "response");
		assertEquals(hashMap.get("type"), "error");
	}

	@Test
	public void tests_decode_wrong_key() throws JSONException {
		jsonObject.put("mode", "get_data");
		jsonObject.put("user_id", "11");
		jsonObject.put("target_user_id", "1");
		jsonObject.put("data_type", "puls");
		jsonObject.put("start_datetime", "1997-11-27 13:25:33");
		jsonObject.put("end_d77777atetime", "1997-11-28 13:25:33");

		hashMap = parser.decode(jsonObject);
		assertEquals(hashMap.get("mode"), "response");
		assertEquals(hashMap.get("type"), "error");
	}

	@Test
	public void tests_encode_message() throws JSONException {
		hashMap.put("mode", "response");
		hashMap.put("type", "message");
		hashMap.put("data", "En string mate");

		jsonObject = parser.encode(hashMap);

		assertTrue(jsonObject.get("mode").equals("response"));
		assertTrue(jsonObject.get("type").equals("message"));
		assertTrue(jsonObject.get("data").equals("En string mate"));
		assertEquals(jsonObject.length(), 3);
	}

	@Test
	public void tests_encode_message_2() throws JSONException {
		jsonObject.put("mode", "non_existing");
		jsonObject.put("type", "message");
		jsonObject.put("data", "dummy");

		hashMap = parser.decode(jsonObject);
		assertTrue(hashMap.get("mode").equals("response"));
		assertTrue(hashMap.get("type").equals("error"));
		assertTrue(hashMap.get("data").equals("There is no such mode as: non_existing"));
	}

	@Test
	public void tests_encode_error() throws JSONException {
		hashMap.put("mode", "response");
		hashMap.put("type", "error");
		hashMap.put("data", "En string mate");

		jsonObject = parser.encode(hashMap);

		assertTrue(jsonObject.get("mode").equals("response"));
		assertTrue(jsonObject.get("type").equals("error"));
		assertTrue(jsonObject.get("data").equals("En string mate"));
		assertEquals(jsonObject.length(), 3);

	}

	@Test
	public void tests_encode_data() throws JSONException {
		hashMap.put("mode", "response");
		hashMap.put("type", "data");
		hashMap.put("data", "En string mate");

		jsonObject = parser.encode(hashMap);

		assertTrue(jsonObject.get("mode").equals("response"));
		assertTrue(jsonObject.get("type").equals("data"));
		assertTrue(jsonObject.get("data").equals("En string mate"));
		assertEquals(jsonObject.length(), 3);

	}

	@Test
	public void tests_encode_empty() {
		String errorString = "";
		try {
			parser.encode(hashMap);
		}
		catch (IllegalArgumentException e) {
			errorString = e.toString();
		}

		assertTrue(errorString.equals("java.lang.IllegalArgumentException: The HashMap given as an argument is empty."));
	}
	
	@Test
	public void tests_deStringify() {
		String dataString = "88_1997-11-27 13:25:33,89_1997-11-27 13:25:34";
		List<Touple> resultList = new ArrayList<Touple>();
		resultList.add(new Touple("88", "1997-11-27 13:25:33"));
		resultList.add(new Touple("89", "1997-11-27 13:25:34"));
		
		List<Touple> toupleList = parser.deStringify(dataString);
		
		assertTrue(resultList.get(0).getPuls().equals(toupleList.get(0).getPuls()));
		assertTrue(resultList.get(0).getDateTime().equals(toupleList.get(0).getDateTime()));
		assertTrue(resultList.get(1).getPuls().equals(toupleList.get(1).getPuls()));
		assertTrue(resultList.get(1).getDateTime().equals(toupleList.get(1).getDateTime()));
	}
	
	@Test
	public void tests_deStringify_empty() {
		String dataString = "";
		List<Touple> resultList = new ArrayList<Touple>();
		List<Touple> toupleList = parser.deStringify(dataString);
		
		assertTrue(resultList.equals(toupleList));
	}
}