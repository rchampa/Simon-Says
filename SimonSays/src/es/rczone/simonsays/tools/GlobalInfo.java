package es.rczone.simonsays.tools;

import android.content.Context;
import android.content.SharedPreferences;
import es.rczone.simonsays.GCMIntentService;

public class GlobalInfo {
	
	
	public final String USERNAME;
	
	public final String RIGHT_MATCH_MOVE = "1";
	public final String FAIL_MATCH_MOVE = "2";
	
	//Keys
	public final String KEY_GAME_ID = "game_id";
	
	//paths
	public final String PATH_PIC_FILE = "avatar";
	public final String PATH_PIC_FILE_MINI = "miniavatar";
	
	// Game activity
	public final String ACCEPT_REQUEST = "1";
	public final String REJECT_REQUEST = "2";
	
	public GlobalInfo(Context context){
		
		SharedPreferences prefs = context.getSharedPreferences(GCMIntentService.PREFERENCES_FILE, Context.MODE_PRIVATE);
		USERNAME = prefs.getString(GCMIntentService.NAME, "");
				
	}
	
	
	

}
