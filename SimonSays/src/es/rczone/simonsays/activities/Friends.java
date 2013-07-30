package es.rczone.simonsays.activities;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.Window;
import es.rczone.simonsays.R;
import es.rczone.simonsays.activities.fragments.FragmentAddFriend;
import es.rczone.simonsays.activities.fragments.FragmentFriendsList;
import es.rczone.simonsays.activities.fragments.listeners.AddFriendListener;
import es.rczone.simonsays.activities.fragments.listeners.ListListener;
import es.rczone.simonsays.controllers.FriendsController;
import es.rczone.simonsays.model.Friend;

public class Friends extends FragmentActivity implements Handler.Callback,ListListener<Friend>,AddFriendListener{
	
	private FragmentFriendsList frgListado;
	private FragmentAddFriend frgAddFriend;
	private FriendsController controller;
	private List<Friend> friends;
	
	
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
      
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}



	@Override
	public void onItemClicked(Friend item) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onItemLongClicked(Friend item) {
		// TODO Auto-generated method stub
		
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
}
