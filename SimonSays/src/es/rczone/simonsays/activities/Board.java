package es.rczone.simonsays.activities;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gcm.GCMRegistrar;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import es.rczone.simonsays.GCMIntentService;
import es.rczone.simonsays.R;
import es.rczone.simonsays.customviews.CustomView;
import es.rczone.simonsays.customviews.CustomViewListener;
import es.rczone.simonsays.model.Colors;
import es.rczone.simonsays.tools.ConnectionListener;
import es.rczone.simonsays.tools.HttpPostConnector;


public class Board extends Activity implements CustomViewListener, ConnectionListener {
	
	public static int MY_TURN = 0;
	public static int OPP_TURN = 1;
	public static String CODE = "code";
	public static String MOVE = "move";
	public static String USER_NAME = "username";
	public static String OP_NAME = "opname";
	public static String GAME_ID = "gameID";
	

	
	enum Mode{MY_TURN, OPP_TURN};
	private String TAG = Board.class.getSimpleName();
	
	private CustomView roscoBlue;
	private CustomView roscoYellow;
	private CustomView roscoGreen;
	private CustomView roscoRed;
	private CustomView send;
	private TextView etName;
	private TextView etOppName;
	private TextView etScoreUser;
	private TextView etScoreOpp;
	
	
	private StringBuilder move;
	private String oppmove; 
	private Mode mode;
	private int gameID;
	private String username;
	
