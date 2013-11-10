package es.rczone.simonsays.activities.server_requests;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import es.rczone.simonsays.GCMIntentService;
import es.rczone.simonsays.activities.fragments.FragmentAddFriend;
import es.rczone.simonsays.model.Friend;
import es.rczone.simonsays.model.Friend.FriendStates;
import es.rczone.simonsays.tools.ConnectionListener;
import es.rczone.simonsays.tools.HttpPostConnector;

public class AddFriendRequest implements ConnectionListener{
	
	private HttpPostConnector post;
	private Context context;

	private FragmentAddFriend fragmento;
	private String nameNewFriend;
	
	
	public AddFriendRequest(FragmentAddFriend fragmento) {
		this.fragmento = fragmento;
		this.context = fragmento.getActivity();
		post = new HttpPostConnector();
	}
	
	@Override
	public boolean inBackground(String... params) {
		
		
		SharedPreferences prefs = context.getSharedPreferences(GCMIntentService.PREFERENCES_FILE, Context.MODE_PRIVATE);
    	String name = prefs.getString(GCMIntentService.NAME, "");
		
		ArrayList<NameValuePair> postParametersToSend = new ArrayList<NameValuePair>();
		
		postParametersToSend.add(new BasicNameValuePair("user_name", name));
		postParametersToSend.add(new BasicNameValuePair("friend_name", params[0]));

		// realizamos una peticion y como respuesta obtenes un array JSON
		JSONArray jdata = post.getserverdata(postParametersToSend, HttpPostConnector.URL_ADD_FRIEND);


		// si lo que obtuvimos no es null, es decir, hay respuesta válida
		if (jdata != null && jdata.length() > 0) {

			try {
				JSONObject json_data = jdata.getJSONObject(0);
				String codeFromServer = json_data.getString("code");
				//String messageFromServer = json_data.getString("message");
				
				if(codeFromServer.equals("100")){
					nameNewFriend = params[0];
					return true;
				}
				else{
					return false;
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return true;
		} else { // json obtenido invalido verificar parte WEB.
			Log.e("JSON  ", "ERROR");
			return false;
		}
	}

	//FIXME used copy-paste in this method
	@Override
	public boolean validateDataBeforeConnection(String... params) {
		String name = params[0];
				
		if(name==null || name.trim().equals("") || name.length()>30){
			//Toast.makeText(this.getActivity(), "El nombre introducido no es válido", Toast.LENGTH_SHORT).show();
			return false;
		}
		
					
		return true;
	}

	@Override
	public void afterGoodConnection() {
		Toast.makeText(this.context, "Nuevo amigo añadido", Toast.LENGTH_SHORT).show();
		fragmento.addFriend(new Friend(nameNewFriend, FriendStates.WAITING_FOR_RESPONSE_FRIENDSHIP));
	}

	@Override
	public void invalidInputData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterErrorConnection() {
		Toast.makeText(this.context, "El nombre de usuario no existe.", Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public Context getContext() {
		return this.context;
	}

}
