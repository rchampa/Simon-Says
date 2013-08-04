package es.rczone.simonsays.activities;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

import es.rczone.simonsays.GCMIntentService;
import es.rczone.simonsays.R;
import es.rczone.simonsays.tools.AsyncConnect;
import es.rczone.simonsays.tools.ConnectionListener;
import es.rczone.simonsays.tools.HttpPostConnector;

public class Login extends Activity implements ConnectionListener{
	
	private HttpPostConnector post;
	private AsyncConnect connection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		
		post = new HttpPostConnector();
			
	}


	public void onClick(View v) {
		
		
		setEnableEditTextFields(false);
		setEnableEditTextFields(true);
		String name = ((EditText)findViewById(R.id.login_et_name)).getText().toString();
		String pass = ((EditText)findViewById(R.id.login_et_password)).getText().toString();
      
        switch(v.getId()){
            case R.id.login_button_login:
            	// register in game server
            	connection = new AsyncConnect(this);
            	connection.execute(name, pass);
            	break;
            case R.id.login_button_forget:
            	Toast.makeText(this, "This function is disable yet.", Toast.LENGTH_SHORT).show();
            	break;
 
        }                 
    }
	
	private void setEnableEditTextFields(boolean b){
		
		((EditText)findViewById(R.id.login_et_name)).setFocusable(b);
		((EditText)findViewById(R.id.login_et_name)).setFocusableInTouchMode(b);
		
		((EditText)findViewById(R.id.login_et_password)).setFocusable(b);
		((EditText)findViewById(R.id.login_et_password)).setFocusableInTouchMode(b);

		
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
					SharedPreferences prefs = getSharedPreferences(GCMIntentService.PREFERENCES_FILE, Context.MODE_PRIVATE);
			        SharedPreferences.Editor editor = prefs.edit();
			        editor.putString(GCMIntentService.NAME, params[0]);
			        editor.commit();
			        connection.attachMessage(json_data.getString("message"));
					GCMRegistrar.register(this, GCMIntentService.SENDER_ID);
					return true;
				}
				else{
					connection.attachMessage(json_data.getString("message"));
					return false;
				}
				
			} catch (JSONException e) {
				connection.attachMessage("There was a problem with internet connection");
				e.printStackTrace();
			}

			return true;
		} else { // json obtenido invalido verificar parte WEB.
			Log.e("JSON  ", "ERROR");
			connection.attachMessage("There was a problem with internet connection");
			return false;
		}

	
	
	}

	@Override
	public boolean validateDataBeforeConnection(String... params) {
		String name = params[0];
		String password = params[1];
		
		if(name==null || name.trim().equals("") || name.length()>30){
			connection.attachMessage("The username is invalid.");
			return false;
		}
		
		if(password==null || password.trim().equals("") || password.length()>30){
			connection.attachMessage("The password is invalid.");
			return false;
		}
		
					
		return true;
	}


	@Override
	public void afterGoodConnection() {
		
    	Toast.makeText(this,"Sesión iniciada.",Toast.LENGTH_LONG).show();

		Intent intent = new Intent(this, MainMenu.class);
		this.startActivity(intent);
		
	}

	@Override
	public void afterErrorConnection() {
		Toast.makeText(this,connection.getMessage(),Toast.LENGTH_LONG).show();
	}

	@Override
	public void invalidInputData() {
		Toast.makeText(this,connection.getMessage(),Toast.LENGTH_LONG).show();
	}
}
