package waldo.waldowebservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import waldo.exceptions.WaldoException;
import waldo.model.Waldo;
import waldo.util.LatLon;


public class WaldoService {

	private final static String WALDO_WEB_SERVICE_URL = "http://kramer.nss.cs.ubc.ca:8080/";

	// waldos
	private List<Waldo> waldos;
	// messages
	private List<String> messages;
	//waldo name
	String wName;
	// keys
	String key;

	/**
	 * Constructor
	 */
	public WaldoService() {
		waldos = new ArrayList<Waldo> ();
		messages = new ArrayList<String> ();
	}

	/**
	 * Initialize a session with the Waldo web service. The session can time out
	 * even while the app is active...
	 * 
	 * @param nameToUse
	 *            The name to go register, can be null if you want Waldo to
	 *            generate a name
	 * @return The name that Waldo gave you
	 */
	public String initSession(String nameToUse) {
		// create URL
		StringBuilder urlBuilder = new StringBuilder(WALDO_WEB_SERVICE_URL);
		urlBuilder.append("initsession/");
		if (nameToUse != null) {
			urlBuilder.append(nameToUse); } 
		else { urlBuilder.append(""); }
		System.out.println("Tunjay URL = " + urlBuilder);

		InputStream in = null;
		try {
			//Make request and return Waldo name
			String myName = makeJSONQuery(urlBuilder);
			return parseSessionFromJSON(myName);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close(); 
			} catch (IOException ioe) {
				throw new WaldoException("initSession: Unable to read the return from http request");
			}
		}
		return null;
	}

	/**
	 * Parse the data returned from the waldo web service
	 * 
	 * @param input
	 *        the input for the JSON
	 * @return The name that Waldo gave you
	 */
	public String parseSessionFromJSON(String input) {
		JSONObject obj;	
		try {
			obj = (JSONObject) new JSONTokener(input).nextValue();
			//Check if the json object contains an error
			if (obj != null) {
				if (obj.has("ErrorNumber")) {
					throw new WaldoException("initSession error");
				} else {
					//retrieve the key and waldo name
					String akey = obj.getString("Key").trim();
					String loc = obj.getString("Loc").trim();
					String name = obj.getString("Name").trim();
					key = akey;
					wName = name;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return wName;
	}


	/**
	 * Get waldos from the Waldo web service.
	 * 
	 * @param numberToGenerate
	 *            The number of Waldos to try to retrieve
	 * @return Waldo objects based on information returned from the Waldo web
	 *         service
	 */
	public List<Waldo> getRandomWaldos(int numberToGenerate) {
		// Format the request  string
		StringBuilder urlBuilder = new StringBuilder(WALDO_WEB_SERVICE_URL);
		urlBuilder.append("getwaldos/");
		urlBuilder.append(key + "/");
		urlBuilder.append(numberToGenerate);
		System.out.println("Tunjay URL = " + urlBuilder);
		// Make the request and get waldos
		InputStream input = null;
		try {
			String w = makeJSONQuery(urlBuilder);
			return parseWaldosFromJSON(w);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (input != null)
					input.close();
			} catch (IOException ioe) {
				throw new WaldoException(
						"getRandomWaldos: Unable read return from http request.");
			}
		}
		return waldos;
	} 

	/**
	 * Parse the waldo information from the Waldo web service.
	 * @param input
	 *        the input for the JSON
	 * @return list of parsed waldos from waldo web service
	 */
	private List<Waldo> parseWaldosFromJSON(String input) {
		//Initialize
		List<Waldo> waldos = new ArrayList<Waldo>();
		JSONArray array;
		try {
			// parse each waldo object
			array = (JSONArray) new JSONTokener(input).nextValue();
			if (array != null) {
				for (int i = 0; i < array.length(); i++) {
					// retrieve the waldo name, location w/ latitude, longitude, time stamp
					JSONObject waldo = array.getJSONObject(i);
					String waldoName = waldo.getString("Name").trim();
					JSONObject location = (JSONObject) waldo.getJSONObject("Loc");
					double latitude = location.getDouble("Lat");
					double longitude = location.getDouble("Long");
					int timeStamp = location.getInt("Tstamp");
					// Create waldo and add it to the list
					LatLon lastLocation = new LatLon(latitude, longitude);
					Date lastUpdated = new Date(timeStamp);
					Waldo waldo1 = new Waldo(waldoName, lastUpdated, lastLocation); 
					waldos.add(waldo1);   
				}
			}
			// Probably there was an error returned. Just let developer know stack trace
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//Debugging
		System.out.println("Tunjay waldos = " + waldos);
		return waldos;
	}

	/**
	 * Return the current list of Waldos that have been retrieved
	 * 
	 * @return The current Waldos
	 */
	public List<Waldo> getWaldos() {
		return waldos;
	}

	/**
	 * Make URL request and retrieve messages
	 * available for the user from the Waldo web service
	 * 
	 * @return A list of messages
	 */
	public List<String> getMessages() {
		//Create URL request string and make request
		StringBuilder urlBuilder = new StringBuilder(WALDO_WEB_SERVICE_URL);
		urlBuilder.append("getmsgs/");
		urlBuilder.append(key + "/");
		System.out.println("Tunjay URL = " + urlBuilder);
		InputStream input = null;
		try {
			String m = makeJSONQuery(urlBuilder);
			return parseMessagesFromJSON(m);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (input != null)
					input.close();
			} catch (IOException ioe) {
				throw new WaldoException(
						"getMessages: Unable read return from http request.");
			}
		}
		return new ArrayList<String>();
	} 

	/**
	 * Parse messages available for the user from 
	 * the Waldo web service and retrieve them
	 * 
	 * @return A list of messages
	 */
	private List<String> parseMessagesFromJSON(String m) {
		// Initialize
		List<String> messages = new ArrayList<String>();
		JSONObject obj;
		try {
			obj = (JSONObject) (new JSONTokener(m).nextValue());
			if (obj != null) {
				// Check if there is an error
				if (obj.has("ErrorNumber")) {
					throw new WaldoException("getMessages error");
				} else {
					JSONArray msgs = (JSONArray) obj.getJSONArray("Messages");
					for (int j = 0; j < msgs.length(); j++) {
						JSONObject message1 = msgs.getJSONObject(j);
						String name = message1.getString("Name").trim();
						String msg = message1.getString("Message").trim();
						//remove the previous messages from the web service
						messages.clear();
						// Add parsed out name and message to the list of messages
						messages.add(name);
						messages.add(msg);
					}
				}	
			}
		} catch (JSONException e) {
			// Probably there was an error returned. Just let developer know stack trace
			e.printStackTrace();
		}
		//Debugging
		System.out.println("Tunjay Messages = " + messages);
		return messages;
	}

	/**
	 * Execute a given query 
	 * 
	 * @param urlBuilder The query with everything but http:
	 * @return The JSON returned from the query 
	 */
	private String makeJSONQuery(StringBuilder urlBuilder) {
		try {
			URL url = new URL(urlBuilder.toString());
			HttpURLConnection client = (HttpURLConnection) url.openConnection();
			client.setRequestProperty("accept", "application/json");
			InputStream in = client.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String returnString = br.readLine();
			client.disconnect();
			return returnString;
		} catch (Exception e) {
			throw new WaldoException("Unable to make JSON query: " + urlBuilder.toString());
		}
	} 
}
