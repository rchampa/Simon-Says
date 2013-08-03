package es.rczone.simonsays.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.widget.Toast;
import es.rczone.simonsays.GCMIntentService;
import es.rczone.simonsays.R;
import es.rczone.simonsays.activities.fragments.FragmentAddFriend;
import es.rczone.simonsays.activities.fragments.FragmentFriendsList;
import es.rczone.simonsays.activities.fragments.listeners.AddFriendListener;
import es.rczone.simonsays.activities.fragments.listeners.ListListener;
import es.rczone.simonsays.controllers.FriendsController;
import es.rczone.simonsays.daos.FriendDAO;
import es.rczone.simonsays.model.Friend;
import es.rczone.simonsays.model.Friend.FriendStates;
import es.rczone.simonsays.tools.AsyncConnect;
import es.rczone.simonsays.tools.ConnectionListener;
import es.rczone.simonsays.tools.HttpPostConnector;

public class Friends extends FragmentActivity implements Handler.Callback,ListListener<Friend>,AddFriendListener,ConnectionListener{
	
	
	private final String YES_ANSWER = "1";
	private final String NO_ANSWER = "2";
	
	private FragmentFriendsList frgListado;
	private FragmentAddFriend frgAddFriend;
	private FriendsController controller;
	private List<Friend> friends;
	private HttpPostConnector post;
	private AsyncConnect connection;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_activity_friends);
        
        post = new HttpPostConnector();
 
        frgListado =(FragmentFriendsList)getSupportFragmentManager().findFragmentById(R.id.frg_friend_list);
        frgListado.setListener(this);
        frgAddFriend = (FragmentAddFriend)getSupportFragmentManager().findFragmentById(R.id.frg_add_friend);
        frgAddFriend.setListener(this);
        
        friends = new ArrayList<Friend>();
        controller = new FriendsController(friends);
		controller.addOutboxHandler(new Handler(this));
		
		controller.handleMessage(FriendsController.MESSAGE_GET_FRIENDS_LIST);
      
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}



	@Override
	public void onItemClicked(Friend item) {
		
		if(item.getState()==FriendStates.ASKED_YOU){
			askConfirmation(item);
		}
	}


	@Override
	public void onItemLongClicked(Friend item) {
		
		//XXX Testing
		item.setState(FriendStates.ACCEPTED);
		new FriendDAO().update(item);
		
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
	public void onDestroy() {
		super.onDestroy();
		controller.dispose();
	}


	@Override
	public void onFriendshipAdded(Friend newFriend) {
		controller.handleMessage(FriendsController.MESSAGE_ADD_FRIEND, newFriend);
	}
	
	private void askConfirmation(final Friend friend){
		
		
		SharedPreferences prefs = Friends.this.getSharedPreferences(GCMIntentService.PREFERENCES_FILE, Context.MODE_PRIVATE);
    	final String name = prefs.getString(GCMIntentService.NAME, "");
		
		AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(Friends.this);
		 
		// Setting Dialog Title
		alertDialog2.setTitle("Confirm send...");
		 
		// Setting Dialog Message
		alertDialog2.setMessage("You have a friendship request");
		 
		// Setting Icon to Dialog
		alertDialog2.setIcon(R.drawable.send_icon_confirm);
		 
		// Setting Positive "Yes" Btn
		alertDialog2.setPositiveButton("Accept",
		        new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		            	connection = new AsyncConnect(Friends.this);
		            	connection.execute(name,friend.getUserName(),YES_ANSWER);
		                
		            }
		        });
		// Setting Negative "NO" Btn
		alertDialog2.setNegativeButton("Reject",
		        new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		            	connection = new AsyncConnect(Friends.this);
		            	connection.execute(name,friend.getUserName(),NO_ANSWER);
		            }
		        });
		 
		// Showing Alert Dialog
		alertDialog2.show();
		
		
	}


	@Override
	public boolean inBackground(String... params) {


		ArrayList<NameValuePair> postParametersToSend = new ArrayList<NameValuePair>();

		postParametersToSend.add(new BasicNameValuePair("name", params[0]));
		postParametersToSend.add(new BasicNameValuePair("friend", params[1]));
		postParametersToSend.add(new BasicNameValuePair("response", params[2]));

		// realizamos una peticion y como respuesta obtenes un array JSON
		JSONArray jdata = post.getserverdata(postParametersToSend, HttpPostConnector.URL_RESPONSE_FRIENDSHIP_REQUEST);


		// si lo que obtuvimos no es null, es decir, hay respuesta válida
		if (jdata != null && jdata.length() > 0) {

			try {
				JSONObject json_data = jdata.getJSONObject(0);
				String codeFromServer = json_data.getString("code");
				//String messageFromServer = json_data.getString("message");
				
				if(codeFromServer.equals("700")){
					FriendDAO dao = new FriendDAO();
					Friend f = dao.get(params[1]);
					f.setState(FriendStates.ACCEPTED);
					dao.update(f);
					connection.attachMessage("You have accepted the friendship.");
					return true;
				}
				else if(codeFromServer.equals("701")){
					FriendDAO dao = new FriendDAO();
					Friend f = dao.get(params[1]);
					f.setState(FriendStates.REJECTED);
					dao.update(f);
					connection.attachMessage("You have rejected the friendship.");
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


	@Override
	public boolean validateDataBeforeConnection(String... params) {
		// FIXME validate with regular expressions
		return true;
	}


	@Override
	public void afterGoodConnection() {
		Toast.makeText(this, connection.getMessage(), Toast.LENGTH_SHORT).show();
	}


	@Override
	public void invalidInputData() {
		Toast.makeText(this, "There was a problem.", Toast.LENGTH_SHORT).show();
		
	}


	@Override
	public void afterErrorConnection() {
		Toast.makeText(this, "There was a problem.", Toast.LENGTH_SHORT).show();
		
	}
}
