package es.rczone.simonsays.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import es.rczone.simonsays.GCMIntentService;
import es.rczone.simonsays.R;
import es.rczone.simonsays.activities.fragments.FragmentAddFriend;
import es.rczone.simonsays.activities.fragments.FragmentFriendsList;
import es.rczone.simonsays.activities.fragments.listeners.AddFriendListener;
import es.rczone.simonsays.activities.fragments.listeners.ListListener;
import es.rczone.simonsays.activities.server_requests.ResponseFriendshipRequest;
import es.rczone.simonsays.controllers.FriendsController;
import es.rczone.simonsays.daos.FriendDAO;
import es.rczone.simonsays.model.Friend;
import es.rczone.simonsays.model.Friend.FriendStates;
import es.rczone.simonsays.tools.AsyncConnect;

public class Friends extends FragmentActivity implements Handler.Callback,ListListener<Friend>,AddFriendListener{
	
	
	private final String YES_ANSWER = "1";
	private final String NO_ANSWER = "2";
	
	private FragmentFriendsList frgListado;
	private FragmentAddFriend frgAddFriend;
	private FriendsController controller;
	private List<Friend> friends;
	private AsyncConnect connection;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_activity_friends);
        
 
        frgListado =(FragmentFriendsList)getSupportFragmentManager().findFragmentById(R.id.frg_friend_list);
        frgListado.setListener(this);
        frgAddFriend = (FragmentAddFriend)getSupportFragmentManager().findFragmentById(R.id.frg_add_friend);
        frgAddFriend.setListener(this);
        
        friends = new ArrayList<Friend>();
        controller = new FriendsController(friends);
		controller.addOutboxHandler(new Handler(this));
		
		controller.handleMessage(FriendsController.MESSAGE_GET_FRIENDS_LIST);
		
		registerReceiver(friendUpdater, new IntentFilter(GCMIntentService.FRIENDS_UPDATER_ACTION));
      
	}
	
	public FriendsController getController(){
		return controller;
	}
	
	@Override
	protected void onResume(){
		registerReceiver(friendUpdater, new IntentFilter(GCMIntentService.FRIENDS_UPDATER_ACTION));
		super.onResume();
	}

	@Override
	protected void onPause(){
		try {
            unregisterReceiver(friendUpdater);
        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        super.onPause();
	}
	
	@Override
    protected void onDestroy() {
		
        try {
        	controller.dispose();
            unregisterReceiver(friendUpdater);
        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }
	
	
	 private final BroadcastReceiver friendUpdater =  new BroadcastReceiver() {

	   @Override
	   public void onReceive(Context context, Intent intent) {
		   controller.handleMessage(FriendsController.MESSAGE_GET_FRIENDS_LIST);
	   }

	};


	@Override
	public void onItemClicked(Friend item) {
		
		if(item.getState()==FriendStates.ASKED_YOU_FOR_FRIENDSHIP){
			askConfirmation(item);
		}
	}


	@Override
	public void onItemLongClicked(Friend item) {
		
		//XXX Testing
		item.setState(FriendStates.ASKED_YOU_FOR_FRIENDSHIP);
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
		            	connection = new AsyncConnect(new ResponseFriendshipRequest(Friends.this),name,friend.getUserName(),YES_ANSWER);
		            	connection.execute();
		                
		            }
		        });
		// Setting Negative "NO" Btn
		alertDialog2.setNegativeButton("Reject",
		        new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		            	connection = new AsyncConnect(new ResponseFriendshipRequest(Friends.this),name,friend.getUserName(),NO_ANSWER);
		            	connection.execute();
		            }
		        });
		 
		// Showing Alert Dialog
		alertDialog2.show();
		
		
	}
	
}
