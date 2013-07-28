package es.rczone.simonsays;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;

import es.rczone.simonsays.R;
import es.rczone.simonsays.tools.HttpPostConnector;
 

 
public class GCMIntentService extends GCMBaseIntentService {
 
    private static final String TAG = "GCMIntentService";
    public static final String DISPLAY_MESSAGE_ACTION = "es.rczone.simonsays.DISPLAY_MESSAGE";
    public static final String EXTRA_MESSAGE = "message";
    public static final String ID = "gcm_id";    
    public static final String SENDER_ID = "391067387670";// Google project id 
    public static final String PREFERENCES_FILE = "rczone.sd.data";
    public static final String NAME = "name";
    public static final String PASS = "password";
    public static final String GCM_ID = "gcm_id";
    public static final String EMAIL = "email";
    public static final String VALID_GCM_ID = "validation";
    
    
 
    public GCMIntentService() {
        super(SENDER_ID);
    }
    
    
    private boolean firstTime(Context context, final String id){
    	GCMRegistrar.setRegisteredOnServer(context, true);
    	
    	final SharedPreferences prefs = getSharedPreferences(GCMIntentService.PREFERENCES_FILE, Context.MODE_PRIVATE);
    	
    	final String name = prefs.getString(GCMIntentService.NAME, "");

    	if(name.equals("")){
    		return true;
    	}
    	else{
    		
    		String gcm_id = prefs.getString(GCMIntentService.GCM_ID, "");
    		
    		if(!id.equals(gcm_id)){
    		
	    		Thread t = new Thread(new Runnable() {
					
					@Override
					public void run() {
						
						HttpPostConnector post = new HttpPostConnector();
						SharedPreferences.Editor editor = prefs.edit();
				        editor.putBoolean(GCMIntentService.VALID_GCM_ID, false);
				        editor.commit();
						
						ArrayList<NameValuePair> postParametersToSend = new ArrayList<NameValuePair>();
	
						postParametersToSend.add(new BasicNameValuePair("name", name));
						postParametersToSend.add(new BasicNameValuePair("gcm_id", id));
	
						// realizamos una peticion y como respuesta obtenes un array JSON
						JSONArray jdata = post.getserverdata(postParametersToSend, HttpPostConnector.UPDATE_ID);
	
	
						if (jdata != null && jdata.length() > 0) {
							
							try{
								JSONObject json_data = jdata.getJSONObject(0);
								String codeFromServer = json_data.getString("code");
								String messageFromServer = json_data.getString("message");
								
								if(codeFromServer.equals("600")){
									editor.putString(GCM_ID, id);
									editor.putBoolean(GCMIntentService.VALID_GCM_ID, true);
							        editor.commit();
								}
								else{
									editor.putBoolean(GCMIntentService.VALID_GCM_ID, false);
									editor.commit();
								}
							} catch (JSONException e) {
								
							}
							
						}
	
					}
				});
	    		
	    		t.start();
    		}
    		return false;
    	}
    }
 
    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        if(firstTime(context,registrationId)){
        	Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
            intent.putExtra(ID, registrationId);
            context.sendBroadcast(intent);//This will be received in ActivityRegister
        }
        
    }
 
    /**
     * Method called on device un registred
     * */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        GCMRegistrar.setRegisteredOnServer(context, false);
        
    }
 
    /**
     * Method called on Receiving a new message
     * */
    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message");
        String message = intent.getExtras().getString("price");
      
        // notifies user
        generateNotification(context, message);
    }
 
    /**
     * Method called on receiving a deleted message
     * */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        // notifies user
        generateNotification(context, message);
    }
 
    /**
     * Method called on Error
     * */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        
    }
 
    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        return super.onRecoverableError(context, errorId);
    }
 
    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message) {
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);
         
        String title = context.getString(R.string.app_name);
         
        Intent notificationIntent = new Intent(context, ActivityMainMenu.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
         
        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;
         
        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);      
 
    }
    
 
}
