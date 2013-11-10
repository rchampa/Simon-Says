package es.rczone.simonsays.activities.server_requests;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import es.rczone.simonsays.activities.Board;
import es.rczone.simonsays.daos.GameDAO;
import es.rczone.simonsays.model.Game;
import es.rczone.simonsays.model.Game.GameStates;
import es.rczone.simonsays.tools.ConnectionListener;
import es.rczone.simonsays.tools.HttpPostConnector;

public class MakeMoveRequest implements ConnectionListener{

	private HttpPostConnector post;
	private Board context;

	public MakeMoveRequest(Board context) {
		this.context = context;
		post = new HttpPostConnector();
	}
	
	
	@Override
	public boolean inBackground(String... params) {
		ArrayList<NameValuePair> postParametersToSend = new ArrayList<NameValuePair>();

		postParametersToSend.add(new BasicNameValuePair("game_id", params[0]));
		postParametersToSend.add(new BasicNameValuePair("player_name", params[1]));
		postParametersToSend.add(new BasicNameValuePair("move", params[2]));
		postParametersToSend.add(new BasicNameValuePair("guess", params[3]));

		// realizamos una peticion y como respuesta obtenes un array JSON
		JSONArray jdata = post.getserverdata(postParametersToSend, HttpPostConnector.URL_MAKE_A_MOVE);


		// si lo que obtuvimos no es null, es decir, hay respuesta válida
		if (jdata != null && jdata.length() > 0) {

			try {
				JSONObject json_data = jdata.getJSONObject(0);
				String codeFromServer = json_data.getString("code");
				//String messageFromServer = json_data.getString("message");
				
				if(codeFromServer.equals("400")){
					Game g = new GameDAO().get(context.getID());
					g.setState(GameStates.WAITING_FOR_MOVE);
					if(context.getGuess()) g.upUserScore();
					new GameDAO().update(g);
					return true;
				}
				else{
					return false;
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return true;
		} else { // json obtenido invalido verificar parte WEB.
			Log.e("JSON  ", "ERROR");
			return false;
		}
	}

	@Override
	public boolean validateDataBeforeConnection(String... params) {
		//FIXME
		return true;
	}

	@Override
	public void afterGoodConnection() {
		Toast.makeText(this.context, "The move has been sent", Toast.LENGTH_SHORT).show();
		context.finish();
		
	}

	@Override
	public void invalidInputData() {
		Toast.makeText(this.context, "The move has not been sent", Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void afterErrorConnection() {
		Toast.makeText(this.context, "Problem connections. The move has not been sent.", Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public Context getContext() {
		return this.context;
	}
}
