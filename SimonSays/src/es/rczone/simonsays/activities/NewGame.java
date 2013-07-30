package es.rczone.simonsays.activities;


import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;
import es.rczone.simonsays.GCMIntentService;
import es.rczone.simonsays.R;
import es.rczone.simonsays.activities.fragments.FragmentFriendsList;
import es.rczone.simonsays.activities.fragments.ItemClickedListener;
import es.rczone.simonsays.model.Friend;
import es.rczone.simonsays.tools.AsyncConnect;
import es.rczone.simonsays.tools.ConnectionListener;
import es.rczone.simonsays.tools.HttpPostConnector;

public class NewGame extends FragmentActivity implements ItemClickedListener<Friend>, ConnectionListener {

	
	private FragmentFriendsList frgListado;
	private HttpPostConnector post;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_activity_new_game);
		
		frgListado =(FragmentFriendsList)getSupportFragmentManager().findFragmentById(R.id.frg_friend_list);
		frgListado.setListener(this);
		post = new HttpPostConnector();
	}

	@Override
	public void onItemClicked(Friend friend) {
		
		SharedPreferences prefs = getSharedPreferences(GCMIntentService.PREFERENCES_FILE, Context.MODE_PRIVATE);
    	String name = prefs.getString(GCMIntentService.NAME, "");
    	
    	new AsyncConnect(this).execute(name, friend.getUserName());
		
	}

	@Override
	public boolean inBackground(String... params) {
		ArrayList<NameValuePair> postParametersToSend = new ArrayList<NameValuePair>();
		
		

		postParametersToSend.add(new BasicNameValuePair("player1_name", params[0]));
		postParametersToSend.add(new BasicNameValuePair("player2_name", params[1]));
		

		// realizamos una peticion y como respuesta obtenes un array JSON
		JSONArray jdata = post.getserverdata(postParametersToSend, HttpPostConnector.URL_REQUEST_NEW_GAME);


		// si lo que obtuvimos no es null, es decir, hay respuesta válida
		if (jdata != null && jdata.length() > 0) {

			try {
				JSONObject json_data = jdata.getJSONObject(0);
				String codeFromServer = json_data.getString("code");
				//String messageFromServer = json_data.getString("message");
				
				if("200".equals(codeFromServer)){
					return true;
				}
				else return false;
				
			

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

	//FIXME
	@Override
	public boolean validateDataBeforeConnection(String... params) {
		String name = params[0];
		String password = params[1];
		
		if(name==null || name.trim().equals("") || name.length()>30){
			Toast.makeText(this, "El nombre no es válido", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if(password==null || password.trim().equals("") || password.length()>30){
			Toast.makeText(this, "La contraseña introducida no es válida", Toast.LENGTH_SHORT).show();
			return false;
		}
		
					
		return true;
	}

	@Override
	public void afterGoodConnection() {
		
		
	}

	@Override
	public void invalidInputData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterErrorConnection() {
		// TODO Auto-generated method stub
		
	}

	
}