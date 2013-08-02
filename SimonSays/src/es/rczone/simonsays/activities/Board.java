package es.rczone.simonsays.activities;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import es.rczone.simonsays.daos.GameDAO;
import es.rczone.simonsays.daos.MoveDAO;
import es.rczone.simonsays.model.Colors;
import es.rczone.simonsays.model.Game;
import es.rczone.simonsays.model.GameStates;
import es.rczone.simonsays.model.Move;
import es.rczone.simonsays.tools.AsyncConnect;
import es.rczone.simonsays.tools.ConnectionListener;
import es.rczone.simonsays.tools.HttpPostConnector;


public class Board extends Activity implements CustomViewListener, ConnectionListener {
	
//	public static int MY_TURN = 0;
//	public static int OPP_TURN = 1;
//	public static String CODE = "code";
//	public static String TURN = "turn";
//	public static String MOVE = "move";
//	public static String USER_NAME = "username";
//	public static String OP_NAME = "opname";
	public static String GAME_ID = "gameID";
	public static String RIGHT = "1";
	public static String FAIL = "2";

	

	
	public enum Mode{OPP_TURN, REPLAY_MOVE, MY_TURN};
	private String TAG = Board.class.getSimpleName();
	
	private CustomView roscoBlue;
	private CustomView roscoYellow;
	private CustomView roscoGreen;
	private CustomView roscoRed;
	private CustomView send;
	private TextView etName;
	private TextView etOppName;
	private TextView etOldThreshold;
	private TextView etNewThreshold;
	private TextView etScoreUser;
	private TextView etScoreOpp;
	
	
	private StringBuilder move;
	private StringBuilder moveToCheck;
	private String oppmove; 
	private Mode mode;
	private int gameID;
	private String username;
	private boolean guess;
	private boolean confirmation = false;
	private boolean isCheckedOppMove = false;//let me know whether the user see the opp move
	private boolean isMatchedOppMove = false;//let me know whether the user replay the opp move  
	private boolean isMyMoveComplete = false;
	private int threshold;
	
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
		
		etOldThreshold = (TextView) findViewById(R.id.board_tv_oldThreshold);
		etNewThreshold = (TextView) findViewById(R.id.board_tv_newThreshold);
		etScoreUser = (TextView) findViewById(R.id.board_tv_score_user);
		etScoreOpp = (TextView) findViewById(R.id.board_tv_score_op);
		
		roscoBlue.setListener(this);
		roscoYellow.setListener(this);
		roscoGreen.setListener(this);
		roscoRed.setListener(this);
		send.setListener(this);
		
		move = new StringBuilder();
		moveToCheck = new StringBuilder();
		
