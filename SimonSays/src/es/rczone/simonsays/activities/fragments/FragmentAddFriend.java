package es.rczone.simonsays.activities.fragments;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import es.rczone.simonsays.GCMIntentService;
import es.rczone.simonsays.R;
import es.rczone.simonsays.activities.fragments.listeners.AddFriendListener;
import es.rczone.simonsays.model.Friend;
import es.rczone.simonsays.model.Friend.FriendStates;
import es.rczone.simonsays.tools.AsyncConnect;
import es.rczone.simonsays.tools.ConnectionListener;
import es.rczone.simonsays.tools.HttpPostConnector;

public class FragmentAddFriend extends Fragment implements ConnectionListener{

	private HttpPostConnector post;
	private String nameNewFriend;
	private AddFriendListener listener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		final View view = inflater.inflate(R.layout.fragment_add_friend, container, false);
		
		final View button = view.findViewById(R.id.addfriends_button_add);
	    button.setOnClickListener(
	        new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	nameNewFriend = ((EditText)view.findViewById(R.id.addfriends_et_name)).getText().toString();
	            	new AsyncConnect(FragmentAddFriend.this).execute(nameNewFriend);
	            }
	        }
	    );
		return view;
	}

	@Override
	public void onActivityCreated(Bundle state) {
		super.onActivityCreated(state);
		post = new HttpPostConnector();
	}
	
	public void setListener(AddFriendListener listener){
		this.listener = listener;
	}
	

	@Override
	public boolean inBackground(String... params) {
		
		
		SharedPreferences prefs = this.getActivity().getSharedPreferences(GCMIntentService.PREFERENCES_FILE, Context.MODE_PRIVATE);
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
		Toast.makeText(this.getActivity(), "Nuevo amigo añadido", Toast.LENGTH_SHORT).show();
		listener.onFriendshipAdded(new Friend(nameNewFriend, FriendStates.WAITING_FOR_RESPONSE_FRIENDSHIP));
	}

	@Override
	public void invalidInputData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterErrorConnection() {
		Toast.makeText(this.getActivity(), "El nombre de usuario no existe.", Toast.LENGTH_SHORT).show();
		
	}

}