package es.rczone.simonsays.activities;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import es.rczone.simonsays.tools.WakeLocker;

public class Register extends Activity implements ConnectionListener{
	
	private HttpPostConnector post;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);
		
		post = new HttpPostConnector();
		
		registerReceiver(mHandleMessageReceiver, new IntentFilter(GCMIntentService.DISPLAY_MESSAGE_ACTION));
	}


	public void onClick(View v) {
		
		
		setEnableEditTextFields(false);
		setEnableEditTextFields(true);
      
        switch(v.getId()){
            case R.id.button_register:
            	GCMRegistrar.register(this, GCMIntentService.SENDER_ID);
            	break;
            case R.id.button_login:
            	Intent intent = new Intent(this, ActivityLogin.class);
            	startActivity(intent);
            	break;
 
        }                 
    } 
	
	
	/**
     * Receiving push messages
     * */
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            
        	String id = intent.getExtras().getString(GCMIntentService.ID);
            if(id==null || "".equals(id.trim())){
            	id = GCMRegistrar.getRegistrationId(Register.this);
            	if("".equals(id))
            		return;
            }
            
            String nombre = ((EditText)findViewById(R.id.register_et_name)).getText().toString();
            String password = ((EditText)findViewById(R.id.register_et_pass)).getText().toString();
            String email = ((EditText)findViewById(R.id.register_et_email)).getText().toString();
                        
            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());
             
            /**
             * Take appropriate action on this message
             * depending upon your app requirement
             * For now i am just displaying it on the screen
             * */
             
            // register in game server
            new AsyncConnect(Register.this).execute(nombre, password, id, email);
                        
          
            // Releasing wake lock
            WakeLocker.release();
        }
    };
	
	private void setEnableEditTextFields(boolean b){
		
		((EditText)findViewById(R.id.register_et_name)).setFocusable(b);
		((EditText)findViewById(R.id.register_et_name)).setFocusableInTouchMode(b);
		
		((EditText)findViewById(R.id.register_et_pass)).setFocusable(b);
		((EditText)findViewById(R.id.register_et_pass)).setFocusableInTouchMode(b);
		
		((EditText)findViewById(R.id.register_et_email)).setFocusable(b);
		((EditText)findViewById(R.id.register_et_email)).setFocusableInTouchMode(b);
		
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
					SharedPreferences prefs = getSharedPreferences(GCMIntentService.PREFERENCES_FILE, Context.MODE_PRIVATE);
			        SharedPreferences.Editor editor = prefs.edit();
			        editor.putString(GCMIntentService.NAME, params[0]);
			        editor.putString(GCMIntentService.PASS, params[1]);
			        editor.putString(GCMIntentService.GCM_ID, params[2]);
			        editor.putString(GCMIntentService.EMAIL, params[3]);
			        editor.putBoolean(GCMIntentService.VALID_GCM_ID, true);
			        editor.commit();
				}
				else if("001".equals(codeFromServer)){
					//Toast.makeText(this, "El nombre de usuario ya está registrado", Toast.LENGTH_SHORT).show();
				}
				else if("-1".equals(codeFromServer)){
					//Toast.makeText(this, "El servidor esta teniendo problemas. No se ha completado el registro.", Toast.LENGTH_SHORT).show();
				}
				else{
					//Toast.makeText(this, "Error desconocido.", Toast.LENGTH_SHORT).show();
				}
			

			} catch (JSONException e) {
				//Toast.makeText(this, "Error desconocido.", Toast.LENGTH_SHORT).show();
			}
			
			
			return true;
		} else { // json obtenido invalido verificar parte WEB.
			Log.e("JSON  ", "ERROR");
			//Toast.makeText(this, "La conexión ha fallado. No se ha completado el registro.", Toast.LENGTH_SHORT).show();
			return false;
		}

	
	}


	//FIXME Check the limits(in mysql data types) of chars of each field, 30 is wrong
	@Override
	public boolean validateDataBeforeConnection(String... params) {
		String name = params[0];
		String password = params[1];
		String gcm_id = params[2]; 
		String email = params[3];
		
		if(name==null || name.trim().equals("") || name.length()>30){
			Toast.makeText(this, "El id introducido no es válido", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if(password==null || password.trim().equals("") || password.length()>30){
			Toast.makeText(this, "La contraseña introducida no es válida", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if(gcm_id==null || gcm_id.trim().equals("") || gcm_id.length()>30){
			Toast.makeText(this, "El nombre introducido no es válido", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if(email==null || email.trim().equals("") || email.length()>30){
			Toast.makeText(this, "El apellido introducido no es válido", Toast.LENGTH_SHORT).show();
			return false;
		}
					
		return true;
	}


	@Override
	public void afterGoodConnection() {
		
		Intent intent = new Intent(Register.this, MainMenu.class);
		this.startActivity(intent);
		
	}


	@Override
	public void afterErrorConnection() {
		Toast.makeText(getApplicationContext(),"Error al conectar con el servidor.",Toast.LENGTH_LONG).show();
		
	}


	@Override
	public void invalidInputData() {
		//Toast.makeText(getApplicationContext(),"Los datos introducidos.",Toast.LENGTH_LONG).show();
	}

}