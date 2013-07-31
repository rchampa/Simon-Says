package es.rczone.simonsays.activities;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import es.rczone.simonsays.R;
import es.rczone.simonsays.customviews.CustomView;
import es.rczone.simonsays.customviews.CustomViewListener;
import es.rczone.simonsays.model.Colors;


public class Board extends Activity implements CustomViewListener{
	
	public static int MY_TURN = 0;
	public static int OPP_TURN = 1;
	public static String CODE = "code";
	public static String MOVE = "move";
	
	enum Mode{MY_TURN, OPP_TURN};
	private String TAG = Board.class.getSimpleName();
	
	private CustomView roscoBlue;
	private CustomView roscoYellow;
	private CustomView roscoGreen;
	private CustomView roscoRed;
	private CustomView send;
	private StringBuilder move;
	private String oppmove; 
	private Mode mode;


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_board);
		
		roscoBlue = (CustomView)findViewById(R.id.roscoBlueView1);
		roscoYellow = (CustomView)findViewById(R.id.roscoYellowView1);
		roscoGreen = (CustomView)findViewById(R.id.roscoGreenView1);
		roscoRed = (CustomView)findViewById(R.id.roscoRedView1);
		send = (CustomView)findViewById(R.id.sendView1);
		
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
				//mode = Mode.OPP_TURN;
				oppmove = extras.getString(MOVE);
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
	 * Clear the buffer.
	 */
	private void resetMove(){
		move.setLength(0);
	}
}
