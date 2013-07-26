package es.rczone.simonsays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class ActivityMainMenu extends Activity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main_menu);
	}

	
	public void onClick(View v) {
		
		Intent intent;
		
		switch(v.getId()){
            case R.id.mainmenu_bt_new_game:
            	
            	break;
            case R.id.mainmenu_bt_resume_game:
            	intent = new Intent(this, ActivityLogin.class);
            	startActivity(intent);
            	break;
            
            case R.id.mainmenu_bt_friends:
            	intent = new Intent(this, FragmentActivityFriends.class);
            	startActivity(intent);
            	break;
 
        }                 
    } 
}