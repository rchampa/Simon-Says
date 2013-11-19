package es.rczone.simonsays.tools;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import android.util.Log;


/**
 * Clase que se encarga de enviar una peticion y gestionar la respuesta 
 * devolviendo un JSON Array.
 * 
 * @author Ricardo
 *
 */
public class HttpPostConnector {
	
	//public static String IP_Server = "192.168.1.126";
	
//	/* Connections */
//	
//	public static String URL_REGISTRATION = "http://" + IP_Server + "/simon_dice/scripts/adduser.php";
//	public static String URL_LOGIN = "http://" + IP_Server + "/simon_dice/scripts/login.php";
//	public static String UPDATE_ID = "http://" + IP_Server + "/simon_dice/scripts/update_id.php";
//	public static String URL_ADD_FRIEND = "http://" + IP_Server + "/simon_dice/scripts/add_friend.php";
//	public static String URL_RESPONSE_FRIENDSHIP_REQUEST = "http://" + IP_Server + "/simon_dice/scripts/response_friendship_request.php";
//	public static String URL_REQUEST_NEW_GAME = "http://" + IP_Server + "/simon_dice/scripts/request_new_game.php";
//	public static String URL_RESPONSE_REQUEST_GAME = "http://" + IP_Server + "/simon_dice/scripts/response_request_game.php";
//	public static String URL_MAKE_A_MOVE = "http://" + IP_Server + "/simon_dice/scripts/make_a_move.php";

	//public static String IP_Server = "memorize.hol.es";
	public static String IP_Server = "gameserver-rczone.rhcloud.com";	
	/* Connections */
	
	public static String URL_REGISTRATION = "http://" + IP_Server + "/scripts/adduser.php";
	public static String URL_LOGIN = "http://" + IP_Server + "/scripts/login.php";
	public static String UPDATE_ID = "http://" + IP_Server + "/scripts/update_id.php";
	public static String URL_ADD_FRIEND = "http://" + IP_Server + "/scripts/add_friend.php";
	public static String URL_RESPONSE_FRIENDSHIP_REQUEST = "http://" + IP_Server + "/scripts/response_friendship_request.php";
	public static String URL_REQUEST_NEW_GAME = "http://" + IP_Server + "/scripts/request_new_game.php";
	public static String URL_RESPONSE_REQUEST_GAME = "http://" + IP_Server + "/scripts/response_request_game.php";
	public static String URL_MAKE_A_MOVE = "http://" + IP_Server + "/scripts/make_a_move.php";
	

	private InputStream is = null;
	private String result = "";

	public JSONArray getserverdata(ArrayList<NameValuePair> parameters,
			String urlwebserver) {

		httppostconnect(parameters, urlwebserver);

		if (is != null) {
			getpostresponse();
			return getjsonarray();
		} else
			return null;

	}

	// peticion HTTP
	private void httppostconnect(ArrayList<NameValuePair> parametros,
			String urlwebserver) {

		//
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(urlwebserver);
			httppost.setEntity(new UrlEncodedFormEntity(parametros));
			// ejecuto peticion enviando datos por POST
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
		}

	}

	private void getpostresponse() {

		// Convierte respuesta a String
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();

			result = sb.toString();
			Log.e("getpostresponse", " result= " + sb.toString());
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}
	}

	public JSONArray getjsonarray() {
		// parse json data
		try {
			JSONArray jArray = new JSONArray(result);
			return jArray;
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
			return null;
		}

	}

}
