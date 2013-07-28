package es.rczone.simonsays;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class FragmentActivityFriends extends FragmentActivity{
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmentactivity_friends);
 
        FragmentFriendsList frgListado =(FragmentFriendsList)getSupportFragmentManager().findFragmentById(R.id.frag_list_friends);
        
        FragmentAddFriend frgAddFriend = (FragmentAddFriend)getSupportFragmentManager().findFragmentById(R.id.frg_add_friend);
        
      
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
