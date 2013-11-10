package es.rczone.simonsays.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import es.rczone.simonsays.R;

public class MainMenu extends Activity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main_menu);
	}

	
	public void onClick(View v) {
		
		Intent intent;
		
		switch(v.getId()){
			case R.id.mainmenu_bt_profile:
        	intent = new Intent(this, Profile.class);
        	startActivity(intent);
        	break;
		
            case R.id.mainmenu_bt_new_game:
            	intent = new Intent(this, NewGame.class);
            	startActivity(intent);
            	break;
            case R.id.mainmenu_bt_games:
            	intent = new Intent(this, Games.class);
            	startActivity(intent);
            	break;
            
            case R.id.mainmenu_bt_friends:
            	intent = new Intent(this, Friends.class);
            	startActivity(intent);
            	break;
            	
            case R.id.mainmenu_bt_form:
            	intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/138q9PpmzHYg7TucPikMt_oax9BllYEfhxDR0o3kzk1g/viewform"));
         	    startActivity(intent);
            	
            	break;
            	
//            //XXX testing
//            case R.id.test:
//            	intent = new Intent(this, Board.class);
//            	intent.putExtra(Board.CODE, Board.OPP_TURN);
//            	intent.putExtra(Board.MOVE, "3-1-0-2");
//            	startActivity(intent);
//            	break;
 
        }                 
    } 
}