	private HttpPostConnector post;


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_board);
		
		post = new HttpPostConnector();
		
		roscoBlue = (CustomView)findViewById(R.id.roscoBlueView1);
		roscoYellow = (CustomView)findViewById(R.id.roscoYellowView1);
		roscoGreen = (CustomView)findViewById(R.id.roscoGreenView1);
		roscoRed = (CustomView)findViewById(R.id.roscoRedView1);
		send = (CustomView)findViewById(R.id.sendView1);
		
		etName = (TextView) findViewById(R.id.board_tv_username);
		etOppName = (TextView) findViewById(R.id.board_tv_opname);
		
		etScoreUser = (TextView) findViewById(R.id.board_tv_score_user);
		etScoreOpp = (TextView) findViewById(R.id.board_tv_score_op);
		
		roscoBlue.setListener(this);
		roscoYellow.setListener(this);
		roscoGreen.setListener(this);
		roscoRed.setListener(this);
		send.setListener(this);
		
		move = new StringBuilder();
		
		mode = Mode.MY_TURN;
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			int code = extras.getInt(CODE, -1);
			if(code==OPP_TURN){
				oppmove = extras.getString(MOVE);
				username = extras.getString(USER_NAME);
				
				etName.setText(username);
				etOppName.setText(extras.getShort(OP_NAME));
								
				gameID = extras.getInt(GAME_ID);
				
				
			}
		}
		
	}
	
	@Override
	protected void onDestroy(){
		setEnableFalseClick(false);
	}

	/**
	 * Method to listen custom views
	 */
	@Override
	public void onClicked(CustomView view) {
		
		if(mode==Mode.OPP_TURN)
			return;

		switch(view.getID()){
		
			case R.id.roscoBlueView1:
				Log.d(TAG, "blue");
				proccessColor(Colors.BLUE);
				break;
			
			case R.id.roscoYellowView1:
				Log.d(TAG, "yellow");
				proccessColor(Colors.YELLOW);
				break;
				
			case R.id.roscoGreenView1:
				Log.d(TAG, "green");
				proccessColor(Colors.GREEN);
				break;
				
			case R.id.roscoRedView1:
				Log.d(TAG, "red");
				proccessColor(Colors.RED);
				break;
				
			case R.id.sendView1:
				Log.d(TAG, "send");
				break;
		}
		
	}
	
	public void onClick(View v) {
		if(mode==Mode.OPP_TURN)
			return;
		
		switch(v.getId()){
            case R.id.board_button_show_opmove:
            	showMove(oppmove);
            	break;
            	
            case R.id.board_button_reset_move:
            	resetMove();
            	break;
            	
            
		}
	}
	
	
	
	private void showMove(String oppmove) {
		
		String[] colors = oppmove.split("-");
		int it=0;
		final long duration = 300;
		final long accuracy = 150;//closer to 0 means more accuracy
		
		int lenght = colors.length;
		
		for(String color : colors){
			mode = Mode.OPP_TURN;
			setEnableFalseClick(true);
			Colors enumColor = Colors.values()[Integer.parseInt(color)];
			Handler handler = new Handler();
			switch (enumColor) {
			case BLUE:
				 	handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							MotionEvent event = MotionEvent.obtain(SystemClock.uptimeMillis(),SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN,0,0,0);
							roscoBlue.onTouchEvent(event);
						}
					}, (duration*it)+accuracy);
				 
				 	handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							roscoBlue.onTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(),SystemClock.uptimeMillis(), MotionEvent.ACTION_UP,0,0,0));
						}
					}, duration*(it+1));
				 
			    
				break;
			case GREEN:
				
					handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						MotionEvent event = MotionEvent.obtain(SystemClock.uptimeMillis(),SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN,0,0,0);
						roscoGreen.onTouchEvent(event);
					}
					}, (duration*it)+accuracy);
			 
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							roscoGreen.onTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(),SystemClock.uptimeMillis(), MotionEvent.ACTION_UP,0,0,0));
						}
					}, duration*(it+1));
					
				break;
			case RED:
				
					handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						MotionEvent event = MotionEvent.obtain(SystemClock.uptimeMillis(),SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN,0,0,0);
						roscoRed.onTouchEvent(event);
					}
					}, (duration*it)+accuracy);
			 
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							roscoRed.onTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(),SystemClock.uptimeMillis(), MotionEvent.ACTION_UP,0,0,0));
						}
					}, duration*(it+1));
					
				break;
			case YELLOW:
				
					handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						MotionEvent event = MotionEvent.obtain(SystemClock.uptimeMillis(),SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN,0,0,0);
						roscoYellow.onTouchEvent(event);
					}
					}, (duration*it)+accuracy);
			 
					handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						roscoYellow.onTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(),SystemClock.uptimeMillis(), MotionEvent.ACTION_UP,0,0,0));
					}
					}, duration*(it+1));
					
				break;
			
			}
			
			it++;
			if(lenght==it){
				
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						setEnableFalseClick(false);
						mode = Mode.MY_TURN;
					}
				}, (duration*it)+accuracy);
				
			}
		
		}
		
	}
	
	
	private void setEnableFalseClick(boolean b){
		roscoBlue.enableFalseClick(b);
		roscoYellow.enableFalseClick(b);
		roscoGreen.enableFalseClick(b);
		roscoRed.enableFalseClick(b);
		send.enableFalseClick(b);
	}

	private void proccessColor(Colors color){
		
		if(mode==Mode.MY_TURN){
			move.append(color.ordinal()+"-");
		}
		else if(mode==Mode.MY_TURN){
			//nothing
		}
		
	}
	
	/**
	 * Clears buffer.
	 */
	private void resetMove(){
		move.setLength(0);
	}

	@Override
	public boolean inBackground(String... params) {
		ArrayList<NameValuePair> postParametersToSend = new ArrayList<NameValuePair>();

		postParametersToSend.add(new BasicNameValuePair("game_id", params[0]));
		postParametersToSend.add(new BasicNameValuePair("player_name", params[1]));
		postParametersToSend.add(new BasicNameValuePair("move", params[2]));

		// realizamos una peticion y como respuesta obtenes un array JSON
		JSONArray jdata = post.getserverdata(postParametersToSend, HttpPostConnector.URL_MAKE_A_MOVE);


		// si lo que obtuvimos no es null, es decir, hay respuesta válida
		if (jdata != null && jdata.length() > 0) {

			try {
				JSONObject json_data = jdata.getJSONObject(0);
				String codeFromServer = json_data.getString("code");
				//String messageFromServer = json_data.getString("message");
				
				if(codeFromServer.equals("400")){
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
		Toast.makeText(this, "The move has been sent", Toast.LENGTH_SHORT).show();
		finish();
		
	}

	@Override
	public void invalidInputData() {
		Toast.makeText(this, "The move has not been sent", Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void afterErrorConnection() {
		Toast.makeText(this, "Problem connections. The move has not been sent.", Toast.LENGTH_SHORT).show();
		
	}
}
