package es.rczone.simonsays;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class ActivitySplash extends Activity {

	// used to know if the back button was pressed in the splash screen activity and avoid opening the next activity
    private boolean mIsBackButtonPressed;
    private static final int SPLASH_DURATION = 3000; // 3 seconds
    private final String PREFS_NAME = "registration_details";
    private final String REG_FIELD = "isRegistered";
 
 
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
 
                // make sure we close the splash screen so the user won't come back when it presses back key
                finish();
                 
                if (!mIsBackButtonPressed) {
                    // start the home screen if the back button wasn't pressed already 
                	// Restore preferences
                	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    boolean isRegistered = settings.getBoolean(REG_FIELD, false);
                    
                    if(isRegistered){
                    	Intent intent = new Intent(ActivitySplash.this, ActivityMainMenu.class);
                    	ActivitySplash.this.startActivity(intent);
                    }
                    else{
                    	Intent intent = new Intent(ActivitySplash.this, ActivityRegister.class);
                    	ActivitySplash.this.startActivity(intent);
                    }
                     
                	
               }
                 
            }
 
        }, SPLASH_DURATION); // time in milliseconds (1 second = 1000 milliseconds) until the run() method will be called
 
    }
 
    @Override
   public void onBackPressed() {
 
        // set the flag to true so the next activity won't start up
        mIsBackButtonPressed = true;
        super.onBackPressed();
 
    }

}
