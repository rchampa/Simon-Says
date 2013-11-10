package es.rczone.simonsays.customviews;

import android.view.MotionEvent;

public interface CustomView {
	
	public boolean onTouchEvent(MotionEvent event);
	public void setListener(CustomViewListener listener);
	public int getID();
	public void enableFalseClick(boolean b);

}
