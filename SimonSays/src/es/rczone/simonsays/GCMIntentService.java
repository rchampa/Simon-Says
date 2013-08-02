package es.rczone.simonsays;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;

import es.rczone.simonsays.activities.Games;
import es.rczone.simonsays.daos.FriendDAO;
import es.rczone.simonsays.daos.GameDAO;
import es.rczone.simonsays.daos.MoveDAO;
import es.rczone.simonsays.model.Friend;
import es.rczone.simonsays.model.Game;
import es.rczone.simonsays.model.GameFactory;
import es.rczone.simonsays.model.GameStates;
import es.rczone.simonsays.model.Move;
import es.rczone.simonsays.model.MovesFactory;
import es.rczone.simonsays.model.Friend.FriendStates;
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
								//String messageFromServer = json_data.getString("message");
								
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
            context.sendBroadcast(intent);//This will be received in Register Activity
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
        Log.i(TAG, "Received message from server");

        String messageFromServer = intent.getExtras().getString("message");
        String notificationMessage="";
        
        try {
			
			JSONObject json_data = new JSONObject(messageFromServer);
			notificationMessage = processMessage(json_data);
			
			
			
			
		} catch (JSONException e) {
			notificationMessage = "Invalid connection.";
		}
        
        
      
        // notifies user
        generateNotification(context, "Memorize", notificationMessage);
    } 
 
    /**
     * Method called on receiving a deleted message
     * */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        // notifies user
        generateNotification(context, "Memorize",message);
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
 
    
    private static void generateNotification(Context context, String title, String message) {
    	NotificationCompat.Builder builder =  new NotificationCompat.Builder(context)  
    	        .setSmallIcon(R.drawable.ic_launcher)  
    	        .setContentTitle(title)  
    	        .setContentText(message);  


    	Intent notificationIntent = new Intent(context, Games.class);  

    	PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);  

    	builder.setContentIntent(contentIntent);  
    	builder.setAutoCancel(true);
    	builder.setLights(Color.GREEN, 500, 500);
    	builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
//    	long[] pattern = {500,500,500,500,500,500,500,500,500};
//    	builder.setVibrate(pattern);
    	//builder.setStyle(new NotificationCompat.InboxStyle());
    	// Add as notification  
    	NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);  
    	manager.notify(1, builder.build());       
 
    }
    
    
    
    /**
     * 	From response request game
     * 	==== ======== ======= ==== 
     * 	300		Accepted game.
     * 	301		Refused game.
     * 	302		The game is already in progress.
     * 	303		The game does not exists.
     * 
     * 	Make a move
	 *	==== = ====
	 *	400		New move added.
	 *	401		The game is not ready.
	 *	402		The game does not exists.
	 *	403		Invalid player, the player do not belong to the game.
	 *	404		Error 404
     * 
     * 	Request a new game
     * 	======= = === ====
	 *	200		New game added, waiting for player to accept the request...
	 *	201		There is already a game in progress.
	 *	202		They are not friends.
	 *	203		$user is not registered.
	 *	204		They have another game in progress.
	 *	205		They have another request. They should accept or refuse that request.	
	 *
	 *	Response a friendship request
	 *	======== = ========== =======
	 *	700		Friendship completed.
	 *	701		Friendship rejected.
     * 
     * @param json_data
     * @return
     */
    
    private String processMessage(JSONObject json_data){
    	
    	try {
			
    		String codeFromServer = json_data.getString("code");
    		
    		if("-1".equals(codeFromServer)){
    			return "Connection failed.";
    		}
    		else if("100".equals(codeFromServer)){
    			String friendName = json_data.getString("user_name");
    			Friend f = new Friend(friendName, FriendStates.ASKED_YOU);
    			new FriendDAO().insert(f);
    			
    			return friendName+" sent you a friendship request.";
    			
    		}
    		
    		else if("200".equals(codeFromServer)){
    			int game_id = json_data.getInt("game_id");
    			GameFactory factory = new GameFactory();
    			String nameOpponent = json_data.getString("player2_name");
    			Friend f = new FriendDAO().get(nameOpponent);
    			Game game = factory.createNewGameFromRequest(game_id, f);
    			new GameDAO().insert(game);
    			
    			return nameOpponent+" sent you a request for a game.";
    			
    		}
    		else if("300".equals(codeFromServer)){
    			
    			int game_id = json_data.getInt("game_id");
    			GameDAO dao = new GameDAO();
    			Game game = dao.get(game_id);
    			game.setMyTurn(true);
    			game.setState(GameStates.IN_PROGRESS);
    			dao.update(game);
    			
    			return "Your friend "+game.getOpponentName()+" is ready to play.";
    		}
			else if("301".equals(codeFromServer)){
				
				int game_id = json_data.getInt("game_id");
    			GameDAO dao = new GameDAO();
    			Game game = dao.get(game_id);
    			game.setState(GameStates.REFUSED);
    			dao.update(game);
    			
    			return "Your friend "+game.getOpponentName()+" refuse to play with you.";
			    			
			}
			else if("400".equals(codeFromServer)){
				
				int game_id = json_data.getInt("game_id");
				String move = json_data.getString("move");
				Move m = new MovesFactory().createMove(game_id, move);
				new MoveDAO().insert(m);
				
				int num_moves = json_data.getInt("level");
				int guess = json_data.getInt("guess");
				
    			GameDAO dao = new GameDAO();
    			Game game = dao.get(game_id);
    			game.setState(GameStates.IN_PROGRESS);
    			game.setMyTurn(false);//Check, this makes turn for opp
    			game.setNumMoves(num_moves);
    			if(guess==1) game.upOppScore();
    			dao.update(game);
    			
				
				return "Your friend "+game.getOpponentName()+" made a move.";
			}
			else if("700".equals(codeFromServer)){
				String friendName = json_data.getString("friendName");
				FriendDAO dao = new FriendDAO(); 
				Friend f = dao.get(friendName);
				f.setState(FriendStates.ACCEPTED);
				dao.update(f);
				
				return "Your friend "+friendName+" accepted your friendship request.";
				
			}
			else if("701".equals(codeFromServer)){
				String friendName = json_data.getString("friend");
				FriendDAO dao = new FriendDAO(); 
				Friend f = dao.get(friendName);
				f.setState(FriendStates.REJECTED);
				dao.update(f);
				
				return "Your friend "+friendName+" rejected your friendship request.";
				
			}
			else{//FIXME should be all cases
				return "Your request is invalid.";
			}
			
			
			
		} 
    	catch (JSONException e) {
    		return "Error processiing the message";
		}
    	
    	
    }
 
}
