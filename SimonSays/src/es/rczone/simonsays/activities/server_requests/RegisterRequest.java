package es.rczone.simonsays.activities.server_requests;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import es.rczone.simonsays.GCMIntentService;
import es.rczone.simonsays.activities.MainMenu;
import es.rczone.simonsays.tools.ConnectionListener;
import es.rczone.simonsays.tools.HttpPostConnector;

public class RegisterRequest implements ConnectionListener{
	
	private HttpPostConnector post;
	private Context context;
	
	private String mensaje;

	public RegisterRequest(Context context) {
		this.context = context;
		post = new HttpPostConnector();
	}

	@Override
	public boolean inBackground(String... params) {
		
		ArrayList<NameValuePair> postParametersToSend = new ArrayList<NameValuePair>();

		postParametersToSend.add(new BasicNameValuePair("name", params[0]));
		postParametersToSend.add(new BasicNameValuePair("password", params[1]));
		postParametersToSend.add(new BasicNameValuePair("gcm_id", params[2]));
		postParametersToSend.add(new BasicNameValuePair("email", params[3]));

		// realizamos una peticion y como respuesta obtenes un array JSON
		JSONArray jdata = post.getserverdata(postParametersToSend, HttpPostConnector.URL_REGISTRATION);


		// si lo que obtuvimos no es null, es decir, hay respuesta válida
		if (jdata != null && jdata.length() > 0) {

			try {
				JSONObject json_data = jdata.getJSONObject(0);
				String codeFromServer = json_data.getString("code");
				//String messageFromServer = json_data.getString("message");
				
				if("000".equals(codeFromServer)){
					//Toast.makeText(this, "Registro completado con éxito.", Toast.LENGTH_SHORT).show();
					SharedPreferences prefs = this.context.getSharedPreferences(GCMIntentService.PREFERENCES_FILE, Context.MODE_PRIVATE);
			        SharedPreferences.Editor editor = prefs.edit();
			        editor.putString(GCMIntentService.NAME, params[0]);
			        editor.putString(GCMIntentService.PASS, params[1]);
			        editor.putString(GCMIntentService.GCM_ID, params[2]);
			        editor.putString(GCMIntentService.EMAIL, params[3]);
			        editor.putBoolean(GCMIntentService.VALID_GCM_ID, true);
			        editor.commit();
			        mensaje = json_data.getString("message");
			        return true;
				}
				else if("001".equals(codeFromServer)){
					mensaje = json_data.getString("message");
					return false;
				}
				else if("-1".equals(codeFromServer)){
					//Toast.makeText(this, "El servidor esta teniendo problemas. No se ha completado el registro.", Toast.LENGTH_SHORT).show();
				}
				else{
					//Toast.makeText(this, "Error desconocido.", Toast.LENGTH_SHORT).show();
				}
			

			} catch (JSONException e) {
				mensaje = "There was a problem with internet connection";
				return false;
			}
			
			
			return true;
		} else { // json obtenido invalido verificar parte WEB.
			Log.e("JSON  ", "ERROR");
			mensaje = "There was a problem with internet connection";
			return false;
		}

	
	}


	@Override
	public boolean validateDataBeforeConnection(String... params) {
		
		String name = params[0];
		String password = params[1]; 
		String email = params[3];
		
		if(name==null || name.trim().equals("") || name.length()>25){
			Toast.makeText(this.context, "The username is invalid.", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if(password==null || password.trim().equals("") || password.length()>25){
			Toast.makeText(this.context, "The password is invalid.", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		
		if(email==null || email.trim().equals("") || email.length()>50){
			Toast.makeText(this.context, "The email is invalid.", Toast.LENGTH_SHORT).show();
			return false;
		}
					
		return true;
	}


	@Override
	public void afterGoodConnection() {
		
		Intent intent = new Intent(this.context, MainMenu.class);
		this.context.startActivity(intent);
		
	}


	@Override
	public void afterErrorConnection() {
		Toast.makeText(this.context,mensaje,Toast.LENGTH_LONG).show();
	}


	@Override
	public void invalidInputData() {
		Toast.makeText(this.context,mensaje,Toast.LENGTH_LONG).show();
	}

	@Override
	public Context getContext() {
		return this.context;
	}
	

}
