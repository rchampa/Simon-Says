package es.rczone.simonsays.activities.server_requests;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import es.rczone.simonsays.GCMIntentService;
import es.rczone.simonsays.daos.FriendDAO;
import es.rczone.simonsays.daos.GameDAO;
import es.rczone.simonsays.model.Friend;
import es.rczone.simonsays.model.Friend.FriendStates;
import es.rczone.simonsays.model.Game;
import es.rczone.simonsays.model.Game.GameStates;
import es.rczone.simonsays.tools.ConnectionListener;
import es.rczone.simonsays.tools.HttpPostConnector;

public class ResponseGameRequest implements ConnectionListener {
	
	private HttpPostConnector post;
	private Context context;
	private String mensaje;
	
	public ResponseGameRequest(Context context) {
		this.context = context;
		post = new HttpPostConnector();
	}
	
	@Override
	public boolean inBackground(String... params) {
		
		ArrayList<NameValuePair> postParametersToSend = new ArrayList<NameValuePair>();
		postParametersToSend.add(new BasicNameValuePair("game_id", params[0]));
		postParametersToSend.add(new BasicNameValuePair("response", params[1]));
		postParametersToSend.add(new BasicNameValuePair("player_name", params[2]));
		

		// realizamos una peticion y como respuesta obtenes un array JSON
		JSONArray jdata = post.getserverdata(postParametersToSend, HttpPostConnector.URL_RESPONSE_REQUEST_GAME);


		// si lo que obtuvimos no es null, es decir, hay respuesta válida
		if (jdata != null && jdata.length() > 0) {

			try {
				JSONObject json_data = jdata.getJSONObject(0);
				String codeFromServer = json_data.getString("code");
				//String messageFromServer = json_data.getString("message");
				
				if("300".equals(codeFromServer)){
					
					GameDAO dao = new GameDAO();
					Game game = dao.get(Integer.parseInt(params[0]));
					game.setState(GameStates.WAITING_FOR_MOVE);
					dao.update(game);
					FriendDAO daof = new FriendDAO();
					Friend f = daof.get(game.getOpponentName());
					f.setState(FriendStates.PLAYING_WITH_YOU);
					daof.update(f);
					
					//Tools.updateFriendsUI(this);
					Intent intent = new Intent(GCMIntentService.FRIENDS_UPDATER_ACTION);
			    	context.sendBroadcast(intent);//This will be received in Friends activity
					
					//Tools.updateGamesUI(this);
			    	Intent intent2 = new Intent(GCMIntentService.GAMES_UPDATER_ACTION);
			    	context.sendBroadcast(intent2);//This will be received in Games activity
										
			    	mensaje ="You have accepted the request game.";
					return true;
				}
				else if("301".equals(codeFromServer)){
					GameDAO dao = new GameDAO();
					Game game = dao.get(Integer.parseInt(params[0]));
					game.setState(GameStates.REFUSED);
					dao.update(game);
					FriendDAO daof = new FriendDAO();
					Friend f = daof.get(game.getOpponentName());
					f.setState(FriendStates.ACCEPTED);
					daof.update(f);
					
					//Tools.updateFriendsUI(this);
					Intent intent = new Intent(GCMIntentService.FRIENDS_UPDATER_ACTION);
			    	context.sendBroadcast(intent);//This will be received in Friends activity
					
					//Tools.updateGamesUI(this);
			    	Intent intent2 = new Intent(GCMIntentService.GAMES_UPDATER_ACTION);
			    	context.sendBroadcast(intent2);//This will be received in Games activity
			    	
			    	mensaje ="You have rejected the request game.";
					return true;
				}
				else return false;
				
			

			} catch (JSONException e) {
				//Toast.makeText(this, "Error desconocido.", Toast.LENGTH_SHORT).show();
				return false;
			}
			
			
		} else { // json obtenido invalido verificar parte WEB.
			Log.e("JSON  ", "ERROR");
			//Toast.makeText(this, "La conexión ha fallado. No se ha completado el registro.", Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	@Override
	public boolean validateDataBeforeConnection(String... params) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void afterGoodConnection() {
		Toast.makeText(this.context, mensaje, Toast.LENGTH_SHORT).show();
	}


	@Override
	public void invalidInputData() {
		Toast.makeText(this.context, "There was a problem.", Toast.LENGTH_SHORT).show();
		
	}


	@Override
	public void afterErrorConnection() {
		Toast.makeText(this.context, "There was a problem.", Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return null;
	}


}
