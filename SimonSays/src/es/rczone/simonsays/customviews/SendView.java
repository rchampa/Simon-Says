package es.rczone.simonsays.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import es.rczone.simonsays.R;

public class SendView extends ImageView implements CustomView{

	public enum States{EYE, HAND, RESET, TIC, SEND};
	protected final Bitmap bitmap;
	protected Drawable onFocusDrawable;
	private CustomViewListener listener;
	private boolean isFalseClick;
	private boolean isShining;
	private States state;
	
	public SendView(Context context, AttributeSet attrs) {
		super(context, attrs);		 		
		bitmap = ((BitmapDrawable)this.getDrawable()).getBitmap(); 
		isFalseClick = false;
		isShining = false;
		state = States.HAND;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		float iX = event.getX();
		float iY = event.getY();

		switch (event.getAction()) {

			case MotionEvent.ACTION_DOWN:
				//Makes sure that X and Y are not less than 0, and no more than the height and width of the image.
				//Or is a false click
				if ( (iX >= 0 & iY >= 0 & iX < bitmap.getWidth() & iY < bitmap.getHeight()) || isFalseClick) {
					if (bitmap.getPixel((int) iX, (int) iY) != 0 || isFalseClick) {
						if(!isShining){
							
							switch(state){
							case EYE:
								setImageResource(R.drawable.eye_button_pushed);
								break;
							case TIC:
								setImageResource(R.drawable.tic_button_pushed);
								break;
							case RESET:
								setImageResource(R.drawable.reset_button_pushed);
								break;
							case SEND:
								setImageResource(R.drawable.send_button_pushed);
								break;
							case HAND:
								setImageResource(R.drawable.touch_button_pushed);
								break;
							default:
								break;
							
							}
							
							isShining = true;
						}
						
					}
				}
				return true;
			
			case MotionEvent.ACTION_UP:
				switch(state){
				case EYE:
					setImageResource(R.drawable.eye_button);
					break;
				case TIC:
					setImageResource(R.drawable.tic_button);
					break;
				case RESET:
					setImageResource(R.drawable.reset_button);
					break;
				case SEND:
					setImageResource(R.drawable.send_button);
					break;
				case HAND:
					setImageResource(R.drawable.touch_button);
					break;
				default:
					break;
				
				}
				isShining = false;
				listener.onClicked(this);
				return true;
		}
		
		return false;

	}
	
	
	@Override
	public void setListener(CustomViewListener listener){
		this.listener = listener;
	}
	@Override
	public int getID(){
		return super.getId();
	}
	@Override
	public void enableFalseClick(boolean b) {
		isFalseClick = b;
	}
	
	public void setStateHand(){
		if(state!=States.HAND){
			setImageResource(R.drawable.touch_button);
			state = States.HAND;
		}
	}
	
	public void setStateReset(){
		if(state!=States.RESET){
			setImageResource(R.drawable.reset_button);
			state = States.RESET;
		}
	}
	
	public void setStateSend(){
		if(state!=States.SEND){
			setImageResource(R.drawable.send_button);
			state = States.SEND;
		}
	}
	
	public void setStateEye(){
		
		if(state!=States.EYE){
			setImageResource(R.drawable.eye_button);
			state = States.EYE;
		}
	}
	
	public void setStateTic(){
		if(state!=States.TIC){
			setImageResource(R.drawable.tic_button);
			state = States.TIC;
		}
	}
	
	public States getState(){
		return state;
	}
}
