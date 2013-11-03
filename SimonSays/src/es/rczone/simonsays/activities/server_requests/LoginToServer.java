package es.rczone.simonsays.activities.server_requests;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gcm.GCMRegistrar;

import es.rczone.simonsays.GCMIntentService;
import es.rczone.simonsays.activities.MainMenu;
import es.rczone.simonsays.tools.ConnectionListener;
import es.rczone.simonsays.tools.HttpPostConnector;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

public class LoginToServer implements ConnectionListener{
	
	private HttpPostConnector post;
	private Context context;

	public LoginToServer(Context context) {
		this.context = context;
		post = new HttpPostConnector();
	}

	@Override
	public boolean inBackground(String... params) {
		/*
		 * Creamos un ArrayList del tipo nombre valor para agregar los datos
		 * recibidos por los parametros anteriores y enviarlo mediante POST a
		 * nuestro sistema para relizar la validacion
		 */
		ArrayList<NameValuePair> postParametersToSend = new ArrayList<NameValuePair>();

		postParametersToSend.add(new BasicNameValuePair("name", params[0]));
		postParametersToSend.add(new BasicNameValuePair("password", params[1]));

		// realizamos una peticion y como respuesta obtenes un array JSON
		JSONArray jdata = post.getserverdata(postParametersToSend, HttpPostConnector.URL_LOGIN);


		// si lo que obtuvimos no es null, es decir, hay respuesta válida
		if (jdata != null && jdata.length() > 0) {

			try {
				JSONObject json_data = jdata.getJSONObject(0);
				String codeFromServer = json_data.getString("code");
				//String messageFromServer = json_data.getString("message");
				
				if(codeFromServer.equals("500")){
					SharedPreferences prefs = this.context.getSharedPreferences(GCMIntentService.PREFERENCES_FILE, Context.MODE_PRIVATE);
			        SharedPreferences.Editor editor = prefs.edit();
			        editor.putString(GCMIntentService.NAME, params[0]);
			        editor.commit();
			        //connection.attachMessage(json_data.getString("message"));
					GCMRegistrar.register(context, GCMIntentService.SENDER_ID);
					return true;
				}
				else{
					//connection.attachMessage(json_data.getString("message"));
					return false;
				}
				
			} catch (JSONException e) {
				//connection.attachMessage("There was a problem with internet connection");
				e.printStackTrace();
			}

			return true;
		} else { // json obtenido invalido verificar parte WEB.
			Log.e("JSON  ", "ERROR");
			//connection.attachMessage("There was a problem with internet connection");
			return false;
		}
	}

	@Override
	public boolean validateDataBeforeConnection(String... params) {
		String name = params[0];
		String password = params[1];
		
		if(name==null || name.trim().equals("") || name.length()>30){
			//Toast...connection.attachMessage("The username is invalid.");
			return false;
		}
		
		if(password==null || password.trim().equals("") || password.length()>30){
			//connection.attachMessage("The password is invalid.");
			return false;
		}
		
					
		return true;
	}

	@Override
	public void afterGoodConnection() {
		
    	Toast.makeText(context,"Sesión iniciada.",Toast.LENGTH_LONG).show();

		Intent intent = new Intent(context, MainMenu.class);
		context.startActivity(intent);
		
	}

	@Override
	public void afterErrorConnection() {
		//Toast.makeText(this,connection.getMessage(),Toast.LENGTH_LONG).show();
	}

	@Override
	public void invalidInputData() {
		//Toast.makeText(this,connection.getMessage(),Toast.LENGTH_LONG).show();
	}

}
