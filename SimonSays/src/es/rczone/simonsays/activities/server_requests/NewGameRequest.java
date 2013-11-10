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
import es.rczone.simonsays.model.GameFactory;
import es.rczone.simonsays.tools.ConnectionListener;
import es.rczone.simonsays.tools.HttpPostConnector;

public class NewGameRequest implements ConnectionListener{
	
	private HttpPostConnector post;
	private Context context;

	public NewGameRequest(Context context) {
		this.context = context;
		post = new HttpPostConnector();
	}
	
	@Override
	public boolean inBackground(String... params) {
		ArrayList<NameValuePair> postParametersToSend = new ArrayList<NameValuePair>();
		postParametersToSend.add(new BasicNameValuePair("player1_name", params[0]));
		postParametersToSend.add(new BasicNameValuePair("player2_name", params[1]));
		

		// realizamos una peticion y como respuesta obtenes un array JSON
		JSONArray jdata = post.getserverdata(postParametersToSend, HttpPostConnector.URL_REQUEST_NEW_GAME);


		// si lo que obtuvimos no es null, es decir, hay respuesta válida
		if (jdata != null && jdata.length() > 0) {

			try {
				JSONObject json_data = jdata.getJSONObject(0);
				String codeFromServer = json_data.getString("code");
				//String messageFromServer = json_data.getString("message");
				
				if("200".equals(codeFromServer)){
					int id = json_data.getInt("game_id");
					FriendDAO dao = new FriendDAO();
					Friend opponent = dao.get(params[1]);
					opponent.setState(FriendStates.WAITING_FOR_RESPONSE_GAME);
					dao.update(opponent);
					Game newGame = new GameFactory().createNewGame(id, opponent);
					new GameDAO().insert(newGame);
					
					//Tools.updateFriendsUI(this);
					Intent intent = new Intent(GCMIntentService.FRIENDS_UPDATER_ACTION);
			    	context.sendBroadcast(intent);//This will be received in Friends activity
					
					//Tools.updateGamesUI(this);
			    	Intent intent2 = new Intent(GCMIntentService.GAMES_UPDATER_ACTION);
			    	context.sendBroadcast(intent2);//This will be received in Games activity
					
					return true;
				}
				else return false;
				
			

			} catch (JSONException e) {
				//Toast.makeText(this, "Error desconocido.", Toast.LENGTH_SHORT).show();
			}
			
			
			return true;
		} else { // json obtenido invalido verificar parte WEB.
			Log.e("JSON  ", "ERROR");
			//Toast.makeText(this, "La conexión ha fallado. No se ha completado el registro.", Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	//FIXME used copy-paste in this method
	@Override
	public boolean validateDataBeforeConnection(String... params) {
		String name = params[0];
		String password = params[1];
		
		if(name==null || name.trim().equals("") || name.length()>30){
			Toast.makeText(this.context, "El nombre no es válido", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if(password==null || password.trim().equals("") || password.length()>30){
			Toast.makeText(this.context, "La contraseña introducida no es válida", Toast.LENGTH_SHORT).show();
			return false;
		}
		
					
		return true;
	}

	@Override
	public void afterGoodConnection() {
		
		Toast.makeText(this.context, "Solicitud de nuevo juego enviada.", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void invalidInputData() {
		Toast.makeText(this.context, "Invalid input data.", Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void afterErrorConnection() {
		Toast.makeText(this.context, "There was an error during connection.", Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public Context getContext() {
		return this.context;
	}

}
