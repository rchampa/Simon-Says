package es.rczone.simonsays.activities;


import java.util.ArrayList;
import java.util.List;

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
import es.rczone.simonsays.GCMIntentService;
import es.rczone.simonsays.R;
import es.rczone.simonsays.activities.fragments.FragmentFriendsList;
import es.rczone.simonsays.activities.fragments.listeners.ListListener;
import es.rczone.simonsays.controllers.FriendsController;
import es.rczone.simonsays.model.Friend;
import es.rczone.simonsays.model.Friend.FriendStates;
import es.rczone.simonsays.tools.AsyncConnect;

public class NewGame extends FragmentActivity implements Handler.Callback, ListListener<Friend> {

	
	private FragmentFriendsList frgListado;
	
	private FriendsController controller;
	private List<Friend> friends;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_activity_new_game);
		
		frgListado =(FragmentFriendsList)getSupportFragmentManager().findFragmentById(R.id.frg_friend_list);
		frgListado.setListener(this);
		
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
	
}