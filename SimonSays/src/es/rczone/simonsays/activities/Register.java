package es.rczone.simonsays.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.google.android.gcm.GCMRegistrar;

import es.rczone.simonsays.GCMIntentService;
import es.rczone.simonsays.R;
import es.rczone.simonsays.activities.server_requests.RegisterRequest;
import es.rczone.simonsays.tools.AsyncConnect2;
import es.rczone.simonsays.tools.Tools;
import es.rczone.simonsays.tools.WakeLocker;

public class Register extends Activity {
	
	private AsyncConnect2 connection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);

		registerReceiver(mHandleMessageReceiver, new IntentFilter(GCMIntentService.DISPLAY_MESSAGE_ACTION));
		
		EditText name = (EditText)findViewById(R.id.register_et_name);
		EditText pass = (EditText)findViewById(R.id.register_et_pass);
		EditText email = (EditText)findViewById(R.id.register_et_email);
		
		//Calling these methods to disable and then enable, produce a focus lost effect.
		Tools.setEnableEditTextFields(false,name,pass,email);
		Tools.setEnableEditTextFields(true,name,pass,email);
		
	}

	@Override
	protected void onResume(){
		registerReceiver(mHandleMessageReceiver, new IntentFilter(GCMIntentService.DISPLAY_MESSAGE_ACTION));
		super.onResume();
	}

	@Override
	protected void onPause(){
		try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        super.onPause();
	}
	
	@Override
    protected void onDestroy() {
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }
	
	public void onClick(View v) {
		
      
        switch(v.getId()){
            case R.id.button_register:
            	GCMRegistrar.register(this, GCMIntentService.SENDER_ID);
            	break;
            case R.id.button_login:
            	Intent intent = new Intent(this, Login.class);
            	startActivity(intent);
            	break;
 
        }                 
    } 
	
	
	/**
     * Receiving push messages
     * */
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            
        	String id = intent.getExtras().getString(GCMIntentService.ID);
            if(id==null || "".equals(id.trim())){
            	id = GCMRegistrar.getRegistrationId(Register.this);
            	if("".equals(id))
            		return;
            }
            
            String nombre = ((EditText)findViewById(R.id.register_et_name)).getText().toString();
            String password = ((EditText)findViewById(R.id.register_et_pass)).getText().toString();
            String email = ((EditText)findViewById(R.id.register_et_email)).getText().toString();
                        
            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());
             
            /**
             * Take appropriate action on this message
             * depending upon your app requirement
             * For now i am just displaying it on the screen
             * */
             
            // register in game server
            connection = new AsyncConnect2(new RegisterRequest(Register.this),nombre,password,id,email);
            connection.execute();
                        
          
            // Releasing wake lock
            WakeLocker.release();
        }
    };
	

}