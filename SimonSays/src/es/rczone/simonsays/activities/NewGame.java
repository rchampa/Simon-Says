package es.rczone.simonsays.activities;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;
import es.rczone.simonsays.GCMIntentService;
import es.rczone.simonsays.R;
import es.rczone.simonsays.activities.fragments.FragmentFriendsList;
import es.rczone.simonsays.activities.fragments.listeners.ListListener;
import es.rczone.simonsays.controllers.FriendsController;
import es.rczone.simonsays.daos.FriendDAO;
import es.rczone.simonsays.daos.GameDAO;
import es.rczone.simonsays.model.Friend;
import es.rczone.simonsays.model.Friend.FriendStates;
import es.rczone.simonsays.model.Game;
import es.rczone.simonsays.model.GameFactory;
import es.rczone.simonsays.tools.AsyncConnect;
import es.rczone.simonsays.tools.ConnectionListener;
import es.rczone.simonsays.tools.HttpPostConnector;
import es.rczone.simonsays.tools.Tools;

public class NewGame extends FragmentActivity implements Handler.Callback, ListListener<Friend>, ConnectionListener {

	
	private FragmentFriendsList frgListado;
	private HttpPostConnector post;
	
	private FriendsController controller;
	private List<Friend> friends;
	
	private Game newGame;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_activity_new_game);
		
		frgListado =(FragmentFriendsList)getSupportFragmentManager().findFragmentById(R.id.frg_friend_list);
		frgListado.setListener(this);
		post = new HttpPostConnector();
		
		friends = new ArrayList<Friend>();
        controller = new FriendsController(friends);
		controller.addOutboxHandler(new Handler(this));
		
		registerReceiver(friendsUpdater, new IntentFilter(GCMIntentService.FRIENDS_UPDATER_ACTION));
	}
	
	private final BroadcastReceiver friendsUpdater =  new BroadcastReceiver() {

	   @Override
	   public void onReceive(Context context, Intent intent) {
		   controller.handleMessage(FriendsController.MESSAGE_GET_FRIENDS_LIST);
	   }

	};

		
	@Override
	protected void onResume(){
		registerReceiver(friendsUpdater, new IntentFilter(GCMIntentService.FRIENDS_UPDATER_ACTION));
		controller.handleMessage(FriendsController.MESSAGE_GET_FRIENDS_LIST);
		super.onResume();
	}

	@Override
	protected void onPause(){
		try {
            unregisterReceiver(friendsUpdater);
        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        super.onPause();
	}
	
	@Override
    protected void onDestroy() {
		
        try {
        	controller.dispose();
            unregisterReceiver(friendsUpdater);
        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }
		
	@Override
	public boolean handleMessage(Message message) {
		
		switch(message.what) {
		case FriendsController.MESSAGE_TO_VIEW_MODEL_UPDATED:
			this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					frgListado.refreshList(friends);
				}
			});
			return true;
		}
		return false;
	}
	
	
	@Override
	public void onItemClicked(Friend friend) {
		
		if(friend.getState()==FriendStates.ACCEPTED){
		
			SharedPreferences prefs = getSharedPreferences(GCMIntentService.PREFERENCES_FILE, Context.MODE_PRIVATE);
	    	String name = prefs.getString(GCMIntentService.NAME, "");
	    	
	    	new AsyncConnect(this).execute(name, friend.getUserName());
		}
		
		
		//XXX testing...
		//new GameDAO().deleteAll();
	}

	@Override
	public void onItemLongClicked(Friend item) {
		// TODO Auto-generated method stub
		
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
					int id = json_data.getInt("game_id");
					FriendDAO dao = new FriendDAO();
					Friend opponent = dao.get(params[1]);
					opponent.setState(FriendStates.WAITING_FOR_RESPONSE_GAME);
					dao.update(opponent);
					newGame = new GameFactory().createNewGame(id, opponent);
					new GameDAO().insert(newGame);
					Tools.updateFriendsUI(this);
					Tools.updateGamesUI(this);
					
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

	//FIXME used copy-paste in this method
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
		
		Toast.makeText(this, "Solicitud de nuevo juego enviada.", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void invalidInputData() {
		Toast.makeText(this, "Invalid input data.", Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void afterErrorConnection() {
		Toast.makeText(this, "There was an error during connection.", Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return null;
	}

	

	
}