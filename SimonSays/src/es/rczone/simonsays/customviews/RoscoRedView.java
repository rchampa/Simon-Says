package es.rczone.simonsays.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import es.rczone.simonsays.R;

public class RoscoRedView extends ImageView implements CustomView{

	protected final Bitmap bitmap;
	protected Drawable onFocusDrawable;
	private CustomViewListener listener;
	private boolean isFalseClick;
	private boolean isShining;
	
	public RoscoRedView(Context context, AttributeSet attrs) {
		super(context, attrs);		 		
		bitmap = ((BitmapDrawable)this.getDrawable()).getBitmap();
		isFalseClick = false;
		isShining = false;
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
						setImageResource(R.drawable.rosco_red_shining);
						isShining = true;
						listener.onClicked(this);
					}
					
				}
			}
			return true;
		
		case MotionEvent.ACTION_UP:
			setImageResource(R.drawable.rosco_red);
			isShining = false;
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
}
