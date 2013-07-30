package es.rczone.simonsays.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.Window;
import es.rczone.simonsays.R;
import es.rczone.simonsays.activities.fragments.ControllerListener;
import es.rczone.simonsays.activities.fragments.FragmentAddFriend;
import es.rczone.simonsays.activities.fragments.FragmentFriendsList;
import es.rczone.simonsays.controllers.FriendsController;

public class Friends extends FragmentActivity implements ControllerListener<FriendsController>{
	
	private FragmentFriendsList frgListado;
	private FragmentAddFriend frgAddFriend;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_activity_friends);
 
        frgListado =(FragmentFriendsList)getSupportFragmentManager().findFragmentById(R.id.frg_friend_list);
        frgListado.setListener(this);
        frgAddFriend = (FragmentAddFriend)getSupportFragmentManager().findFragmentById(R.id.frg_add_friend);
        
      
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public void onControllerCreated(FriendsController controller) {
		frgAddFriend.setController(controller);
		
	}

}
