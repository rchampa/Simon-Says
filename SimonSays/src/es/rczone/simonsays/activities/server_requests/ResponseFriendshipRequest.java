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
import es.rczone.simonsays.activities.Friends;
import es.rczone.simonsays.controllers.FriendsController;
import es.rczone.simonsays.daos.FriendDAO;
import es.rczone.simonsays.model.Friend;
import es.rczone.simonsays.model.Friend.FriendStates;
import es.rczone.simonsays.tools.ConnectionListener;
import es.rczone.simonsays.tools.HttpPostConnector;

public class ResponseFriendshipRequest implements ConnectionListener{

	private HttpPostConnector post;
	private Friends context;
	private String mensaje;
	
	public ResponseFriendshipRequest(Friends context) {
		this.context = context;
		post = new HttpPostConnector();
	}
	
	@Override
	public boolean inBackground(String... params) {


		ArrayList<NameValuePair> postParametersToSend = new ArrayList<NameValuePair>();

		postParametersToSend.add(new BasicNameValuePair("name", params[0]));
		postParametersToSend.add(new BasicNameValuePair("friend", params[1]));
		postParametersToSend.add(new BasicNameValuePair("response", params[2]));

		// realizamos una peticion y como respuesta obtenes un array JSON
		JSONArray jdata = post.getserverdata(postParametersToSend, HttpPostConnector.URL_RESPONSE_FRIENDSHIP_REQUEST);


		// si lo que obtuvimos no es null, es decir, hay respuesta válida
		if (jdata != null && jdata.length() > 0) {

			try {
				JSONObject json_data = jdata.getJSONObject(0);
				String codeFromServer = json_data.getString("code");
				//String messageFromServer = json_data.getString("message");
				
				if(codeFromServer.equals("700")){
					FriendDAO dao = new FriendDAO();
					Friend f = dao.get(params[1]);
					f.setState(FriendStates.ACCEPTED);
					dao.update(f);
					mensaje = "You have accepted the friendship.";
					context.getController().handleMessage(FriendsController.MESSAGE_GET_FRIENDS_LIST);
					return true;
				}
				else if(codeFromServer.equals("701")){
					FriendDAO dao = new FriendDAO();
					Friend f = dao.get(params[1]);
					f.setState(FriendStates.REJECTED);
					dao.update(f);
					mensaje = "You have rejected the friendship.";
					context.getController().handleMessage(FriendsController.MESSAGE_GET_FRIENDS_LIST);
					return true;
				}
				else{
					return false;
				}
				
			} catch (JSONException e) {
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
		// FIXME validate with regular expressions
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
		return this.context;
	}
}