		//mode = Mode.MY_TURN;
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			
			gameID = extras.getInt(GAME_ID);
			prepareGame(gameID);
		}
		
	}
	
	public void prepareGame(int gameID){
		SharedPreferences prefs = getSharedPreferences(GCMIntentService.PREFERENCES_FILE, Context.MODE_PRIVATE);
		username = prefs.getString(GCMIntentService.NAME, "");
		etName.setText(" "+username);
    	
		Game item = new GameDAO().get(gameID);
		etOppName.setText(" "+item.getOpponentName());	
		etScoreUser.setText(""+item.getUserScore());
		etScoreOpp.setText(""+item.getOppScore()); 
		threshold = item.getNumMoves();
		etOldThreshold.setText("Opp threshold: "+(threshold-1));
		etNewThreshold.setText("User threshold: "+(threshold));
		
		boolean isMyTurn = item.isMyTurn();
		
		if(!isMyTurn){
			Move m = new MoveDAO().getMoveOfGame(gameID);
			oppmove = m.getMove();
			mode = Mode.OPP_TURN;
			Toast.makeText(this, "Check the opponent's move", Toast.LENGTH_SHORT).show();
			isCheckedOppMove = false;
			isMatchedOppMove = false;
		}
		else{
			mode = Mode.MY_TURN;
			isCheckedOppMove = true;
			isMatchedOppMove = true;
			oppmove ="";
		}
    	
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
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
				
				if(!isCheckedOppMove && mode!=Mode.MY_TURN){
					Toast.makeText(this, "You should check you opponent's move first", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(!isMatchedOppMove){
					Toast.makeText(this, "You must try to replay the opponent's move before send a new move.", Toast.LENGTH_SHORT).show();
					
					return;
				}
				
				if(isMyMoveComplete){
					
					askConfirmation();
						
				}
				else{
					Toast.makeText(this, "You should complete your move.", Toast.LENGTH_SHORT).show();
				}
				break;
		}
		
	}
	
	public void onClick(View v) {
		
		switch(v.getId()){
            case R.id.board_button_show_opmove:
            	
            	if(isCheckedOppMove)
            		Toast.makeText(this, "You just can see the opponent's move once.", Toast.LENGTH_SHORT).show();
            	else
            		showMove(oppmove);
            	
            	break;
            	
            case R.id.board_button_reset_move:
            	resetMove();
            	Toast.makeText(this, "Your move has been reset", Toast.LENGTH_SHORT).show();
            	break;
            	
            case R.id.board_button_checkMove:
            	
            	if(isCheckedOppMove && isMatchedOppMove && mode==Mode.MY_TURN){
            		Toast.makeText(this, "You should make your move!", Toast.LENGTH_SHORT).show();
            		return;
            	}
            		
            	
            	if(matchMoves()){
            		Toast.makeText(this, "You are right!", Toast.LENGTH_SHORT).show();
            		isMatchedOppMove = true;
            		guess = true;
            	}
            	else{
            		Toast.makeText(this, "You are fail", Toast.LENGTH_SHORT).show();
            		isMatchedOppMove=true;
            		guess = false;
            	}
            	
            	mode = Mode.MY_TURN;
            	resetMove();            
            	break;
            
		}
	}
	
	private boolean matchMoves(){
		
		String[] colorsOpp = oppmove.split("-");
		String[] colorsCheck = moveToCheck.toString().split("-");
		isCheckedOppMove = true;
	
		return Arrays.equals(colorsOpp, colorsCheck);
		
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
						mode = Mode.REPLAY_MOVE;
						resetMove();
						isCheckedOppMove = true;
						Toast.makeText(Board.this, "Now you have to reproduce the same succession of colors in the board", Toast.LENGTH_SHORT).show();
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
			int n = move.length();
			if(n<(threshold)*2){
				move.append(color.ordinal()+"-");
				if(move.length()==threshold*2){
					Toast.makeText(this, "You reach the threshold", Toast.LENGTH_SHORT).show();
					isMyMoveComplete=true;
				}
			}
			else{
				Toast.makeText(this, "You should not exceed the threshold", Toast.LENGTH_SHORT).show();
				isMyMoveComplete=false;
			}
			
		}
		else if(mode==Mode.REPLAY_MOVE){
			int n = moveToCheck.length();
			if(n<(threshold-1)*2){
				moveToCheck.append(color.ordinal()+"-");
				if(moveToCheck.length()==(threshold-1)*2)
					Toast.makeText(this, "You reach the threshold", Toast.LENGTH_SHORT).show();
			}
			else
				Toast.makeText(this, "You should not exceed the threshold", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	
	/**
	 * Clears the buffers.
	 */
	private void resetMove(){
		move.setLength(0);
		moveToCheck.setLength(0);
	}
	
	
	
	private void askConfirmation(){
		
		confirmation = false;
		
		AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(Board.this);
		 
		// Setting Dialog Title
		alertDialog2.setTitle("Confirm send...");
		 
		// Setting Dialog Message
		alertDialog2.setMessage("Are you sure you want to send the move?");
		 
		// Setting Icon to Dialog
		alertDialog2.setIcon(R.drawable.send_icon_confirm);
		 
		// Setting Positive "Yes" Btn
		alertDialog2.setPositiveButton("YES",
		        new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		                
		            	new AsyncConnect(Board.this).execute(""+gameID,username,move.toString(),guess?RIGHT:FAIL);
		                
		            }
		        });
		// Setting Negative "NO" Btn
		alertDialog2.setNegativeButton("NO",
		        new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		               
		                dialog.cancel();
		            }
		        });
		 
		// Showing Alert Dialog
		alertDialog2.show();
		
		
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
					Game g = new GameDAO().get(gameID);
					g.setState(GameStates.WAITING_FOR_MOVE);
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
