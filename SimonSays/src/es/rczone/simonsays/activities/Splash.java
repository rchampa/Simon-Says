package es.rczone.simonsays.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import es.rczone.simonsays.GCMIntentService;
import es.rczone.simonsays.R;



public class Splash extends Activity {

	// used to know if the back button was pressed in the splash screen activity and avoid opening the next activity
    private boolean mIsBackButtonPressed;
    private static final int SPLASH_DURATION = 3000; // 3 seconds
 
 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
       
 
        setContentView(R.layout.activity_splash);
 
        Handler handler = new Handler();
 
        // run a thread after 2 seconds to start the home screen
        handler.postDelayed(new Runnable() {
 
            @Override
            public void run() {
               
               
                if (!mIsBackButtonPressed) {
                	
                	SharedPreferences prefs = Splash.this.getSharedPreferences(GCMIntentService.PREFERENCES_FILE, Context.MODE_PRIVATE);
                	
                	String name = prefs.getString(GCMIntentService.NAME, "");
//                	String pass = prefs.getString(GCMIntentService.PASS, "");
//                	String email = prefs.getString(GCMIntentService.EMAIL, "");
                	
                	if(name.equals("")){
                		Intent intent = new Intent(Splash.this, Register.class);
	                	Splash.this.startActivity(intent); 
                	}
                	else{
                		if(prefs.getBoolean(GCMIntentService.VALID_GCM_ID, true)){
	                		Intent intent = new Intent(Splash.this, MainMenu.class);
		                	Splash.this.startActivity(intent);
                		}
                		else{//GCM_ID invalid
                			Intent intent = new Intent(Splash.this, Sync.class);
		                	Splash.this.startActivity(intent);
                		}
                	}
                	
                	
//                	// Make sure the device has the proper dependencies.
//                    GCMRegistrar.checkDevice(ActivitySplash.this);
//                    // Make sure the manifest was properly set - comment out this line
//                    // while developing the app, then uncomment it when it's ready.
//                    GCMRegistrar.checkManifest(ActivitySplash.this);
//	                // Get GCM registration id
//	        		String regId = GCMRegistrar.getRegistrationId(ActivitySplash.this);
//	                
//	                if (regId.equals("")) {//not registered yet
//	                	Intent intent = new Intent(ActivitySplash.this, ActivityRegister.class);
//	                	ActivitySplash.this.startActivity(intent);
//	                }
//	                else{
//	                	Intent intent = new Intent(ActivitySplash.this, ActivityMainMenu.class);
//	                	ActivitySplash.this.startActivity(intent);
//	                }
                }
                
                // 	make sure we close the splash screen so the user won't come back when it presses back key
                finish();
            }
 
        }, SPLASH_DURATION); // time in milliseconds (1 second = 1000 milliseconds) until the run() method will be called
 
    }
 
    @Override
   public void onBackPressed() {
 
        // set the flag to true so the next activity won't start up
    	// anbd avoid interruput the splash activity
        mIsBackButtonPressed = true;
        super.onBackPressed();
 
    }

}
